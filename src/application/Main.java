package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import technicien.TechnicienController;
import gestionUtilisateurs.User;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        showLoginView(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void showLoginView(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/authentification/login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showTechnicienView(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/technicien/technicienView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Technician Dashboard");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showRegistrationView(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(Main.class.getResource("/authentification/registration.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Repair Workshop Registration");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
