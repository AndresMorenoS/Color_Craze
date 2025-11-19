# Color Craze - Architecture Overview

## System Overview

Color Craze is a multiplayer real-time game built with a modern client-server architecture. Players paint blocks as they move, competing to paint the most blocks in 30 seconds.

## Architecture Style

The system follows a **Client-Server Architecture** with **Real-Time Communication** patterns:

- **Presentation Layer**: React SPA (Single Page Application)
- **Application Layer**: Spring Boot REST API + WebSocket Server
- **Business Logic**: Game Service with concurrent game management
- **Domain Model**: Object-oriented game state management

## Key Design Decisions

### 1. Technology Choices

**Frontend: React + TypeScript + Vite**
- **Why React?** Component-based architecture for UI modularity
- **Why TypeScript?** Type safety reduces runtime errors
- **Why Vite?** Fast development builds and Hot Module Replacement

**Backend: Spring Boot + Java 17**
- **Why Spring Boot?** Enterprise-grade framework with WebSocket support
- **Why Java 17?** Strong typing, excellent concurrency support, mature ecosystem

### 2. Communication Protocol

**HTTP REST API for Game Management**
- Creating and joining games
- Fetching game state
- Standard CRUD operations

**WebSocket (STOMP) for Real-Time Updates**
- 30 FPS game state broadcasts
- Player movement commands
- Low-latency communication
- Bidirectional communication channel

### 3. Game Loop Architecture

The game runs on a **fixed timestep game loop** at ~30 FPS (33ms per frame):

```
Loop (every 33ms):
  1. Process player inputs (from WebSocket messages)
  2. Update physics (gravity, velocity, collisions)
  3. Paint cells under players
  4. Calculate scores
  5. Broadcast game state to all players
```

### 4. Concurrency Model

**Multiple Games Running Simultaneously**
- Each game runs in its own thread
- `ConcurrentHashMap` for thread-safe game storage
- Scheduled executors for timers (lobby, game duration)

**Thread Safety Considerations**
- Game state is isolated per game instance
- WebSocket broadcasts are thread-safe via Spring's SimpMessagingTemplate
- Player collections use concurrent data structures

### 5. State Management

**Game State Machine**
```
LOBBY (15s waiting period)
  ↓
PLAYING (30s gameplay)
  ↓
FINISHED (results display)
```

**Client State**
- React hooks manage UI state
- WebSocket subscriptions update state in real-time
- Canvas renders game state every frame

### 6. Physics Engine

**Movement Physics**
- Horizontal velocity: 0.3 units/frame
- Gravity: 0.08 units/frame²
- Jump velocity: -1.2 units/frame
- Parabolic jump trajectory

**Collision Detection**
- Boundary checks (walls, floor, ceiling)
- Platform collision (landing on paintable blocks)
- Block-based grid system (40x30 cells)

## Scalability Considerations

### Current Implementation
- In-memory game state
- Single server instance
- Direct WebSocket connections

### Horizontal Scaling (Future)
To scale beyond a single server:

1. **Distributed Cache**: Use Redis for shared game state
2. **Message Broker**: Use RabbitMQ/Redis for WebSocket message distribution
3. **Load Balancer**: Sticky sessions for WebSocket connections
4. **Session Persistence**: Store active games in distributed cache

See [diagrams/5-deployment-diagram.puml](diagrams/5-deployment-diagram.puml) for the high availability architecture.

## Performance Characteristics

### Backend
- **Game Loop**: 30 FPS (33ms frame time)
- **Lobby Timeout**: 15 seconds
- **Game Duration**: 30 seconds
- **Max Players per Game**: 4
- **Board Size**: 40x30 = 1200 cells

### Frontend
- **Rendering**: Canvas-based (60 FPS capable)
- **Network Updates**: Receives updates at 30 FPS
- **Bundle Size**: ~200-300 KB (gzipped)

## Security Considerations

### Current Implementation
- CORS enabled for cross-origin requests
- WebSocket requires valid game code
- Player IDs are UUIDs (hard to guess)

### Future Enhancements
- Authentication/Authorization
- Rate limiting on API endpoints
- WebSocket message validation
- Anti-cheat mechanisms (server-side validation)

## Data Flow

### Game Creation Flow
```
Player → Browser → REST API → GameService → Game Model
                                    ↓
                             WebSocket Broker → All Clients
```

### Movement Flow
```
Player Input → Browser → WebSocket → GameService → Player Model
                                          ↓
                                    Game Loop (30 FPS)
                                          ↓
                              WebSocket Broker → All Clients
```

### Game End Flow
```
Timer Expires → GameService → Calculate Scores → Game Model
                                    ↓
                          WebSocket Broker (results topic) → All Clients
                                    ↓
                          Remove game after 60s cleanup
```

## Testing Strategy

### Backend Testing
- **Unit Tests**: Service layer logic (physics, scoring)
- **Integration Tests**: REST API endpoints
- **WebSocket Tests**: Message handling

### Frontend Testing
- **Component Tests**: React components
- **Integration Tests**: Page navigation
- **E2E Tests**: Full game flow

## Monitoring & Observability

### Current Implementation
- Spring Boot Actuator (health checks)
- Console logging

### Recommended Additions
- **Metrics**: Prometheus + Grafana
- **Logging**: ELK Stack (Elasticsearch, Logstash, Kibana)
- **Tracing**: Distributed tracing for request flows
- **Alerting**: Alert on high error rates, slow responses

## Documentation

### Architecture Diagrams
All diagrams are in PlantUML format in [`diagrams/`](diagrams/):

1. **Context Diagram**: System boundaries
2. **Component Diagram**: Major components
3. **Class Diagram**: Backend structure
4. **Sequence Diagram**: Game flow
5. **Deployment Diagram**: High availability setup

### Code Documentation
- **Backend**: JavaDoc comments in service classes
- **Frontend**: TypeScript interfaces for type documentation
- **API**: OpenAPI/Swagger (recommended addition)

## Development Workflow

### Backend Development
```bash
cd backend
mvn clean install  # Build
mvn spring-boot:run  # Run
mvn test  # Test
```

### Frontend Development
```bash
cd frontend
npm install  # Install dependencies
npm run dev  # Development server
npm run build  # Production build
npm run preview  # Preview production build
```

## Technology Stack Summary

| Layer | Technology | Purpose |
|-------|-----------|---------|
| Frontend UI | React 18 | Component-based UI |
| Frontend Language | TypeScript | Type-safe JavaScript |
| Frontend Build | Vite | Fast builds and HMR |
| Frontend Routing | React Router | SPA navigation |
| Frontend Graphics | HTML5 Canvas | Game rendering |
| Backend Framework | Spring Boot 3.2 | Application server |
| Backend Language | Java 17 | Business logic |
| Backend Build | Maven | Dependency management |
| Backend Server | Embedded Tomcat | HTTP/WebSocket server |
| Real-Time Protocol | STOMP over WebSocket | Bidirectional messaging |
| HTTP Protocol | REST API | Game management |

## References

- [Main README](../README.md) - Setup and running instructions
- [Diagrams README](diagrams/README.md) - Viewing diagram instructions
- [Spring WebSocket Docs](https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#websocket)
- [React Documentation](https://react.dev/)
- [PlantUML Documentation](https://plantuml.com/)
