# 🏃 Race Runner API

> API REST — Gestion d'inscriptions à des courses  
> **Spring Boot 4.0.3 • Java 21 • PostgreSQL • Docker**

---

## 🛠 Stack technique

| Technologie | Version / Détail |
|---|---|
| Java | 21 |
| Spring Boot | 4.0.3 |
| Spring Web MVC | spring-boot-starter-webmvc |
| Spring Data JPA | spring-boot-starter-data-jpa |
| Flyway | spring-boot-starter-flyway + flyway-database-postgresql 11.20.3 |
| PostgreSQL Driver | 42.7.10 |
| Docker | PostgreSQL + Adminer via docker-compose |

---

## 🚀 Lancer le projet

### Prérequis
- Java 21
- Maven
- Docker Desktop lancé

### 1 — Démarrer la base de données

```bash
docker compose up -d
```

Lance PostgreSQL (port 5432) et Adminer (port 8081).

### 2 — Lancer l'application

```bash
mvn spring-boot:run
```

L'API est disponible sur **http://localhost:8080**

### 3 — Adminer

Accéder à **http://localhost:8081**

| Champ | Valeur |
|---|---|
| System | PostgreSQL |
| Server | race_postgres |
| Username | race |
| Password | race |
| Database | race_db |

---

## 📦 Modèle de données

### Runner

| Champ | Type Java |
|---|---|
| id | Long (auto-généré) |
| firstName | String |
| lastName | String |
| email | String |
| age | Integer |

### Race

| Champ | Type Java |
|---|---|
| id | Long (auto-généré) |
| name | String |
| date | LocalDate |
| location | String |
| maxParticipants | Integer |

### Registration

| Champ | Type Java |
|---|---|
| id | Long (auto-généré) |
| runner | Runner (@ManyToOne — FK runner_id) |
| race | Race (@ManyToOne — FK race_id) |
| registrationDate | LocalDate (auto : date du jour) |

---

## 🔌 Endpoints implémentés

### Coureurs — `/runners`

| Méthode | URL | Description | Status |
|---|---|---|---|
| GET | `/runners` | Lister tous les coureurs | 200 |
| GET | `/runners/{id}` | Récupérer un coureur | 200 / 404 |
| GET | `/runners/{id}/races` | Lister les courses d'un coureur | 200 |
| POST | `/runners` | Créer un coureur | **201 Created** |
| PUT | `/runners/{id}` | Modifier un coureur | 200 / 404 |
| DELETE | `/runners/{id}` | Supprimer un coureur | **204 No Content** |

**Body POST/PUT :**
```json
{
  "firstName": "Alice",
  "lastName": "Martin",
  "email": "alice@example.com",
  "age": 30
}
```

---

### Courses — `/races`

| Méthode | URL | Description | Status |
|---|---|---|---|
| GET | `/races` | Lister toutes les courses | 200 |
| GET | `/races?location=Paris` | Filtrer par lieu *(bonus)* | 200 |
| GET | `/races/{id}` | Récupérer une course | 200 |
| POST | `/races` | Créer une course | **201 Created** |
| PUT | `/races/{id}` | Modifier une course | 200 |
| DELETE | `/races/{id}` | Supprimer une course | **204 No Content** |

**Body POST/PUT :**
```json
{
  "name": "Semi-marathon de Paris",
  "date": "2026-06-01",
  "location": "Paris",
  "maxParticipants": 500
}
```

---

### Inscriptions — `/races` (RegistrationController)

| Méthode | URL | Description | Status |
|---|---|---|---|
| GET | `/races/{id_race}/registrations` | Participants d'une course | 200 |
| GET | `/races/runners/{id_runner}` | Inscriptions d'un coureur | 200 |
| POST | `/races/{id_race}/registrations` | Inscrire un coureur | **201 Created** |
| DELETE | `/races/{id_race}/{id_runner}` | Supprimer une inscription | **204 No Content** |

**Body POST :**
```json
{ "runnerId": 1 }
```

> Le body est une `Map<String, Long>` — pas de DTO, on lit directement `body.get("runnerId")`.

---

## 🗄 Repositories et requêtes

Deux approches ont été utilisées pour générer les requêtes SQL.

### Méthodes auto-générées par Spring Data JPA

Spring Data JPA génère automatiquement le SQL à partir du nom de la méthode.  
Pour naviguer dans une relation `@ManyToOne`, on utilise le suffixe `_Id` :

```java
List<Registration> getRegistrationByRace_Id(Long id_race);
List<Registration> getRegistrationByRunner_Id(Long id_runner);
```

> Sans `_Id`, Spring ne saurait pas naviguer dans l'objet `Race` ou `Runner` pour accéder à leur `id`.

### Requêtes JPQL avec `@Query`

Pour explorer l'annotation `@Query` et écrire des requêtes plus explicites :

| Repository | Méthode | Requête JPQL |
|---|---|---|
| RaceRepository | CountRegistrationByRace | `SELECT COUNT(r) FROM Registration r WHERE r.race.id=:id_race` |
| RegistrationRepository | CountRegistrationByRace | `SELECT COUNT(r) FROM Registration r WHERE r.race.id=:id_race` |
| RegistrationRepository | getRacesByRunner | `SELECT r.race FROM Registration r WHERE r.runner.id = :id_runner` |

> `getRacesByRunner` est utilisée par `GET /runners/{id}/races` — elle retourne directement une `List<Race>` sans passer par les `Registration`.

---

## 🏗 Architecture

```
src/main/java/com/takima/race/runner/
├── controllers/
│   ├── RaceController.java         → /races
│   ├── RegistrationController.java → /races (inscriptions)
│   └── RunnerController.java       → /runners
├── entities/
│   ├── Race.java
│   ├── Registration.java
│   └── Runner.java
├── repositories/
│   ├── RaceRepository.java
│   ├── RegistrationRepository.java
│   └── RunnerRepository.java
└── services/
    ├── RaceService.java
    ├── RegistrationService.java
    └── RunnerService.java
```

> `RaceController` et `RegistrationController` sont tous deux mappés sur `/races`. Spring les différencie grâce aux sous-chemins des routes.

---

## 📋 Codes HTTP

| Code | Signification | Utilisé pour |
|---|---|---|
| 200 OK | Succès | GET, PUT |
| 201 Created | Ressource créée | POST (`@ResponseStatus(HttpStatus.CREATED)`) |
| 204 No Content | Succès sans réponse | DELETE (`@ResponseStatus(HttpStatus.NO_CONTENT)`) |
| 404 Not Found | Ressource inexistante | Runner introuvable (`RunnerService.getById`) |

---

## ⭐ Bonus — Filtre par location

```
GET /races?location=Paris
```

Sans paramètre, toutes les courses sont retournées. Avec le paramètre, seules les courses dont le lieu correspond exactement sont retournées.

- **RaceRepository** : `findByLocation(String location)` — méthode auto-générée par Spring Data JPA
- **RaceService** : si `location` non null et non vide → `findByLocation`, sinon `findAll`
- **RaceController** : `@RequestParam(required = false) String location` — paramètre totalement optionnel
