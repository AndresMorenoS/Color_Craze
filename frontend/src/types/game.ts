export const PlayerColor = {
  RED: 'RED',
  BLUE: 'BLUE',
  GREEN: 'GREEN',
  YELLOW: 'YELLOW'
} as const;

export type PlayerColor = typeof PlayerColor[keyof typeof PlayerColor];

export interface Player {
  id: string;
  name: string;
  color: string;
  x: number;
  y: number;
  score: number;
}

export interface GameState {
  gameCode: string;
  state: 'LOBBY' | 'PLAYING' | 'FINISHED';
  players: Player[];
  board: { [key: string]: string };
  remainingTime: number;
}

export interface GameResult {
  winnerId: string;
  winnerName: string;
  winnerColor: string;
  scores: PlayerScore[];
}

export interface PlayerScore {
  id: string;
  name: string;
  color: string;
  score: number;
}

export interface CreateGameResponse {
  gameCode: string;
  playerId: string;
  color: string;
}

export interface JoinGameResponse {
  playerId: string;
  success: boolean;
  message: string;
  color: string;
}
