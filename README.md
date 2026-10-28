# Donaton

Plataforma web para coordinar **donaciones**, **necesidades**, **centros de acopio**, **logística**, **transporte** y **voluntarios** en situaciones de emergencia (terremotos, incendios e inundaciones) en Chile.

> Trabajo evaluativo de Desarrollo Fullstack, 5.º semestre. Docente: Fernando Sepúlveda.

---

## Tabla de contenidos

- [Arquitectura](#arquitectura)
- [Servicios](#servicios)
- [Tecnologías](#tecnologías)
- [Requisitos](#requisitos)
- [Configuración](#configuración)
- [Ejecución con Docker](#ejecución-con-docker)
- [Ejecución sin Docker](#ejecución-sin-docker)
- [Tests](#tests)
- [Estructura del repositorio](#estructura-del-repositorio)
- [Notas de seguridad](#notas-de-seguridad)
- [Licencia](#licencia)

---

## Arquitectura

```
 Frontend (Angular 20 + Ionic)                :8100
          │
          ▼
 API Gateway (Spring Cloud Gateway)           :8085
          │
          ▼
 BFF (Node.js + Express)                      :3000
          │  REST, con URLs configuradas por variables de entorno
          ▼
 Microservicios (Spring Boot)                 :8080–8089
          │
          ▼
 MySQL 8.0                                    :3307 (host) → :3306 (contenedor)

 Eureka Server                                :8090  (registro y descubrimiento de servicios)
```

El frontend se comunica con el **API Gateway**, que redirige todas las rutas al **BFF**. El BFF llama a cada microservicio según su URL configurada. Las notificaciones en tiempo real usan WebSocket (STOMP) directamente contra el servicio de notificaciones.

## Servicios

| Servicio | Puerto | Base de datos | Ruta principal | Descripción |
|---|---|---|---|---|
| `eureka-server` | 8090 | — | — | Descubrimiento de servicios |
| `api-gateway` | 8085 | — | `/**` → BFF | Punto de entrada único |
| `BFF` (`Backend/BFF`) | 3000 | — | `/donacion`, `/usuario`, `/necesidad`, `/logistica`, `/notificacion`, `/chat`, `/contacto` | Agregador para el frontend; envía correos del formulario de contacto |
| `Donaton_Registro` | 8084 | `donaton_registro_db` | `/registro` | Registro de usuarios |
| `Donaton-Autenticacion` | 8089 | — | `/auth` | Login y validación |
| `Donaton_Donaciones` | 8080 | `donaton_donaciones_db` | `/donacion` | Gestión de donaciones |
| `Donaton_Necesidad` | 8082 | `donaton_necesidad_db` | `/necesidad` | Gestión de necesidades |
| `Donaton_Notificaciones` | 8083 | `donaton_notificacion_db` | `/notificacion`, `/chat`, WebSocket `/ws-notificaciones` | Notificaciones y chat |
| `Donaton-Logistica` | 8081 | `donaton_logistica_db` | `/inventario`, `/envio`, `/voluntario` | Inventario, envíos y voluntarios |
| `Donaton-Transporte` | 8086 | `donaton_transport_db` | `/transporte` | Gestión de transporte |
| `Donaton-Acopio` | 8087 | `donaton_acopio_db` | `/acopio` | Centros de acopio |
| `Donaton-Pago` | 8088 | `donaton_pago_db` | `/pagos` | Pagos |
| `FrontendWeb/Donaton-Web` | 8100 | — | — | Aplicación Angular/Ionic |

## Tecnologías

- **Frontend:** Angular 20, Ionic 8, Capacitor 8 (Android), Leaflet (mapas), `@stomp/stompjs`
- **Backend:** Java 17, Spring Boot (Maven), Spring Cloud Gateway, Spring Cloud Netflix Eureka, Spring WebSocket
- **BFF:** Node.js 20, Express 5, nodemailer
- **Base de datos:** MySQL 8.0
- **Infraestructura:** Docker y Docker Compose

## Requisitos

- **Recomendado:** Docker y Docker Compose.
- **Desarrollo sin Docker:** Java 17, Maven (o el wrapper `./mvnw`), Node.js 20, Angular CLI y MySQL 8.

## Configuración

### 1. Variables de entorno (`.env`)

El BFF necesita credenciales para enviar correos desde el formulario de contacto. Crea un archivo `.env` en la raíz del proyecto (está ignorado por Git):

```env
EMAIL_USER=tu_correo@ejemplo.com
EMAIL_PASS=tu_contraseña_de_aplicacion
CONTACTO_DESTINO=correo_que_recibe_los_contactos@ejemplo.com
```

### 2. URL de la API en el frontend

Los archivos `FrontendWeb/Donaton-Web/src/environments/environment.ts` y `environment.prod.ts` tienen una IP de red local fija (`192.168.100.10`):

```ts
apiUrl: 'http://192.168.100.10:8085',
wsUrl: 'ws://192.168.100.10:8083',
```

Cámbiala por `localhost` o por la IP de tu equipo antes de ejecutar el proyecto.

### 3. Variables de los microservicios

Cada servicio Spring Boot lee estas variables (con sus valores por defecto entre paréntesis):

| Variable | Usada por | Valor por defecto |
|---|---|---|
| `DB_HOST` | Servicios con base de datos | `localhost` |
| `DB_PORT` | Servicios con base de datos | `3307` |
| `EUREKA_URI` | Todos los servicios Spring | `http://localhost:8090/eureka` |
| `BFF_URI` | `api-gateway` | `http://localhost:3000` |
| `AUTH_SERVICE_URL` | `Donaton_Registro` | — |
| `LOGISTICA_URL` | `Donaton_Registro` | — |

El BFF usa `REGISTRO_URL`, `NECESIDAD_URL`, `DONACION_URL`, `NOTIFICACION_URL`, `LOGISTICA_URL`, `TRANSPORTE_URL` y `ACOPIO_URL`, todas con valores `localhost` por defecto. En Docker Compose ya están definidas.

## Ejecución con Docker

```bash
git clone https://github.com/Niveo21/Donaton.git
cd Donaton

# Después de crear el .env y ajustar las URLs del frontend:
docker compose up --build
```

Una vez levantado:

| Componente | URL |
|---|---|
| Frontend | http://localhost:8100 |
| API Gateway | http://localhost:8085 |
| Eureka | http://localhost:8090 |
| MySQL | `localhost:3307` (usuario `root`, sin contraseña) |

> **Nota:** `mysql-init/init.sql` crea las bases de datos solo la primera vez que se inicializa el volumen de MySQL. Para empezar desde cero, ejecuta `docker compose down -v` (esto **borra todos los datos**).

## Ejecución sin Docker

Levanta los componentes en este orden:

1. **MySQL 8.0** en el puerto `3307`, con el usuario `root` sin contraseña, y ejecuta `mysql-init/init.sql`.
2. **Eureka Server:** `Backend/eureka-server` → `./mvnw spring-boot:run` (puerto 8090).
3. **Microservicios Java:** en cada carpeta de `Backend/` (excepto `BFF`), ejecuta `./mvnw spring-boot:run` o `mvn spring-boot:run`.
4. **API Gateway:** `Backend/api-gateway` → `./mvnw spring-boot:run` (puerto 8085).
5. **BFF:**
   ```bash
   cd Backend/BFF
   npm install
   node src/index.js
   ```
   Escucha en el puerto 3000.
6. **Frontend:**
   ```bash
   cd FrontendWeb/Donaton-Web
   npm install
   npm start
   ```
   Abre http://localhost:4200.

## Tests

- **Backend:** cada microservicio incluye pruebas unitarias. Ejecútalas dentro de su carpeta con `./mvnw test`.
- **Frontend:** `npm test` (usa Karma y requiere un navegador Chrome).

## Estructura del repositorio

```
Donaton/
├── Backend/
│   ├── BFF/                    # Node.js + Express (agregador para el frontend)
│   ├── api-gateway/            # Spring Cloud Gateway
│   ├── eureka-server/          # Eureka
│   ├── Donaton_Registro/
│   ├── Donaton-Autenticacion/
│   ├── Donaton_Donaciones/
│   ├── Donaton_Necesidad/
│   ├── Donaton_Notificaciones/
│   ├── Donaton-Logistica/
│   ├── Donaton-Transporte/
│   ├── Donaton-Acopio/
│   └── Donaton-Pago/
├── FrontendWeb/
│   └── Donaton-Web/            # Angular 20 + Ionic
├── mysql-init/
│   └── init.sql                # Creación de las bases de datos
├── docker-compose.yml
└── .vscode/
```

## Notas de seguridad

Este proyecto está pensado para desarrollo y evaluación académica. Antes de desplegarlo en un entorno real:

- MySQL usa el usuario `root` **sin contraseña** y expone el puerto `3307`.
- Todos los microservicios se conectan a la base de datos como `root`.
- Las credenciales de correo deben ir solo en `.env`. No las subas al repositorio.
- El contenedor del frontend ejecuta `ng serve` (servidor de desarrollo), no un build de producción.

## Licencia

El repositorio no incluye un archivo de licencia en la raíz. El `package.json` del BFF indica MIT, pero no hay una licencia definida para el proyecto completo.
