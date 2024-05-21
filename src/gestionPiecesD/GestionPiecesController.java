package gestionPiecesD;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class GestionPiecesController {
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField prixHTField;
    @FXML
    private TableView<Piece> piecesTable;
    @FXML
    private TableColumn<Piece, Number> idColumn;
    @FXML
    private TableColumn<Piece, String> descriptionColumn;
    @FXML
    private TableColumn<Piece, Number> prixHTColumn;

    private ObservableList<Piece> piecesList;
    private PieceDAO pieceDAO = new PieceDAO();

    @FXML
    public void initialize() {
        piecesList = FXCollections.observableArrayList(pieceDAO.obtenirToutesPieces());
        piecesTable.setItems(piecesList);

        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        prixHTColumn.setCellValueFactory(cellData -> cellData.getValue().prixHTProperty());
    }

    @FXML
    private void handleAjouterPiece() {
        String description = descriptionField.getText();
        double prixHT = Double.parseDouble(prixHTField.getText());
        Piece piece = new Piece(0, description, prixHT);
        pieceDAO.ajouterPiece(piece);
        piecesList.add(piece);
    }

    @FXML
    private void handleModifierPiece() {
        Piece selectedPiece = piecesTable.getSelectionModel().getSelectedItem();
        if (selectedPiece != null) {
            selectedPiece.setDescription(descriptionField.getText());
            selectedPiece.setPrixHT(Double.parseDouble(prixHTField.getText()));
            pieceDAO.modifierPiece(selectedPiece);
            piecesTable.refresh();
        }
    }

    @FXML
    private void handleSupprimerPiece() {
        Piece selectedPiece = piecesTable.getSelectionModel().getSelectedItem();
        if (selectedPiece != null) {
            pieceDAO.supprimerPiece(selectedPiece.getId());
            piecesList.remove(selectedPiece);
        }
    }

    @FXML
    private void handlePieceSelection(MouseEvent event) {
        Piece selectedPiece = piecesTable.getSelectionModel().getSelectedItem();
        if (selectedPiece != null) {
            descriptionField.setText(selectedPiece.getDescription());
            prixHTField.setText(String.valueOf(selectedPiece.getPrixHT()));
        }
    }
    @FXML
    private void handleQuitter() {
        Stage stage = (Stage) piecesTable.getScene().getWindow();
        stage.close();
    }
}