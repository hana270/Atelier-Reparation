package OrderReparation;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import gestionAppareils.Appareil;
import gestionsPiecesC.GestionPiecesChangeesController;

public class OrdresReparationController {

	@FXML
	private TableColumn<Order, String> idColumn;
	@FXML
	private TableColumn<Order, String> appareilColumn;
	@FXML
	private TableColumn<Order, String> descriptionColumn;
	@FXML
	private TableColumn<Order, String> hoursColumn;

	@FXML
	private TableView<Order> ordresTable;
    private OrderDAO orderDAO;

    @FXML
    public void initialize() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/atelierreparation", "root", "");
            if (connection != null) {
                System.out.println("Connected to the database!");
            }
            orderDAO = new OrderDAO(connection);
            populateOrdresTable(); // Assurez-vous que la méthode est appelée lors de l'initialisation du contrôleur
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void populateOrdresTable() {
        ordresTable.getItems().clear();

        try {
            ResultSet resultSet = orderDAO.getAllOrders();
            if (resultSet == null) {
                System.out.println("Failed to retrieve data from the database.");
                return;
            }

            ObservableList<Order> orders = FXCollections.observableArrayList();
            while (resultSet.next()) {
                Appareil appareil = new Appareil(
                        resultSet.getInt("appareil_id"),
                        resultSet.getString("appareil_description"),
                        resultSet.getString("appareil_marque"),
                        resultSet.getInt("appareil_categorieId")
                );
                Order order = new Order(
                        resultSet.getInt("id"),
                        appareil,
                        resultSet.getString("problemDescription"),
                        resultSet.getDouble("nbHoursOfLabor")
                );
                orders.add(order);
            }

            orders.sort((o1, o2) -> Double.compare(o1.getNbHoursOfLabor(), o2.getNbHoursOfLabor()));
            ordresTable.getItems().addAll(orders);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        idColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getId())));
        appareilColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAppareil().getDescription()));
        descriptionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProblemDescription()));
        hoursColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getNbHoursOfLabor())));
    }
    
    @FXML
    private void addOrderAction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddOrder.fxml"));
            Parent root = loader.load();
            AddOrderController controller = loader.getController();
            controller.setOrdresReparationController(this);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void updateOrderAction() {
        Order selectedOrder = ordresTable.getSelectionModel().getSelectedItem();
        if (selectedOrder != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdateOrder.fxml"));
                Parent root = loader.load();
                UpdateOrderController controller = loader.getController();
                controller.setSelectedOrder(selectedOrder);
                controller.setOrdresReparationController(this);
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public OrderDAO getOrderDAO() {
        return orderDAO;
    }
    
    @FXML
    private void deleteOrderAction() {
        Order selectedOrder = ordresTable.getSelectionModel().getSelectedItem();
        if (selectedOrder != null) {
            orderDAO.deleteOrder(selectedOrder.getId());
            populateOrdresTable();
        }
    }

    public TableView<Order> getOrdresTable() {
        return ordresTable;
    }
    @FXML
    public void refreshTable() {
        populateOrdresTable();
    }
    
    
    @FXML
    private void handleManagePiecesChangees() {
        Order selectedOrdre = ordresTable.getSelectionModel().getSelectedItem();
        if (selectedOrdre != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/gestionsPiecesC/GestionPiecesChangees.fxml"));
                Stage stage = new Stage();
                stage.setTitle("Gérer Pièces Changées");
Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/atelierreparation", "root", "");
                GestionPiecesChangeesController controller = new GestionPiecesChangeesController(connection);
                
                loader.setController(controller);
                
                Scene scene = new Scene(loader.load());
                stage.setScene(scene);

                controller.setOrdreId(selectedOrdre.getId());

                stage.showAndWait();
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        }
    }
}