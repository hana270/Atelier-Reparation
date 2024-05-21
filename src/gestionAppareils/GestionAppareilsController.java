package gestionAppareils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.*;

public class GestionAppareilsController {

    @FXML
    private TableView<Appareil> tableAppareils;
    @FXML
    private TableColumn<Appareil, Integer> colId;
    @FXML
    private TableColumn<Appareil, String> colDescription;
    @FXML
    private TableColumn<Appareil, String> colMarque;
    @FXML
    private TableColumn<Appareil, Integer> colCategorie;
    @FXML
    private Button btnAjouter;

    private ObservableList<Appareil> appareilsList = FXCollections.observableArrayList();

  
    @FXML
    private void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colMarque.setCellValueFactory(new PropertyValueFactory<>("marque"));
        colCategorie.setCellValueFactory(new PropertyValueFactory<>("categorie"));

        loadAppareils();
    }


    private void loadAppareils() {
        appareilsList.clear();
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/atelierreparation", "root", "")) {
            String query = "SELECT a.id, a.description, a.marque, c.libelle AS categorie FROM appareils a JOIN categories c ON a.categorie_id = c.id";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                appareilsList.add(new Appareil(
                        resultSet.getInt("id"),
                        resultSet.getString("description"),
                        resultSet.getString("marque"),
                        resultSet.getString("categorie")
                ));
            }

            tableAppareils.setItems(appareilsList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void ajouterAppareil() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gestionAppareils/ajouterAppareil.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Ajouter Appareil");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            loadAppareils();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void modifierAppareil() {
        Appareil selectedAppareil = tableAppareils.getSelectionModel().getSelectedItem();
        if (selectedAppareil != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/gestionAppareils/modifierAppareil.fxml"));
                Scene scene = new Scene(loader.load());
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setTitle("Modifier Appareil");
                stage.initModality(Modality.APPLICATION_MODAL);

                AjouterAppareilController controller = loader.getController();
                controller.setAppareil(selectedAppareil);

                stage.showAndWait();

                loadAppareils();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void supprimerAppareil() {
        Appareil selectedAppareil = tableAppareils.getSelectionModel().getSelectedItem();
        if (selectedAppareil != null) {
            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/atelierreparation", "root", "")) {
 // Supprimer les demandes de réparation associées à l'appareil
                String deleteDemandesReparationSQL = "DELETE FROM demandes_reparation WHERE appareil_id = ?";
                PreparedStatement deleteDemandesReparationStatement = connection.prepareStatement(deleteDemandesReparationSQL);
                deleteDemandesReparationStatement.setInt(1, selectedAppareil.getId());
                deleteDemandesReparationStatement.executeUpdate();
 // Supprimer l'appareil lui-même
                String deleteAppareilSQL = "DELETE FROM appareils WHERE id = ?";
                PreparedStatement deleteAppareilStatement = connection.prepareStatement(deleteAppareilSQL);
                deleteAppareilStatement.setInt(1, selectedAppareil.getId());
                deleteAppareilStatement.executeUpdate();

                loadAppareils(); 
// Actualiser la liste des appareils après suppression
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

   


}
