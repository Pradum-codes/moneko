# 📦 Local Database Schema (Phase-1)

This document describes the local-only database schema for the offline-first, local-first personal finance Android application.

## 🎯 Scope

- **Platform:** Android + Room (SQLite)
- **Language:** Kotlin
- **Architecture:** Offline-first
- **Current Phase:** No backend, no sync implementation yet
- **Data Source:** Local database is the only source of truth for UI

This schema is designed to support future synchronization without requiring destructive migrations.

## 🎯 Design Goals

- ✅ Mobile writes directly to local database
- ✅ Database remains usable entirely offline
- ✅ Every record is independently syncable
- ✅ Avoid duplicated or derived data
- ✅ Simple, explicit, and extensible schema
- ✅ No premature optimization or backend assumptions

## 🧠 Core Principles

### 📱 Local-first
All writes happen locally. UI reads exclusively from the local DB.

### 🚫 No derived data storage
Values such as balances are computed, never stored.

### 🗑️ Soft deletes
Records are marked as deleted instead of being physically removed.

### 🔄 State machine per entity
Each entity tracks its own sync lifecycle.

## 🔄 Sync State Machine (Local-Only)

Every entity includes a `syncState` field with the following states:

```
LOCAL_ONLY → PENDING_SYNC → SYNCED
```

### SyncState Enum

```kotlin
enum class SyncState {
    PENDING_SYNC,  // Created locally, needs sync when available
    SYNCED,        // Confirmed by server
    REJECTED,      // Server refused the change
    CONFLICT       // Server has newer version
}
```

> **Note:** Sync logic is not implemented in Phase-1. These fields exist to avoid schema changes later.

## 📚 Common Fields (All Tables)

All entities share the following fields:

| Field | Type | Description |
|-------|------|-------------|
| `id` | String (UUID) | Client-generated primary key |
| `createdAt` | Long | Creation timestamp (epoch millis) |
| `updatedAt` | Long | Last update timestamp |
| `syncState` | SyncState | Local sync lifecycle state |
| `localVersion` | Int | Incremented on each local update |
| `isDeleted` | Boolean | Soft delete flag |emented in Phase-1. These fields exist to avoid schema changes later.

📚 Common Fields (All Tables)

All entities share the following fields:

Field	Type	Description
id	String (UUID)	Client-generated primary key
createdAt	Long	Creation timestamp (epoch millis)
updatedAt	Long	Last update timestamp
syncState	Enum	Local sync lifecycle state
localVersion	Int	Incremented on each local update
##  Expense Table

Stores outgoing transactions.

```kotlin
@Entity(
    tableName = "expense",
    indices = [
        Index("createdAt"),
        Index("category"),
        Index("syncState")
    ]
)
data class ExpenseEntity(
    @PrimaryKey val id: String,
    val amount: Long,           // Smallest currency unit (e.g., paise, cents)
    val category: String,
    val note: String?,          // Optional description
    val createdAt: Long,
    val updatedAt: Long,
    val syncState: SyncState,
    val localVersion: Int,
    val isDeleted: Boolean
)
```

### 📝 Notes
- 💰 **Amount storage:** Stored in smallest currency unit (e.g., paise, cents)
- 🚫 **No floating-point:** Avoids precision issues with currency calculations
- 📊 **Indexed fields:** `createdAt` for chronological queries, `category` for filtering
## 💰 Income Table

Stores incoming funds.

```kotlin
@Entity(
    tableName = "income",
    indices = [
        Index("createdAt"),
        Index("source"),
        Index("syncState")
    ]
)
data class IncomeEntity(
    @PrimaryKey val id: String,
    val amount: Long,           // Smallest currency unit (e.g., paise, cents)
    val source: String,         // Income source (salary, freelance, etc.)
    val note: String?,          // Optional description
    val createdAt: Long,
    val updatedAt: Long,
    val syncState: SyncState,
    val localVersion: Int,
    val isDeleted: Boolean
)
```

### 📝 Notes
- 💰 **Amount storage:** Consistent with expense table (smallest currency unit)
- 🏢 **Source tracking:** Enables income categorization and analysis
- 📊 **Indexed fields:** `createdAt` for chronological queries, `source` for filtering, `syncState` for sync operations

## 🤝 Lending Table

Tracks money lent to or borrowed from others.

```kotlin
enum class LendingType {
    LENT,      // Money given to someone else
    BORROWED   // Money received from someone else
}

@Entity(
    tableName = "lending",
    indices = [
        Index("createdAt"),
        Index("counterparty"),
        Index("type"),
        Index("syncState")
    ]
)
data class LendingEntity(
    @PrimaryKey val id: String,
    val type: LendingType,      // LENT or BORROWED
    val amount: Long,           // Smallest currency unit
    val counterparty: String,   // Person/entity involved
    val note: String?,          // Optional description
    val isSettled: Boolean = false,  // Settlement status
    val createdAt: Long,
    val updatedAt: Long,
    val syncState: SyncState,
    val localVersion: Int,
    val isDeleted: Boolean
)
```

### 📝 Notes
- 🎯 **Explicit direction:** `LENT` vs `BORROWED` prevents ambiguity during balance computation
- 👥 **Counterparty tracking:** Enables relationship-based lending analysis
- ✅ **Settlement status:** `isSettled` tracks whether debt has been repaid
- 📊 **Indexed fields:** `createdAt`, `counterparty`, and `type` for efficient querying


## ❌ Transactions Table (Intentionally Omitted)

There is **no generic Transactions table** in this schema.

### 🎯 Rationale
- ✅ **Already transactional:** Expense, Income, and Lending entities are inherently transactional
- 🚫 **Prevents duplication:** Avoids storing the same data in multiple places
- ⚡ **Avoids conflicts:** Reduces reconciliation complexity during sync operations
- 🔍 **Unified views:** Combined transaction views can be created using SQL queries or Room `@Query` methods

### 💡 Future Implementation
```kotlin
// Example: Unified transaction view
@Query("""
    SELECT id, amount, createdAt, 'EXPENSE' as type, category as description 
    FROM expense WHERE isDeleted = 0
    UNION ALL
    SELECT id, amount, createdAt, 'INCOME' as type, source as description 
    FROM income WHERE isDeleted = 0
    ORDER BY createdAt DESC
""")
suspend fun getAllTransactions(): List<TransactionView>
```

## 🧮 Balance Calculation (Derived)

Balance is calculated dynamically, never stored:

```kotlin
// Pseudo-formula
Balance = SUM(Income) - SUM(Expense) ± Lending

// Detailed calculation
Current Balance = 
    SUM(income.amount WHERE isDeleted = false)
  - SUM(expense.amount WHERE isDeleted = false)  
  + SUM(lending.amount WHERE type = BORROWED AND isDeleted = false AND isSettled = false)
  - SUM(lending.amount WHERE type = LENT AND isDeleted = false AND isSettled = false)
```

### 📊 Balance Types
```kotlin
data class BalanceInfo(
    val currentBalance: Long,           // Available balance
    val totalIncome: Long,             // Lifetime income
    val totalExpenses: Long,           // Lifetime expenses  
    val totalLent: Long,               // Money lent to others
    val totalBorrowed: Long,           // Money borrowed from others
    val pendingLent: Long,             // Unsettled money lent
    val pendingBorrowed: Long          // Unsettled money borrowed
)
```

### ✅ Benefits
- 🎯 **Always accurate:** No risk of balance/transaction mismatch
- ⚡ **Sync-safe:** Eliminates balance reconciliation during sync
- 🔄 **Offline-friendly:** Works correctly across offline updates

---

## 📋 Critical Business Rules

### 🗑️ Soft Delete Query Requirements
**MANDATORY:** All DAO queries MUST filter out `isDeleted = true` unless explicitly required for admin operations.

```kotlin
// ✅ Correct
@Query("SELECT * FROM expense WHERE isDeleted = 0 ORDER BY createdAt DESC")

// ❌ Wrong - will include deleted records
@Query("SELECT * FROM expense ORDER BY createdAt DESC")
```

### 🤝 Lending Settlement Rules
1. **Settlement Effect:** When `isSettled = true`, the lending entry no longer contributes to balance calculations
2. **Immutable After Settlement:** Settled lending records cannot be modified (except soft delete)
3. **Settlement Creates History:** Settlement doesn't delete the record, it closes the obligation

### 🔄 Sync State Lifecycle
- **New records:** Always start with `PENDING_SYNC` state
- **No LOCAL_ONLY:** All data needs eventual sync in future phases
- **State transitions:** Only backend can move records to `SYNCED`, `REJECTED`, or `CONFLICT`

---