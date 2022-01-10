package nl.tudelft.sem.reservation.communication;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import nl.tudelft.sem.reservation.builder.Builder;
import nl.tudelft.sem.reservation.builder.Director;
import nl.tudelft.sem.reservation.builder.ReservationBuilder;
import nl.tudelft.sem.reservation.entity.Reservation;
import nl.tudelft.sem.reservation.exception.InvalidReservationException;
import nl.tudelft.sem.reservation.repository.ReservationRepository;
import nl.tudelft.sem.reservation.validators.CheckAvailabilityValidator;
import nl.tudelft.sem.reservation.validators.CheckIfRoomIsNotReservedAlready;
import nl.tudelft.sem.reservation.validators.EmployeesCannotReserveMoreThanTwoWeeksInAdvance;
import nl.tudelft.sem.reservation.validators.EmployeesMakeEditCancelReservationForThemselves;
import nl.tudelft.sem.reservation.validators.EmployeesOneReservationDuringTimeSlot;
import nl.tudelft.sem.reservation.validators.SecretariesCanOnlyReserveEditForTheirResearchMembers;
import nl.tudelft.sem.reservation.validators.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


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

    /**
     * Check if a given user made a certain reservation.
     *
     * @param madeBy the id of the user
     * @param reservationId the id of the reservation
     * @return if the user made the reservation
     */
    @GetMapping("/checkUser")
    public boolean checkUser(@RequestParam long madeBy, @RequestParam long reservationId) {

        Reservation reservation = reservationRepo.findById(reservationId).orElse(null);
        if (reservation != null) {
            return reservation.getMadeBy().equals(madeBy);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "RESERVATION_NOT_FOUND");
        }
    }

    /**
     * Get a room by id.
     *
     * @param id the id of the room
     * @return the room
     */
    @GetMapping("/getRoom")
    public long getRoom(@RequestParam long id) {

        Reservation reservation = reservationRepo.findById(id).orElse(null);
        if (reservation != null) {
            return reservation.getRoomId();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "RESERVATION_NOT_FOUND");
        }
    }

    /**
     * Check what rooms are free in a given timeslot.
     *
     * @param rooms the rooms to check
     * @param startTime the start of the timeslot
     * @param endTime the end of the timeslot
     * @return the rooms that are free in the timeslot
     */
    @GetMapping("/checkTimeslot")
    public List<Long> checkTimeslot(@RequestParam List<Long> rooms,
                                    @RequestParam String startTime,
                                    @RequestParam String endTime) {
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

    /**
     * Edit a reservation.
     *
     * @param reservationId the reservation id
     * @param roomId a potential new roomId
     * @param start a potential new start time
     * @param end a potential new end time
     * @param editPurpose the purpose of this edit
     * @param token an authorization token
     * @return a status message regarding the success
     */
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    @GetMapping("/editReservation")
    public String editReservation(@RequestParam long reservationId,
                                  @RequestParam long roomId,
                                  @RequestParam LocalDateTime start,
                                  @RequestParam LocalDateTime end,
                                  @RequestParam String editPurpose,
                                  @RequestHeader("Authorization") String token) {
        Reservation reservation = reservationRepo.findById(reservationId).orElse(null);
        if (reservation == null) {
            return "Reservation was not found";
        }
        long isForId = reservation.getUserId();
        long madeById = reservation.getMadeBy();

        if (roomId != -1 || start != null || end != null) {
            if (roomId == -1) {
                roomId = reservation.getRoomId();
            }
            if (start == null) {
                start = reservation.getStart();
            }
            if (end == null) {
                end = reservation.getEnd();
            }
        } else {
            return "There is nothing to edit for the reservation";
        }

        if (getUserType(token).equals("ADMIN")
                || getUser(token).equals(isForId)
                || getUser(token).equals(madeById)) {
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
                boolean isValid = handle(handler, reservation, token);
                System.out.print("Reservation status = " + isValid);
                reservationRepo.save(reservation);
            } catch (InvalidReservationException e) {
                e.printStackTrace();
            }
        } else {
            return "You do not have access to edit this reservation";
        }

        return "Reservation was edited successfully";
    }

    /**
     * Cancel a reservation.
     *
     * @param reservationId the id of the reservation
     * @param cancelPurpose the purpose of cancelling the reservation
     * @param token an authorization token
     * @return a status message regarding the success
     */
    @GetMapping("/cancelReservation")
    public String cancelReservation(@RequestParam long reservationId,
                                    @RequestParam String cancelPurpose,
                                    @RequestHeader("Authorization") String token) {
        Reservation reservation = reservationRepo.findById(reservationId).orElse(null);
        if (reservation == null) {
            return "Reservation was not found";
        }
        long isForId = reservation.getUserId();
        long madeById = reservation.getMadeBy();

        if (getUserType(token).equals("ADMIN")
                || getUser(token).equals(isForId)
                || getUser(token).equals(madeById)) {
            reservation.cancelReservation(cancelPurpose);

            reservationRepo.save(reservation);
            return "Reservation was cancelled successfully";
        } else {
            return "You do not have access to cancel this reservation";
        }
    }

    /**
     * Make a new reservation.
     *
     * @param targetUserOrGroupId who the reservation is for
     * @param roomId the room id for the reservation
     * @param start the start time of the reservation
     * @param end the end time of the reservation
     * @param purpose the purpose of the reservation
     * @param token an authorization token
     * @return a status message regarding the success
     */
    @PostMapping("/makeReservation")
    public String makeReservation(@RequestParam Long targetUserOrGroupId,
                                  @RequestParam Long roomId,
                                  @RequestParam LocalDateTime start,
                                  @RequestParam LocalDateTime end,
                                  @RequestParam String purpose,
                                  @RequestHeader("Authorization") String token) {
        Long userId = UserCommunication.getUser(token);
        Builder builder = new ReservationBuilder(userId, roomId, start, end);
        Director director = new Director(builder);

        if (Objects.equals(targetUserOrGroupId, userId)) {
            director.buildSelfReservation();
        } else if (UserCommunication.getUserType(token).equals("ADMIN")) {
            director.buildAdminReservation(targetUserOrGroupId);
        } else {
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

    /**
     * Get the type of the user with given authentication token.
     * Added to allow unit testing possible.
     *
     * @param token the authentication token of the user
     * @return the type of the user
     */
    public String getUserType(String token) {
        return UserCommunication.getUserType(token);
    }

    /**
     * Get the id of the user with given authentication token.
     * Added to allow unit testing possible.
     *
     * @param token the authentication token of the user
     * @return the id of the user
     */
    public Long getUser(String token) {
        return UserCommunication.getUser(token);
    }

    /**
     * Activate the handle method of a validator.
     * Added to allow unit testing possible.
     *
     * @param handler the validator in use
     * @param reservation the reservation to validate
     * @param token the authentication token of the user
     * @return the success of the validator
     */
    public boolean handle(Validator handler, Reservation reservation, String token)
            throws InvalidReservationException {
        return handler.handle(reservation, token);
    }
    
    @GetMapping("getSchedule")
    public List<Reservation> getSchedule(@RequestParam long userId) {
        return reservationRepo.findAllByUserId(userId);
    }
}
