import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useWebSocketGame } from '../hooks/useWebSocketGame';
import { useGameTimer } from '../hooks/useGameTimer';
import { PlayerColor, type JoinGameResponse } from '../types/game';
import { BACKEND_API_URL } from '../utils/websocket';
import './LobbyPage.css';

const LobbyPage = () => {
  const { code } = useParams<{ code: string }>();
  const navigate = useNavigate();
  const [playerId, setPlayerId] = useState<string | null>(localStorage.getItem('playerId'));
  const [playerName] = useState<string>(localStorage.getItem('playerName') || '');
  const [selectedColor, setSelectedColor] = useState<PlayerColor | null>(
    (localStorage.getItem('playerColor') as PlayerColor) || null
  );
  const [joined, setJoined] = useState(!!playerId);
  const [error, setError] = useState('');

  const { gameState } = useWebSocketGame(code || null, playerId);
  const { displayTime, formatTime } = useGameTimer(gameState?.remainingTime);

  useEffect(() => {
    if (gameState?.state === 'PLAYING') {
      navigate(`/game/${code}`);
    }
  }, [gameState, code, navigate]);

  const joinGame = async (color: PlayerColor) => {
    if (!code || !playerName) return;

    try {
      const response = await fetch(`${BACKEND_API_URL}/games/${code}/join`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ playerName, color }),
      });

      const data: JoinGameResponse = await response.json();
      
      if (data.success) {
        setPlayerId(data.playerId);
        localStorage.setItem('playerId', data.playerId);
        setSelectedColor(color);
        localStorage.setItem('playerColor', data.color);
        setJoined(true);
      } else {
        setError(data.message);
      }
    } catch (err) {
      setError('Failed to join game');
    }
  };

  const getColorHex = (color: string): string => {
    const colorMap: { [key: string]: string } = {
      RED: '#FF0000',
      BLUE: '#0000FF',
      GREEN: '#00FF00',
      YELLOW: '#FFFF00',
    };
    return colorMap[color] || '#000000';
  };

  const availableColors = Object.values(PlayerColor).filter(
    (color) => !gameState?.players.some((p) => p.color === color && p.id !== playerId)
  );

  return (
    <div className="lobby-page">
      <div className="lobby-container">
        <h1 className="title">Game Lobby</h1>
        <div className="game-code">
          <span>Game Code: </span>
          <strong>{code}</strong>
        </div>

        {gameState && (
          <div className="timer">
            Starting in: <strong>{formatTime(displayTime)}</strong>
          </div>
        )}

        {error && <div className="error">{error}</div>}

        {!joined ? (
          <div className="color-selection">
            <h2>Select Your Color</h2>
            <div className="color-grid">
              {availableColors.map((color) => (
                <button
                  key={color}
                  className="color-button"
                  style={{ backgroundColor: getColorHex(color) }}
                  onClick={() => joinGame(color)}
                >
                  {color}
                </button>
              ))}
            </div>
          </div>
        ) : (
          <>
            <div className="your-color">
              <h3>Your Color</h3>
              <div
                className="color-display"
                style={{ backgroundColor: getColorHex(selectedColor || gameState?.players.find(p => p.id === playerId)?.color || '') }}
              />
            </div>

            <div className="players-list">
              <h3>Players ({gameState?.players.length || 0}/4)</h3>
              <div className="players">
                {gameState?.players.map((player) => (
                  <div key={player.id} className="player-item">
                    <div
                      className="player-color"
                      style={{ backgroundColor: getColorHex(player.color) }}
                    />
                    <span className="player-name">{player.name}</span>
                    {player.id === playerId && <span className="you-badge">YOU</span>}
                  </div>
                ))}
              </div>
            </div>

            <div className="waiting-message">
              Waiting for game to start...
            </div>
          </>
        )}
      </div>
    </div>
  );
};

export default LobbyPage;
