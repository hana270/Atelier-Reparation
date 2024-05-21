package OrderReparation;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import session.Session;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import gestionAppareils.Appareil;


public class AddOrderController {

    private OrdresReparationController ordresReparationController;

    @FXML
    private ComboBox<String> appareilComboBox;
    @FXML
    private TextField problemDescriptionField;

    @FXML
    private Spinner<Integer> nbHoursOfLaborSpinner;
    private Set<String> appareilsDejaAjoutes = new HashSet<>(); // Déclaration de l'ensemble

    
 private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/atelierreparation", "root", "");
    }
    
public void setOrdresReparationController(OrdresReparationController controller) {
        this.ordresReparationController = controller;
    }

    @FXML
    private void initialize() {
        populateAppareilComboBox();
    }

    private void populateAppareilComboBox() {
        appareilComboBox.getItems().clear();
        try (Connection connection = getConnection()) {
        	String query = "SELECT appareils.id, appareils.description " +
                    "FROM appareils " +
                    "JOIN demandes_reparation ON appareils.id = demandes_reparation.appareil_id " +
                    "WHERE appareils.id NOT IN (SELECT appareil_id FROM orders)";

        	PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            ObservableList<String> appareilsDisponibles = FXCollections.observableArrayList();
            while (resultSet.next()) {
                appareilsDisponibles.add(resultSet.getString("description"));
            }
            appareilComboBox.setItems(appareilsDisponibles);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    private boolean isAppareilInOrders(String appareilDescription) {
        if (ordresReparationController != null) {
            ObservableList<Order> orders = ordresReparationController.getOrdresTable().getItems();
            for (Order order : orders) {
                if (order.getAppareil().getDescription().equals(appareilDescription)) {
                    return true;}}
        }
        return false; }
    
    @FXML
    private void handleAddOrder() {
        String appareilDescription = appareilComboBox.getValue();
        String problemDescription = problemDescriptionField.getText();
        int nbHoursOfLabor = nbHoursOfLaborSpinner.getValue();

        if (appareilDescription != null && !appareilDescription.isEmpty() && problemDescription != null && !problemDescription.isEmpty()) {
            try {
                int appareilId = getAppareilId(appareilDescription);
                Appareil appareil = new Appareil(appareilId, appareilDescription, "", 0);
                Order newOrder = new Order(appareil, problemDescription, nbHoursOfLabor);
                OrderDAO orderDAO = new OrderDAO(getConnection());
                orderDAO.addOrder(newOrder);
                ordresReparationController.refreshTable();
                Stage stage = (Stage) appareilComboBox.getScene().getWindow();
                stage.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Veuillez remplir tous les champs.");
        }
    }


    private int getAppareilId(String description) throws SQLException {
        int appareilId = -1;
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/atelierreparation", "root", "");
        String query = "SELECT id FROM appareils WHERE description = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, description);
        ResultSet resultSet = statement.executeQuery();
        
        if (resultSet.next()) {
            appareilId = resultSet.getInt("id");
        }
        
        return appareilId;
    }
}
