package nl.tudelft.sem.reservation.communication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("reservation")
public class ReservationController {

    /*
    @Autowired
    LectureRepository lectureRepository;
    @Autowired
    UserRepository userRepository;*/

    /**
     * Gets all lectures.
     *
     * @return all lectures
     */
    @GetMapping
    public String returnHi() {
        //return lectureRepository.findAll();
        return "hello from reservation";
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
