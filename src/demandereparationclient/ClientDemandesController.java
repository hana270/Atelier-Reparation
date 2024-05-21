package demandereparationclient;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import session.Session;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import gestionDemandes.DemandeReparation;

public class ClientDemandesController {

    @FXML
    private TableView<DemandeReparation> tableDemandes;
    @FXML
    private TableColumn<DemandeReparation, Integer> colId;
    @FXML
    private TableColumn<DemandeReparation, String> colDescription;
    @FXML
    private TableColumn<DemandeReparation, String> colStatut;
    @FXML
    private TableColumn<DemandeReparation, Timestamp> colDateDemande;

    private ObservableList<DemandeReparation> demandesList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));
        colDateDemande.setCellValueFactory(new PropertyValueFactory<>("dateDemande"));

        loadDemandes(); }

    public void loadDemandes() {
        demandesList.clear();
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/atelierreparation", "root", "")) {
            String query = "SELECT * FROM demandes_reparation WHERE client_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, Session.getInstance().getUser().getId());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
            	demandesList.add(new DemandeReparation(
            		    resultSet.getInt("id"),
            		    resultSet.getInt("appareil_id"),
            		    resultSet.getInt("client_id"),
            		    resultSet.getString("statut"),
            		    resultSet.getTimestamp("date_demande"),
            		    resultSet.getString("description")
            		));

            }
            tableDemandes.setItems(demandesList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void modifierDemande() {
        DemandeReparation selectedDemande = tableDemandes.getSelectionModel().getSelectedItem();
        if (selectedDemande != null && "En attente".equals(selectedDemande.getStatut())) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/demandereparationclient/modifierDemande.fxml"));
                Parent root = loader.load();

                ModifierDemandeController controller = loader.getController();
                controller.setDemande(selectedDemande);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Modifier Demande de Réparation");
                stage.showAndWait();
                loadDemandes();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Erreur", "Vous ne pouvez pas modifier cette demande car elle a déjà été traitée.");
        }
    }
   
    @FXML
    private void supprimerDemande() {
        DemandeReparation selectedDemande = tableDemandes.getSelectionModel().getSelectedItem();
        if (selectedDemande != null && "En attente".equals(selectedDemande.getStatut())) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Êtes-vous sûr de vouloir supprimer cette demande ?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                   try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/atelierreparation", "root", "")) {
                        String query = "DELETE FROM demandes_reparation WHERE id = ?";
                        PreparedStatement statement = connection.prepareStatement(query);
                        statement.setInt(1, selectedDemande.getId());
                        statement.executeUpdate();
                        loadDemandes(); // Refresh the table view
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            showAlert("Erreur", "Vous ne pouvez pas supprimer cette demande car elle a déjà été traitée.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
