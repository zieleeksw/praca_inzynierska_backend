package com.example.praza_inzynierska.exercises;

import com.example.praza_inzynierska.training.dto.UserExerciseRequest;
import com.example.praza_inzynierska.training.models.BaseAppExercises;
import com.example.praza_inzynierska.training.models.UserExercise;
import com.example.praza_inzynierska.training.repositories.BaseAppExercisesRepository;
import com.example.praza_inzynierska.training.repositories.UserExerciseRepository;
import com.example.praza_inzynierska.training.services.UserExercisesService;
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

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserExercisesServiceTest {


    @Mock
    private UserExerciseRepository userExerciseRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BaseAppExercisesRepository baseAppExercisesRepository;

    @InjectMocks
    private UserExercisesService userExercisesService;

    @Test
    void fetchUserExercises_ReturnsUserExercisesList() {
        long userId = 1L;
        List<UserExercise> userExercises = Arrays.asList(new UserExercise(), new UserExercise());
        when(userExerciseRepository.findByUserId(userId)).thenReturn(userExercises);
        ResponseEntity<List<UserExercise>> response = userExercisesService.fetchUserExercises(userId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userExercises.size(), Objects.requireNonNull(response.getBody()).size());
    }

    @Test
    void fetchUserExercises_ThrowsException_ShouldReturnInternalServerError() {
        long userId = 1L;
        when(userExerciseRepository.findByUserId(userId)).thenThrow(RuntimeException.class);

        ResponseEntity<List<UserExercise>> response = userExercisesService.fetchUserExercises(userId);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void addUserExercise_UserNotFound_ShouldReturnInternalServerError() {
        UserExerciseRequest model = new UserExerciseRequest();
        when(userRepository.findById(model.getUserId())).thenReturn(Optional.empty());
        ResponseEntity<Boolean> response = userExercisesService.addUserExercise(model);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void addUserExercise_UserExerciseExists_ShouldReturnFalse() {
        UserExerciseRequest model = new UserExerciseRequest();
        when(userRepository.findById(model.getUserId())).thenReturn(Optional.of(new User()));
        when(userExerciseRepository.findByName(model.getName())).thenReturn(new UserExercise());
        ResponseEntity<Boolean> response = userExercisesService.addUserExercise(model);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(false, response.getBody());
    }

    @Test
    void addUserExercise_BaseAppExerciseExists_ShouldReturnFalse() {
        UserExerciseRequest model = new UserExerciseRequest();
        when(userRepository.findById(model.getUserId())).thenReturn(Optional.of(new User()));
        when(userExerciseRepository.findByName(model.getName())).thenReturn(null);
        when(baseAppExercisesRepository.findByName(model.getName())).thenReturn(new BaseAppExercises());
        ResponseEntity<Boolean> response = userExercisesService.addUserExercise(model);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(false, response.getBody());
    }

    @Test
    void addUserExercise_ThrowsException_ShouldReturnInternalServerError() {
        UserExerciseRequest model = new UserExerciseRequest(); // Ustaw odpowiednie pola
        when(userRepository.findById(model.getUserId())).thenThrow(RuntimeException.class);
        ResponseEntity<Boolean> response = userExercisesService.addUserExercise(model);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void deleteUserExercise_UserExerciseNotFound_ShouldReturnInternalServerError() {
        Long userId = 1L;
        when(userExerciseRepository.findById(userId)).thenReturn(Optional.empty());
        ResponseEntity<Void> response = userExercisesService.deleteUserExercise(userId);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void deleteUserExercise_ThrowsException_ShouldReturnInternalServerError() {
        Long userId = 1L;
        when(userExerciseRepository.findById(userId)).thenThrow(RuntimeException.class);
        ResponseEntity<Void> response = userExercisesService.deleteUserExercise(userId);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void addUserExercise_SavesUserExercise_ShouldReturnTrue() {
        UserExerciseRequest model = new UserExerciseRequest();
        model.setUserId(1L);
        model.setName("New Exercise");
        User mockUser = new User();
        when(userRepository.findById(model.getUserId())).thenReturn(Optional.of(mockUser));
        when(userExerciseRepository.findByName(model.getName())).thenReturn(null);
        when(baseAppExercisesRepository.findByName(model.getName())).thenReturn(null);
        ResponseEntity<Boolean> response = userExercisesService.addUserExercise(model);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody());
        Mockito.verify(userExerciseRepository).save(Mockito.any(UserExercise.class));
    }

    @Test
    void deleteUserExercise_DeletesUserExercise_ShouldReturnOk() {
        Long userId = 1L;
        UserExercise mockExercise = new UserExercise();
        when(userExerciseRepository.findById(userId)).thenReturn(Optional.of(mockExercise));
        ResponseEntity<Void> response = userExercisesService.deleteUserExercise(userId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Mockito.verify(userExerciseRepository).deleteById(userId);
    }
}
