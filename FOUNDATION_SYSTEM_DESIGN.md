# Moneko - Local-First Personal Finance Android App

A local-first personal finance application that works offline and syncs seamlessly when connected. Built following the PHASE-0 Foundation & Contract Design pattern for robust data synchronization.

## 📱 Project Overview

Moneko is designed with a **mobile-first, local-first** architecture where:
- Mobile app works completely offline
- Data is stored locally with SQLite/Room
- Background sync when network is available
- Server acts as authoritative source for conflicts
- Optimistic UI updates with conflict resolution

## 🏗️ PHASE-0 - FOUNDATION & CONTRACT DESIGN

### Objectives
- Remove ambiguity before coding
- Define what data exists and who owns it
- Establish synchronization contracts
- Handle failure scenarios gracefully

---

## 📊 Core Entities

### 1. Expense Entity
Represents money going out.

```kotlin
data class Expense(
    val id: String,           // UUID
    val amount: BigDecimal,
    val description: String,
    val category: String,
    val createdAt: Instant,
    val updatedAt: Instant,
    val state: SyncState      // LOCAL_ONLY, PENDING_SYNC, SYNCED, CONFLICT, REJECTED
)
```

**Rules:**
- ✅ Created on mobile
- ✅ Can be edited until confirmed by server
- ⚠️ Server decides final validity
- 🚫 Cannot be deleted after server confirmation

### 2. Income Entity
Represents money coming in.

```kotlin
data class Income(
    val id: String,           // UUID
    val amount: BigDecimal,
    val source: String,
    val createdAt: Instant,
    val state: SyncState
)
```

**Rules:**
- ✅ Created on mobile
- ✅ Always increases balance locally
- ⚠️ Server validates ownership only

### 3. Lending Entity
Tracks obligations, not cash flow.

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

**Rules:**
- 🚫 Cannot be deleted after settlement
- ✅ Settlement creates events, not deletion
- ⚠️ Settlement must be confirmed by server

---

## 🏛️ Ownership & Responsibility Matrix

| Concern | Mobile | Backend |
|---------|--------|---------|
| UI responsiveness | ✅ | ❌ |
| Offline capability | ✅ | ❌ |
| Data storage | ⚠️ temporary | ✅ permanent |
| Business invariants | ❌ | ✅ |
| Conflict resolution | ❌ | ✅ |
| Final truth | ❌ | ✅ |

**Golden Rule:** Mobile proposes, Backend disposes.

---

## 🔄 Sync Architecture

### Mutation Types
Every entity supports three operations:
- `CREATE` - Add new entity
- `UPDATE` - Modify existing entity  
- `DELETE` - Remove entity (soft delete)

### Mutation Contract

```kotlin
data class SyncMutation(
    val mutationId: String,        // UUID for idempotency
    val entityType: EntityType,    // EXPENSE | INCOME | LENDING
    val operation: Operation,      // CREATE | UPDATE | DELETE
    val entityId: String,          // Entity UUID
    val payload: JsonObject,       // Entity data
    val clientTimestamp: Instant,
    val clientVersion: Int         // For conflict detection
)

enum class EntityType { EXPENSE, INCOME, LENDING }
enum class Operation { CREATE, UPDATE, DELETE }
```

### Local State Machine

```
LOCAL_ONLY     → Entity created locally, not yet synced
    ↓
PENDING_SYNC   → Queued for upload to server
    ↓
SYNCED         → Confirmed by server
    ↔ CONFLICT  → Server has newer version
    ↓
REJECTED       → Server refused the change
```

---

## 🌐 API Contracts

### 1. Push Mutations (Mobile → Backend)

**Endpoint:** `POST /sync/push`

```json
{
  "deviceId": "device-123",
  "mutations": [
    {
      "mutationId": "uuid-1",
      "entityType": "EXPENSE",
      "operation": "CREATE", 
      "entityId": "uuid-2",
      "payload": {
        "amount": 350.00,
        "description": "Lunch",
        "category": "Food"
      },
      "clientTimestamp": "2026-01-19T10:00:00Z",
      "clientVersion": 1
    }
  ]
}
```

**Response:**
```json
{
  "results": [
    {
      "mutationId": "uuid-1",
      "status": "APPLIED",      // APPLIED | REJECTED | CONFLICT
      "serverVersion": 2,
      "reason": null            // Error reason if rejected
    }
  ]
}
```

### 2. Pull Changes (Backend → Mobile)

**Endpoint:** `POST /sync/pull`

```json
{
  "deviceId": "device-123",
  "since": "2026-01-19T09:00:00Z"
}
```

**Response:**
```json
{
  "changes": [
    {
      "entityType": "EXPENSE",
      "entityId": "uuid-3",
      "operation": "UPDATE",
      "payload": { "amount": 400.00 },
      "serverVersion": 3,
      "timestamp": "2026-01-19T10:05:00Z"
    }
  ],
  "newSyncTimestamp": "2026-01-19T10:05:00Z"
}
```

### 3. Bootstrap New Device

**Endpoint:** `POST /sync/bootstrap`

```json
{
  "deviceId": "new-device-456"
}
```

**Response:**
```json
{
  "expenses": [...],           // Recent expenses
  "incomes": [...],            // Recent incomes  
  "lendings": [...],           // Open lendings
  "balances": {
    "current": 1250.00,
    "pending": -50.00
  },
  "aggregates": {
    "monthlySpend": {...}
  },
  "lastSyncTimestamp": "2026-01-19T10:00:00Z"
}
```

---

## 💾 Local Storage Strategy

### Database Schema (Room/SQLite)

```sql
-- Entities
CREATE TABLE expenses (id TEXT PRIMARY KEY, amount REAL, description TEXT, ...);
CREATE TABLE incomes (id TEXT PRIMARY KEY, amount REAL, source TEXT, ...);
CREATE TABLE lendings (id TEXT PRIMARY KEY, person TEXT, amount REAL, ...);

-- Sync tracking
CREATE TABLE sync_mutations (
    mutation_id TEXT PRIMARY KEY,
    entity_type TEXT,
    operation TEXT,
    entity_id TEXT,
    payload TEXT,
    status TEXT,
    created_at INTEGER
);

CREATE TABLE sync_metadata (
    key TEXT PRIMARY KEY,
    value TEXT
);
```

### Data Retention Rules

**Mobile Storage:**
- ✅ Last 6 months of detailed data
- ✅ Aggregated summaries for older data
- ✅ All pending mutations
- ✅ Sync metadata

**Cloud Storage:**
- ✅ Complete historical data
- ✅ All mutations log
- ✅ Device sync states

---

## ⚡ Business Rules & Invariants

### Server-Side Invariants (Never enforced on mobile)
1. **Balance Validation:** User balance cannot go below zero
2. **Ownership:** Expenses must belong to authenticated user
3. **Immutability:** Settled lendings cannot be modified
4. **Idempotency:** Duplicate `mutationId` must be ignored
5. **Timestamps:** Server time is authoritative

### Mobile-Side Rules (Soft validation)
1. **Optimistic Updates:** Show changes immediately
2. **Offline Queue:** Store mutations when offline
3. **Conflict Indicators:** Show when server data differs
4. **Retry Logic:** Auto-retry failed syncs

---

## 🚀 Development Phases

### Phase 1: Local-Only Implementation
- [ ] Set up Room database with entities
- [ ] Implement CRUD operations
- [ ] Build basic UI (Compose)
- [ ] Add offline-first architecture
- [ ] Implement local state management

### Phase 2: Sync Foundation  
- [ ] Design mutation queue system
- [ ] Implement sync state machine
- [ ] Add network layer (Retrofit/Ktor)
- [ ] Build conflict detection logic
- [ ] Add retry mechanisms

### Phase 3: Backend Integration
- [ ] Implement push/pull endpoints
- [ ] Add authentication system
- [ ] Build conflict resolution UI
- [ ] Implement bootstrap flow
- [ ] Add comprehensive error handling

### Phase 4: Production Features
- [ ] Background sync workers
- [ ] Push notifications
- [ ] Data export/import
- [ ] Advanced conflict resolution
- [ ] Performance optimization

---

## 🛠️ Technology Stack

### Android
- **UI:** Jetpack Compose
- **Database:** Room + SQLite
- **Networking:** Retrofit + OkHttp
- **Background Work:** WorkManager
- **Dependency Injection:** Hilt
- **Architecture:** MVVM + Repository Pattern

### Backend (Future)
- **Runtime:** Node.js/Go/Spring Boot
- **Database:** PostgreSQL
- **Authentication:** JWT
- **API:** REST/GraphQL
- **Queue:** Redis/RabbitMQ

---

## 📋 Phase-0 Deliverables Checklist

- [x] **Entity Definitions** - Core data structures defined
- [x] **Mutation Format** - Sync contract established  
- [x] **Sync API Contracts** - Push/pull endpoints specified
- [x] **Local State Machines** - Entity lifecycle defined
- [x] **Ownership Rules** - Responsibility matrix created
- [x] **Invariants List** - Business rules documented
- [x] **Data Retention** - Storage strategy planned
- [x] **Bootstrap Flow** - New device setup defined

---

## 🎯 Success Metrics

- ✅ App works completely offline
- ✅ < 3 seconds sync time for typical datasets  
- ✅ Zero data loss during network interruptions
- ✅ Conflicts resolved without user confusion
- ✅ New devices bootstrap in < 10 seconds

---

## 📚 Additional Resources

- [Local-First Software Principles](https://www.inkandswitch.com/local-first/)
- [Android Room Database Guide](https://developer.android.com/training/data-storage/room)
- [Jetpack Compose Documentation](https://developer.android.com/jetpack/compose)

---

**Note:** This README follows the Phase-0 Foundation pattern. Implementation should begin only after all stakeholders agree on these contracts.