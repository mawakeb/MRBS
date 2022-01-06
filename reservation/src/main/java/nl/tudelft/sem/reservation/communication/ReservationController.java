package nl.tudelft.sem.reservation.communication;

import com.google.gson.reflect.TypeToken;
import nl.tudelft.sem.reservation.builder.Builder;
import nl.tudelft.sem.reservation.builder.Director;
import nl.tudelft.sem.reservation.builder.ReservationBuilder;
import nl.tudelft.sem.reservation.entity.Reservation;
import nl.tudelft.sem.reservation.exception.InvalidReservationException;
import nl.tudelft.sem.reservation.repository.ReservationRepository;
import nl.tudelft.sem.reservation.validators.*;
import org.h2.engine.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reservation")
public class ReservationController {


    private final transient ReservationRepository reservationRepo;

    @Autowired
    public ReservationController(ReservationRepository reservationRepo) {
        this.reservationRepo = reservationRepo;
    }


    /**
     * Gets all lectures.
     *
     * @return all lectures
     */
    @GetMapping("")
    public String returnHi() {
        //return lectureRepository.findAll();
        return "hello_from_reservation";
    }

    @GetMapping("/checkUser")
    public boolean checkUser(@RequestParam long madeBy, @RequestParam long reservationId) {

        Reservation reservation = reservationRepo.findById(reservationId).orElse(null);
        if (reservation!=null){
            return reservation.getMadeBy().equals(madeBy);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "RESERVATION_NOT_FOUND");
        }
    }

    @GetMapping("/checkTimeslot")
    public List<Long> checkTimeslot(@RequestParam List<Long> rooms, @RequestParam String startTime
            , @RequestParam String endTime) {
        List<Long> filteredRooms = new ArrayList<>();

        @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
        Set<Long> takenRooms = reservationRepo
                .findAllByRoomIdInAndCancelledIsFalseAndStartBeforeAndEndAfter(rooms,
                LocalDateTime.parse(startTime), LocalDateTime.parse(endTime))
                .stream().map(Reservation::getRoomId).collect(Collectors.toSet());

        for (Long l : rooms) {
            if (!takenRooms.contains(l)) {
                filteredRooms.add(l);
            }
        }

        return filteredRooms;
    }

    @GetMapping("/editReservation")
    public boolean editReservation(@RequestParam long reservationId, @RequestParam long roomId
            , @RequestParam LocalDateTime start, @RequestParam LocalDateTime end
            , @RequestParam String editPurpose, @RequestHeader("Authorization") String token) {
        Reservation reservation = reservationRepo.findById(reservationId).orElse(null);
        if (reservation == null) return false;
        long isForId = reservation.getUserId();
        long madeById = reservation.getMadeBy();

        if (roomId != 0 || start != null || end != null) {
            if (roomId != 0) roomId = reservation.getRoomId();
            if (start != null) start = reservation.getStart();
            if (end != null) end = reservation.getEnd();
        } else {
            return false;
        }

        if (UserCommunication.getUserType(token).equals("ADMIN")
                || UserCommunication.getUser(token).equals(isForId)
                || UserCommunication.getUser(token).equals(madeById)) {
            // set up validators
            Validator handler = new CheckAvailabilityValidator();
            handler.setNext(new CheckIfRoomIsNotReservedAlready(), token);
            handler.setNext(new EmployeesCannotReserveMoreThanTwoWeeksInAdvance(), token);
            handler.setNext(new EmployeesMakeEditCancelReservationForThemselves(), token);
            handler.setNext(new EmployeesOneReservationDuringTimeSlot(), token);
            handler.setNext(new SecretariesCanOnlyReserveEditForTheirResearchMembers(), token);

            // edit the reservation
            reservation.changeLocation(roomId, editPurpose);
            reservation.changeTime(start, end, editPurpose);

            // perform the checks and save the reservation if valid
            try {
                boolean isValid = handler.handle(reservation, token);
                System.out.print("Reservation status = " + isValid);
                reservationRepo.save(reservation);
            } catch (InvalidReservationException e) {
                e.printStackTrace();
            }
        } else {
            return false;
        }

        return true;
    }

    @GetMapping("/cancelReservation")
    public boolean cancelReservation(@RequestParam long reservationId, @RequestParam String cancelPurpose, @RequestHeader("Authorization") String token) {
        Reservation reservation = reservationRepo.findById(reservationId).orElse(null);
        if (reservation == null) return false;
        long isForId = reservation.getUserId();
        long madeById = reservation.getMadeBy();

        if (UserCommunication.getUserType(token).equals("ADMIN")
                || UserCommunication.getUser(token).equals(isForId)
                || UserCommunication.getUser(token).equals(madeById)) {
            reservation.cancelReservation(cancelPurpose);

            reservationRepo.save(reservation);
            return true;
        } else {
            return false;
        }
    }

    @PostMapping("/makeReservation")
    public String makeReservation(@RequestParam Long targetUserOrGroupId, @RequestParam Long roomId,
                                  @RequestParam LocalDateTime start, @RequestParam LocalDateTime end,
                                  @RequestParam String purpose,
                                  @RequestHeader("Authorization") String token) {
        Long userId = UserCommunication.getUser(token);
        Builder builder = new ReservationBuilder(userId, roomId, start, end);
        Director director = new Director(builder);

        if (Objects.equals(targetUserOrGroupId, userId)) {
            director.buildSelfReservation();
        }
        else if (UserCommunication.getUserType(token).equals("ADMIN")) {
            director.buildAdminReservation(targetUserOrGroupId);
        }
        else {
            director.buildGroupReservation(targetUserOrGroupId, purpose);
        }

        Reservation reservation = builder.build();

        Validator handler = new CheckAvailabilityValidator();
        handler.setNext(new CheckIfRoomIsNotReservedAlready(), token);
        handler.setNext(new EmployeesCannotReserveMoreThanTwoWeeksInAdvance(), token);
        handler.setNext(new EmployeesMakeEditCancelReservationForThemselves(), token);
        handler.setNext(new EmployeesOneReservationDuringTimeSlot(), token);
        handler.setNext(new SecretariesCanOnlyReserveEditForTheirResearchMembers(), token);

        try {
            boolean isValid = handler.handle(reservation, token);
            System.out.print("Reservation status = " + isValid);
            reservationRepo.save(reservation);
        } catch (InvalidReservationException e) {
            e.printStackTrace();
        }

        return "Reservation successful!";
    }
}
