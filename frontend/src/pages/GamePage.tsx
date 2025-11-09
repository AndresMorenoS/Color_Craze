import { useEffect, useRef } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useWebSocketGame } from '../hooks/useWebSocketGame';
import { useMovementControls } from '../hooks/useMovementControls';
import { useGameTimer } from '../hooks/useGameTimer';
import './GamePage.css';

const CELL_SIZE = 20;

const GamePage = () => {
  const { code } = useParams<{ code: string }>();
  const navigate = useNavigate();
  const canvasRef = useRef<HTMLCanvasElement>(null);
  const [playerId] = [localStorage.getItem('playerId')];

  const { gameState, gameResult, move } = useWebSocketGame(code || null, playerId);
  const { displayTime, formatTime } = useGameTimer(gameState?.remainingTime);

  useMovementControls(move, gameState?.state === 'PLAYING');

  useEffect(() => {
    if (gameResult) {
      navigate(`/results/${code}`);
    }
  }, [gameResult, code, navigate]);

  useEffect(() => {
    if (!gameState || !canvasRef.current) return;

    const canvas = canvasRef.current;
    const ctx = canvas.getContext('2d');
    if (!ctx) return;

    const boardWidth = 40;
    const boardHeight = 30;
    canvas.width = boardWidth * CELL_SIZE;
    canvas.height = boardHeight * CELL_SIZE;

    // Background - darker gray for unpaintable areas (air)
    ctx.fillStyle = '#2a2a2a';
    ctx.fillRect(0, 0, canvas.width, canvas.height);

    const colorMap: { [key: string]: string } = {
      RED: '#FF0000',
      BLUE: '#0000FF',
      GREEN: '#00FF00',
      YELLOW: '#FFFF00',
    };

    // Create a set of paintable blocks for quick lookup
    const paintableSet = new Set(gameState.paintableBlocks || []);

    // Draw paintable blocks with light gray (unpainted platforms/surfaces)
    paintableSet.forEach((pos) => {
      const [x, y] = pos.split(',').map(Number);
      const color = gameState.board[pos];
      
      if (!color) {
        // Unpainted paintable block - light gray with border
        ctx.fillStyle = '#666666';
        ctx.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        ctx.strokeStyle = '#555555';
        ctx.lineWidth = 1;
        ctx.strokeRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
      }
    });

    // Draw painted blocks
    Object.entries(gameState.board).forEach(([pos, color]) => {
      const [x, y] = pos.split(',').map(Number);
      ctx.fillStyle = colorMap[color] || '#888888';
      ctx.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
    });

    // Draw players
    gameState.players.forEach((player) => {
      ctx.fillStyle = colorMap[player.color] || '#FFFFFF';
      ctx.strokeStyle = '#FFFFFF';
      ctx.lineWidth = 2;
      
      const playerX = player.x * CELL_SIZE;
      const playerY = player.y * CELL_SIZE;
      
      ctx.beginPath();
      ctx.arc(
        playerX + CELL_SIZE / 2,
        playerY + CELL_SIZE / 2,
        CELL_SIZE / 2,
        0,
        Math.PI * 2
      );
      ctx.fill();
      ctx.stroke();

      ctx.fillStyle = '#FFFFFF';
      ctx.font = 'bold 10px Arial';
      ctx.textAlign = 'center';
      ctx.fillText(player.name, playerX + CELL_SIZE / 2, playerY - 5);
    });
  }, [gameState]);

  if (!gameState) {
    return (
      <div className="game-page">
        <div className="loading">Connecting...</div>
      </div>
    );
  }

  return (
    <div className="game-page">
      <div className="game-header">
        <div className="timer-display">
          Time: <strong>{formatTime(displayTime)}</strong>
        </div>
        <div className="game-code-display">Game: {code}</div>
      </div>

      <div className="scoreboard">
        {gameState.players.map((player) => (
          <div key={player.id} className="score-item">
            <div
              className="score-color"
              style={{
                backgroundColor:
                  player.color === 'RED'
                    ? '#FF0000'
                    : player.color === 'BLUE'
                    ? '#0000FF'
                    : player.color === 'GREEN'
                    ? '#00FF00'
                    : '#FFFF00',
              }}
            />
            <span className="score-name">{player.name}</span>
            <span className="score-value">{player.score}</span>
          </div>
        ))}
      </div>

      <div className="canvas-container">
        <canvas ref={canvasRef} />
      </div>

      <div className="controls-hint">
        Use Arrow Keys or WASD to move • Space or W/↑ to jump
      </div>
    </div>
  );
};

export default GamePage;
