package org.ebndrnk.leverxfinalproject.service.game;

import org.ebndrnk.leverxfinalproject.model.dto.auth.UserDto;
import org.ebndrnk.leverxfinalproject.model.dto.game.GameRequest;
import org.ebndrnk.leverxfinalproject.model.dto.game.GameResponse;
import org.ebndrnk.leverxfinalproject.model.entity.auth.User;
import org.ebndrnk.leverxfinalproject.model.entity.game.GameObject;
import org.ebndrnk.leverxfinalproject.repository.game.GameRepository;
import org.ebndrnk.leverxfinalproject.service.auth.user.UserService;
import org.ebndrnk.leverxfinalproject.util.exception.dto.GameNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameServiceImplTest {

    @InjectMocks
    private GameServiceImpl gameService;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private UserService userService;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateGameObject() {
        GameRequest request = new GameRequest();
        GameObject gameObject = new GameObject();
        GameResponse response = new GameResponse();
        UserDto userDto = new UserDto();
        User user = new User();

        when(userService.getCurrentUser()).thenReturn(userDto);
        when(modelMapper.map(userDto, User.class)).thenReturn(user);
        when(modelMapper.map(request, GameObject.class)).thenReturn(gameObject);

        gameObject.setSeller(user.getProfile());

        when(gameRepository.save(gameObject)).thenReturn(gameObject);
        when(modelMapper.map(gameObject, GameResponse.class)).thenReturn(response);

        GameResponse result = gameService.createGameObject(request);

        assertNotNull(result);
        verify(gameRepository).save(gameObject);
        verify(modelMapper).map(userDto, User.class);
    }


    @Test
    void testGetGameObjectById_GameExists() {
        Long gameId = 1L;
        GameObject gameObject = new GameObject();
        GameResponse response = new GameResponse();

        when(gameRepository.findById(gameId)).thenReturn(Optional.of(gameObject));
        when(modelMapper.map(gameObject, GameResponse.class)).thenReturn(response);

        GameResponse result = gameService.getGameObjectById(gameId);

        assertNotNull(result);
    }

    @Test
    void testGetGameObjectById_GameNotFound() {
        Long gameId = 1L;
        when(gameRepository.findById(gameId)).thenReturn(Optional.empty());

        assertThrows(GameNotFoundException.class, () -> gameService.getGameObjectById(gameId));
    }

    @Test
    void testDeleteGameObjectById_GameNotFound() {
        Long gameId = 1L;
        when(gameRepository.existsById(gameId)).thenReturn(false);

        assertThrows(GameNotFoundException.class, () -> gameService.deleteGameObjectById(gameId));
    }

}

