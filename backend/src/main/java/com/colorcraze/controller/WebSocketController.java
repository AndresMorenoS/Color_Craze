package com.colorcraze.controller;

import com.colorcraze.dto.MoveRequest;
import com.colorcraze.service.GameService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {
    
    private final GameService gameService;
    
    public WebSocketController(GameService gameService) {
        this.gameService = gameService;
    }
    
    @MessageMapping("/games/{code}/move")
    public void handleMove(@DestinationVariable String code, MoveRequest moveRequest) {
        gameService.handleMove(code, moveRequest);
    }
}
