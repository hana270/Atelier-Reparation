package gestionAppareils;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AjouterAppareilController {

    @FXML
    private TextField descriptionField;
    @FXML
    private TextField marqueField;
    @FXML
    private ComboBox<String> categorieComboBox;

    private Appareil appareil;

    @FXML
    private void initialize() {
        loadCategories();
    }

    private void loadCategories() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/AtelierReparation", "root", "")) {
            String query = "SELECT libelle FROM categories";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                categorieComboBox.getItems().add(resultSet.getString("libelle"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setAppareil(Appareil appareil) {
        this.appareil = appareil;
        descriptionField.setText(appareil.getDescription());
        marqueField.setText(appareil.getMarque());
        categorieComboBox.setValue(getCategorieLibelle(appareil.getCategorieId()));
    }

    private String getCategorieLibelle(int categorieId) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/atelierreparation", "root", "")) {
            String query = "SELECT libelle FROM categories WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, categorieId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("libelle");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @FXML
    private void ajouterAppareil() {
        String description = descriptionField.getText();
        String marque = marqueField.getText();
        String categorie = categorieComboBox.getValue();

        if (description.isEmpty() || marque.isEmpty() || categorie == null) {
           return;
        }
        int categorieId = getCategorieId(categorie);
        if (categorieId == -1) {
            return;
        }
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/atelierreparation", "root", "")) {
            String query;
            PreparedStatement statement;

            if (appareil == null) {  // Ajout d'un nouvel appareil
                query = "INSERT INTO appareils (description, marque, categorie_id) VALUES (?, ?, ?)";
                statement = connection.prepareStatement(query);
                statement.setString(1, description);
                statement.setString(2, marque);
                statement.setInt(3, categorieId);
            } else {  
 // Modification d'un appareil existant
                query = "UPDATE appareils SET description = ?, marque = ?, categorie_id = ? WHERE id = ?";
                statement = connection.prepareStatement(query);
                statement.setString(1, description);
                statement.setString(2, marque);
                statement.setInt(3, categorieId);
                statement.setInt(4, appareil.getId());
            }

            statement.executeUpdate();
            Stage stage = (Stage) descriptionField.getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getCategorieId(String libelle) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/atelierreparation", "root", "")) {
            String query = "SELECT id FROM categories WHERE libelle = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, libelle);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
