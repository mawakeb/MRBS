package nl.tudelft.sem.room.communication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalTime;
import nl.tudelft.sem.room.entity.Building;
import nl.tudelft.sem.room.repository.BuildingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class BuildingControllerTest {



    @Mock
    transient BuildingRepository buildingRepo;

    transient BuildingController controller;
    transient BuildingController spyController;
    private transient Building building;

    @BeforeEach
    void setUp() {

        controller = new BuildingController(buildingRepo);
        building = new Building(10L, "building",
                LocalTime.of(8, 0), LocalTime.of(18, 0));

        //mock static communication methods
        spyController = Mockito.spy(controller);
    }

    @Test
    void createBuilding() {
        lenient().doReturn(true).when(spyController).checkAdmin(Mockito.any());
        when(buildingRepo.findById(11L)).thenReturn(null);
        String success = spyController.createBuilding("token", 11L, "building2",
                LocalTime.of(8, 0).toString(), LocalTime.of(18, 0).toString());
        assertEquals("Building has been saved successfully", success);
        verify(buildingRepo, times(1)).save(any(Building.class));
    }

    @Test
    void createBuildingButNotAdmin() {
        lenient().doReturn(false).when(spyController).checkAdmin(Mockito.any());
        assertThrows(ResponseStatusException.class, () -> {
            spyController.createBuilding("token", 11L, "building3",
                    LocalTime.of(8, 0).toString(), LocalTime.of(18, 0).toString());
        });
    }

    @Test
    void createBuildingButAlreadyExists() {
        lenient().doReturn(true).when(spyController).checkAdmin(Mockito.any());
        when(buildingRepo.findById(11L)).thenReturn(building);
        assertThrows(ResponseStatusException.class, () -> {
            spyController.createBuilding("token", 11L, "building4",
                    LocalTime.of(8, 0).toString(), LocalTime.of(18, 0).toString());
        });
    }

    @Test
    void checkAvailable() {
        when(buildingRepo.findById(1L)).thenReturn(building);
        boolean success = controller.checkAvailable(1L, LocalTime.of(9, 0).toString(),
                LocalTime.of(11, 0).toString());
        assertTrue(success);
    }

    @Test
    void checkNotAvailable() {
        when(buildingRepo.findById(1L)).thenReturn(null);
        boolean success = controller.checkAvailable(1L, LocalTime.of(9, 0).toString(),
                LocalTime.of(11, 0).toString());
        assertFalse(success);
    }
}