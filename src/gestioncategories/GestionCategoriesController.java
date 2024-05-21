package gestioncategories;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.sql.ResultSet;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GestionCategoriesController implements Initializable {
    @FXML
    private TextField libelleTextField;
    @FXML
    private TextField tarifTextField;
    @FXML
    private TableView<Categorie> tableView;
    @FXML
    private TableColumn<Categorie, Integer> idColumn;
    @FXML
    private TableColumn<Categorie, String> libelleColumn;
    @FXML
    private TableColumn<Categorie, Double> tarifColumn;

    private ObservableList<Categorie> categoriesList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        libelleColumn.setCellValueFactory(new PropertyValueFactory<>("libelle"));
        tarifColumn.setCellValueFactory(new PropertyValueFactory<>("tarif"));

        listerCategories();
    }

    @FXML
    private void ajouterCategorie() {
        String libelle = libelleTextField.getText();
        double tarif = Double.parseDouble(tarifTextField.getText());
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/atelierreparation", "root", "")) {
            String sql = "INSERT INTO categories (libelle, tarif) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, libelle);
            statement.setDouble(2, tarif);
            statement.executeUpdate();

            categoriesList.add(new Categorie(libelle, tarif));

            libelleTextField.clear();
            tarifTextField.clear();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void listerCategories() {
        categoriesList.clear();

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/atelierreparation", "root", "")) {
            String sql = "SELECT * FROM categories";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String libelle = resultSet.getString("libelle");
                double tarif = resultSet.getDouble("tarif");
                Categorie categorie = new Categorie(id, libelle, tarif);
                categoriesList.add(categorie);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        tableView.setItems(categoriesList);
    }

    @FXML
    private void afficherFormulaireAjout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gestioncategories/ajoutCategorie.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Ajouter une Catégorie");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            listerCategories(); // Rafraîchir la liste des catégories après l'ajout
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void afficherFormulaireModification() {
        Categorie selectedCategorie = tableView.getSelectionModel().getSelectedItem();
        if (selectedCategorie != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/gestioncategories/ajoutCategorie.fxml"));
                Parent root = loader.load();
                
                AjoutCategorieController controller = loader.getController();
                controller.setCategorie(selectedCategorie);
                
                Stage stage = new Stage();
                stage.setTitle("Modifier une Catégorie");
                stage.setScene(new Scene(root));
                stage.showAndWait();
                listerCategories(); // Rafraîchir la liste des catégories après la modification
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void supprimerCategorie() {
        Categorie selectedCategorie = tableView.getSelectionModel().getSelectedItem();
        if (selectedCategorie != null) {
            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/atelierreparation", "root", "")) {
                // Supprimer les appareils appartenant à la catégorie
                String deleteAppareilsSQL = "DELETE FROM appareils WHERE categorie_id = ?";
                PreparedStatement deleteAppareilsStatement = connection.prepareStatement(deleteAppareilsSQL);
                deleteAppareilsStatement.setInt(1, selectedCategorie.getId());
                deleteAppareilsStatement.executeUpdate();

                // Supprimer la catégorie
                String deleteCategorieSQL = "DELETE FROM categories WHERE id = ?";
                PreparedStatement deleteCategorieStatement = connection.prepareStatement(deleteCategorieSQL);
                deleteCategorieStatement.setInt(1, selectedCategorie.getId());
                deleteCategorieStatement.executeUpdate();

                listerCategories(); // Actualiser la liste des catégories après suppression
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void quitter() {
        Stage stage = (Stage) tableView.getScene().getWindow();
        stage.close();
    }

}
