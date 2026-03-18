package seedu.address.ui;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.location.Location;

/**
 * Panel containing the planner.
 */
public class PlannerListPanel extends UiPart<Region> {
    private static final String FXML = "PlannerListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(PlannerListPanel.class);

    @FXML
    private ListView<Location> plannerListView;

    /**
     * Creates a {@code LocationListPanel} with the given {@code ObservableList}.
     */
    public PlannerListPanel(ObservableList<Location> plannerList) {
        super(FXML);
        plannerListView.setItems(plannerList);
        plannerListView.setCellFactory(listView -> new PlannerListPanel.PlannerListViewCell());
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
