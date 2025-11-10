import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useWebSocketGame } from '../hooks/useWebSocketGame';
import type { GameResult } from '../types/game';
import './ResultsPage.css';

const ResultsPage = () => {
  const { code } = useParams<{ code: string }>();
  const navigate = useNavigate();
  const [playerId] = [localStorage.getItem('playerId')];
  const { gameResult: wsResult } = useWebSocketGame(code || null, playerId);
  const [result, setResult] = useState<GameResult | null>(null);

  useEffect(() => {
    if (wsResult) {
      setResult(wsResult);
    }
  }, [wsResult]);

  const getColorHex = (color: string): string => {
    const colorMap: { [key: string]: string } = {
      RED: '#FF0000',
      BLUE: '#0000FF',
      GREEN: '#00FF00',
      YELLOW: '#FFFF00',
    };
    return colorMap[color] || '#000000';
  };

  const handleFinish = () => {
    localStorage.removeItem('playerId');
    localStorage.removeItem('playerColor');
    navigate('/');
  };

  if (!result) {
    return (
      <div className="results-page">
        <div className="loading">Loading results...</div>
      </div>
    );
  }

  return (
    <div className="results-page">
      <div className="results-container">
        <h1 className="title">Game Over!</h1>

        <div className="winner-section">
          <div className="trophy">🏆</div>
          <h2 className="winner-title">Winner</h2>
          <div
            className="winner-color"
            style={{ backgroundColor: getColorHex(result.winnerColor) }}
          />
          <div className="winner-name">{result.winnerName}</div>
          <div className="winner-score">
            {result.scores.find(s => s.id === result.winnerId)?.score || 0} blocks painted
          </div>
        </div>

        <div className="scores-section">
          <h3>Final Scores</h3>
          <div className="scores-list">
            {result.scores.map((score, index) => (
              <div key={score.id} className="score-row">
                <div className="rank">#{index + 1}</div>
                <div
                  className="player-color-small"
                  style={{ backgroundColor: getColorHex(score.color) }}
                />
                <div className="player-name">{score.name}</div>
                <div className="player-score">{score.score} blocks</div>
              </div>
            ))}
          </div>
        </div>

        <button onClick={handleFinish} className="play-again-button">
          Finalizar
        </button>
      </div>
    </div>
  );
};

export default ResultsPage;
