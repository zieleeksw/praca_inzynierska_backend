package com.example.praza_inzynierska.user;

import com.example.praza_inzynierska.user.models.NutritionConfig;
import com.example.praza_inzynierska.user.models.User;
import com.example.praza_inzynierska.user.repositories.NutritionConfigRepository;
import com.example.praza_inzynierska.user.repositories.UserRepository;
import com.example.praza_inzynierska.user.services.UserNutritionConfigService;
import com.example.praza_inzynierska.user.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserNutritionConfigServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private NutritionConfigRepository nutritionConfigRepository;


    @InjectMocks
    private UserNutritionConfigService service;

    @Test
    void addUserNutritionConfig_UserExists_ReturnsSuccessMessage() {
        long userId = 1L;
        NutritionConfig config = new NutritionConfig();
        User user = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        ResponseEntity<String> response = service.addUserNutritionConfig(userId, config);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User nutrition config added successfully.", response.getBody());
        Mockito.verify(nutritionConfigRepository, Mockito.times(1)).save(Mockito.any(NutritionConfig.class));
    }

    @Test
    void addUserNutritionConfig_UserNotFound_ReturnsErrorMessage() {
        long userId = 1L;
        NutritionConfig config = new NutritionConfig();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        ResponseEntity<String> response = service.addUserNutritionConfig(userId, config);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error adding user nutrition config.", response.getBody());
        Mockito.verify(nutritionConfigRepository, Mockito.never()).save(Mockito.any(NutritionConfig.class));
    }

    @Test
    void addUserNutritionConfig_ExceptionOccurs_ReturnsErrorMessage() {
        long userId = 1L;
        NutritionConfig config = new NutritionConfig();
        when(userRepository.findById(userId)).thenThrow(RuntimeException.class);
        ResponseEntity<String> response = service.addUserNutritionConfig(userId, config);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error adding user nutrition config.", response.getBody());
    }

    @Test
    void changeActivityLevel_UserAndConfigExist_ReturnsOk() {
        long userId = 1L;
        String newActivityLevel = "high";
        User user = new User();
        NutritionConfig config = new NutritionConfig();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(nutritionConfigRepository.findByUserId(userId)).thenReturn(config);
        ResponseEntity<Void> response = service.changeActivityLevel(userId, newActivityLevel);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Mockito.verify(nutritionConfigRepository, Mockito.times(1)).save(config);
        assertEquals(newActivityLevel, config.getActivityLevel());
    }

    @Test
    void fetchCaloriesNeeded_UserExists_ReturnsNutritionConfig() {
        long userId = 1L;
        User user = new User();
        NutritionConfig expectedConfig = new NutritionConfig();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(nutritionConfigRepository.findByUserId(userId)).thenReturn(expectedConfig);
        var response = service.fetchCaloriesNeeded(userId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedConfig, response.getBody());
    }

    @Test
    void fetchCaloriesNeeded_UserDoesNotExist_ReturnsInternalServerError() {
        long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        var response = service.fetchCaloriesNeeded(userId);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void fetchCaloriesNeeded_ExceptionOccurs_ReturnsInternalServerError() {
        long userId = 1L;
        when(userRepository.findById(userId)).thenThrow(RuntimeException.class);
        var response = service.fetchCaloriesNeeded(userId);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void changeTargetWeight_UserAndConfigExist_UpdatesConfigAndReturnsOk() {
        long userId = 1L;
        double targetWeight = 75.0;
        User user = new User();
        NutritionConfig config = new NutritionConfig();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(nutritionConfigRepository.findByUserId(userId)).thenReturn(config);
        ResponseEntity<Void> response = service.changeTargetWeight(userId, targetWeight);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Mockito.verify(nutritionConfigRepository).save(config);
        assertEquals(targetWeight, config.getTargetWeight());
    }

    @Test
    void changeCurrentWeight_UserAndConfigExist_UpdatesConfigAndReturnsOk() {
        long userId = 1L;
        double currentWeight = 75.0;
        User user = new User();
        NutritionConfig config = new NutritionConfig();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(nutritionConfigRepository.findByUserId(userId)).thenReturn(config);
        ResponseEntity<Void> response = service.changeCurrentWeight(userId, currentWeight);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Mockito.verify(nutritionConfigRepository).save(config);
        assertEquals(currentWeight, config.getCurrentWeight());
    }

    @Test
    void changeTargetWeight_UserExists_ConfigNotFound_ReturnsNotFound() {
        long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(nutritionConfigRepository.findByUserId(userId)).thenReturn(null);

        ResponseEntity<Void> response = service.changeTargetWeight(userId, 75.0);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Mockito.verify(nutritionConfigRepository, Mockito.never()).save(Mockito.any(NutritionConfig.class));
    }

    @Test
    void changeTargetWeight_UserNotFound_ReturnsNotFound() {
        long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        ResponseEntity<Void> response = service.changeTargetWeight(userId, 75.0);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void changeTargetWeight_ExceptionOccurs_ReturnsInternalServerError() {
        long userId = 1L;
        when(userRepository.findById(userId)).thenThrow(RuntimeException.class);
        ResponseEntity<Void> response = service.changeTargetWeight(userId, 75.0);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
