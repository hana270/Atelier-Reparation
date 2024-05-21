package gestioncategories;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AjoutCategorieController {
    @FXML
    private TextField libelleField;
    @FXML
    private TextField tarifField;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton; 

    private Categorie categorie;

    @FXML
    private void handleSave() {
        String libelle = libelleField.getText();
        double tarif = Double.parseDouble(tarifField.getText());

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/atelierreparation", "root", "")) {
            String sql;
            PreparedStatement statement;
            if (categorie == null) {
                sql = "INSERT INTO categories (libelle, tarif) VALUES (?, ?)";
                statement = connection.prepareStatement(sql);
                statement.setString(1, libelle);
                statement.setDouble(2, tarif);
            } else {
                sql = "UPDATE categories SET libelle = ?, tarif = ? WHERE id = ?";
                statement = connection.prepareStatement(sql);
                statement.setString(1, libelle);
                statement.setDouble(2, tarif);
                statement.setInt(3, categorie.getId());
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
        if (categorie != null) {
            libelleField.setText(categorie.getLibelle());
            tarifField.setText(String.valueOf(categorie.getTarif()));
        }
    }
   
    @FXML
    private void retourListeCategories() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("listeCategories.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
