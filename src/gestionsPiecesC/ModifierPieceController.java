package gestionsPiecesC;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import gestionPiecesD.Piece;

public class ModifierPieceController {

    @FXML
    private ComboBox<Piece> pieceComboBox;
    @FXML
    private TextField quantiteField;

    private PieceChangee pieceChangee;
    private PieceChangeeDAO pieceChangeeDAO;
    private Connection connection;

    public void setConnection(Connection connection) {
        this.connection = connection;
        this.pieceChangeeDAO = new PieceChangeeDAO(connection);
    }

    public void setPieceChangee(PieceChangee pieceChangee) {
        this.pieceChangee = pieceChangee;
        if (pieceChangee != null) {
            try {
                List<Piece> pieces = pieceChangeeDAO.getAlPieces();
                ObservableList<Piece> piecesObservableList = FXCollections.observableArrayList(pieces);
                pieceComboBox.setItems(piecesObservableList);  
                for (Piece piece : pieces) {
                    if (piece.getId() == pieceChangee.getPieceId()) {
                        pieceComboBox.setValue(piece);
                        break;
                    }
                }

// Affichez la quantité actuelle de la pièce changée
                quantiteField.setText(String.valueOf(pieceChangee.getQuantite()));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
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
// Mettre à jour la pièce changée dans la base de données
            pieceChangee.setPieceId(selectedPiece.getId());
            pieceChangee.setQuantite(quantite);
            
// Mettre à jour la description 
            pieceChangee.setDescription(selectedPiece.getDescription()); 
            pieceChangeeDAO.updatePieceChangee(pieceChangee);
            Stage stage = (Stage) pieceComboBox.getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
