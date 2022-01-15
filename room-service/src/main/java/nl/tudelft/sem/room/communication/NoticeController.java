package nl.tudelft.sem.room.communication;

import java.util.List;
import nl.tudelft.sem.room.entity.RoomNotice;
import nl.tudelft.sem.room.repository.NoticeRepository;
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
@RequestMapping("/room/notice")
public class NoticeController {

    private final transient NoticeRepository noticeRepo;

    @Autowired
    public NoticeController(NoticeRepository repo) {
        this.noticeRepo = repo;
    }

    /**
     * Leave a message in the database regarding maintenance of the room.
     *
     * @param token authentication token of the user
     * @param reservationId id of the reservation which the user faced problems in.
     * @param message the actual message
     * @return a success message if the process was successfull, return an error message otherwise
     */
    @PostMapping("/leaveNotice")
    public String leaveNotice(@RequestHeader("Authorization") String token,
                              @RequestParam long reservationId,
                              @RequestParam String message) {

        long userId = UserCommunication.getUserId(token);
        //if the user is the owner of the reservation, save a new notice in NoticeRepository
        if (ReservationCommunication
                .checkUserToReservation(userId, reservationId, token)) {
            long roomId = ReservationCommunication
                    .getRoomWithReservation(reservationId, token);
            RoomNotice notice = new RoomNotice(roomId, reservationId, message);
            noticeRepo.save(notice);
            return "Notice saved successfully";
        } else {
            return "There was an error";
        }
    }

    /**
     * Retrieve all the notices left for a room.
     *
     * @param token authentication token of the user
     * @param roomId if of the room to retrieve the messages
     * @return the list of RoomNotice
     */
    @GetMapping("/getNotice")
    public List<RoomNotice> getNotice(@RequestHeader("Authorization") String token,
                                      @RequestParam long roomId) {

        if (UserCommunication.getUserType(token).equals("Admin")) {
            List<RoomNotice> notices = noticeRepo.findByRoomId(roomId);
            return notices;
        } else {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "You do not have access to maintenance notices");
        }
    }
}
