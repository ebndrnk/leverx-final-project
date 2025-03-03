package org.ebndrnk.leverxfinalproject.controller.game;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ebndrnk.leverxfinalproject.model.dto.game.GameObjectDto;
import org.ebndrnk.leverxfinalproject.service.game.GameObjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/object")
public class GameController {
    private final GameObjectService gameObjectService;


    //TODO uriOfNewResource
    @PostMapping
    public ResponseEntity<GameObjectDto> addANewObject(@RequestBody GameObjectDto gameObjectDto){
        return ResponseEntity.ok(gameObjectService.addANewObject(gameObjectDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GameObjectDto> editAnObject(@PathVariable Long id,
                                                      @Valid @RequestBody GameObjectDto gameObjectDto){
        final var dto = gameObjectService.editAnObject(id, gameObjectDto);
        final  var  location  = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path( "/{id}" ).build(dto.getId());
        return ResponseEntity.created(location).body(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameObjectDto> retrieveGameObject(@PathVariable Long id){
        return ResponseEntity.ok(gameObjectService.retrieveGameObjectById(id));
    }

    @GetMapping
    public ResponseEntity<List<GameObjectDto>> retrieveGameObjects(){
        return ResponseEntity.ok(gameObjectService.retrieveGameObjects());
    }


    //TODO свои ошибки
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAnObject(@PathVariable Long id){
        try {
            gameObjectService.deleteById(id);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

//    @PatchMapping
//    public ResponseEntity<GameObjectDto> patchAnObject(@PathVariable Long id,
//                                                       @Valid @RequestBody GameObjectDto gameObjectDto){
//        final var dto = gameObjectService.patchAnObject(id, gameObjectDto);
//        final  var  location  = ServletUriComponentsBuilder.fromCurrentContextPath()
//                .path( "/{id}" ).build(dto.getId());
//        return ResponseEntity.created(location).body(dto);
//    }
}
