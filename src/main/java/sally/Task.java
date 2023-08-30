package sally;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a general task with task description and completion status.
 */
public class Task {
    protected String task;
    protected boolean toBeDone;

    /**
     * Constructs a Task object with the given task description and sets it as not done by default.
     *
     * @param task The description of the task.
     */
    public Task(String task) {
        this.task = task;
        this.toBeDone = true;
    }

    /**
     * Marks the task as done.
     */
    public void mark() {
        toBeDone = false;
    }

    /**
     * Unmarks the task as done, setting its completion status to not done.
     */
    public void unmark() {
        toBeDone = true;
    }

    /**
     * Returns the status icon indicating whether the task is done or not done.
     *
     * @return The status icon as a string.
     */
    public String getStatusIcon() {
        return (toBeDone ? "[ ]" : "[X]");
    }

    /**
     * Returns a string representation of the task formatted for storage in a file.
     * If the task is an instance of Todo, Event, or Deadline, returns their specific string representations.
     *
     * @return The string representation of the task for storage.
     */
    public String toFileString() {
        if (this instanceof Todo) {
            Todo todo = (Todo) this;
            return todo.toString();
        } else if (this instanceof Event) {
            Event event = (Event) this;
            return event.toString();
        } else if (this instanceof Deadline) {
            Deadline deadline = (Deadline) this;
            return deadline.toString();
        } else {
            return this.toString();
        }
    }

    /**
     * Converts a string representation of date and time to a LocalDateTime object.
     *
     * @param input The input string representing date and time.
     * @return The LocalDateTime object parsed from the input string.
     */
    private static LocalDateTime convertToDateTime(String input) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("MMM dd yyyy, h:mm a");
        return LocalDateTime.parse(input, inputFormatter);
    }

    /**
     * Converts a string representation of a task from a file to a Task object.
     *
     * @param fileString The string representation of the task from the file.
     * @return The Task object created from the file string.
     */
    public static Task fromFileString(String fileString) {
        String[] split = fileString.split("]\\[");
        String splitOne = split[0]; // [D
        String[] toGetTaskType = splitOne.split("\\[");
        String taskType = toGetTaskType[1]; // D

        String splitTwo = split[1]; // " ] soc (by:sun)"
        String[] toGetStatusIcon = splitTwo.split("] ");
        String statusIcon = toGetStatusIcon[0]; // " "
        boolean isDone = false;

        if (statusIcon.equals(" ")) {
            isDone = false;
        } else {
            isDone = true;
        }

        String taskDescription = toGetStatusIcon[1];

        Task newTask;

        if (taskType.equals("T")) {
            newTask = new Todo(taskDescription);
        } else if (taskType.equals("E")) {
            String[] toGetFromTo = taskDescription.split(" \\(from: | to: |\\)");
            String task = toGetFromTo[0];
            LocalDateTime from = convertToDateTime(toGetFromTo[1]);
            LocalDateTime to = convertToDateTime(toGetFromTo[2]);
            newTask = new Event(task, from, to);

        } else if (taskType.equals("D")) {
            String[] toGetBy = taskDescription.split(" \\(by: |\\)");
            String task = toGetBy[0];
            LocalDateTime by = convertToDateTime(toGetBy[1]);
            newTask = new Deadline(task, by);

        } else {
            newTask = new Task(taskDescription);
        }

        if (isDone) {
            newTask.mark();
        }

        return newTask;
    }

    /**
     * Returns a string representation of the task for display.
     *
     * @return The string representation of the task.
     */
    @Override
    public String toString() {
        return getStatusIcon() + " " + task;
    }
}
