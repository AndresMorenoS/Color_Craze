# Color Craze

A multiplayer real-time game where players paint blocks as they move. The player who paints the most blocks in 30 seconds wins!

## Architecture

### Backend
- **Language**: Java 17
- **Framework**: Spring Boot + Maven
- **Features**:
  - REST API for game creation and joining
  - WebSocket support for real-time communication
  - Concurrent game management with threading
  - Physics simulation (lateral movement + parabolic jump)

### Frontend
- **Framework**: React + TypeScript + Vite
- **Features**:
  - WebSocket connection for real-time updates
  - Canvas-based game rendering
  - Movement controls (Arrow keys or WASD)
  - Real-time scoreboard

## Game Flow

1. **Home Page**: Create a new game or join an existing one with a game code
2. **Lobby**: Wait for players (max 4) and select colors. Auto-starts after 15 seconds
3. **Game**: 30 seconds of gameplay where players move and paint blocks
4. **Results**: Display winner and final scores

## Setup & Running

### Backend

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

The backend will start on `http://localhost:8080`

### Frontend

```bash
cd frontend
npm install
npm run dev
```

The frontend will start on `http://localhost:5173`

## Game Controls

- **Movement**: Arrow Left/Right or A/D
- **Jump**: Arrow Up, W, or Space
- Combine movement and jump for parabolic jumps!

## Technical Details

- **Lobby Duration**: 15 seconds
- **Game Duration**: 30 seconds
- **Max Players**: 4
- **Board Size**: 40x30 cells
- **Tick Rate**: ~30 FPS
- **Available Colors**: Red, Blue, Green, Yellow

## API Endpoints

### REST
- `POST /api/games` - Create a new game
- `POST /api/games/{code}/join` - Join an existing game
- `GET /api/games/{code}/state` - Get current game state

### WebSocket
- `/topic/games/{code}/state` - Game state updates
- `/topic/games/{code}/results` - Final results
- `/app/games/{code}/move` - Send player movement

## Architecture Diagrams

Detailed architecture documentation with PlantUML diagrams is available in [`docs/diagrams/`](docs/diagrams/):

- **Context Diagram**: System boundaries and external actors
- **Component Diagram**: Major components and their relationships
- **Class Diagram**: Backend object-oriented design
- **Sequence Diagram**: Game flow and interactions
- **Deployment Diagram**: High availability deployment architecture

See [docs/diagrams/README.md](docs/diagrams/README.md) for viewing instructions.

## Development

Built with:
- Spring Boot 3.2.0
- React 18
- TypeScript
- Vite
- STOMP over WebSocket
- HTML5 Canvas
