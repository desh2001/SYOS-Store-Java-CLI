# SYOS Store — AI Quick Reference

> Compact reference for AI agents. All 11 design patterns implemented.

## Identity
- **Name**: SYOS Java CLI | **Stack**: Java 17+ / MySQL / CLI
- **Size**: 50 Java files | **DB**: `syos_db` on `localhost:3306`
- **Compile**: `javac -cp "lib/mysql-connector-j-9.7.0.jar" -d bin $(find src -name "*.java")`

## Packages
| Package | Files | Pattern |
|---------|-------|---------|
| `command/` | 10 | Command |
| `dao/` | 4 | DAO (implements Gateway) |
| `decorator/` | 5 | Decorator |
| `factory/` | 2 | Factory |
| `gateway/` | 3 | Gateway |
| `model/` | 5 | DTO/Model |
| `observer/` | 4 | Observer |
| `report/` | 6 | Template Method |
| `state/` | 5 | State |
| `strategy/` | 3 | Strategy |
| `util/` | 1 | Singleton |
| `view/` | 1 | View |

## All Patterns ✅
1. **Singleton** → `DatabaseConnection`, `MySQLDAOFactory`
2. **DAO** → `ItemDAO`, `StockDAO`, `BillDAO`, `ReportDAO`
3. **DTO** → `Bill`, `BillItem`, `Item`, `ItemStock`, `Stock`
4. **Command** → 9 commands for all menu actions
5. **Decorator** → `BasicBill` → `DiscountDecorator` → `TaxDecorator`
6. **Observer** → `ReorderLevelObserver`, `LowStockObserver`
7. **Factory** → `DAOFactory` → `MySQLDAOFactory`
8. **Template Method** → `ReportTemplate` + 5 reports
9. **State** → `LoginState` → `MainMenuState` → `InventoryMenuState`
10. **Gateway** → `ItemGateway`, `StockGateway`, `BillGateway`
11. **Strategy** → `FEFOStrategy`, `FIFOStrategy`

## State Flow
```
Login → MainMenu → {Inventory, Billing, OnlineStore, Reports, Logout→Login}
```

## Modification Guide
| Task | Where |
|------|-------|
| New menu option | New `Command` + add to State |
| New report | Extend `ReportTemplate` + add to `ReportDAO`/`GenerateReportCommand` |
| New bill modifier | Extend `BillDecorator` + add to `CreateBillCommand` |
| New stock strategy | Implement `StockDepletionStrategy` |
| New observer | Implement `StockObserver` + register in `MySQLDAOFactory` |
