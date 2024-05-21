package demandereparationclient;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import gestionDemandes.DemandeReparation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ModifierDemandeController {

    @FXML
    private TextArea descriptionField;
    @FXML
    private ComboBox<String> categorieComboBox;
    @FXML
    private ComboBox<String> appareilComboBox;

    private DemandeReparation demande;

    public void setDemande(DemandeReparation demande) {
        this.demande = demande;
        descriptionField.setText(demande.getDescription());
        loadCategories();
        loadAppareils();
    }

    private void loadCategories() {
        ObservableList<String> categories = FXCollections.observableArrayList();
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/atelierreparation", "root", "")) {
            String query = "SELECT libelle FROM categories";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                categories.add(resultSet.getString("libelle"));
            }
            categorieComboBox.setItems(categories);
            categorieComboBox.setValue(demande.getCategorie());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadAppareils() {
        ObservableList<String> appareils = FXCollections.observableArrayList();
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/atelierreparation", "root", "")) {
            String query = "SELECT id, description FROM appareils WHERE categorie = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, demande.getCategorie());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
     // Ajoute la description de l'appareil à la liste
                appareils.add(resultSet.getString("description"));
    if (resultSet.getInt("id") == demande.getAppareilId()) {
                    appareilComboBox.setValue(resultSet.getString("description"));
                }
            }
            appareilComboBox.setItems(appareils);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void enregistrer() {
        demande.setDescription(descriptionField.getText());
        String categorie = categorieComboBox.getValue();
        String appareil = appareilComboBox.getValue();

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/atelierreparation", "root", "")) {
            String query = "UPDATE demandes_reparation SET description = ?, categorie = ?, appareil = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, demande.getDescription());
            statement.setString(2, categorie);
            statement.setString(3, appareil);
            statement.setInt(4, demande.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
 Stage stage = (Stage) descriptionField.getScene().getWindow();
        stage.close();
    }
}

