package seedu.address.model.day;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import seedu.address.model.day.calorie.CalorieCount;
import seedu.address.model.day.calorie.Input;
import seedu.address.model.day.calorie.Output;
import seedu.address.model.tag.Tag;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Day {

    private List<Input> calorieInputList;
    private int totalCalorieIn;
    private int totalCalorieOut;
    private List<Output> calorieOutputList;

    // Identity fields
    private final Date date;
    private final Weight weight;
    private final Email email;

    // Data fields
    private final Address address;
    private final Set<Tag> tags = new HashSet<>();

    /**
     * Class constructor
     */
    public Day(Date date, Weight weight, Email email, Address address, Set<Tag> tags) {
        requireAllNonNull(date, weight, email, address, tags);
        this.date = date;
        this.weight = weight;
        this.email = email;
        this.address = address;
        this.tags.addAll(tags);
        this.calorieOutputList = new ArrayList<Output>();
        this.calorieInputList = new ArrayList<Input>();
    }

    public void addTotalCalorieOut(CalorieCount calorieCount) {
        totalCalorieOut += Integer.parseInt(calorieCount.toString());
    }

    public List<Output> getCalorieOutputList() {
        return calorieOutputList;
    }

    public List<Input> getCalorieInputList() {
        return calorieInputList;
    }

    public void addTotalCalorieInput(CalorieCount calorieInput) {
        totalCalorieIn += Integer.parseInt(calorieInput.toString());
    }

    /**
     * add a calorie input into the calorieInputList and update the total calorie input
     */
    public void addCalorieInput(Input calorieInput) {
        calorieInputList.add(calorieInput);
        addTotalCalorieInput(calorieInput.getCalorieCount());
    }
    /**
     * returns the total input calorie
     */
    public int getTotalCalorieIn() {
        return totalCalorieIn;
    }

    /**
     * add a calorie output into the calorieOutputList and update the total calorie output
     */
    public void addCalorieOutput(Output calorieOutput) {
        calorieOutputList.add(calorieOutput);
        addTotalCalorieOut(calorieOutput.getCalorieCount());
    }

    /**
     * returns the total output calorie
     */
    public int getTotalOutputCalorie() {
        return totalCalorieOut;
    }

    public Date getDate() {
        return date;
    }

    public Weight getWeight() {
        return weight;
    }

    public Email getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }


    /**
     * Returns true if both persons of the same name have at least one other identity field that is the same.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSameDay(Day otherDay) {
        if (otherDay == this) {
            return true;
        }

        return otherDay != null
                && otherDay.getDate().equals(getDate())
                && (otherDay.getEmail().equals(getEmail()) || otherDay.getWeight().equals(getWeight()));
    }

    /**
     * Returns true if both days have the same identity and data fields.
     * This defines a stronger notion of equality between two days.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Day)) {
            return false;
        }

        Day otherDay = (Day) other;
        return otherDay.getDate().equals(getDate())
                && otherDay.getWeight().equals(getWeight())
                && otherDay.getEmail().equals(getEmail())
                && otherDay.getAddress().equals(getAddress())
                && otherDay.getTags().equals(getTags());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(date, weight, email, address, tags);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getDate())
                .append(" Weight: ")
                .append(getWeight())
                .append(" Email: ")
                .append(getEmail())
                .append(" Address: ")
                .append(getAddress())
                .append(" Tags: ");
        getTags().forEach(builder::append);
        return builder.toString();
    }

}