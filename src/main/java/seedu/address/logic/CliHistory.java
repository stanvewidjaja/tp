package seedu.address.logic;

import java.util.ArrayList;

/**
 * Represents the history of user command inputs, with reference to a point in the history
 */
public class CliHistory {
    // acts as a stack, index 0 is the oldest command, index (size() - 1) is the latest
    private final ArrayList<String> history;
    // refers to a point in history, if -1, means that it is currently not pointing to it
    private int indexPointer;

    CliHistory() {
        history = new ArrayList<>();
        indexPointer = -1;
    }

    /**
     * Adds a user input into the history stack
     * @param input user command entered to be stored in history
     */
    public void addInput(String input) {
        assert (input != null);
        history.add(input);
        indexPointer = -1;
    }

    /**
     * Sets the pointer to the most recent entry
     */
    public void setPointerMostRecent() {
        if (history.isEmpty()) {
            return;
        }
        indexPointer = history.size() - 1;
    }

    /**
     * Returns the previous String input (command) in history from where the pointer is pointing to.
     * If the pointer is currently not pointing to an index (-1), returns the most recently entered command,
     * which is the last element of history. If pointer at the oldest command, returns itself.
     * .
     * Mimics behaviour of pressing up arrow in the CLI
     * @return the String before the current point in history, can be empty
     */
    public String getPrevious() {
        // no history yet
        if (history.isEmpty()) {
            return "";
        }
        // pointer not initialized, return latest command in history
        if (indexPointer == -1) {
            indexPointer = history.size() - 1;
            return history.get(indexPointer);
        }
        // pointer to the oldest command already, return itself
        if (indexPointer == 0) {
            return history.get(0);
        }
        // decrement pointer and get the command in history
        return history.get(--indexPointer);
    }

    /**
     * Returns the next String input (command) in history from where the pointer is pointing to.
     * If the pointer is currently not pointing to an index (-1), returns an empty String
     * If pointer at the latest command, reset point and return empty string.
     * .
     * Mimics behaviour of pressing down arrow in the CLI
     * @return the String after the current point in history, can be empty
     */
    public String getNext() {
        // no history yet
        if (history.isEmpty()) {
            return "";
        }
        // pointer not initialized, return latest command in history
        if (indexPointer == -1) {
            return "";
        }
        // pointer to the most recent command already, return blank and reset pointer
        if (indexPointer == history.size() - 1) {
            indexPointer = -1;
            return "";
        }
        // increment pointer and get the command in history
        return history.get(++indexPointer);
    }
}
