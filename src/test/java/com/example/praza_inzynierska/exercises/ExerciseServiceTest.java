package com.example.praza_inzynierska.exercises;

import com.example.praza_inzynierska.training.dto.ExerciseRequest;
import com.example.praza_inzynierska.training.models.BaseAppExercises;
import com.example.praza_inzynierska.training.models.Exercise;
import com.example.praza_inzynierska.training.models.UserExercise;
import com.example.praza_inzynierska.training.repositories.BaseAppExercisesRepository;
import com.example.praza_inzynierska.training.repositories.ExerciseRepository;
import com.example.praza_inzynierska.training.repositories.UserExerciseRepository;
import com.example.praza_inzynierska.training.services.ExerciseService;
import com.example.praza_inzynierska.user.models.User;
import com.example.praza_inzynierska.user.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExerciseServiceTest {

    @Mock
    private ExerciseRepository exerciseRepository;

    @Mock
    private BaseAppExercisesRepository baseAppExercisesRepository;

    @Mock
    private UserExerciseRepository userExerciseRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ExerciseService exerciseService;

    @Test
    public void testAddExercise_Success() {
        ExerciseRequest model = new ExerciseRequest();
        model.setUserId(1L);
        User user = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        ResponseEntity<Void> responseEntity = exerciseService.addExercise(model);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Mockito.verify(exerciseRepository, Mockito.times(1)).save(Mockito.any(Exercise.class));
    }

    @Test
    public void testAddExercise_UserNotFound() {
        ExerciseRequest model = new ExerciseRequest();
        model.setUserId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        ResponseEntity<Void> responseEntity = exerciseService.addExercise(model);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        Mockito.verify(exerciseRepository, Mockito.never()).save(Mockito.any(Exercise.class));
    }

    @Test
    public void testAddExercise_ExceptionThrown() {
        ExerciseRequest model = new ExerciseRequest();
        model.setUserId(1L);
        when(userRepository.findById(1L)).thenThrow(RuntimeException.class);
        ResponseEntity<Void> responseEntity = exerciseService.addExercise(model);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        Mockito.verify(exerciseRepository, Mockito.never()).save(Mockito.any(Exercise.class));
    }

    @Test
    public void testFetchExercisesByDateAndName_Success() {
        Long userId = 1L;
        String date = "2024-02-25";
        String name = "Pushups";
        User user = new User();
        Exercise exercise = new Exercise();
        List<Exercise> exercises = new ArrayList<>();
        exercises.add(exercise);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(exerciseRepository.findByDateAndUserIdAndName(date, userId, name)).thenReturn(exercises);
        ResponseEntity<List<Exercise>> responseEntity = exerciseService.fetchExercisesByDateAndName(userId, date, name);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(exercises, responseEntity.getBody());
    }

    @Test
    public void testFetchExercisesByDateAndName_UserNotFound() {
        Long userId = 1L;
        String date = "2024-02-25";
        String name = "Pushups";
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        ResponseEntity<List<Exercise>> responseEntity = exerciseService.fetchExercisesByDateAndName(userId, date, name);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }

    @Test
    public void testFetchExercisesByDateAndName_ExceptionThrown() {
        Long userId = 1L;
        String date = "2024-02-25";
        String name = "Pushups";
        when(userRepository.findById(userId)).thenThrow(RuntimeException.class);
        ResponseEntity<List<Exercise>> responseEntity = exerciseService.fetchExercisesByDateAndName(userId, date, name);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }

    @Test
    public void testFetchExercisesByDate_Success() {
        Long userId = 1L;
        String date = "2024-02-25";
        User user = new User();
        Exercise exercise = new Exercise();
        List<Exercise> exercises = new ArrayList<>();
        exercises.add(exercise);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(exerciseRepository.findByDateAndUserId(date, userId)).thenReturn(exercises);
        ResponseEntity<List<Exercise>> responseEntity = exerciseService.fetchExercisesByDate(userId, date);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(exercises, responseEntity.getBody());
    }

    @Test
    public void testFetchExercisesByDate_UserNotFound() {
        Long userId = 1L;
        String date = "2024-02-25";
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        ResponseEntity<List<Exercise>> responseEntity = exerciseService.fetchExercisesByDate(userId, date);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }

    @Test
    public void testFetchExercisesByDate_ExceptionThrown() {
        Long userId = 1L;
        String date = "2024-02-25";
        when(userRepository.findById(userId)).thenThrow(RuntimeException.class);
        ResponseEntity<List<Exercise>> responseEntity = exerciseService.fetchExercisesByDate(userId, date);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }

    @Test
    public void testDeleteExerciseById_Success() {
        Long exerciseId = 1L;
        Exercise exercise = new Exercise();
        when(exerciseRepository.findById(exerciseId)).thenReturn(Optional.of(exercise));
        ResponseEntity<Void> responseEntity = exerciseService.deleteExerciseById(exerciseId);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Mockito.verify(exerciseRepository, Mockito.times(1)).deleteById(exerciseId);
    }

    @Test
    public void testDeleteExerciseById_ExerciseNotFound() {
        Long exerciseId = 1L;
        when(exerciseRepository.findById(exerciseId)).thenReturn(Optional.empty());
        ResponseEntity<Void> responseEntity = exerciseService.deleteExerciseById(exerciseId);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        Mockito.verify(exerciseRepository, Mockito.never()).deleteById(Mockito.anyLong());
    }

    @Test
    public void testDeleteExerciseById_ExceptionThrown() {
        Long exerciseId = 1L;
        when(exerciseRepository.findById(exerciseId)).thenThrow(RuntimeException.class);
        ResponseEntity<Void> responseEntity = exerciseService.deleteExerciseById(exerciseId);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        Mockito.verify(exerciseRepository, Mockito.never()).deleteById(Mockito.anyLong());
    }

    @Test
    public void testDeleteExerciseByDateAndName_Success() {
        String date = "2024-02-25";
        String name = "Pushups";
        ResponseEntity<Void> responseEntity = exerciseService.deleteExerciseByDateAndName(date, name);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Mockito.verify(exerciseRepository, Mockito.times(1)).deleteByDateAndName(date, name);
    }

    @Test
    public void testDeleteExerciseByDateAndName_ExceptionThrown() {
        String date = "2024-02-25";
        String name = "Pushups";
        Mockito.doThrow(RuntimeException.class).when(exerciseRepository).deleteByDateAndName(date, name);
        ResponseEntity<Void> responseEntity = exerciseService.deleteExerciseByDateAndName(date, name);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        Mockito.verify(exerciseRepository, Mockito.times(1)).deleteByDateAndName(date, name);
    }

    @Test
    public void testFetchAvailableExercises_Success() {
        long userId = 1L;
        List<BaseAppExercises> baseAppExercisesList = List.of(new BaseAppExercises(1, "Exercise1"), new BaseAppExercises(2, "Exercise2"));
        List<UserExercise> userExercisesList = List.of(new UserExercise(1L, new User(), "UserExercise1"), new UserExercise(2L, new User(), "UserExercise2"));
        when(baseAppExercisesRepository.findAll()).thenReturn(baseAppExercisesList);
        when(userExerciseRepository.findByUserId(userId)).thenReturn(userExercisesList);
        ResponseEntity<List<String>> responseEntity = exerciseService.fetchAvailableExercises(userId);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<String> expected = baseAppExercisesList.stream().map(BaseAppExercises::getName).collect(Collectors.toList());
        expected.addAll(userExercisesList.stream().map(UserExercise::getName).toList());
        assertEquals(expected, responseEntity.getBody());
    }

    @Test
    public void testFetchAvailableExercises_ExceptionThrown() {
        long userId = 1L;
        when(baseAppExercisesRepository.findAll()).thenThrow(RuntimeException.class);
        ResponseEntity<List<String>> responseEntity = exerciseService.fetchAvailableExercises(userId);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }

    @Test
    public void testFetchChartExercises_Success() {
        Long userId = 1L;
        String date = "2024-02";
        String name = "Pushups";
        List<Exercise> targetMonthExercises = new ArrayList<>();
        targetMonthExercises.add(new Exercise(1L, null, name, date + "-01", 10, 20.0));
        targetMonthExercises.add(new Exercise(1L, null, name, date + "-02", 10, 20.0));
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(exerciseRepository.findByUserIdAndNameAndDateContaining(userId, name, date)).thenReturn(targetMonthExercises);
        ResponseEntity<List<Exercise>> responseEntity = exerciseService.fetchChartExercises(userId, date, name);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<Exercise> expected = new ArrayList<>();
        expected.add(new Exercise(1L, null, date + "-01", name, 12, 22.5));
        expected.add(new Exercise(2L, null, date + "-02", name, 12, 22.5));
        List<Exercise> actual = responseEntity.getBody();
        assertEquals(expected.size(), actual.size());
    }

    @Test
    public void testFetchChartExercises_UserNotFound() {
        Long userId = 1L;
        String date = "2024-02";
        String name = "Pushups";
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        ResponseEntity<List<Exercise>> responseEntity = exerciseService.fetchChartExercises(userId, date, name);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }

    @Test
    public void testFetchChartExercises_ExceptionThrown() {
        Long userId = 1L;
        String date = "2024-02";
        String name = "Pushups";
        when(userRepository.findById(userId)).thenThrow(RuntimeException.class);
        ResponseEntity<List<Exercise>> responseEntity = exerciseService.fetchChartExercises(userId, date, name);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }
}


