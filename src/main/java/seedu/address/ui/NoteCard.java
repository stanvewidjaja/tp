package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.model.location.NoteContent;

/**
 * Represents a Note in the GUI
 */
public class NoteCard extends UiPart<Region> {
    private static final String FXML = "NoteCard.fxml";

    @FXML
    private VBox notePane;
    @FXML
    private TextArea note;

    /**
     * Creates a {@code NoteCard} with the given {@code NoteContent} to display.
     */
    public NoteCard(NoteContent noteContent) {
        super(FXML);
        note.setText(noteContent.toString());
    }
}
