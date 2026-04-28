package com.syos.state;

import com.syos.factory.DAOFactory;
import java.util.Scanner;

public class AppContext {
    private AppState currentState;

    public AppContext(AppState initialState) {
        this.currentState = initialState;
    }

    public void run(Scanner scanner, DAOFactory factory) {
        while (currentState != null) {
            currentState = currentState.handleState(scanner, factory);
        }
        System.out.println("\n [*] Application exited. Goodbye!");
    }

    public AppState getCurrentState() {
        return currentState;
    }
}


