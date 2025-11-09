package com.colorcraze.controller;

import com.colorcraze.dto.*;
import com.colorcraze.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/games")
public class GameController {
    
    private final GameService gameService;
    
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }
    
    @PostMapping
    public ResponseEntity<CreateGameResponse> createGame(@RequestBody CreateGameRequest request) {
        CreateGameResponse response = gameService.createGame(request.getPlayerName());
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/{code}/join")
    public ResponseEntity<JoinGameResponse> joinGame(
            @PathVariable String code,
            @RequestBody JoinGameRequest request) {
        JoinGameResponse response = gameService.joinGame(
                code, request.getPlayerName(), request.getColor());
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{code}/state")
    public ResponseEntity<GameStateDTO> getGameState(@PathVariable String code) {
        GameStateDTO state = gameService.getGameState(code);
        if (state == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(state);
    }
}
