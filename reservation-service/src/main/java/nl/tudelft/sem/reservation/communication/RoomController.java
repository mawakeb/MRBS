package nl.tudelft.sem.reservation.communication;

import nl.tudelft.sem.reservation.entity.Reservation;
import nl.tudelft.sem.reservation.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RoomController {

    private final transient ReservationRepository reservationRepo;

    @Autowired
    public RoomController(ReservationRepository reservationRepo) {
        this.reservationRepo = reservationRepo;
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
}
