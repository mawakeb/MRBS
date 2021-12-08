package nl.tudelft.sem.room.service;

import com.google.gson.Gson;
import nl.tudelft.sem.room.communication.ReservationCommunication;
import nl.tudelft.sem.room.entity.RoomNotice;
import nl.tudelft.sem.room.repository.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomNoticeService implements RoomService {

    private final transient NoticeRepository noticeRepo;
    protected static Gson gson = new Gson();

    @Autowired
    public RoomNoticeService(NoticeRepository repo) {
        this.noticeRepo = repo;
    }

    public void leaveNotice(long userId, long reservationId, String message) {

        //make a list containing userId and reservationId to check if the user is the one who made the reservation
        List<String> sendList = List.of(
                Long.toString(userId),
                Long.toString(reservationId)
        );
        String parsedList = gson.toJson(sendList);

        //if so, save a new notice in NoticeRepository
        if (ReservationCommunication.checkUserToReservation(parsedList)) {
            RoomNotice notice = new RoomNotice(userId, reservationId, message);
            noticeRepo.save(notice);
        }
    }
}
