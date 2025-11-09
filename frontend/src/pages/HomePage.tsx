import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { BACKEND_API_URL } from '../utils/websocket';
import type { CreateGameResponse } from '../types/game';
import './HomePage.css';

const HomePage = () => {
  const [playerName, setPlayerName] = useState('');
  const [gameCode, setGameCode] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const createGame = async () => {
    if (!playerName.trim()) {
      setError('Please enter your name');
      return;
    }

    setLoading(true);
    setError('');

    try {
      const response = await fetch(`${BACKEND_API_URL}/games`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ playerName }),
      });

      const data: CreateGameResponse = await response.json();
      localStorage.setItem('playerId', data.playerId);
      localStorage.setItem('playerName', playerName);
      localStorage.setItem('playerColor', data.color);
      navigate(`/lobby/${data.gameCode}`);
    } catch (err) {
      setError('Failed to create game. Is the server running?');
    } finally {
      setLoading(false);
    }
  };

  const joinGame = () => {
    if (!playerName.trim()) {
      setError('Please enter your name');
      return;
    }
    if (!gameCode.trim()) {
      setError('Please enter a game code');
      return;
    }

    localStorage.setItem('playerName', playerName);
    navigate(`/lobby/${gameCode.toUpperCase()}`);
  };

  return (
    <div className="home-page">
      <div className="home-container">
        <h1 className="title">Color Craze</h1>
        <p className="subtitle">Paint more blocks to win!</p>

        <div className="input-section">
          <input
            type="text"
            placeholder="Enter your name"
            value={playerName}
            onChange={(e) => setPlayerName(e.target.value)}
            className="input"
          />
        </div>

        {error && <div className="error">{error}</div>}

        <div className="button-section">
          <button onClick={createGame} disabled={loading} className="button primary">
            {loading ? 'Creating...' : 'Create Game'}
          </button>
        </div>

        <div className="divider">OR</div>

        <div className="input-section">
          <input
            type="text"
            placeholder="Enter game code"
            value={gameCode}
            onChange={(e) => setGameCode(e.target.value.toUpperCase())}
            className="input"
            maxLength={6}
          />
        </div>

        <div className="button-section">
          <button onClick={joinGame} className="button secondary">
            Join Game
          </button>
        </div>
      </div>
    </div>
  );
};

export default HomePage;
