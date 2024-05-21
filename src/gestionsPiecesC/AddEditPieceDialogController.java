package gestionsPiecesC;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.SQLException;
import gestionPiecesD.Piece;
import gestionPiecesD.PieceDAO;

public class AddEditPieceDialogController {

    @FXML
    private ComboBox<Piece> pieceComboBox;
    @FXML
    private TextField quantiteField;

    private int ordreId;
    private PieceChangee pieceChangee;
    private PieceChangeeDAO pieceChangeeDAO;
    private Connection connection;

    public void setConnection(Connection connection) {
        this.connection = connection;
        this.pieceChangeeDAO = new PieceChangeeDAO(connection);
    }

    public void setOrdreId(int ordreId) {
        this.ordreId = ordreId;
        loadPieces();
    }

    public void setPieceChangee(PieceChangee pieceChangee) {
        this.pieceChangee = pieceChangee;
        if (pieceChangee != null) {
            Piece selectedPiece = getPieceById(pieceChangee.getPieceId());
            pieceComboBox.setValue(selectedPiece);
            quantiteField.setText(String.valueOf(pieceChangee.getQuantite()));
        }
    }

    private Piece getPieceById(int pieceId) {
        for (Piece piece : pieceComboBox.getItems()) {
            if (piece.getId() == pieceId) {
                return piece;
            }
        }
        return null;
    }
    
    private void loadPieces() {
        PieceDAO pieceDAO = new PieceDAO();
        ObservableList<Piece> piecesObservableList = FXCollections.observableArrayList(pieceDAO.obtenirToutesPieces());
        pieceComboBox.setItems(piecesObservableList);
        if (pieceChangee != null) {
            piecesObservableList.remove(pieceChangee.getPiece());
        }
        
        pieceComboBox.setItems(piecesObservableList);
    }

    @FXML
    private void handleSave() {
        Piece selectedPiece = pieceComboBox.getValue();
        String quantiteText = quantiteField.getText();

        if (selectedPiece == null) {
            System.out.println("Veuillez sélectionner une pièce.");
            return;
        }

        int quantite;
        try {
            quantite = Integer.parseInt(quantiteText);
        } catch (NumberFormatException e) {
            System.out.println("La quantité doit être un nombre valide.");
            return;
        }

        if (quantite <= 0) {
            System.out.println("La quantité doit être supérieure à zéro.");
            return;
        }

        try {
            System.out.println("Ordre ID: " + ordreId);
            System.out.println("Piece ID: " + selectedPiece.getId());
            System.out.println("Quantité: " + quantite);

            PieceChangee newPieceChangee = new PieceChangee(0, ordreId, selectedPiece.getId(), quantite, selectedPiece, selectedPiece.getDescription());
            pieceChangeeDAO.addPieceChangee(newPieceChangee);

            Stage stage = (Stage) pieceComboBox.getScene().getWindow();
            stage.close();
        } catch(SQLException e) {
            e.printStackTrace();}}}
