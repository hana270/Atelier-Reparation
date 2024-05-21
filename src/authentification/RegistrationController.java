package authentification;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.Main;
import connexion.Connexion;
import gestionUtilisateurs.User;

public class RegistrationController {
    @FXML
    private TextField nomField;
    @FXML
    private TextField adresseField;
    @FXML
    private TextField telephoneField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private ComboBox<String> roleComboBox;

    @FXML
    public void initialize() {
        roleComboBox.getItems().addAll("Client", "Technician");
    }

    @FXML
    private void handleRegister() {
        String nom = nomField.getText();
        String adresse = adresseField.getText();
        String telephone = telephoneField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String role = roleComboBox.getValue();

        if (nom.isEmpty() || adresse.isEmpty() || telephone.isEmpty() || email.isEmpty() || password.isEmpty() || role == null) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter all fields");
            return;
        }

        Connection connection = Connexion.obtenirConnexion();
        if (connection != null) {
            try {
                String sql = "INSERT INTO clients (nom, adresse, telephone, email, password, role) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                statement.setString(1, nom);
                statement.setString(2, adresse);
                statement.setString(3, telephone);
                statement.setString(4, email);
                statement.setString(5, password);
                statement.setString(6, role);
                int rowsInserted = statement.executeUpdate();
                
// Récupération de l'identifiant généré par bdd après l'insertion
                int generatedId = -1;
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    generatedId = generatedKeys.getInt(1);
                }

                if (rowsInserted > 0) {
                    showAlert(Alert.AlertType.INFORMATION, "Registration Successful!", "Welcome " + nom);
                    openLoginPage();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Database Error!", "Failed to register user");
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error!", "Error inserting user data: " + e.getMessage());
            } finally {
                Connexion.fermerConnexion(connection);
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Database Error!", "Failed to connect to the database");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void openLoginPage() {
        try {
            Main.showLoginView((Stage) nomField.getScene().getWindow());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
