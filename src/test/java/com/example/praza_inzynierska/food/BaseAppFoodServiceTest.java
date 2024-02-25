package com.example.praza_inzynierska.food;

import com.example.praza_inzynierska.food.models.BaseAppFood;
import com.example.praza_inzynierska.food.repositories.BaseAppFoodRepository;
import com.example.praza_inzynierska.food.services.BaseAppFoodService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BaseAppFoodServiceTest {

    @Mock
    private BaseAppFoodRepository baseAppFoodRepository;

    @InjectMocks
    private BaseAppFoodService baseAppFoodService;

    @Test
    public void shouldReturnListOfBaseAppFood() {
        BaseAppFood food1 = new BaseAppFood();
        BaseAppFood food2 = new BaseAppFood();
        List<BaseAppFood> expectedList = Arrays.asList(food1, food2);
        when(baseAppFoodRepository.findAll()).thenReturn(expectedList);
        ResponseEntity<List<BaseAppFood>> response = baseAppFoodService.fetchAllBaseAppFood();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedList, response.getBody());
    }

    @Test
    public void shouldReturnInternalServerErrorOnException() {
        when(baseAppFoodRepository.findAll()).thenThrow(new RuntimeException());
        ResponseEntity<List<BaseAppFood>> response = baseAppFoodService.fetchAllBaseAppFood();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}

