package hu.bute.daai.amorg.todolabor.data;

/**
 * Created by android on 2016. 03. 08..
 */
public class Todo {
    public enum Priority { LOW, MEDIUM, HIGH }

    private String title;
    private Priority priority;
    private String dueDate;
    private String description;

    public Todo(String aTitle, Priority aPriority,
                String aDueDate, String aDescription)
    {
        title = aTitle;
        priority = aPriority;
        dueDate = aDueDate;
        description = aDescription;
    }

    public String getTitle() {
        return title;
    }

    public Priority getPriority() {
        return priority;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getDescription() {
        return description;
    }
}