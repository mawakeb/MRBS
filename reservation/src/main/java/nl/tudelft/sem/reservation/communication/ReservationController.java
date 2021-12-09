package nl.tudelft.sem.reservation.communication;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import nl.tudelft.sem.reservation.entity.Reservation;
import nl.tudelft.sem.reservation.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("")
public class ReservationController {


    private final transient ReservationRepository reservationRepo;
    protected static Gson gson = new Gson();

    @Autowired
    public ReservationController(ReservationRepository reservationRepo) {
        this.reservationRepo = reservationRepo;
    }


    /**
     * Gets all lectures.
     *
     * @return all lectures
     */
    @GetMapping
    public String returnHi() {
        //return lectureRepository.findAll();
        return "hello_from_reservation";
    }

    @GetMapping("checkUser")
    public boolean checkUser(@RequestBody String q) {
        List<String> list = gson.fromJson(q, new TypeToken<List<String>>() {}.getType());
        long reservationId = Long.parseLong(list.get(1));
        Reservation reservation = reservationRepo.findById(reservationId).orElse(null);
        if (reservation!=null){
            long userId = Long.parseLong(list.get(0));
            if (reservation.getUserId().equals(userId)) return true;
            else return false;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "RESERVATION_NOT_FOUND");
        }
    }

    /*@GetMapping("getByJoinCode")
    public Lecture getAllLecturesByJoinCode(String joinCode) {
        List<Lecture> lectureList = lectureRepository.findAllByJoinCodeEquals(joinCode);
        if (!lectureList.isEmpty()) {
            return lectureList.get(0);
        } else {
            return null;
        }
    }*/


}
