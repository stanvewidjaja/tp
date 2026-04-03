package seedu.address.ui;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.location.Location;
import seedu.address.model.location.NoteContent;

/**
 * Panel containing the planner.
 */
public class PlannerListPanel extends UiPart<Region> {
    private static final String FXML = "PlannerListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(PlannerListPanel.class);

    @FXML
    private VBox noteContainer;

    @FXML
    private ListView<Location> plannerListView;

    @FXML
    private Label plannerHeader;

    /**
     * Creates a {@code LocationListPanel} with the given {@code ObservableList}.
     */
    public PlannerListPanel(ObservableList<Location> plannerList) {
        super(FXML);
        plannerListView.setItems(plannerList);
        plannerListView.setCellFactory(listView -> new PlannerListPanel.PlannerListViewCell());
        showNote(new NoteContent("TEST"));
    }

    public void setPlannerHeader(String date) {
        if (date == null) {
            logger.warning("Invalid DateParser usage. Displaying default Planner header.");
            plannerHeader.setText("Start planning...");
        } else if (date.isEmpty()) {
            plannerHeader.setText("Start planning...");
        } else {
            plannerHeader.setText(date);
        }
    }

    /**
     * Displays note in the planner pane if non-null. Otherwise, clears the note
     * @param note Valid note to display
     */
    public void showNote(NoteContent note) {
        noteContainer.getChildren().clear();

        if (note == null || note.toString().isBlank()) {
            return; // nothing shown
        }

        NoteCard noteNode = new NoteCard(note);
        noteContainer.getChildren().add(noteNode.getRoot());
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Location} using a {@code LocationCard}.
     */
    class PlannerListViewCell extends ListCell<Location> {
        @Override
        protected void updateItem(Location location, boolean empty) {
            super.updateItem(location, empty);

            if (empty || location == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new PlannerCard(location).getRoot());
            }
        }
    }

}
