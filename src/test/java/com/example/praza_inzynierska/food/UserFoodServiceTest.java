package com.example.praza_inzynierska.food;


import com.example.praza_inzynierska.food.dto.UserFoodRequest;
import com.example.praza_inzynierska.food.models.BaseAppFood;
import com.example.praza_inzynierska.food.models.UserFood;
import com.example.praza_inzynierska.food.repositories.BaseAppFoodRepository;
import com.example.praza_inzynierska.food.repositories.UserFoodRepository;
import com.example.praza_inzynierska.food.services.UserFoodService;
import com.example.praza_inzynierska.user.models.User;
import com.example.praza_inzynierska.user.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class UserFoodServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BaseAppFoodRepository baseAppFoodRepository;

    @Mock
    private UserFoodRepository userFoodRepository;

    @InjectMocks
    private UserFoodService userFoodService;

    @Test
    public void fetchUserFood_ShouldReturnUserFoodList() {
        long userId = 1L;
        List<UserFood> expected = Arrays.asList(new UserFood(), new UserFood());
        when(userFoodRepository.findByUserId(userId)).thenReturn(expected);
        var response = userFoodService.fetchUserFood(userId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
    }

    @Test
    public void fetchUserFood_ShouldReturnInternalServerErrorOnException() {
        long userId = 1L;
        when(userFoodRepository.findByUserId(userId)).thenThrow(RuntimeException.class);
        var response = userFoodService.fetchUserFood(userId);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void addUserFood_ShouldReturnTrueWhenFoodAdded() {
        UserFoodRequest model = new UserFoodRequest();
        when(userRepository.findById(model.getUserId())).thenReturn(Optional.of(new User()));
        when(userFoodRepository.findByProductNameAndUserId(model.getProductName(), model.getUserId())).thenReturn(Optional.empty());
        when(baseAppFoodRepository.findByProductName(model.getProductName())).thenReturn(Optional.empty());
        var response = userFoodService.addUserFood(model);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody());
    }

    @Test
    public void addUserFood_ShouldReturnFalseWhenFoodExists() {
        UserFoodRequest model = new UserFoodRequest();
        when(userRepository.findById(model.getUserId())).thenReturn(Optional.of(new User()));
        when(userFoodRepository.findByProductNameAndUserId(model.getProductName(), model.getUserId())).thenReturn(Optional.of(new UserFood()));
        var response = userFoodService.addUserFood(model);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(false, response.getBody());
    }

    @Test
    public void addUserFood_ShouldReturnInternalServerErrorWhenUserNotFound() {
        UserFoodRequest model = new UserFoodRequest();
        when(userRepository.findById(model.getUserId())).thenReturn(Optional.empty());
        var response = userFoodService.addUserFood(model);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void deleteUserFoodByName_ShouldReturnOkWhenFoodDeleted() {
        Long userId = 1L;
        String foodName = "Apple";
        when(userFoodRepository.findByProductNameAndUserId(foodName, userId)).thenReturn(Optional.of(new UserFood()));
        var response = userFoodService.deleteUserFoodByName(userId, foodName);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userFoodRepository).deleteById(userId);
    }

    @Test
    public void deleteUserFoodByName_ShouldReturnInternalServerErrorWhenFoodNotFound() {
        long userId = 1L;
        String foodName = "Nonexistent";
        when(userFoodRepository.findByProductNameAndUserId(foodName, userId)).thenReturn(Optional.empty());
        var response = userFoodService.deleteUserFoodByName(userId, foodName);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void deleteUserFoodByName_ShouldReturnInternalServerErrorWhenExceptionOccurs() {
        long userId = 1L;
        String foodName = "Apple";
        when(userFoodRepository.findByProductNameAndUserId(foodName, userId)).thenThrow(new RuntimeException());
        var response = userFoodService.deleteUserFoodByName(userId, foodName);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void addUserFood_ShouldReturnFalseWithInternalServerErrorWhenExceptionOccurs() {
        UserFoodRequest model = new UserFoodRequest();
        when(userRepository.findById(model.getUserId())).thenThrow(new RuntimeException());
        var response = userFoodService.addUserFood(model);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(false, response.getBody());
    }

    @Test
    public void addUserFood_ShouldReturnFalseWhenBaseAppFoodAlreadyExists() {
        UserFoodRequest model = new UserFoodRequest();
        model.setUserId(1L);
        model.setProductName("ExistingBaseAppProduct");
        model.setKcal(100);
        model.setFat(10);
        model.setCarbs(20);
        model.setProteins(5);
        User user = new User();
        BaseAppFood baseAppFood = new BaseAppFood();
        when(userRepository.findById(model.getUserId())).thenReturn(Optional.of(user));
        when(userFoodRepository.findByProductNameAndUserId(model.getProductName(), model.getUserId())).thenReturn(Optional.empty());
        when(baseAppFoodRepository.findByProductName(model.getProductName())).thenReturn(Optional.of(baseAppFood));
        ResponseEntity<Boolean> response = userFoodService.addUserFood(model);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(false, response.getBody());
    }
}
