package client;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Button;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import gestionUtilisateurs.User;

import java.io.IOException;
import java.util.List;

public class ListeClientsController {

    @FXML
    private TextField searchField;
    @FXML
    private TableView<User> clientTableView;
    @FXML
    private TableColumn<User, String> nomColumn;
    @FXML
    private TableColumn<User, String> adresseColumn;

    @FXML
    private TableColumn<User, String> telephoneColumn;

    @FXML
    private TableColumn<User, String> emailColumn;

    @FXML
    private Button retourButton;

    public void setClients(List<User> clients) {
        ObservableList<User> clientData = clientTableView.getItems();
        clientData.clear();
        clientData.addAll(clients);
        
    }

    public void initialize() {
       nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        adresseColumn.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        telephoneColumn.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterClients(newValue);
        });
    }

    private void filterClients(String keyword) {
        ObservableList<User> clients = clientTableView.getItems();
        FilteredList<User> filteredData = new FilteredList<>(clients, p -> true);
        filteredData.setPredicate(client -> {
            if (keyword == null || keyword.isEmpty()) {
                return true;
            }
            String lowerCaseFilter = keyword.toLowerCase();
            if (client.getNom().toLowerCase().contains(lowerCaseFilter) ||
                client.getAdresse().toLowerCase().contains(lowerCaseFilter) ||
                client.getTelephone().toLowerCase().contains(lowerCaseFilter) ||
                client.getEmail().toLowerCase().contains(lowerCaseFilter)) {
                return true;}
            return false;    });

        SortedList<User> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(clientTableView.comparatorProperty());
        clientTableView.setItems(sortedData);
    }

    @FXML
    private void handleRetour() {
        Stage currentStage = (Stage) retourButton.getScene().getWindow();
        currentStage.close();
    }

}
