package seedu.address.logic.commands;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.calorie.*;
import seedu.address.model.day.Date;
import seedu.address.model.day.Day;
import seedu.address.model.day.Weight;
import seedu.address.model.tag.Tag;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.*;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_DAYS;


public class ChangeCommand extends Command {

    public static final String COMMAND_WORD = "change";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the calorie identified "
            + "by the index number used in the displayed calorie list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_TIME + "TIME] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " 1 ";

    public static final String MESSAGE_EDIT_DAY_SUCCESS = "Edited Calorie: %1$s";


    private Index index;
    private LocalDate date;
    private Index calorieIndex;
    private ChangeCalorieDescriptor changeCalorieDescriptor;

    /**
     * @param index of the calorie in the list to edit.
     * @param changeCalorieDescriptor details to edit the calorie with.
     */
    public ChangeCommand(Index index, ChangeCommand.ChangeCalorieDescriptor changeCalorieDescriptor,
                         Index calorieIndex) {
        requireNonNull(index);
        requireNonNull(changeCalorieDescriptor);

        this.index = index;
        this.changeCalorieDescriptor = new ChangeCommand.ChangeCalorieDescriptor(changeCalorieDescriptor);
        this.calorieIndex = calorieIndex;
    }

    /**
     * @param date of the calorie is in.
     * @param changeCalorieDescriptor details to edit the calorie with.
     */
    public ChangeCommand(LocalDate date, ChangeCommand.ChangeCalorieDescriptor changeCalorieDescriptor,
                         Index calorieIndex) {
        requireNonNull(date);
        requireNonNull(changeCalorieDescriptor);

        this.date = date;
        this.changeCalorieDescriptor = new ChangeCommand.ChangeCalorieDescriptor(changeCalorieDescriptor);
        this.calorieIndex = calorieIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Day> lastShownList = model.getMyFitnessBuddy().getDayList();
        Day editDay;

        if (index != null) {
            if (index.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_DAY_DISPLAYED_INDEX);
            } else {
                System.out.println(index.getZeroBased());
                editDay = lastShownList.get(index.getZeroBased());
            }
        } else {
            editDay = model.getDay(date);
        }

        CalorieManager calorieManager = editDay.getCalorieManager();
        Calorie calorieToEdit = calorieManager.getCalorie(changeCalorieDescriptor.getIsOut(), calorieIndex);

        Calorie editedCalorie = createEditedCalorie(calorieToEdit, changeCalorieDescriptor);
        CalorieManager cm = calorieManager.setCalorie(calorieIndex, changeCalorieDescriptor.getIsOut(), editedCalorie);

        Date date = editDay.getDate();
        Weight weight = editDay.getWeight();
        Set<Tag> tag = editDay.getTags();
        Day editedDay = new Day(date, weight, tag, cm);

        model.setDay(editDay, editedDay);
        model.updateFilteredDayList(PREDICATE_SHOW_ALL_DAYS);

        return new CommandResult(String.format(MESSAGE_EDIT_DAY_SUCCESS, editedCalorie));
    }

    /**
     * Creates and returns a {@code Day} with the details of {@code dayToEdit}
     * edited with {@code editDayDescriptor}.
     */
    private static Calorie createEditedCalorie(Calorie calorieToEdit,
                                               ChangeCommand.ChangeCalorieDescriptor changeCalorieDescriptor) {
        assert changeCalorieDescriptor != null;

        Time updatedTime = changeCalorieDescriptor.getTime().orElse(calorieToEdit.getTime());
        CalorieCount updatedCalorieCount = changeCalorieDescriptor.getCalorieCount()
                .orElse(calorieToEdit.getCalorieCount());

        Boolean isOut = changeCalorieDescriptor.getIsOut();
        if (isOut) {
            Exercise updatedExercise = changeCalorieDescriptor.getExercise()
                    .orElse(((Output) calorieToEdit).getExercise());
            return new Output(updatedTime, updatedExercise, updatedCalorieCount);
        } else {
            Food updatedFood = changeCalorieDescriptor.getFood()
                    .orElse(((Input) calorieToEdit).getFood());
            return new Input(updatedTime, updatedFood, updatedCalorieCount);
        }
    }

    /**
     * Stores the details to edit the calorie with. Each non-empty field value will replace the
     * corresponding field value of the calorie.
     */
    public static class ChangeCalorieDescriptor {

        private Time time;
        private Food food;
        private Exercise exercise;
        private CalorieCount calorieCount;
        private Boolean isOut;

        public ChangeCalorieDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public ChangeCalorieDescriptor(ChangeCommand.ChangeCalorieDescriptor toCopy) {
            setTime(toCopy.time);
            setFood(toCopy.food);
            setExercise(toCopy.exercise);
            setCalorieCount(toCopy.calorieCount);
            setIsOut(toCopy.isOut);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            System.out.println();
            return CollectionUtil.isAnyNonNull(time, calorieCount, food, exercise);
        }

        public void setIsOut(Boolean isOut) {
            this.isOut = isOut;
        }

        public Boolean getIsOut() {
            return isOut;
        }

        public void setTime(Time time) {
            this.time = time;
        }

        public Optional<Time> getTime() {
            return Optional.ofNullable(time);
        }

        public void setFood(Food food) {
            this.food = food;
        }

        public Optional<Food> getFood() {
            return Optional.ofNullable(food);
        }

        public void setExercise(Exercise exercise) {
            this.exercise = exercise;
        }

        public Optional<Exercise> getExercise() {
            return Optional.ofNullable(exercise);
        }

        public void setCalorieCount(CalorieCount calorieCount) {
            this.calorieCount = calorieCount;
        }
        public Optional<CalorieCount> getCalorieCount() {
            return Optional.ofNullable(calorieCount);
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditCommand.EditDayDescriptor)) {
                return false;
            }

            // state check
            ChangeCommand.ChangeCalorieDescriptor e = (ChangeCommand.ChangeCalorieDescriptor) other;

            return getTime().equals(e.getTime())
                    && getTime().equals(e.getTime())
                    && getCalorieCount().equals(e.getCalorieCount())
                    && getExercise().equals(e.getExercise())
                    && getFood().equals(e.getFood());
        }
    }
}

