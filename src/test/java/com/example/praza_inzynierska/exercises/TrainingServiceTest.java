package com.example.praza_inzynierska.exercises;

import com.example.praza_inzynierska.training.dto.AddTrainingBlockRequest;
import com.example.praza_inzynierska.training.dto.ExerciseToTrainingRequest;
import com.example.praza_inzynierska.training.dto.TrainingRequest;
import com.example.praza_inzynierska.training.models.Exercise;
import com.example.praza_inzynierska.training.models.Training;
import com.example.praza_inzynierska.training.models.TrainingExercise;
import com.example.praza_inzynierska.training.repositories.ExerciseRepository;
import com.example.praza_inzynierska.training.repositories.TrainingExerciseRepository;
import com.example.praza_inzynierska.training.repositories.TrainingRepository;
import com.example.praza_inzynierska.training.services.TrainingService;
import com.example.praza_inzynierska.user.models.User;
import com.example.praza_inzynierska.user.repositories.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrainingServiceTest {

    @Mock
    private TrainingRepository trainingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TrainingExerciseRepository trainingExerciseRepository;

    @Mock
    private ExerciseRepository exerciseRepository;

    @InjectMocks
    private TrainingService trainingService;

    private static ExerciseToTrainingRequest request;
    private static AddTrainingBlockRequest addTrainingBlockRequest;


    @BeforeAll
    static void beforeAll() {
        request = new ExerciseToTrainingRequest();
        request.setTrainingId(1L);
        request.setName("SampleExercise");
        request.setRepetition(10);
        request.setWeight(100L);
        addTrainingBlockRequest = new AddTrainingBlockRequest();
        addTrainingBlockRequest.setTrainingId(1L);
        addTrainingBlockRequest.setUserId(1L);
        addTrainingBlockRequest.setDate("2023-01-01");
    }

    @Test
    void getTrainingsByUserId_UserNotFound_ShouldReturnInternalServerError() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        ResponseEntity<List<Training>> response = trainingService.getTrainingsByUserId(userId);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void getTrainingsByUserId_Success_ShouldReturnTrainings() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(trainingRepository.findByUserId(userId)).thenReturn(List.of(new Training()));
        ResponseEntity<List<Training>> response = trainingService.getTrainingsByUserId(userId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, Objects.requireNonNull(response.getBody()).size());
    }

    @Test
    void getTrainingsByUserId_ThrowsException_ShouldReturnInternalServerError() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenThrow(RuntimeException.class);
        ResponseEntity<List<Training>> response = trainingService.getTrainingsByUserId(userId);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void addTrainingToUser_UserNotFound_ShouldReturnInternalServerError() {
        TrainingRequest model = new TrainingRequest();
        when(userRepository.findById(model.getUserId())).thenReturn(Optional.empty());
        ResponseEntity<Boolean> response = trainingService.addTrainingToUser(model);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void addTrainingToUser_TrainingExists_ShouldReturnFalse() {
        TrainingRequest model = new TrainingRequest();
        when(userRepository.findById(model.getUserId())).thenReturn(Optional.of(new User()));
        when(trainingRepository.findByName(model.getName())).thenReturn(Optional.of(new Training()));
        ResponseEntity<Boolean> response = trainingService.addTrainingToUser(model);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(false, response.getBody());
    }

    @Test
    void addTrainingToUser_Success_ShouldReturnTrue() {
        TrainingRequest model = new TrainingRequest();
        when(userRepository.findById(model.getUserId())).thenReturn(Optional.of(new User()));
        when(trainingRepository.findByName(model.getName())).thenReturn(Optional.empty());
        ResponseEntity<Boolean> response = trainingService.addTrainingToUser(model);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody());
    }

    @Test
    void addTrainingToUser_Failure_InternalErrror() {
        TrainingRequest model = new TrainingRequest();
        when(userRepository.findById(model.getUserId())).thenReturn(Optional.of(new User()));
        when(trainingRepository.findByName(model.getName())).thenReturn(Optional.empty());
        when(trainingRepository.save(Mockito.any())).thenThrow(new RuntimeException());
        ResponseEntity<Boolean> response = trainingService.addTrainingToUser(model);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void deleteTraining_TrainingNotFound_ShouldReturnInternalServerError() {
        Long trainingId = 1L;
        when(trainingRepository.findById(trainingId)).thenReturn(Optional.empty());
        ResponseEntity<Void> response = trainingService.deleteTraining(trainingId);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void deleteTraining_Success_ShouldReturnOk() {
        Long trainingId = 1L;
        when(trainingRepository.findById(trainingId)).thenReturn(Optional.of(new Training()));
        ResponseEntity<Void> response = trainingService.deleteTraining(trainingId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void fetchTrainingById_TrainingNotFound_ShouldReturnInternalServerError() {
        Long trainingId = 1L;
        when(trainingRepository.findById(trainingId)).thenReturn(Optional.empty());
        ResponseEntity<Training> response = trainingService.fetchTrainingById(trainingId);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void fetchTrainingById_Success_ShouldReturnTraining() {
        Long trainingId = 1L;
        Training expectedTraining = new Training();
        when(trainingRepository.findById(trainingId)).thenReturn(Optional.of(expectedTraining));
        ResponseEntity<Training> response = trainingService.fetchTrainingById(trainingId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedTraining, response.getBody());
    }

    @Test
    void deleteExerciseFromTrainingByName_TrainingNotFound_ShouldReturnInternalServerError() {
        Long trainingId = 1L;
        String exerciseName = "Squats";
        when(trainingRepository.findById(trainingId)).thenReturn(Optional.empty());
        ResponseEntity<Void> response = trainingService.deleteExerciseFromTrainingByName(trainingId, exerciseName);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void deleteTraining_ShouldReturnInternalServerError_WhenExceptionThrown() {
        Long trainingId = 1L;
        doThrow(RuntimeException.class).when(trainingRepository).findById(anyLong());
        ResponseEntity<Void> response = trainingService.deleteTraining(trainingId);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode(),
                "Expected INTERNAL_SERVER_ERROR status");
    }

    @Test
    void fetchTrainingById_ShouldReturnInternalServerError_WhenExceptionThrown() {
        Long trainingId = 1L;
        when(trainingRepository.findById(anyLong())).thenThrow(RuntimeException.class);
        ResponseEntity<Training> response = trainingService.fetchTrainingById(trainingId);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode(),
                "Expected INTERNAL_SERVER_ERROR status");
    }

    @Test
    public void testDeleteExerciseFromTrainingByNameNotFound() {
        when(trainingRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Void> response = trainingService.deleteExerciseFromTrainingByName(1L, "SampleExercise");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testDeleteExerciseFromTrainingByNameSuccess() {
        Training mockTraining;
        TrainingExercise mockExercise;
        mockTraining = new Training();
        mockExercise = new TrainingExercise();
        mockExercise.setName("SampleExercise");
        mockTraining.setExercises(List.of(mockExercise));
        when(trainingRepository.findById(anyLong())).thenReturn(Optional.of(mockTraining));
        doNothing().when(trainingExerciseRepository).deleteAll(anyList());
        ResponseEntity<Void> response = trainingService.deleteExerciseFromTrainingByName(1L, "SampleExercise");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(trainingExerciseRepository, times(1)).deleteAll(anyList());
    }

    @Test
    public void testDeleteExerciseFromTrainingByNameException() {
        when(trainingRepository.findById(anyLong())).thenThrow(new RuntimeException());
        ResponseEntity<Void> response = trainingService.deleteExerciseFromTrainingByName(1L, "SampleExercise");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testFetchExercisesByTrainingIdAndNameSuccess() {
        List<TrainingExercise> mockExercises;
        TrainingExercise mockExercise1 = new TrainingExercise();
        TrainingExercise mockExercise2 = new TrainingExercise();
        mockExercises = Arrays.asList(mockExercise1, mockExercise2);
        when(trainingExerciseRepository.findByTrainingIdAndName(anyLong(), anyString())).thenReturn(mockExercises);
        ResponseEntity<List<TrainingExercise>> response = trainingService.fetchExercisesByTrainingIdAndName(1L, "ExerciseName");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testFetchExercisesByTrainingIdAndNameException() {
        when(trainingExerciseRepository.findByTrainingIdAndName(anyLong(), anyString())).thenThrow(new RuntimeException());
        ResponseEntity<List<TrainingExercise>> response = trainingService.fetchExercisesByTrainingIdAndName(1L, "ExerciseName");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody() == null || response.getBody().isEmpty());
    }

    @Test
    public void testAddExerciseToTrainingTrainingNotFound() {
        when(trainingRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Void> response = trainingService.addExerciseToTraining(request);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testAddExerciseToTrainingSuccess() {
        when(trainingRepository.findById(anyLong())).thenReturn(Optional.of(new Training()));
        when(trainingExerciseRepository.save(any(TrainingExercise.class))).thenReturn(new TrainingExercise());
        ResponseEntity<Void> response = trainingService.addExerciseToTraining(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(trainingExerciseRepository, times(1)).save(any(TrainingExercise.class));
    }

    @Test
    public void testAddExerciseToTrainingException() {
        when(trainingRepository.findById(anyLong())).thenThrow(new RuntimeException());
        ResponseEntity<Void> response = trainingService.addExerciseToTraining(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testAddTrainingBlockToDayTrainingNotFound() {
        when(trainingRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Void> response = trainingService.addTrainingBlockToDay(addTrainingBlockRequest);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testAddTrainingBlockToDayUserNotFound() {
        when(trainingRepository.findById(anyLong())).thenReturn(Optional.of(new Training()));
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Void> response = trainingService.addTrainingBlockToDay(addTrainingBlockRequest);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testAddTrainingBlockToDaySuccess() {
        AddTrainingBlockRequest addTrainingBlockRequest = new AddTrainingBlockRequest();
        addTrainingBlockRequest.setTrainingId(1L);
        addTrainingBlockRequest.setUserId(2L);
        Training training = new Training();
        training.setExercises(new ArrayList<>());
        User user = new User();
        when(trainingRepository.findById(anyLong())).thenReturn(Optional.of(training));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        ResponseEntity<Void> response = trainingService.addTrainingBlockToDay(addTrainingBlockRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(exerciseRepository, times(training.getExercises().size())).save(any(Exercise.class));
    }

    @Test
    public void testAddTrainingBlockToDayException() {
        when(trainingRepository.findById(anyLong())).thenThrow(new RuntimeException());
        ResponseEntity<Void> response = trainingService.addTrainingBlockToDay(addTrainingBlockRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}


