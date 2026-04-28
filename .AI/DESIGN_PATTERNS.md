# Design Patterns — Implementation Reference

> All 11 design patterns are now **implemented**. This document provides code references for each.

## Pattern Index

| # | Pattern | Status | Package |
|---|---------|--------|---------|
| 1 | Singleton | ✅ | `util/`, `factory/` |
| 2 | DAO | ✅ | `dao/` |
| 3 | DTO/Model | ✅ | `model/` |
| 4 | Command | ✅ | `command/` |
| 5 | Decorator | ✅ | `decorator/` |
| 6 | Observer | ✅ | `observer/` |
| 7 | Factory | ✅ | `factory/` |
| 8 | Template Method | ✅ | `report/` |
| 9 | State | ✅ | `state/` |
| 10 | Gateway | ✅ | `gateway/` |
| 11 | Strategy | ✅ | `strategy/` |

---

## 1. Singleton Pattern ✅

**Files**: `DatabaseConnection.java`, `MySQLDAOFactory.java`

Thread-safe Singleton with `volatile` + `synchronized`:
```java
private static volatile DatabaseConnection instance;
public static synchronized DatabaseConnection getInstance() throws SQLException {
    if (instance == null) instance = new DatabaseConnection();
    else if (instance.getConnection().isClosed()) instance = new DatabaseConnection();
    return instance;
}
```

**Checklist**: Private constructor ✅ | Static instance ✅ | Global access ✅ | Lazy init ✅ | Thread-safe ✅

---

## 2. DAO Pattern ✅

**Files**: `ItemDAO.java`, `StockDAO.java`, `BillDAO.java`, `ReportDAO.java`

All DAOs implement Gateway interfaces:
- `ItemDAO implements ItemGateway`
- `StockDAO implements StockGateway`
- `BillDAO implements BillGateway`

---

## 3. DTO / Model Pattern ✅

**Files**: `Bill.java`, `BillItem.java`, `Item.java`, `ItemStock.java`, `Stock.java`

All raw arrays replaced:
- `int[]` → `BillItem` (with `itemId`, `itemCode`, `name`, `quantity`, `unitPrice`, `total`)
- `String[]` → `ItemStock` (with `itemId`, `itemCode`, `itemName`, `unitPrice`, `shelfQuantity`)
- Loose parameters → `Bill` (with `billType`, `totalAmount`, `cashTendered`, `changeAmount`, `List<BillItem>`)

---

## 4. Command Pattern ✅

**Files**: `Command.java` (interface) + 9 concrete commands

```java
public interface Command { void execute(); }
```

| Command | Menu Action |
|---------|------------|
| `AddItemCommand` | Inventory → Add Item |
| `UpdateItemCommand` | Inventory → Update Item |
| `DeleteItemCommand` | Inventory → Delete Item |
| `AddStockCommand` | Inventory → Add Stock |
| `UpdateStockCommand` | Inventory → Update Stock |
| `ReshelveStockCommand` | Inventory → Reshelve (uses Strategy) |
| `CreateBillCommand` | POS Billing (uses Decorator) |
| `GenerateReportCommand` | Reports sub-menu |
| `OpenOnlineStoreCommand` | Online Store launcher |

Commands receive dependencies via constructor injection (Scanner + Gateway interfaces from Factory).

---

## 5. Decorator Pattern ✅

**Files**: `BillComponent.java`, `BasicBill.java`, `BillDecorator.java`, `DiscountDecorator.java`, `TaxDecorator.java`

```
BillComponent (interface)
├── BasicBill (concrete component — wraps Bill model)
└── BillDecorator (abstract decorator)
    ├── DiscountDecorator (applies % discount)
    └── TaxDecorator (applies % tax)
```

Usage in `CreateBillCommand`:
```java
BillComponent pricing = new BasicBill(bill);
pricing = new DiscountDecorator(pricing, 10.0);  // 10% off
pricing = new TaxDecorator(pricing, 8.0);         // 8% tax
double finalTotal = pricing.calculateTotal();
```

---

## 6. Observer Pattern ✅

**Files**: `StockObserver.java`, `StockSubject.java`, `ReorderLevelObserver.java`, `LowStockObserver.java`

```java
public interface StockObserver {
    void onStockChanged(int itemId, String itemName, int newQuantity);
}
```

- **ReorderLevelObserver**: Warns when shelf stock < 20
- **LowStockObserver**: Warns when shelf stock ≤ 5 or = 0
- **StockSubject**: Manages observer list, called by `BillDAO` after billing

Observers are registered in `MySQLDAOFactory` and shared via `BillDAO`.

---

## 7. Factory Pattern ✅

**Files**: `DAOFactory.java` (interface), `MySQLDAOFactory.java` (concrete + Singleton)

```java
public interface DAOFactory {
    ItemGateway createItemDAO();
    StockGateway createStockDAO();
    BillGateway createBillDAO();
}
```

`MySQLDAOFactory` also sets up Observer pattern (registers stock observers).

---

## 8. Template Method Pattern ✅

**Files**: `ReportTemplate.java` (abstract) + 5 concrete reports

```java
public abstract class ReportTemplate {
    public final void generate() {      // Template Method
        printHeader();
        // execute query, for each row: formatRow(rs)
        printFooter();
    }
    protected abstract String getTitle();
    protected abstract String getQuery();
    protected abstract String[] getHeaders();
    protected abstract String formatRow(ResultSet rs);
    protected void printHeader() { /* default 4-col */ }  // Hook — overridable
    protected void printFooter() { /* default 4-col */ }  // Hook — overridable
}
```

`StockReport` and `BillReport` override `printHeader()`/`printFooter()` for 5-column layouts.

---

## 9. State Pattern ✅

**Files**: `AppState.java` (interface), `AppContext.java`, `LoginState.java`, `MainMenuState.java`, `InventoryMenuState.java`

```java
public interface AppState {
    AppState handleState(Scanner scanner, DAOFactory factory);
}
```

State transitions:
```
LoginState → MainMenuState → InventoryMenuState → MainMenuState
MainMenuState → LoginState (logout)
```

`AppContext` runs the state machine loop:
```java
while (currentState != null) {
    currentState = currentState.handleState(scanner, factory);
}
```

---

## 10. Gateway Pattern ✅

**Files**: `ItemGateway.java`, `StockGateway.java`, `BillGateway.java`

Interfaces abstracting data access — DAOs implement these:
- `ItemDAO implements ItemGateway`
- `StockDAO implements StockGateway`
- `BillDAO implements BillGateway`

All consuming code references Gateway interfaces, not concrete DAOs.

---

## 11. Strategy Pattern ✅

**Files**: `StockDepletionStrategy.java` (interface), `FEFOStrategy.java`, `FIFOStrategy.java`

```java
public interface StockDepletionStrategy {
    String getOrderByClause();
    String getStrategyName();
}
```

- **FEFOStrategy**: `ORDER BY expiry_date ASC` (default)
- **FIFOStrategy**: `ORDER BY stock_id ASC`

Used in `StockDAO.reshelveItem(itemId, qty, strategy)` — the SQL query incorporates the strategy's ORDER BY clause.
