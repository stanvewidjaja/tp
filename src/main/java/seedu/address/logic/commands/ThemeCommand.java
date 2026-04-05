package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

import seedu.address.commons.core.Theme;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.Model;

/**
 * Changes the application theme.
 */
public class ThemeCommand extends Command {
    public static final String COMMAND_WORD = "theme";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Changes the application theme.\n"
            + "Parameters: THEME_NAME (light OR dark)\n"
            + "Example: " + COMMAND_WORD + " dark";

    public static final String MESSAGE_SUCCESS = "Theme set to %1$s.";

    private final Theme theme;

    /**
     * Creates a {@code ThemeCommand} to change the application theme.
     *
     * @param theme The theme to switch to.
     */
    public ThemeCommand(Theme theme) {
        requireNonNull(theme);
        assert theme == Theme.LIGHT || theme == Theme.DARK;
        this.theme = theme;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.setTheme(theme);
        return new CommandResult(String.format(MESSAGE_SUCCESS, theme.getDisplayName().toLowerCase()));
    }

    public Theme getTheme() {
        return theme;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof ThemeCommand)) {
            return false;
        }

        ThemeCommand otherThemeCommand = (ThemeCommand) other;
        return theme == otherThemeCommand.theme;
    }

    @Override
    public int hashCode() {
        return Objects.hash(theme);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("theme", theme)
                .toString();
    }
}
