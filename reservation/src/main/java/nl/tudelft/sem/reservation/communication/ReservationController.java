package nl.tudelft.sem.reservation.communication;

import com.google.gson.reflect.TypeToken;
import nl.tudelft.sem.reservation.entity.Reservation;
import nl.tudelft.sem.reservation.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("")
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

    @GetMapping("checkUser")
    public boolean checkUser(@RequestParam long userId, @RequestParam long reservationId) {

        Reservation reservation = reservationRepo.findById(reservationId);
        if (reservation!=null){
            return reservation.getUserId().equals(userId);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "RESERVATION_NOT_FOUND");
        }
    }

    @GetMapping("checkTimeslot")
    public List<Long> checkTimeslot(@RequestParam List<Long> rooms, @RequestParam String startTime, @RequestParam String endTime) {
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

    @GetMapping("editReservation")
    public boolean editReservation(@RequestParam long reservationId) {
        return false;
    }

    @GetMapping("cancelReservation")
    public boolean cancelReservation(@RequestParam long reservationId, @RequestParam String cancelPurpose) {
        Reservation reservation = reservationRepo.findById(reservationId);

        if (reservation == null) return false;

        //long userId = reservation.getUserId(); needed when checking for the same user that made the reservation

        if (UserCommunication.getUserType().equals("ADMIN")) { // or user is same as userId
            reservation.setCancelled(true);
            reservation.setEditPurpose(cancelPurpose);
            reservationRepo.save(reservation);
            return true;
        } else {
            return false;
        }
    }
}
