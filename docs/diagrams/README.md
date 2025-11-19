# Color Craze - Architecture Diagrams

This directory contains PlantUML diagrams documenting the architecture of the Color Craze multiplayer game system.

## 📋 Diagram Overview

### 1. Context Diagram (`1-context-diagram.puml`)
**Purpose:** Shows the system boundaries and external actors interacting with Color Craze.

**Key Elements:**
- Players (external actors)
- Color Craze system boundary
- Web browser as the interface
- Communication protocols (HTTP/HTTPS, WebSocket)

**When to use:** To understand the high-level system context and how users interact with the application.

---

### 2. Component Diagram (`2-component-diagram.puml`)
**Purpose:** Illustrates the major components of the system and their relationships.

**Key Elements:**
- **Frontend Components:**
  - React Router (navigation)
  - Pages (Home, Lobby, Game, Results)
  - Canvas Renderer (game visualization)
  - WebSocket Client (real-time communication)
  - Hooks (movement controls, game timer)

- **Backend Components:**
  - REST Controllers (HTTP API)
  - WebSocket Controller (real-time messages)
  - GameService (business logic)
  - Model Layer (Game, Player, Board, Cell)
  - Game Engine (physics, painting, collision, scoring)

- **Infrastructure:**
  - WebSocket Broker (STOMP protocol)

**When to use:** To understand how different parts of the system interact and which technologies are used in each layer.

---

### 3. Class Diagram (`3-class-diagram.puml`)
**Purpose:** Shows the object-oriented design of the backend Java application.

**Key Elements:**
- **Controllers:** GameController, WebSocketController
- **Service:** GameService (core business logic)
- **Model:** Game, Player, Board, Cell, PlayerColor, GameState
- **DTOs:** Request/Response objects for API communication
- **Config:** WebSocket and CORS configuration

**When to use:** To understand the backend code structure, class relationships, and available methods.

---

### 4. Sequence Diagram (`4-sequence-diagram.puml`)
**Purpose:** Demonstrates the flow of interactions during a typical game session.

**Key Phases:**
1. **Game Creation:** Player 1 creates a game
2. **Player Joins:** Player 2 joins the game
3. **Lobby Countdown:** 15-second waiting period
4. **Game Starts:** State transition to PLAYING
5. **Gameplay Loop:** Real-time movement and painting at 30 FPS
6. **Game Ends:** Results calculation and display

**When to use:** To understand the chronological flow of events and message exchanges between components.

---

### 5. Deployment Diagram (`5-deployment-diagram.puml`)
**Purpose:** Shows the deployment architecture optimized for high availability.

**Key Elements:**
- **Client Layer:** Multiple browser-based clients
- **CDN:** Static asset delivery (React app)
- **Load Balancer:** Nginx/HAProxy with health checks
- **Application Servers:** Multiple Spring Boot instances (Docker containers)
- **Message Broker Cluster:** Redis/RabbitMQ for WebSocket message distribution
- **Distributed Cache:** Redis cluster for shared game state
- **Monitoring:** Prometheus + Grafana
- **Logging:** ELK Stack (Elasticsearch, Logstash, Kibana)

**High Availability Features:**
- Horizontal scaling with multiple app instances
- Load balancing with sticky sessions for WebSocket
- Distributed cache for state synchronization
- Message broker for event distribution
- Auto-healing with container orchestration
- Health monitoring and alerting

**When to use:** To understand production deployment architecture, scalability, and availability strategies.

---

## 🛠️ Viewing the Diagrams

### Option 1: Online PlantUML Viewer
1. Go to [PlantUML Web Server](http://www.plantuml.com/plantuml/uml/)
2. Copy the content of any `.puml` file
3. Paste it into the text area
4. The diagram will render automatically

### Option 2: VS Code Extension
1. Install the [PlantUML extension](https://marketplace.visualstudio.com/items?itemName=jebbs.plantuml)
2. Open any `.puml` file
3. Press `Alt+D` (Windows/Linux) or `Option+D` (Mac) to preview

### Option 3: IntelliJ IDEA Plugin
1. Install the [PlantUML Integration plugin](https://plugins.jetbrains.com/plugin/7017-plantuml-integration)
2. Open any `.puml` file
3. The diagram will appear in a side panel

### Option 4: Command Line (with PlantUML installed)
```bash
# Install PlantUML (requires Java)
# On macOS with Homebrew:
brew install plantuml

# Generate PNG images
plantuml docs/diagrams/*.puml

# Generate SVG images (better for web)
plantuml -tsvg docs/diagrams/*.puml
```

### Option 5: GitHub Rendering
Some GitHub tools and browser extensions can render PlantUML diagrams directly:
- [PlantUML Viewer Chrome Extension](https://chrome.google.com/webstore/detail/plantuml-viewer/)
- [GitHub + PlantUML](https://github.com/qjebbs/vscode-plantuml)

---

## 📚 Architecture Documentation

### Technology Stack

**Frontend:**
- React 18
- TypeScript
- Vite (build tool)
- React Router (navigation)
- STOMP over WebSocket (real-time communication)
- HTML5 Canvas (game rendering)

**Backend:**
- Java 17
- Spring Boot 3.2.0
- Maven
- Spring WebSocket (STOMP)
- Embedded Tomcat

### Communication Protocols

**REST API Endpoints:**
- `POST /api/games` - Create a new game
- `POST /api/games/{code}/join` - Join an existing game
- `GET /api/games/{code}/state` - Get current game state

**WebSocket Topics:**
- `/topic/games/{code}/state` - Game state updates (broadcast)
- `/topic/games/{code}/results` - Final results (broadcast)

**WebSocket Destinations:**
- `/app/games/{code}/move` - Send player movement

### Game Specifications

- **Lobby Duration:** 15 seconds
- **Game Duration:** 30 seconds
- **Max Players:** 4
- **Board Size:** 40x30 cells
- **Tick Rate:** ~30 FPS (33ms per frame)
- **Available Colors:** Red, Blue, Green, Yellow
- **Controls:** Arrow keys or WASD for movement, Space/Up/W for jump

### Design Patterns Used

1. **MVC (Model-View-Controller):** Separation of concerns in backend
2. **Service Layer Pattern:** Business logic isolated in GameService
3. **DTO Pattern:** Data transfer between layers
4. **State Machine:** Game state management (LOBBY → PLAYING → FINISHED)
5. **Observer Pattern:** WebSocket subscriptions for real-time updates
6. **Game Loop Pattern:** Fixed timestep game loop at 30 FPS
7. **Concurrent Collections:** Thread-safe game management

---

## 🔄 Update Instructions

When modifying the architecture:

1. **Update the relevant diagram(s)** to reflect the changes
2. **Maintain consistency** across all diagrams
3. **Add notes** to explain significant architectural decisions
4. **Regenerate images** if using rendered versions
5. **Update this README** if adding new diagrams

---

## 📞 Questions?

For questions about the architecture or diagrams, please refer to the main [README.md](../../README.md) or open an issue in the repository.
