package nl.tudelft.sem.room.communication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;
import nl.tudelft.sem.room.entity.RoomNotice;
import nl.tudelft.sem.room.repository.NoticeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NoticeControllerTest {

    @Mock
    transient NoticeController controller;
    @Mock
    transient NoticeRepository noticeRepo;

    private transient List<RoomNotice> noticeList;

    @BeforeEach
    void setUp() {
        controller = new NoticeController(noticeRepo);
        noticeList = List.of(new RoomNotice(1L, 101L, "door is broken"),
                new RoomNotice(1L, 102L, "problems with computer"),
                new RoomNotice(2L, 103L, "window is broken"));
    }

    @Test
    void getNotice() {
        when(noticeRepo.findByRoomId(1L)).thenReturn(noticeList);
        List<RoomNotice> actual = controller.getNotice(1L);
        assertEquals(noticeList, actual);
    }
}