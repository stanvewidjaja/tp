package seedu.address.ui;

import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.location.Location;

/**
 * An UI component that displays information of a {@code Location} in the planner.
 */
public class PlannerCard extends UiPart<Region> {

    private static final String FXML = "PlannerListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Location location;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private FlowPane tags;

    /**
     * Creates a {@code LocationCode} with the given {@code Location} and index to display.
     */
    public PlannerCard(Location location) {
        super(FXML);
        this.location = location;
        name.setText(location.getName().fullName);
        phone.setText(location.getPhone().value);
        address.setText(location.getAddress().value);
        email.setText(location.getEmail().value);
        location.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
    }
}
