import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

const BACKEND_URL = 'http://localhost:8080';

export const createWebSocketClient = (
  gameCode: string,
  onStateUpdate: (state: any) => void,
  onResult: (result: any) => void
): Client => {
  const client = new Client({
    webSocketFactory: () => new SockJS(`${BACKEND_URL}/ws`),
    debug: (str) => {
      console.log('STOMP:', str);
    },
    onConnect: () => {
      console.log('Connected to WebSocket');
      
      client.subscribe(`/topic/games/${gameCode}/state`, (message) => {
        const state = JSON.parse(message.body);
        onStateUpdate(state);
      });
      
      client.subscribe(`/topic/games/${gameCode}/results`, (message) => {
        const result = JSON.parse(message.body);
        onResult(result);
      });
    },
    onStompError: (frame) => {
      console.error('Broker error:', frame.headers['message']);
      console.error('Details:', frame.body);
    },
  });

  return client;
};

export const sendMove = (
  client: Client,
  gameCode: string,
  playerId: string,
  direction: string,
  jump: boolean
) => {
  if (client.connected) {
    client.publish({
      destination: `/app/games/${gameCode}/move`,
      body: JSON.stringify({ playerId, direction, jump }),
    });
  }
};

export const BACKEND_API_URL = BACKEND_URL + '/api';
