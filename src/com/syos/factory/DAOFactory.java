package com.syos.factory;

import com.syos.gateway.ItemGateway;
import com.syos.gateway.StockGateway;
import com.syos.gateway.BillGateway;

public interface DAOFactory {
    ItemGateway createItemDAO();
    StockGateway createStockDAO();
    BillGateway createBillDAO();
}


