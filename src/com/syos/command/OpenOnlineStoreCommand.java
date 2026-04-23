package com.syos.command;

import com.syos.factory.DAOFactory;
import com.syos.view.OnlineStoreCLI;
import java.util.Scanner;

/**
 * Command Pattern — Encapsulates the "Open Online Store" action.
 * Delegates to OnlineStoreCLI view.
 */
public class OpenOnlineStoreCommand implements Command {
    private Scanner scanner;
    private DAOFactory factory;

    public OpenOnlineStoreCommand(Scanner scanner, DAOFactory factory) {
        this.scanner = scanner;
        this.factory = factory;
    }

    @Override
    public void execute() {
        System.out.println("\n [*] Launching Online Store CLI...");
        new OnlineStoreCLI(scanner, factory).start();
    }
}
