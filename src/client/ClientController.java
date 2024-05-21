package client;

import java.io.IOException;

import application.Main;
import demandereparationclient.ClientDemandesController;
import demandereparationclient.DemandeReparationController;
import gestionUtilisateurs.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import session.Session;

public class ClientController {
    @FXML
    private Label welcomeLabel;

    private Session session = Session.getInstance();
    
   
    public void initData(User user) {
        welcomeLabel.setText("Welcome " + user.getRole() + ": " + user.getNom());
    }

    @FXML
    private void demandeReparation() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/demandereparationclient/demandeReparation.fxml"));
            
            Parent root = loader.load();
            DemandeReparationController controller = loader.getController(); 
            User user = Session.getInstance().getUser();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Demande de Réparation");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }  
    
    @FXML
    private void consulterMesDemandes(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/demandereparationclient/clientDemandes.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Mes Demandes de Réparation");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    
    private void resizeWindow(double width, double height) {
        Stage stage = (Stage) welcomeLabel.getScene().getWindow();
        stage.setWidth(width);
        stage.setHeight(height);
    }

    private void adjustWindowSize() {
        double newWidth = 800; 
        double newHeight = 600; 
        resizeWindow(newWidth, newHeight);
    }

}
