package seedu.address.model.location.dates;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;

/**
 * Represents a {@code VisitDate} that occurs every day
 */
public class EveryDayDate extends VisitDate {
    // Only allowed instance of EveryDayDate
    public static final EveryDayDate EVERYDAY_DATE = new EveryDayDate();

    /**
     * Constructs a {@code EveryDayDate}.
     */
    private EveryDayDate() {
    }

    /**
     * @return true, since it occurs everyday
     */
    @Override
    public boolean isOn(LocalDate date) {
        requireNonNull(date);
        return true;
    }

    @Override
    public String toDataString() {
        return "everyday";
    }

    @Override
    public String toString() {
        return "Everyday";
    }

    @Override
    public String toSortString() {
        return this.toString();
    }

    @Override
    public boolean equals(Object other) {
        return this == other;
    }
}
