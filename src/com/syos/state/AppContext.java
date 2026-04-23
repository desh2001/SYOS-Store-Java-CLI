package com.syos.state;

import com.syos.factory.DAOFactory;
import java.util.Scanner;

/**
 * State Pattern — Context class that holds the current state and runs the state loop.
 * The application transitions between states (Login → MainMenu → InventoryMenu, etc.)
 * without using boolean flags or nested loops.
 */
public class AppContext {
    private AppState currentState;

    public AppContext(AppState initialState) {
        this.currentState = initialState;
    }

    /**
     * Run the application state machine.
     * Continues transitioning between states until a state returns null (exit).
     */
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
