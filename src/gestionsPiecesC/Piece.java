package gestionsPiecesC;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Piece {
    private final IntegerProperty id;
    private final StringProperty description;
    private final DoubleProperty prixHT;
    private final BooleanProperty selected;
    private final IntegerProperty quantite; 

    
    public Piece(int id, String description, double prixHT) {
        this.id = new SimpleIntegerProperty(id);
        this.description = new SimpleStringProperty(description);
        this.prixHT = new SimpleDoubleProperty(prixHT);
        this.selected = new SimpleBooleanProperty(false);
        this.quantite = new SimpleIntegerProperty(0); // Initialisation de la quantité à 0
    }

    // Getters and setters for id
    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    // Getters and setters for description
    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    // Getters and setters for prixHT
    public double getPrixHT() {
        return prixHT.get();
    }

    public DoubleProperty prixHTProperty() {
        return prixHT;
    }

    public void setPrixHT(double prixHT) {
        this.prixHT.set(prixHT);
    }

    // Getters and setters for selected
    public boolean isSelected() {
        return selected.get();
    }

    public BooleanProperty selectedProperty() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

    // Getters and setters for quantite
    public int getQuantite() {
        return quantite.get();
    }

    public IntegerProperty quantiteProperty() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite.set(quantite);
    }
  
}
