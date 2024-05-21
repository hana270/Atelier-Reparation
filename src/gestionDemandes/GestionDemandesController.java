package gestionDemandes;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.DefaultStringConverter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import javafx.beans.property.SimpleStringProperty;

public class GestionDemandesController {

    @FXML
    private TableView<DemandeReparation> tableDemandes;
    @FXML
    private TableColumn<DemandeReparation, Integer> colId;
    @FXML
    private TableColumn<DemandeReparation, Integer> colClientId;
    @FXML
    private TableColumn<DemandeReparation, Integer> colAppareilId;
    @FXML
    private TableColumn<DemandeReparation, String> colDescription;
    @FXML
    private TableColumn<DemandeReparation, String> colStatut;
    @FXML
    private TableColumn<DemandeReparation, java.sql.Timestamp> colDateDemande;

    private ObservableList<DemandeReparation> demandesList = FXCollections.observableArrayList();

    @FXML
    private TableColumn<DemandeReparation, String> colClientNom;
    @FXML
    private TableColumn<DemandeReparation, String> colAppareilDescription;
    
    
    @FXML
    private void initialize() {
        colStatut.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        colStatut.setOnEditCommit(event -> {
            DemandeReparation demande = event.getRowValue();
            String nouveauStatut = event.getNewValue();
            demande.setStatut(nouveauStatut);
            modifierStatut(demande, nouveauStatut);
        });
// Liaison des colonnes aux propriétés des objets DemandeReparation
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colClientNom.setCellValueFactory(new PropertyValueFactory<>("clientNom"));
        colAppareilDescription.setCellValueFactory(new PropertyValueFactory<>("appareilDescription"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));
        colDateDemande.setCellValueFactory(new PropertyValueFactory<>("dateDemande"));

        loadDemandes();
    }


    private void loadDemandes() {
        demandesList.clear();
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/atelierreparation", "root", "")) {
            String query = "SELECT d.id, d.client_id, d.statut, d.date_demande, d.description, d.appareil_id, " +
                           "a.description AS appareil_description, c.nom AS client_nom " +
                           "FROM demandes_reparation d " +
                           "INNER JOIN appareils a ON d.appareil_id = a.id " +
                           "INNER JOIN clients c ON d.client_id = c.id";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int clientId = resultSet.getInt("client_id");
                String statut = resultSet.getString("statut");
                Timestamp dateDemande = resultSet.getTimestamp("date_demande");
                String description = resultSet.getString("description");
                int appareilId = resultSet.getInt("appareil_id");
                String appareilDescription = resultSet.getString("appareil_description");
                String clientNom = resultSet.getString("client_nom");

                demandesList.add(new DemandeReparation(id, clientId, statut, dateDemande, description, appareilId, appareilDescription, clientNom));
            }
            tableDemandes.setItems(demandesList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

 private void modifierStatut(DemandeReparation demande, String nouveauStatut) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/atelierreparation", "root", "")) {
            String query = "UPDATE demandes_reparation SET statut = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, nouveauStatut);
            statement.setInt(2, demande.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tableDemandes.refresh();  }
   
    }
