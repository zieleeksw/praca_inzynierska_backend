package com.example.praza_inzynierska.user;

import com.example.praza_inzynierska.user.dto.DimensionsRequest;
import com.example.praza_inzynierska.user.models.BodyDimensions;
import com.example.praza_inzynierska.user.models.User;
import com.example.praza_inzynierska.user.repositories.DimensionsRepository;
import com.example.praza_inzynierska.user.repositories.UserRepository;
import com.example.praza_inzynierska.user.services.DimensionsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DimensionsServiceTest {

    @InjectMocks
    private DimensionsService dimensionsService;

    @Mock
    private DimensionsRepository dimensionsRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    void fetchDimensions_UserExists_ReturnsDimensions() {
        Long userId = 1L;
        User user = new User();
        List<BodyDimensions> expectedDimensions = List.of(new BodyDimensions());
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(dimensionsRepository.findByUserId(userId)).thenReturn(expectedDimensions);
        var response = dimensionsService.fetchDimensions(userId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedDimensions, response.getBody());
    }

    @Test
    void fetchDimensions_UserDoesNotExist_ReturnsInternalServerError() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        var response = dimensionsService.fetchDimensions(userId);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void fetchDimensions_ExceptionOccurs_ReturnsInternalServerError() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenThrow(RuntimeException.class);
        var response = dimensionsService.fetchDimensions(userId);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void addDimensions_UserExists_SavesDimensions() {
        DimensionsRequest request = new DimensionsRequest();
        request.setUserId(1L);
        User user = new User();
        when(userRepository.findById(request.getUserId())).thenReturn(Optional.of(user));
        var response = dimensionsService.addDimensions(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(dimensionsRepository, times(1)).save(any(BodyDimensions.class));
    }

    @Test
    void addDimensions_UserDoesNotExist_ReturnsInternalServerError() {
        DimensionsRequest request = new DimensionsRequest();
        request.setUserId(1L);
        when(userRepository.findById(request.getUserId())).thenReturn(Optional.empty());
        var response = dimensionsService.addDimensions(request);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void addDimensions_ExceptionOccurs_ReturnsInternalServerError() {
        DimensionsRequest request = new DimensionsRequest();
        request.setUserId(1L);
        when(userRepository.findById(request.getUserId())).thenThrow(RuntimeException.class);
        var response = dimensionsService.addDimensions(request);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void deleteDimensionsById_DimensionExists_DeletesDimension() {
        Long dimensionId = 1L;
        BodyDimensions dimension = new BodyDimensions();
        when(dimensionsRepository.findById(dimensionId)).thenReturn(Optional.of(dimension));
        var response = dimensionsService.deleteDimensionsById(dimensionId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(dimensionsRepository, times(1)).deleteById(dimensionId);
    }

    @Test
    void deleteDimensionsById_DimensionDoesNotExist_ReturnsInternalServerError() {
        Long dimensionId = 1L;
        when(dimensionsRepository.findById(dimensionId)).thenReturn(Optional.empty());
        var response = dimensionsService.deleteDimensionsById(dimensionId);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void deleteDimensionsById_ExceptionOccurs_ReturnsInternalServerError() {
        Long dimensionId = 1L;
        when(dimensionsRepository.findById(dimensionId)).thenThrow(RuntimeException.class);
        var response = dimensionsService.deleteDimensionsById(dimensionId);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
