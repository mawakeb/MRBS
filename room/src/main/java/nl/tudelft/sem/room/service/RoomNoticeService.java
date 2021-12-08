package nl.tudelft.sem.room.service;

import nl.tudelft.sem.room.communication.ReservationCommunication;
import nl.tudelft.sem.room.entity.RoomNotice;
import nl.tudelft.sem.room.repository.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomNoticeService implements RoomService {

    private final transient NoticeRepository noticeRepo;

    @Autowired
    public RoomNoticeService(NoticeRepository repo) {
        this.noticeRepo = repo;
    }

    public void leaveNotice(long userId, long reservationId, String message) {

        //if so, save a new notice in NoticeRepository
        if (ReservationCommunication.checkUserToReservation(userId, reservationId)) {
            RoomNotice notice = new RoomNotice(userId, reservationId, message);
            noticeRepo.save(notice);
        }
    }
}
