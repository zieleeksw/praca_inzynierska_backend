package com.example.praza_inzynierska.food;

import com.example.praza_inzynierska.food.dto.FoodRequest;
import com.example.praza_inzynierska.food.dto.FoodResponse;
import com.example.praza_inzynierska.food.models.BaseAppFood;
import com.example.praza_inzynierska.food.models.Food;
import com.example.praza_inzynierska.food.models.UserFood;
import com.example.praza_inzynierska.food.repositories.BaseAppFoodRepository;
import com.example.praza_inzynierska.food.repositories.FoodRepository;
import com.example.praza_inzynierska.food.repositories.UserFoodRepository;
import com.example.praza_inzynierska.food.services.FoodService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FoodServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private FoodRepository foodRepository;

    @Mock
    private BaseAppFoodRepository baseAppFoodRepository;

    @Mock
    private UserFoodRepository userFoodRepository;

    @InjectMocks
    private FoodService foodService;

    @Test
    public void shouldReturnOkWhenUserFoundAndFoodSaved() {
        FoodRequest foodRequest = new FoodRequest();
        foodRequest.setAuthorId(1L);
        User user = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(foodRepository.save(any(Food.class))).thenReturn(new Food());
        ResponseEntity<Void> response = foodService.addFood(foodRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Mockito.verify(foodRepository, times(1)).save(any(Food.class));
    }

    @Test
    public void shouldReturnInternalServerErrorWhenUserNotFound() {
        FoodRequest foodRequest = new FoodRequest();
        foodRequest.setAuthorId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        ResponseEntity<Void> response = foodService.addFood(foodRequest);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void shouldReturnInternalServerErrorWhenExceptionThrown() {
        FoodRequest foodRequest = new FoodRequest();
        foodRequest.setAuthorId(1L);
        when(userRepository.findById(1L)).thenThrow(RuntimeException.class);
        ResponseEntity<Void> response = foodService.addFood(foodRequest);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void shouldReturnFoodListForValidUserAndDate() {
        Long userId = 1L;
        String date = "2023-01-01";
        List<Food> expectedFoodList = List.of(new Food());
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(foodRepository.findByDateAndUserId(date, userId)).thenReturn(expectedFoodList);
        ResponseEntity<List<Food>> response = foodService.fetchFoodByDate(userId, date);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedFoodList, response.getBody());
    }

    @Test
    public void shouldReturnInternalServerErrorWhenUserNotFoundForFetch() {
        Long userId = 1L;
        String date = "2023-01-01";
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        ResponseEntity<List<Food>> response = foodService.fetchFoodByDate(userId, date);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void shouldReturnInternalServerErrorOnExceptionForFetch() {
        Long userId = 1L;
        String date = "2023-01-01";
        when(userRepository.findById(userId)).thenThrow(new RuntimeException());
        ResponseEntity<List<Food>> response = foodService.fetchFoodByDate(userId, date);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void shouldReturnOkWhenFoodDeletedSuccessfully() {
        Long foodId = 1L;
        when(foodRepository.findById(foodId)).thenReturn(Optional.of(new Food()));
        ResponseEntity<Void> response = foodService.deleteFoodById(foodId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(foodRepository).deleteById(foodId);
    }

    @Test
    public void shouldReturnInternalServerErrorWhenFoodNotFoundForDelete() {
        Long foodId = 1L;
        when(foodRepository.findById(foodId)).thenReturn(Optional.empty());
        ResponseEntity<Void> response = foodService.deleteFoodById(foodId);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void shouldReturnInternalServerErrorOnExceptionForDelete() {
        Long foodId = 1L;
        when(foodRepository.findById(foodId)).thenThrow(new RuntimeException());
        ResponseEntity<Void> response = foodService.deleteFoodById(foodId);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void shouldReturnCombinedFoodListForUser() {
        long userId = 1L;
        List<BaseAppFood> baseAppFoods = List.of(new BaseAppFood());
        List<UserFood> userFoods = List.of(new UserFood());
        when(baseAppFoodRepository.findAll()).thenReturn(baseAppFoods);
        when(userFoodRepository.findByUserId(userId)).thenReturn(userFoods);
        ResponseEntity<List<FoodResponse>> response = foodService.fetchAvailableFood(userId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void shouldReturnInternalServerErrorWhenExceptionOccursInFetchAvailableFood() {
        long userId = 1L;
        when(baseAppFoodRepository.findAll()).thenThrow(new RuntimeException());
        ResponseEntity<List<FoodResponse>> response = foodService.fetchAvailableFood(userId);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void shouldReturnFoodResponseWhenBaseAppFoodFoundByName() {
        String name = "Apple";
        BaseAppFood baseAppFood = new BaseAppFood();
        when(baseAppFoodRepository.findByProductName(name)).thenReturn(Optional.of(baseAppFood));
        ResponseEntity<FoodResponse> response = foodService.findFoodByName(name);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void shouldReturnFoodResponseWhenUserFoodFoundByName() {
        String name = "Banana";
        when(baseAppFoodRepository.findByProductName(name)).thenReturn(Optional.empty());
        UserFood userFood = UserFood.builder().productName("Banana").build();
        when(userFoodRepository.findByProductName(name)).thenReturn(Optional.of(userFood));
        ResponseEntity<FoodResponse> response = foodService.findFoodByName(name);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void shouldReturnInternalServerErrorWhenNoFoodFoundByName() {
        String name = "NonexistentFood";
        when(baseAppFoodRepository.findByProductName(name)).thenReturn(Optional.empty());
        when(userFoodRepository.findByProductName(name)).thenReturn(Optional.empty());
        ResponseEntity<FoodResponse> response = foodService.findFoodByName(name);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void shouldReturnInternalServerErrorWhenExceptionOccursInFindFoodByName() {
        String name = "ErrorProneFood";
        when(baseAppFoodRepository.findByProductName(name)).thenThrow(new RuntimeException());
        ResponseEntity<FoodResponse> response = foodService.findFoodByName(name);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void shouldReturnOkWithDailySumsWhenUserFound() {
        Long userId = 1L;
        String date = "2023-02";
        User user = new User();
        List<Food> foods = Arrays.asList(
                Food.builder().date("2023-02-01").build(),
                Food.builder().date("2023-02-02").build()
        );
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(foodRepository.findByUserIdAndDateContaining(userId, date)).thenReturn(foods);
        ResponseEntity<List<Food>> response = foodService.fetchChartFood(userId, date);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).size() > 0);
    }

    @Test
    public void shouldReturnNotFoundWhenUserNotPresent() {
        Long userId = 1L;
        String date = "2023-02";
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        ResponseEntity<List<Food>> response = foodService.fetchChartFood(userId, date);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void shouldReturnInternalServerErrorOnException() {
        Long userId = 1L;
        String date = "2023-02";
        when(userRepository.findById(anyLong())).thenThrow(new RuntimeException());
        ResponseEntity<List<Food>> response = foodService.fetchChartFood(userId, date);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

}
