# DemosportsAPL

Android app (Kotlin + Jetpack Compose) for managing the Annual Premier League across 25 tables and 25 sports.

## Build Instructions

**Requirements:** Android Studio Hedgehog (2023.1.1+), JDK 17, Android SDK 34

```bash
git clone <repo>
cd demosportsapl
./gradlew assembleDebug        # Build
./gradlew test                 # Run unit tests
./gradlew installDebug         # Install on device/emulator
```

## Architecture
- **Package:** `com.demosportsapl.app`
- **DI:** ServiceLocator (no Hilt)
- **DB:** Room 2.6.1 + KSP
- **UI:** Jetpack Compose + Material 3 (BOM 2024.04.01)
- **Nav:** Navigation Compose 2.7.7
- **State:** StateFlow / collectAsState()
- **Session:** DataStore Preferences

## Roles & Permissions
| Role | Capabilities |
|------|-------------|
| Super Admin | Override roster lock (reason logged), full access |
| Sport Admin | Enter scores, declare walk-overs |
| Chairman | Edit own table roster (before 60-min lock) |
| General | View only |

## Rules
- **Points:** Gold=5, Silver=3, Bronze=1
- **Tie-breakers:** Points → Gold count → Silver count → Bronze count → Table name (A-Z)
- **Roster lock:** 60 min before match; Super Admin can override
- **Walk-over:** 3-0 after 10-min grace period; Sport Admin only
- **Offline:** Cached reads for all; Sport Admin score queue; Chairman roster = NO

## Demo Data
- 25 sports, 25 tables (Table 1–25), 35 fixtures (past/today/future)
- Seeded automatically on first launch via `SeedData.seedIfNeeded()`
- Default session: Table 25, General User
