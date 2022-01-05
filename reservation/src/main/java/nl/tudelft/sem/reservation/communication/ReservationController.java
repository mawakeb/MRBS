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

    // TODO: Check if the current user is the one that made the reservation
    @GetMapping("/editReservation")
    public boolean editReservation(@RequestParam long reservationId, @RequestParam long roomId
            , @RequestParam LocalDateTime start, @RequestParam LocalDateTime end
            , @RequestParam String editPurpose) {
        Reservation reservation = reservationRepo.findById(reservationId).orElse(null);
        if (reservation == null) return false;
        //long userId = reservation.getUserId(); needed when checking for the same user that made the reservation

        if (roomId != 0 || start != null || end != null) {
            if (roomId != 0) roomId = reservation.getRoomId();
            if (start != null) start = reservation.getStart();
            if (end != null) end = reservation.getEnd();
        } else {
            return false;
        }

        if (UserCommunication.getUserType().equals("ADMIN")) { // or user is same as userId
            if (reservationRepo.findAllByRoomIdAndCancelledIsFalseAndStartBeforeAndEndAfter(roomId, start, end) == null) {
                return false;
            } else {

                reservation.changeLocation(roomId, editPurpose);
                reservation.changeTime(start, end, editPurpose);

                reservationRepo.save(reservation);
                return true;
            }
        } else {
            return false;
        }
    }

    // TODO: Check if the current user is the one that made the reservation
    @GetMapping("/cancelReservation")
    public boolean cancelReservation(@RequestParam long reservationId, @RequestParam String cancelPurpose) {
        Reservation reservation = reservationRepo.findById(reservationId).orElse(null);
        if (reservation == null) return false;
        //long userId = reservation.getUserId(); needed when checking for the same user that made the reservation

        if (UserCommunication.getUserType().equals("ADMIN")) { // or user is same as userId
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
                                  @RequestParam String purpose) {
        Long userId = UserCommunication.getUser();
        Builder builder = new ReservationBuilder(userId, roomId, start, end);
        Director director = new Director(builder);

        if (Objects.equals(targetUserOrGroupId, userId)) {
            director.buildSelfReservation();
        }
        else if (UserCommunication.getUserType().equals("ADMIN")) {
            director.buildAdminReservation(targetUserOrGroupId);
        }
        else {
            director.buildGroupReservation(targetUserOrGroupId, purpose);
        }

        Reservation reservation = builder.build();

        Validator handler = new CheckAvailabilityValidator();
        handler.setNext(new CheckIfRoomIsNotReservedAlready());
        handler.setNext(new EmployeesCannotReserveMoreThanTwoWeeksInAdvance());
        handler.setNext(new EmployeesMakeEditCancelReservationForThemselves());
        handler.setNext(new EmployeesOneReservationDuringTimeSlot());
        handler.setNext(new SecretariesCanOnlyReserveEditForTheirResearchMembers());

        try {
            boolean isValid = handler.handle(reservation);
            System.out.print("Reservation status = " + isValid);
            reservationRepo.save(reservation);
        } catch (InvalidReservationException e) {
            e.printStackTrace();
        }

        return "Reservation successful!";
    }
}
