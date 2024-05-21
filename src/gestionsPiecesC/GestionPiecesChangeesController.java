package gestionsPiecesC;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class GestionPiecesChangeesController {

    @FXML
    private TableView<PieceChangee> piecesTable;
    @FXML
    private TableColumn<PieceChangee, String> descriptionColumn;
    @FXML
    private TableColumn<PieceChangee, Integer> quantiteColumn;
    @FXML
    private Button addButton;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;

    private ObservableList<PieceChangee> piecesChangees = FXCollections.observableArrayList();
    private PieceChangeeDAO pieceChangeeDAO;
    private int ordreId;
    private Connection connection; // Declare connection variable

    public GestionPiecesChangeesController(Connection connection) {
        this.connection = connection;
        this.pieceChangeeDAO = new PieceChangeeDAO(connection);
    }

    @FXML
    private void initialize() {
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        quantiteColumn.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        piecesTable.setItems(piecesChangees);
    }

    public void setOrdreId(int ordreId) {
        this.ordreId = ordreId;
        loadPiecesChangees();}

    private void loadPiecesChangees() {
        try {
            List<PieceChangee> piecesChangees = pieceChangeeDAO.getPiecesChangeesByOrdreId(ordreId);
            piecesTable.getItems().clear();
            piecesTable.getItems().addAll(piecesChangees);
        } catch (SQLException e) {
            e.printStackTrace();
        }}


    @FXML
    private void handleAddPiece() {
        PieceChangee newPiece = new PieceChangee();
        showPieceDialog(newPiece);
    }
    

    @FXML
    private void handleDeletePiece() {
        PieceChangee selectedPiece = piecesTable.getSelectionModel().getSelectedItem();
        if (selectedPiece != null) {
            try {
                pieceChangeeDAO.deletePieceChangee(selectedPiece.getId());
                piecesChangees.remove(selectedPiece);
            } catch (SQLException e) {
                e.printStackTrace();
            }}}

    private void showPieceDialog(PieceChangee piece) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gestionsPiecesC/ajoutPieceC.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Ajouter Pièce");

            Scene scene = new Scene(loader.load());
            stage.setScene(scene);

            AddEditPieceDialogController controller = loader.getController();
            controller.setConnection(connection);
            controller.setPieceChangee(piece);
            controller.setOrdreId(ordreId);

            stage.showAndWait();

            loadPiecesChangees();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleEditPiece() {
        PieceChangee selectedPiece = piecesTable.getSelectionModel().getSelectedItem();
        if (selectedPiece != null) {
            showEditPieceDialog(selectedPiece);
        }
    }

    private void showEditPieceDialog(PieceChangee piece) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gestionsPiecesC/modifierPieceC.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Modifier Pièce");

            Scene scene = new Scene(loader.load());
            stage.setScene(scene);

            ModifierPieceController controller = loader.getController();
            controller.setConnection(connection);
            controller.setPieceChangee(piece);

            stage.showAndWait();

            loadPiecesChangees();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
