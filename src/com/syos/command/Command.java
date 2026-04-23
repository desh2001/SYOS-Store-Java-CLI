package com.syos.command;

/**
 * Command Pattern — Interface for encapsulating actions as objects.
 * Each menu action is a separate Command implementation.
 */
public interface Command {
    /**
     * Execute the command's action.
     * All user interaction (Scanner I/O) happens within execute().
     */
    void execute();
}
