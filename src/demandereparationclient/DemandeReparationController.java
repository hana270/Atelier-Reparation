package demandereparationclient;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import session.Session;
import gestionUtilisateurs.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import gestionDemandes.DemandeReparation;

public class DemandeReparationController {

    @FXML
    private TextField descriptionField;
    @FXML
    private ComboBox<String> categorieComboBox;
    @FXML
    private ComboBox<String> appareilComboBox;

    @FXML
    private void initialize() {
        loadCategories();
        chargerAppareils(); 
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

 
    
    
//Prévention de la Visualisation d'un Appareil Déjà Ajouté
    @FXML
    private void handleSubmit() {
        String description = descriptionField.getText();
        String categorie = categorieComboBox.getValue();
        String appareil = appareilComboBox.getValue();

        if (description.isEmpty() || categorie == null || appareil == null) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please fill all the fields.");
            return;
        }

        User user = Session.getInstance().getUser();
        if (user == null) {
            showAlert(Alert.AlertType.ERROR, "User Error!", "No user session found. Please log in again.");
            return;
        }
        int clientId = user.getId();
        int appareilId = getAppareilId(appareil);

        if (appareilId == -1) {
            showAlert(Alert.AlertType.ERROR, "Appareil Error!", "Selected appareil not found.");
            return;
        }
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/atelierreparation", "root", "")) {
            String insertDemandeQuery = "INSERT INTO demandes_reparation (client_id, appareil_id, date_demande, statut, description) VALUES (?, ?, NOW(), 'En attente', ?)";
            PreparedStatement demandeStatement = connection.prepareStatement(insertDemandeQuery);
            demandeStatement.setInt(1, clientId);
            demandeStatement.setInt(2, appareilId);
            demandeStatement.setString(3, description);
            demandeStatement.executeUpdate();

            showAlert(Alert.AlertType.INFORMATION, "Success!", "Repair request submitted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error!", "An error occurred while processing your request.");
        }
    }
// cherche l'ID d'un appareil
    private int getAppareilId(String appareil) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/atelierreparation", "root", "")) {
            String query = "SELECT id FROM appareils WHERE description = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, appareil);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

//recharge la liste des appareils en fonction de la catg selectionne
    @FXML
    private void chargerAppareils() {
        appareilComboBox.getItems().clear();
        String selectedCategorie = categorieComboBox.getValue();
        if (selectedCategorie != null && !selectedCategorie.isEmpty()) {
            int categorieId = getCategorieId(selectedCategorie);
            if (categorieId != -1) {
                try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/atelierreparation", "root", "")) {
                    String query = "SELECT description FROM appareils WHERE categorie_id = ?";
                    PreparedStatement statement = connection.prepareStatement(query);
                    statement.setInt(1, categorieId);
                    ResultSet resultSet = statement.executeQuery();

                    while (resultSet.next()) {
                        appareilComboBox.getItems().add(resultSet.getString("description"));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private int getCategorieId(String categorie) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/atelierreparation", "root", "")) {
            String query = "SELECT id FROM categories WHERE libelle = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, categorie);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
