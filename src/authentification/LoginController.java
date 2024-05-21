package authentification;

import gestionUtilisateurs.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import session.Session;
import technicien.TechnicienController;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.Main;
import client.ClientController;
import connexion.Connexion;

public class LoginController {
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;

    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Login Error!", "Please enter email and password");
            return;
        }

        Connection connection = Connexion.obtenirConnexion();
        if (connection != null) {
            try {
                String sql = "SELECT * FROM clients WHERE email = ? AND password = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, email);
                statement.setString(2, password);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    User user = new User(
                    	resultSet.getInt("id"), 
                        resultSet.getString("nom"),
                        resultSet.getString("adresse"),
                        resultSet.getString("telephone"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        resultSet.getString("role")
                    );

                    Session.getInstance().setUser(user);

                    String role = user.getRole();
                    String nom = user.getNom();
                    showAlert(Alert.AlertType.INFORMATION, "Login Successful!", "Welcome " + role + ": " + nom);

                    if (role.equals("Technician")) {
                        showTechnicienView();
                    } else if (role.equals("Client")) {
                        showClientView();
                    } else {
                        System.out.println("Le rôle de l'utilisateur n'est pas reconnu.");
                    }
                } else {
                    showAlert(Alert.AlertType.ERROR, "Login Error!", "Invalid email or password");
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error!", "Error executing login query: " + e.getMessage());
            } finally {
                Connexion.fermerConnexion(connection);
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Database Error!", "Failed to connect to the database");
        }
    }

    private void showTechnicienView() {
        Stage primaryStage = (Stage) emailField.getScene().getWindow();
        Main.showTechnicienView(primaryStage);
    }

    private void showClientView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/clientView.fxml"));
            Parent root = loader.load();
            ClientController controller = loader.getController();
            controller.initData(Session.getInstance().getUser());

            Scene scene = new Scene(root);
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Client Dashboard");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
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
    private void openRegistrationPage() {
        try {
            Main.showRegistrationView((Stage) emailField.getScene().getWindow());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
