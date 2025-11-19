# Guía Rápida - Diagramas de Arquitectura

## 📋 Diagramas Disponibles

### 1. Diagrama de Contexto (`1-context-diagram.puml`)
Muestra los límites del sistema y los actores externos que interactúan con Color Craze.

**Elementos clave:**
- Jugadores (actores externos)
- Sistema Color Craze
- Navegador web como interfaz
- Protocolos de comunicación (HTTP/HTTPS, WebSocket)

---

### 2. Diagrama de Componentes (`2-component-diagram.puml`)
Ilustra los componentes principales del sistema y sus relaciones.

**Componentes Frontend:**
- React Router (navegación)
- Páginas (Home, Lobby, Game, Results)
- Canvas Renderer (visualización del juego)
- Cliente WebSocket (comunicación en tiempo real)
- Hooks (controles de movimiento, temporizador)

**Componentes Backend:**
- Controladores REST (API HTTP)
- Controlador WebSocket (mensajes en tiempo real)
- GameService (lógica de negocio)
- Capa de Modelo (Game, Player, Board, Cell)
- Motor del Juego (física, pintura, colisiones, puntuación)

---

### 3. Diagrama de Clases (`3-class-diagram.puml`)
Muestra el diseño orientado a objetos de la aplicación backend en Java.

**Elementos principales:**
- **Controladores:** GameController, WebSocketController
- **Servicio:** GameService (lógica central del negocio)
- **Modelo:** Game, Player, Board, Cell, PlayerColor, GameState
- **DTOs:** Objetos de solicitud/respuesta para comunicación API
- **Configuración:** WebSocket y CORS

---

### 4. Diagrama de Secuencia (`4-sequence-diagram.puml`)
Demuestra el flujo de interacciones durante una sesión típica de juego.

**Fases clave:**
1. **Creación del juego:** El Jugador 1 crea una partida
2. **Jugador se une:** El Jugador 2 se une a la partida
3. **Cuenta regresiva del lobby:** Período de espera de 15 segundos
4. **Inicio del juego:** Transición al estado PLAYING
5. **Bucle de juego:** Movimiento y pintura en tiempo real a 30 FPS
6. **Fin del juego:** Cálculo de resultados y visualización

---

### 5. Diagrama de Despliegue (`5-deployment-diagram.puml`)
Muestra la arquitectura de despliegue optimizada para alta disponibilidad.

**Elementos clave:**
- **Capa de Clientes:** Múltiples clientes basados en navegador
- **CDN:** Entrega de activos estáticos (aplicación React)
- **Balanceador de Carga:** Nginx/HAProxy con verificaciones de salud
- **Servidores de Aplicación:** Múltiples instancias de Spring Boot (contenedores Docker)
- **Clúster de Message Broker:** Redis/RabbitMQ para distribución de mensajes WebSocket
- **Caché Distribuida:** Clúster de Redis para estado compartido del juego
- **Monitoreo:** Prometheus + Grafana
- **Logging:** Stack ELK (Elasticsearch, Logstash, Kibana)

**Características de Alta Disponibilidad:**
- Escalamiento horizontal con múltiples instancias de aplicación
- Balanceo de carga con sesiones persistentes para WebSocket
- Caché distribuida para sincronización de estado
- Message broker para distribución de eventos
- Auto-recuperación con orquestación de contenedores
- Monitoreo de salud y alertas

---

## 🛠️ Cómo Ver los Diagramas

### Opción 1: Visor en Línea de PlantUML
1. Ve a [PlantUML Web Server](http://www.plantuml.com/plantuml/uml/)
2. Copia el contenido de cualquier archivo `.puml`
3. Pégalo en el área de texto
4. El diagrama se renderizará automáticamente

### Opción 2: Extensión de VS Code
1. Instala la [extensión PlantUML](https://marketplace.visualstudio.com/items?itemName=jebbs.plantuml)
2. Abre cualquier archivo `.puml`
3. Presiona `Alt+D` (Windows/Linux) o `Option+D` (Mac) para previsualizar

### Opción 3: Plugin de IntelliJ IDEA
1. Instala el [plugin de Integración PlantUML](https://plugins.jetbrains.com/plugin/7017-plantuml-integration)
2. Abre cualquier archivo `.puml`
3. El diagrama aparecerá en un panel lateral

### Opción 4: Línea de Comandos (con PlantUML instalado)
```bash
# Instalar PlantUML (requiere Java)
# En macOS con Homebrew:
brew install plantuml

# En Ubuntu/Debian:
sudo apt-get install plantuml

# Generar imágenes PNG
plantuml docs/diagrams/*.puml

# Generar imágenes SVG (mejor para web)
plantuml -tsvg docs/diagrams/*.puml
```

---

## 📚 Documentación Adicional

### Stack Tecnológico

**Frontend:**
- React 18
- TypeScript
- Vite (herramienta de construcción)
- React Router (navegación)
- STOMP sobre WebSocket (comunicación en tiempo real)
- HTML5 Canvas (renderizado del juego)

**Backend:**
- Java 17
- Spring Boot 3.2.0
- Maven
- Spring WebSocket (STOMP)
- Tomcat embebido

### Especificaciones del Juego

- **Duración del Lobby:** 15 segundos
- **Duración del Juego:** 30 segundos
- **Máximo de Jugadores:** 4
- **Tamaño del Tablero:** 40x30 celdas
- **Tasa de Actualización:** ~30 FPS (33ms por fotograma)
- **Colores Disponibles:** Rojo, Azul, Verde, Amarillo
- **Controles:** Flechas o WASD para movimiento, Espacio/Arriba/W para saltar

---

## 🔄 Instrucciones de Actualización

Al modificar la arquitectura:

1. **Actualiza el(los) diagrama(s) relevante(s)** para reflejar los cambios
2. **Mantén la consistencia** entre todos los diagramas
3. **Agrega notas** para explicar decisiones arquitectónicas significativas
4. **Regenera las imágenes** si usas versiones renderizadas
5. **Actualiza este README** si agregas nuevos diagramas

---

## 📞 ¿Preguntas?

Para preguntas sobre la arquitectura o los diagramas, consulta el [README principal](../../README.md) o abre un issue en el repositorio.

## Enlaces Útiles

- [Documentación Completa (Inglés)](README.md)
- [Visión General de Arquitectura](../ARCHITECTURE.md)
- [README Principal](../../README.md)
