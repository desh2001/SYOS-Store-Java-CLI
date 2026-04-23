package com.syos.state;

import com.syos.factory.DAOFactory;
import java.util.Scanner;

/**
 * State Pattern — Interface for application states.
 * Each state handles its own display and input processing,
 * and returns the next state to transition to.
 */
public interface AppState {
    /**
     * Display the UI for this state and handle user input.
     * @param scanner Scanner for user input
     * @param factory DAOFactory for creating DAO instances
     * @return The next AppState to transition to, or null to exit the application
     */
    AppState handleState(Scanner scanner, DAOFactory factory);
}
