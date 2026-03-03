# 💰 Moneko - Personal Finance Android App

[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-blue.svg)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-orange.svg)](https://developer.android.com/jetpack/compose)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

A **local-first personal finance application** that works completely offline and syncs seamlessly when connected. Built with modern Android development practices and a robust offline-first architecture.

## 🚀 Features

### Core Functionality
- **💸 Expense Tracking** - Record and categorize your expenses
- **💰 Income Management** - Track income from multiple sources  
- **🤝 Lending & Borrowing** - Manage money lent to or borrowed from others
- **📊 Financial Overview** - Real-time balance calculations and insights

### Technical Highlights
- **🔄 Offline-First Architecture** - Works completely offline with background sync
- **📱 Mobile-First Design** - Optimized for mobile experience
- **⚡ Optimistic UI Updates** - Instant feedback with conflict resolution
- **🛡️ Robust Data Sync** - Built-in state machine for reliable synchronization
- **🗄️ Local Database** - SQLite/Room for fast, reliable local storage

## 🏗️ Architecture

Moneko follows a **local-first, mobile-first** architecture:

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Android App   │    │    Backend      │    │   Web Portal    │
│  (Primary UI)   │◄──►│   (Sync Hub)    │◄──►│   (Optional)    │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         ▼                       ▼                       ▼
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│ SQLite/Room DB  │    │   PostgreSQL    │    │    Browser      │
│   (Local)       │    │ (Authoritative) │    │    Cache        │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

### Design Principles
- **Mobile proposes, Backend disposes** - Mobile creates data, backend validates
- **Offline-first** - All functionality available without internet
- **Conflict resolution** - Server acts as authoritative source for conflicts
- **No derived data** - Balances computed in real-time, never stored

## 📱 Tech Stack

### Android App
- **Language:** Kotlin
- **UI Framework:** Jetpack Compose
- **Database:** Room (SQLite)
- **Architecture:** MVVM + Local-First
- **Minimum SDK:** 24 (Android 7.0)
- **Target SDK:** 36

### Backend (Planned)
- **Language:** TBD (Kotlin/Java/Go/Python)
- **Database:** PostgreSQL
- **API:** REST/GraphQL
- **Authentication:** TBD

### Web Portal (Planned)
- **Framework:** TBD (React/Vue/Angular)
- **Purpose:** Optional web access to data

## 🚀 Getting Started

### Prerequisites
- Android Studio Arctic Fox or later
- JDK 11 or later
- Android SDK with API level 36

### Setup
1. **Clone the repository**
   ```bash
   git clone https://github.com/your-username/moneko.git
   cd moneko
   ```

2. **Open in Android Studio**
   - Open the `moneko-android` directory in Android Studio
   - Let Gradle sync complete

3. **Run the app**
   - Connect an Android device or start an emulator
   - Click Run (▶️) or use `Ctrl+R`

### Project Structure
```
moneko/
├── moneko-android/          # Android application
│   ├── app/                 # Main app module
│   │   ├── src/main/        # Source code
│   │   └── build.gradle.kts # App-level build config
│   └── gradle/              # Gradle configuration
├── backend/                 # Future backend implementation
├── web/                     # Future web portal
├── FOUNDATION_SYSTEM_DESIGN.md  # System architecture
├── LOCAL_SCHEMA.md          # Database schema documentation
└── README.md               # This file
```

## 📊 Data Model

### Core Entities

#### 💸 Expense
```kotlin
data class Expense(
    val id: String,           // UUID
    val amount: BigDecimal,
    val description: String,
    val category: String,
    val createdAt: Instant,
    val updatedAt: Instant,
    val state: SyncState      // Tracks sync status
)
```

#### 💰 Income
```kotlin
data class Income(
    val id: String,           // UUID
    val amount: BigDecimal,
    val source: String,
    val createdAt: Instant,
    val state: SyncState
)
```

#### 🤝 Lending
```kotlin
data class Lending(
    val id: String,           // UUID
    val person: String,
    val amount: BigDecimal,
    val type: LendingType,    // LENT | BORROWED
    val status: LendingStatus, // OPEN | SETTLED
    val createdAt: Instant,
    val state: SyncState
)
```

### Sync State Machine
```
LOCAL_ONLY → PENDING_SYNC → SYNCED
     ↓             ↓           ↓
 CONFLICT ←→ REJECTED    CONFIRMED
```

## 🗺️ Roadmap

### Phase 0: Foundation ✅ (Current)
- [x] System design and architecture
- [x] Database schema design
- [x] Android project setup
- [ ] Core entity models
- [ ] Local database implementation

### Phase 1: Core Android App 🚧
- [ ] UI implementation with Compose
- [ ] CRUD operations for all entities
- [ ] Local database with Room
- [ ] Business logic and calculations
- [ ] Basic reporting and insights

### Phase 2: Offline Sync 📋
- [ ] Sync state machine implementation
- [ ] Conflict detection and resolution
- [ ] Background sync service
- [ ] Network state management

### Phase 3: Backend Integration 📋
- [ ] Backend API development
- [ ] Authentication system
- [ ] Data synchronization
- [ ] Multi-device support

### Phase 4: Advanced Features 📋
- [ ] Web portal
- [ ] Advanced analytics
- [ ] Export/import functionality
- [ ] Backup and restore

## 🤝 Contributing

### Development Process
1. **Design First** - All features start with design documentation
2. **Local-First** - Implement offline functionality first
3. **Test-Driven** - Write tests for core business logic
4. **Code Review** - All changes require review

### Getting Involved
1. Check existing issues or create new ones
2. Fork the repository
3. Create a feature branch
4. Make changes with tests
5. Submit a pull request

### Development Guidelines
- Follow Kotlin coding conventions
- Use Jetpack Compose best practices
- Maintain offline-first architecture
- Document architecture decisions
- Write comprehensive tests

## 📄 Documentation

- [**Foundation System Design**](FOUNDATION_SYSTEM_DESIGN.md) - Complete system architecture and design decisions
- [**Local Schema**](LOCAL_SCHEMA.md) - Detailed database schema and entity relationships

## 🤝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👥 Authors

- **pradumcodes** - Initial work and architecture

## 🙏 Acknowledgments

- Inspired by local-first software principles
- Built with modern Android development practices
- Designed for real-world offline usage scenarios

---

**Made with ❤️ for better personal finance management**