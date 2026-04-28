package com.syos.gateway;

import com.syos.model.Bill;
import java.sql.SQLException;

public interface BillGateway {
    int processBill(Bill bill) throws SQLException;
}


