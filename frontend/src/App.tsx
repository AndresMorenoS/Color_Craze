import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import HomePage from './pages/HomePage';
import LobbyPage from './pages/LobbyPage';
import GamePage from './pages/GamePage';
import ResultsPage from './pages/ResultsPage';
import './App.css';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/lobby/:code" element={<LobbyPage />} />
        <Route path="/game/:code" element={<GamePage />} />
        <Route path="/results/:code" element={<ResultsPage />} />
      </Routes>
    </Router>
  );
}

export default App;
