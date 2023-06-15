package tdtu.project.finaltodo;

public class firebasemodel {
    private String title;
    private String description;
    private  String due;

    public firebasemodel() {

    }

    public firebasemodel(String title, String description,String due) {
        this.title = title;
        this.description = description;
        this.due = due;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDue() {
        return due;
    }

    public void setDue(String due) {
        this.due = due;
    }
}
