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
    public String editReservation(@RequestParam long reservationId, @RequestParam long roomId,
                                  @RequestParam LocalDateTime start,
                                  @RequestParam LocalDateTime end,
                                  @RequestParam String editPurpose,
                                  @RequestHeader("Authorization") String token) {

        Reservation reservation = reservationRepo.findById(reservationId).orElse(null);
        if (reservation == null) {
            return "Reservation was not found";
        }

        //refactoring to reduce cyclomatic complexity
        List<Object> changes = checkChanges(reservation, roomId, start, end);

        int size = 4;
        if (changes.size() == size) {
            return "There is nothing to edit for the reservation";
        }

        if (getUserType(token).equals("ADMIN")
                || getUser(token).equals(reservation.getUserId())
                || getUser(token).equals(reservation.getMadeBy())) {

            //refactoring to reduce LOC
            return editActualReservation(reservation, token, changes, editPurpose);
        }

        return "You do not have access to edit this reservation";
    }

    /**
     * Edit the actual reservation in the database.
     *
     * @param reservation reservation to be changed
     * @param token authentication token of the user
     * @param changes the changes to be made
     * @param editPurpose the reason why user is making the edit
     */
    public String editActualReservation(Reservation reservation, String token,
                                      List<Object> changes, String editPurpose) {
        //refactored to reduce LOC
        Validator handler = setUpChainOfResponsibility(token);

        // edit the reservation
        reservation.changeLocation((long) changes.get(0), editPurpose);
        reservation.changeTime((LocalDateTime) changes.get(1),
                (LocalDateTime) changes.get(2), editPurpose);

        // perform the checks and save the reservation if valid
        try {
            boolean isValid = handle(handler, reservation, token);
            reservationRepo.save(reservation);
        } catch (InvalidReservationException e) {
        }

        return "Reservation was edited successfully";
    }

    /**
     * Check if there are meaningful changes when editing a reservation.
     *
     * @param reservation the reservation to edit
     * @param roomId new roomId
     * @param start new start time
     * @param end new end time
     * @return list of the final fields of the reservation with an
     *          extra integer at the end if none of the fields were changed
     */
    public List<Object> checkChanges(Reservation reservation, long roomId,
                                     LocalDateTime start, LocalDateTime end) {
        List<Object> changes = new ArrayList<>();
        changes.add(roomId);
        changes.add(start);
        changes.add(end);

        if ((long) changes.get(0) == -1 && changes.get(1) == null && changes.get(2) == null) {
            changes.add(-1);
        }
        if ((long) changes.get(0) == -1) {
            changes.set(0, reservation.getRoomId());
        }
        if (changes.get(1) == null) {
            changes.set(1, reservation.getStart());
        }
        if (changes.get(2) == null) {
            changes.set(2, reservation.getEnd());
        }

        return changes;
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
     * @param userId    who the reservation is for
     * @param groupId   which group the reservation is for
     * @param roomId    the room id for the reservation
     * @param start     the start time of the reservation
     * @param end       the end time of the reservation
     * @param purpose   the purpose of the reservation
     * @param purpose   the kind of reservation
     * @param token     an authorization token
     * @return a status message regarding the success
     */
    @PostMapping("/makeReservation")
    @SuppressWarnings("DataflowAnomalyAnalysis")
    public String makeReservation(@RequestParam Long userId,
                                  @RequestParam Long groupId,
                                  @RequestParam Long roomId,
                                  @RequestParam LocalDateTime start,
                                  @RequestParam LocalDateTime end,
                                  @RequestParam String purpose,
                                  @RequestParam String type,
                                  @RequestHeader("Authorization") String token) {
        Long madeBy = getUser(token);
        Builder builder = getBuilder(roomId, start, end, madeBy);
        Director director = getDirector(builder);

        if (type.equals("SELF")) {
            director.buildSelfReservation();
        } else if (type.equals("SINGLE")) {
            director.buildSingleReservation(userId, groupId, purpose);
        } else if (type.equals("GROUP")) {
            director.buildGroupReservation(groupId, purpose);
        } else director.buildAdminReservation(userId);

        Reservation reservation = builder.build();

        Validator handler = setUpChainOfResponsibility(token);

        try {
            boolean isValid = handler.handle(reservation, token);
            reservationRepo.save(reservation);
        } catch (InvalidReservationException e) {
            return "Invalid reservation!";
        }

        return "Reservation successful!";
    }

    public Director getDirector(Builder builder) {
        Director director = new Director(builder);
        return director;
    }

    public Builder getBuilder(Long roomId, LocalDateTime start, LocalDateTime end, Long madeBy) {
        Builder builder = new ReservationBuilder(madeBy, roomId, start, end);
        return builder;
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

    /**
     * Sets up the chain of validators in order.
     *
     * @param token authentication token of the user
     * @return the Validator with all the chain set up
     */
    public Validator setUpChainOfResponsibility(String token) {
        Validator handler = new CheckAvailabilityValidator();
        handler.setNext(new CheckIfRoomIsNotReservedAlready(), token);
        handler.setNext(new EmployeesCannotReserveMoreThanTwoWeeksInAdvance(), token);
        handler.setNext(new EmployeesMakeEditCancelReservationForThemselves(), token);
        handler.setNext(new EmployeesOneReservationDuringTimeSlot(), token);
        handler.setNext(new SecretariesCanOnlyReserveEditForTheirResearchMembers(), token);

        return handler;
    }
}
