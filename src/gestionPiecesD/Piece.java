package gestionPiecesD;

import javafx.beans.property.*;

public class Piece {
    private final IntegerProperty id;
    private final StringProperty description;
    private final DoubleProperty prixHT;

    public Piece(int id, String description, double prixHT) {
        this.id = new SimpleIntegerProperty(id);
        this.description = new SimpleStringProperty(description);
        this.prixHT = new SimpleDoubleProperty(prixHT);
    }
    @Override
    public String toString() {
        return this.getDescription() + " -- " + this.getPrixHT() + " dt";
    }
    
    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public double getPrixHT() {
        return prixHT.get();
    }

    public void setPrixHT(double prixHT) {
        this.prixHT.set(prixHT);
    }

    public DoubleProperty prixHTProperty() {
        return prixHT;
    }
    
}
