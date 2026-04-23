package com.syos.gateway;

import com.syos.model.Bill;
import java.sql.SQLException;

/**
 * Gateway Pattern — Interface abstracting Bill data access operations.
 * Decouples business logic from persistence implementation.
 */
public interface BillGateway {
    int processBill(Bill bill) throws SQLException;
}
