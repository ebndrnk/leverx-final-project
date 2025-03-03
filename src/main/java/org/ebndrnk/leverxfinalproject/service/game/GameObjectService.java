package org.ebndrnk.leverxfinalproject.service.game;

import lombok.RequiredArgsConstructor;
import org.ebndrnk.leverxfinalproject.model.dto.game.GameObjectDto;
import org.ebndrnk.leverxfinalproject.model.entity.game.GameObject;
import org.ebndrnk.leverxfinalproject.repository.game.GameObjectRepository;
import org.ebndrnk.leverxfinalproject.service.auth.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GameObjectService {
    private final GameObjectRepository gameObjectRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;

    public GameObjectDto addANewObject(GameObjectDto gameObjectDto){
        GameObject gameObject = modelMapper.map(gameObjectDto, GameObject.class);
        gameObject.setUser(userService.getCurrentUser());

        return modelMapper.map(gameObjectRepository.save(gameObject), GameObjectDto.class);
    }

    public GameObjectDto editAnObject(Long objectId, GameObjectDto gameObjectDto){
        GameObject gameObject = modelMapper.map(gameObjectDto, GameObject.class);
        gameObject.setId(objectId);

        return modelMapper.map(gameObjectRepository.save(gameObject), GameObjectDto.class);
    }

    public GameObjectDto retrieveGameObjectById(Long id){
        try {
            GameObject gameObject = gameObjectRepository.findById(id).orElseThrow();
            return modelMapper.map(gameObject, GameObjectDto.class);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public List<GameObjectDto> retrieveGameObjects(){
        return gameObjectRepository.findAll().stream()
                .map(o -> modelMapper.map(o, GameObjectDto.class))
                .toList();
    }

    public void deleteById(Long id){
        gameObjectRepository.deleteById(id);
    }

    public Object patchAnObject(Long id, GameObjectDto gameObjectDto) {
        return null;
    }
}
