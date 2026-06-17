<div align="center">

# 🏥 Plataforma Web de Gestión de Citas Médicas

### Sistema de microservicios para el agendamiento médico en tiempo real

*Centraliza, sincroniza y optimiza la reserva de citas, eliminando el sobre-agendamiento y reduciendo el ausentismo.*

<br>

![Estado](https://img.shields.io/badge/Estado-En%20desarrollo-blue?style=for-the-badge)
![Arquitectura](https://img.shields.io/badge/Arquitectura-Microservicios-6DB33F?style=for-the-badge)
![Licencia](https://img.shields.io/badge/Uso-Académico-lightgrey?style=for-the-badge)

<br>

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2025.1-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Spring Security](https://img.shields.io/badge/Security-JWT%20%2B%20BCrypt-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)
![RabbitMQ](https://img.shields.io/badge/RabbitMQ-Mensajería-FF6600?style=for-the-badge&logo=rabbitmq&logoColor=white)
![Resilience4J](https://img.shields.io/badge/Resilience4J-Tolerancia%20a%20fallos-1f6feb?style=for-the-badge)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Angular](https://img.shields.io/badge/Angular-Frontend-DD0031?style=for-the-badge&logo=angular&logoColor=white)

<br>

#### 🧰 Stack tecnológico

<img src="https://skillicons.dev/icons?i=java,spring,postgres,rabbitmq,docker,angular,maven,git,github,postman" alt="Stack" />

</div>

---

## 📑 Tabla de contenidos

- [Descripción](#-descripción)
- [Características principales](#-características-principales)
- [Arquitectura de microservicios](#-arquitectura-de-microservicios)
- [Stack tecnológico](#-stack-tecnológico-detallado)
- [Cómo ejecutar el proyecto](#-cómo-ejecutar-el-proyecto)
- [Endpoints principales](#-endpoints-principales)
- [Usuarios de prueba](#-usuarios-de-prueba)
- [Estructura del proyecto](#-estructura-del-proyecto)
- [Equipo de desarrollo](#-equipo-de-desarrollo)
- [Información académica](#-información-académica)

---

## 📖 Descripción

La **Plataforma Web de Gestión de Citas Médicas** es una solución basada en **microservicios** que moderniza el agendamiento de un centro médico, reemplazando los procesos manuales (teléfono y mensajería) por un sistema centralizado, seguro y escalable.

El sistema resuelve los principales problemas del agendamiento tradicional:

- ❌ **Sobre-agendamiento** de un mismo médico en el mismo horario.
- ⏳ **Largos tiempos de espera** y saturación de los canales de atención.
- 📉 **Ausentismo** de pacientes por falta de recordatorios.
- 🔐 **Manejo inseguro** de los datos de los usuarios.

---

## ✨ Características principales

| | Característica | Descripción |
|:--:|---|---|
| 🔐 | **Autenticación segura** | Login con **JWT** y contraseñas cifradas con **BCrypt**. |
| 👥 | **Control de roles** | `PACIENTE`, `MÉDICO`, `RECEPCIONISTA` y `ADMIN`. |
| 📅 | **Reserva de citas** | Agendar, reprogramar y cancelar citas en tiempo real. |
| 🚫 | **Anti sobre-agendamiento** | Bloqueo optimista + restricción de unicidad por médico/horario. |
| 🩺 | **Historia clínica** | Registro de atenciones y diagnósticos por paciente. |
| 📊 | **Reportes** | Ausentismo, productividad médica y citas por periodo. |
| 🔄 | **Comunicación entre servicios** | Llamadas con **OpenFeign** y tolerancia a fallos con **Resilience4J**. |
| 📨 | **Mensajería asíncrona** | Eventos de citas vía **RabbitMQ** hacia el servicio de reportes. |
| 🧭 | **Descubrimiento y balanceo** | **Eureka** + **API Gateway** como punto único de entrada. |

---

## 🧩 Arquitectura de microservicios

El sistema está dividido en servicios independientes, cada uno con **su propia base de datos**. El cliente nunca accede directamente a un servicio: todo pasa por el **API Gateway**, que valida la seguridad y rutea la petición.

```
Cliente (Angular)
       │
       ▼
  API Gateway  ──valida JWT y propaga identidad──►  Microservicios
   (8080)                                            (cada uno con su BD)
       │
       └──► Eureka Server (8761): directorio donde los servicios se registran
```

| Microservicio | Puerto | Base de datos | Responsabilidad |
|---|:--:|---|---|
| 🧭 **eureka-server** | `8761` | — | Service discovery (registro de servicios) |
| 🚪 **api-gateway** | `8080` | — | Punto único de entrada, valida JWT y CORS |
| 🔐 **auth-service** | `8081` | `auth_db` | Autenticación, usuarios y roles |
| 🧑‍🤝‍🧑 **paciente-service** | `8082` | `paciente_db` | Datos de los pacientes |
| 🩺 **doctor-service** | `8083` | `doctor_db` | Médicos y especialidades |
| 📅 **citas-service** | `8084` | `citas_db` | Reservas, agenda y asistencias |
| 📋 **historial-medico-service** | `8085` | `historial_db` | Historias clínicas / atenciones |
| 📊 **reporte-service** | `8086` | `reporte_db` | Reportes e indicadores |

---

## 🛠️ Stack tecnológico detallado

| Capa | Tecnologías |
|---|---|
| **Lenguaje** | Java 21 |
| **Framework** | Spring Boot 4.0 · Spring MVC · Spring Data JPA · Spring Security |
| **Microservicios** | Spring Cloud 2025.1 · Eureka · Spring Cloud Gateway · OpenFeign · Resilience4J |
| **Seguridad** | JWT (JSON Web Token) · BCrypt |
| **Base de datos** | PostgreSQL 17 |
| **Mensajería** | RabbitMQ |
| **Frontend** | Angular |
| **DevOps** | Docker · Docker Compose |
| **Build / Pruebas** | Maven (multi-módulo) · Postman |

---

## 🚀 Cómo ejecutar el proyecto

### ✅ Requisitos previos
- **Java 21**
- **Maven 3.9+**
- **PostgreSQL 17** (con las bases de datos creadas)
- **RabbitMQ** (para `citas-service` y `reporte-service`)

### ⚙️ Variables de entorno

```bash
JWT_SECRET=tu-clave-secreta-jwt
DB_HOST=localhost
DB_PORT=5432
DB_USER=postgres
DB_PASSWORD=tu-password
```

### ▶️ Ejecución (local)

```bash
# 1) Compilar todo el backend desde la carpeta backend/
cd backend
mvn clean install

# 2) Arrancar en orden:
#    eureka-server  →  api-gateway  →  los demás servicios
mvn -pl eureka-server spring-boot:run
mvn -pl api-gateway   spring-boot:run
mvn -pl auth-service  spring-boot:run
# ... y el resto de microservicios
```

> 🧭 El panel de Eureka queda disponible en **http://localhost:8761**
> 🚪 Todas las peticiones entran por el gateway en **http://localhost:8080**

---

## 🔌 Endpoints principales

> Todas las rutas se consumen a través del **API Gateway** (`http://localhost:8080`).

| Método | Endpoint | Descripción | Acceso |
|:--:|---|---|:--:|
| `POST` | `/api/auth/registro` | Registrar usuario | Público |
| `POST` | `/api/auth/login` | Iniciar sesión (devuelve JWT) | Público |
| `GET/POST/PUT/DELETE` | `/api/pacientes` | Gestión de pacientes | 🔐 |
| `GET/POST/PUT/DELETE` | `/api/medicos` | Gestión de médicos | 🔐 |
| `GET/POST/PUT/DELETE` | `/api/especialidades` | Gestión de especialidades | 🔐 |
| `GET/POST/PUT/DELETE` | `/api/citas` | Reserva y gestión de citas | 🔐 |
| `GET/POST` | `/api/historial` | Historia clínica | 🔐 |
| `GET` | `/api/reportes` | Reportes e indicadores | 🔐 |

---

## 👤 Usuarios de prueba

| Rol | Email | Contraseña |
|---|---|---|
| ADMIN | `admin@gestion.pe` | `Gestion2026` |
| MÉDICO | `medico.Flores@gestion.pe` | `Gestion2026` |
| PACIENTE | `paciente.Quispe@gestion.pe` | `Gestion2026` |

---

## 📂 Estructura del proyecto

```
ProyectoGestionCitasDSWII/
├── backend/                        # Proyecto Maven multi-módulo
│   ├── eureka-server/              # Service discovery
│   ├── api-gateway/                # Gateway + validación JWT + CORS
│   ├── auth-service/               # Autenticación y usuarios
│   ├── paciente-service/           # Información de pacientes
│   ├── doctor-service/             # Médicos y especialidades
│   ├── citas-service/              # Agenda, reservas y asistencias
│   ├── historial-medico-service/   # Historia clínica
│   └── reporte-service/            # Reportes (Feign + RabbitMQ)
└── frontend/                       # Aplicación Angular (paciente, médico, admin)
```

---

## 👨‍💻 Equipo de desarrollo

| Integrante | Rol | GitHub |
|---|---|---|
| **Carlos Sebasthian Rumiche Pain** | Coordinador | [@Rumitche24P](https://github.com/Rumitche24P) |
| **Ronaldo Ronaldiño Abarca Chávez** | Desarrollador | [@abarcaronaldo](https://github.com/abarcaronaldo) |
| **Adriano Alejandro Del Piero Bramon Muro** | Desarrollador | — |
| **Jonathan Pierre Siesquen Zuloaga** | Desarrollador | [@JpSiesquen](https://github.com/JpSiesquen) |

---

## 🎓 Información académica

<div align="center">

**Instituto de Educación Superior CIBERTEC**
Escuela de Tecnologías de la Información — Computación e Informática

| | |
|---|---|
| 📚 **Curso** | Desarrollo de Servicios Web II (4698) |
| 👨‍🏫 **Profesor** | Alex Javier Tomaylla Castillo |
| 🎯 **Ciclo** | Sexto — 2026 |

</div>

---

<div align="center">

⭐ *Proyecto desarrollado con fines académicos* ⭐

</div>
