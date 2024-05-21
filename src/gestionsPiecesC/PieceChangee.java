package gestionsPiecesC;

import javafx.beans.property.*;
import gestionPiecesD.Piece;

public class PieceChangee {
    private final IntegerProperty id;
    private final IntegerProperty ordreId;
    private final IntegerProperty pieceId;
    private final IntegerProperty quantite;
    private final ObjectProperty<Piece> piece;
    private final StringProperty description;

    public PieceChangee(int id, int ordreId, int pieceId, int quantite, Piece piece, String description) {
        this.id = new SimpleIntegerProperty(id);
        this.ordreId = new SimpleIntegerProperty(ordreId);
        this.pieceId = new SimpleIntegerProperty(pieceId);
        this.quantite = new SimpleIntegerProperty(quantite);
        this.piece = new SimpleObjectProperty<>(piece);
        this.description = new SimpleStringProperty(description);
    }
    
    // Constructeur alternatif sans l'objet Piece
    public PieceChangee(int id, int ordreId, int pieceId, int quantite, String description) {
        this(id, ordreId, pieceId, quantite, null, description);
    }

    // Constructeur par défaut
    public PieceChangee() {
        this.id = new SimpleIntegerProperty();
        this.ordreId = new SimpleIntegerProperty();
        this.pieceId = new SimpleIntegerProperty();
        this.quantite = new SimpleIntegerProperty();
        this.piece = new SimpleObjectProperty<>();
        this.description = new SimpleStringProperty();
    }

    // Getters et setters
    public IntegerProperty idProperty() {
        return id;
    }

    public IntegerProperty ordreIdProperty() {
        return ordreId;
    }

    public IntegerProperty pieceIdProperty() {
        return pieceId;
    }

    public IntegerProperty quantiteProperty() {
        return quantite;
    }

    public ObjectProperty<Piece> pieceProperty() {
        return piece;
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public int getId() {
        return id.get();
    }

    public int getOrdreId() {
        return ordreId.get();
    }

    public int getPieceId() {
        return pieceId.get();
    }

    public int getQuantite() {
        return quantite.get();
    }

    public Piece getPiece() {
        return piece.get();
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public void setQuantite(int quantite) {
        this.quantite.set(quantite);
    }

    public void setPieceId(int pieceId) {
        this.pieceId.set(pieceId);
    }
}
