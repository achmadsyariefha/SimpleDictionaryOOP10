package sample;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.Date;

public class Dictionary {

    private SimpleIntegerProperty id;
    private SimpleStringProperty english;
    private SimpleStringProperty indonesia;
    private Date dateCreated;
    private Date dateModified;

    public Dictionary(int id, String english, String indonesia, Date dateCreated, Date dateModified) {
        this.id = new SimpleIntegerProperty (id);
        this.english = new SimpleStringProperty(english);
        this.indonesia = new SimpleStringProperty(indonesia);
        this.dateCreated = dateCreated;
        this.dateModified = dateModified;


    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getEnglish() {
        return english.get();
    }

    public SimpleStringProperty englishProperty() {
        return english;
    }

    public void setEnglish(String english) {
        this.english.set(english);
    }

    public String getIndonesia() {
        return indonesia.get();
    }

    public SimpleStringProperty indonesiaProperty() {
        return indonesia;
    }

    public void setIndonesia(String indonesia) {
        this.indonesia.set(indonesia);
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateModified() {
        return dateModified;
    }

    public void setDateModified(Date dateModified) {
        this.dateModified = dateModified;
    }
}
