# SYOS Store Java CLI — Project Overview

> **Purpose**: This document provides a comprehensive, AI-consumable overview of the SYOS (Synex Outlet Store) Java CLI application. It covers project structure, architecture, every file's role, and a detailed analysis of software design patterns.

---

## 1. Project Summary

| Attribute         | Detail                                                      |
|-------------------|-------------------------------------------------------------|
| **Project Name**  | SYOS Store Java CLI                                         |
| **Language**      | Java (JDK 17+)                                              |
| **UI**            | Command-Line Interface (CLI)                                |
| **Database**      | MySQL (`syos_db`) via JDBC (`mysql-connector-j-9.7.0`)      |
| **Build System**  | None (manual compilation via VS Code Java Projects)         |
| **Architecture**  | Layered: Model → Gateway → DAO → Command → State → View    |
| **Domain**        | Retail Point-of-Sale & Online Store management system       |

### Core Features
1. **Admin Authentication** — Hardcoded username/password login (State Pattern).
2. **Inventory Management** — CRUD for items; warehouse stock add/update; FEFO-based shelf reshelving (Command + Strategy Patterns).
3. **POS Billing** — In-store billing with shelf-stock deduction, optional discount/tax, and receipt printing (Command + Decorator + Observer Patterns).
4. **Online Store** — Browse items, multi-item cart, Cash-on-Delivery ordering (DTO + Gateway Patterns).
5. **Reports** — Daily sales, reshelving needs, reorder levels, warehouse stock, transaction history (Template Method Pattern).

---

## 2. Directory & File Map

```
SYOS-Store-Java-CLI/
├── .AI/                                   # AI context documentation
│   ├── AI_QUICK_REFERENCE.md
│   ├── DESIGN_PATTERNS.md
│   └── PROJECT_OVERVIEW.md
├── .gitignore
├── README.md
├── lib/
│   ├── mysql-connector-j-9.7.0.jar        # MySQL JDBC driver (USED)
│   └── javafx-*.jar / *.dll               # JavaFX libraries (NOT used)
└── src/
    ├── App.java                            # Entry point — State + Factory setup
    └── com/syos/
        ├── command/                        # Command Pattern
        │   ├── Command.java                #   Interface
        │   ├── AddItemCommand.java         #   Inventory: add item
        │   ├── UpdateItemCommand.java      #   Inventory: update item
        │   ├── DeleteItemCommand.java      #   Inventory: delete item
        │   ├── AddStockCommand.java        #   Inventory: add warehouse stock
        │   ├── UpdateStockCommand.java     #   Inventory: update warehouse stock
        │   ├── ReshelveStockCommand.java   #   Inventory: warehouse→shelf transfer
        │   ├── CreateBillCommand.java      #   POS billing (+ Decorator)
        │   ├── GenerateReportCommand.java  #   Report sub-menu
        │   └── OpenOnlineStoreCommand.java #   Online store launcher
        ├── dao/                            # DAO Pattern (implements Gateway interfaces)
        │   ├── BillDAO.java                #   Bill processing + Observer notifications
        │   ├── ItemDAO.java                #   Item CRUD
        │   ├── ReportDAO.java              #   Delegates to Template Method reports
        │   └── StockDAO.java               #   Stock ops + Strategy pattern
        ├── decorator/                      # Decorator Pattern
        │   ├── BillComponent.java          #   Component interface
        │   ├── BasicBill.java              #   Concrete component
        │   ├── BillDecorator.java          #   Abstract decorator
        │   ├── DiscountDecorator.java      #   Discount modifier
        │   └── TaxDecorator.java           #   Tax modifier
        ├── factory/                        # Factory Pattern
        │   ├── DAOFactory.java             #   Abstract factory interface
        │   └── MySQLDAOFactory.java        #   Concrete factory (Singleton)
        ├── gateway/                        # Gateway Pattern
        │   ├── ItemGateway.java            #   Item access interface
        │   ├── StockGateway.java           #   Stock access interface
        │   └── BillGateway.java            #   Bill access interface
        ├── model/                          # DTO / Model Pattern
        │   ├── Bill.java                   #   Bill transaction DTO
        │   ├── BillItem.java               #   Bill line-item DTO
        │   ├── Item.java                   #   Item entity
        │   ├── ItemStock.java              #   Item + shelf stock DTO
        │   └── Stock.java                  #   Warehouse stock batch entity
        ├── observer/                       # Observer Pattern
        │   ├── StockObserver.java          #   Observer interface
        │   ├── StockSubject.java           #   Subject (event broadcaster)
        │   ├── ReorderLevelObserver.java   #   Warns when stock < 20
        │   └── LowStockObserver.java       #   Warns when stock < 5 or 0
        ├── report/                         # Template Method Pattern
        │   ├── ReportTemplate.java         #   Abstract template
        │   ├── DailySalesReport.java       #   Daily sales
        │   ├── ReshelvingReport.java       #   Reshelving needs
        │   ├── ReorderReport.java          #   Reorder levels
        │   ├── StockReport.java            #   Warehouse batches
        │   └── BillReport.java             #   All transactions
        ├── state/                          # State Pattern
        │   ├── AppState.java               #   State interface
        │   ├── AppContext.java             #   Context (state machine runner)
        │   ├── LoginState.java             #   Login screen
        │   ├── MainMenuState.java          #   Main menu
        │   └── InventoryMenuState.java     #   Inventory sub-menu
        ├── strategy/                       # Strategy Pattern
        │   ├── StockDepletionStrategy.java #   Strategy interface
        │   ├── FEFOStrategy.java           #   First Expired First Out
        │   └── FIFOStrategy.java           #   First In First Out
        ├── util/
        │   └── DatabaseConnection.java     # Singleton (thread-safe)
        └── view/
            └── OnlineStoreCLI.java         # Online store UI (uses Factory + DTO)
```

**Total source files: 50 Java files**

---

## 3. Design Pattern Summary

| # | Pattern              | Status        | Key Files                                                |
|---|----------------------|---------------|----------------------------------------------------------|
| 1 | **Singleton**        | ✅ Implemented | `DatabaseConnection`, `MySQLDAOFactory`                  |
| 2 | **DAO**              | ✅ Implemented | `ItemDAO`, `StockDAO`, `BillDAO`, `ReportDAO`            |
| 3 | **DTO / Model**      | ✅ Implemented | `Bill`, `BillItem`, `Item`, `ItemStock`, `Stock`         |
| 4 | **Command**          | ✅ Implemented | 9 command classes in `com.syos.command`                  |
| 5 | **Decorator**        | ✅ Implemented | `BillComponent`, `BasicBill`, `DiscountDecorator`, `TaxDecorator` |
| 6 | **Observer**         | ✅ Implemented | `StockObserver`, `StockSubject`, 2 concrete observers    |
| 7 | **Factory**          | ✅ Implemented | `DAOFactory`, `MySQLDAOFactory`                          |
| 8 | **Template Method**  | ✅ Implemented | `ReportTemplate` + 5 concrete reports                    |
| 9 | **State**            | ✅ Implemented | `AppState`, `AppContext`, 3 concrete states              |
| 10| **Gateway**          | ✅ Implemented | `ItemGateway`, `StockGateway`, `BillGateway`             |
| 11| **Strategy**         | ✅ Implemented | `StockDepletionStrategy`, `FEFOStrategy`, `FIFOStrategy` |

---

## 4. Data Flow

```
┌───────────┐     ┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│ App.java  │────▶│ State Layer  │────▶│Command Layer │────▶│  DAO Layer   │
│ (entry)   │     │ LoginState   │     │ AddItemCmd   │     │ ItemDAO      │
│           │     │ MainMenuState│     │ CreateBillCmd│     │ StockDAO     │
│           │     │ InventoryMenu│     │ ReshelveCmd  │     │ BillDAO      │
└───────────┘     └──────────────┘     │ ReportCmd    │     │ ReportDAO    │
                                       └──────┬───────┘     └──────┬───────┘
                                              │                     │
                  ┌──────────────┐            │              ┌──────┴───────┐
                  │  Decorator   │◄───────────┘              │   Gateway    │
                  │ DiscountDec  │                           │ ItemGateway  │
                  │ TaxDecorator │                           │ StockGateway │
                  └──────────────┘                           │ BillGateway  │
                                                             └──────┬───────┘
                  ┌──────────────┐     ┌──────────────┐            │
                  │  Observer    │◄────│  Factory     │     ┌──────┴───────┐
                  │ ReorderLevel │     │ MySQLDAO     │     │   Database   │
                  │ LowStock     │     │  Factory     │     │  (syos_db)   │
                  └──────────────┘     └──────────────┘     └──────────────┘

                  ┌──────────────┐     ┌──────────────┐
                  │  Strategy    │     │  Template    │
                  │ FEFO/FIFO    │     │ ReportTempl  │
                  └──────────────┘     └──────────────┘
```

---

## 5. Database Schema (Inferred)

```sql
items (
    item_id     INT PRIMARY KEY AUTO_INCREMENT,
    item_code   VARCHAR,
    item_name   VARCHAR,
    unit_price  DOUBLE
)

stock_store (
    stock_id    INT PRIMARY KEY AUTO_INCREMENT,
    item_id     INT REFERENCES items(item_id),
    batch_no    VARCHAR,
    quantity    INT,
    expiry_date DATE
)

shelf_stock (
    item_id     INT UNIQUE REFERENCES items(item_id),
    quantity    INT
)

bills (
    bill_id         INT PRIMARY KEY AUTO_INCREMENT,
    bill_type       VARCHAR,    -- 'OFFLINE' or 'ONLINE'
    total_amount    DOUBLE,
    cash_paid       DOUBLE,
    balance_amount  DOUBLE,
    bill_date       DATETIME
)

bill_items (
    bill_id     INT REFERENCES bills(bill_id),
    item_id     INT REFERENCES items(item_id),
    quantity    INT,
    item_total  DOUBLE
)
```

---

## 6. AI Context Notes

- **Codebase size**: 50 Java files, ~2000+ lines of source code.
- **Complexity**: Medium — well-structured with 11 design patterns.
- **Language features**: Java generics, interfaces, abstract classes, method overriding.
- **Testing**: Zero test files. No test framework configured.
- **Build**: No build tool (Maven/Gradle). Compile with: `javac -cp "lib/mysql-connector-j-9.7.0.jar" -d bin $(find src -name "*.java")`
- **Comments**: Mix of English and Sinhala (සිංහල).
- **Encoding**: UTF-8.
