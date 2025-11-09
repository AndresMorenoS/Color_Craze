import { useState, useEffect, useCallback } from 'react';
import { Client } from '@stomp/stompjs';
import { createWebSocketClient, sendMove } from '../utils/websocket';
import type { GameState, GameResult } from '../types/game';

export const useWebSocketGame = (gameCode: string | null, playerId: string | null) => {
  const [client, setClient] = useState<Client | null>(null);
  const [gameState, setGameState] = useState<GameState | null>(null);
  const [gameResult, setGameResult] = useState<GameResult | null>(null);
  const [connected, setConnected] = useState(false);

  useEffect(() => {
    if (!gameCode) return;

    const wsClient = createWebSocketClient(
      gameCode,
      (state: GameState) => {
        setGameState(state);
        setConnected(true);
      },
      (result: GameResult) => {
        setGameResult(result);
      }
    );

    wsClient.activate();
    setClient(wsClient);

    return () => {
      wsClient.deactivate();
    };
  }, [gameCode]);

  const move = useCallback(
    (direction: string, jump: boolean) => {
      if (client && gameCode && playerId) {
        sendMove(client, gameCode, playerId, direction, jump);
      }
    },
    [client, gameCode, playerId]
  );

  return { gameState, gameResult, connected, move };
};
