package technicien;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import session.Session;
import gestionUtilisateurs.User;
import client.ClientController;
import client.ListeClientsController;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javafx.stage.Modality;

public class TechnicienController {

    @FXML
    private Label welcomeLabel;
    private User user;
    private ListeClientsController listeClientsController;

    
    @FXML
    public void initialize() {
        User user = Session.getInstance().getUser();
        if (user != null) {
            welcomeLabel.setText("Welcome " + user.getRole() + ": " + user.getNom());
        } else {
            // Handle the case where there is no user in session
            welcomeLabel.setText("Welcome!");
        }
    }
    
    @FXML
    private void listClients() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/listeClients.fxml"));
            Parent root = loader.load();

ListeClientsController controller = loader.getController();
List<User> clients = getClients();
controller.setClients(clients);  // <-- NullPointerException happens here

            // Show the client list view in a new stage
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Clients");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<User> getClients() {
        List<User> clients = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/atelierreparation","root","")) {
            String sql = "SELECT * FROM clients WHERE role = 'Client'";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
            	int id = resultSet.getInt("id");
                String nom = resultSet.getString("nom");
                String adresse = resultSet.getString("adresse");
                String telephone = resultSet.getString("telephone");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                String role = resultSet.getString("role");

                User client = new User(id,nom, adresse, telephone, email, password, role);
                clients.add(client);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clients;
    }
    
    @FXML
    private void listeAppareils() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gestionAppareils/listeAppareils.fxml"));
            Parent root = loader.load();

            // Show the appareil list view in a new stage
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Appareils");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void listeCategories() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gestionCategories/listeCategories.fxml"));
            Parent root = loader.load();

            // Show the appareil list view in a new stage
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Appareils");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    @FXML
    private void listeDemandes() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gestionDemandes/GestionDemandes.fxml"));
            Parent root = loader.load();

 Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Demandes");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            
     } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void showGestionPieces() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gestionPiecesD/GestionPieces.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Gestion des pièces détachées");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void listeOrdresReparation() {
    	try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/OrderReparation/ListeOrdres.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Gestion des pièces détachées");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    	}


}