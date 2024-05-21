package facturation;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import OrderReparation.Order;
import OrderReparation.OrderDAO;

import gestionAppareils.Appareil;

public class FacturationController {
	@FXML
    private Label idLabel;
    @FXML
    private Label appareilLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Label nbHoursLabel;
    @FXML
    private Label totalLabel;

    private Connection connection;
    private FactureDAO factureDAO;

    public FacturationController() {
        // Constructeur par défaut
    }

    public FacturationController(Connection connection) {
        this.connection = connection;
        this.factureDAO = new FactureDAO(connection);
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
        this.factureDAO = new FactureDAO(connection);
    }

    public void showFacture(int orderId) {
        try {
            Facture facture = factureDAO.getFactureByOrderId(orderId);
            if (facture != null) {
                idLabel.setText(String.valueOf(facture.getId()));
                appareilLabel.setText(facture.getAppareil());
                descriptionLabel.setText(facture.getDescription());
                nbHoursLabel.setText(String.valueOf(facture.getNbHoursOfLabor()));
                totalLabel.setText(String.valueOf(facture.getTotal()));
            } else {
                // Gérer si l'ID de commande n'existe pas
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer l'exception SQL
        }
    }
}