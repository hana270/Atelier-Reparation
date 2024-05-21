package OrderReparation;

import java.sql.*;
import gestionAppareils.Appareil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class UpdateOrderController {
    @FXML
    private TextField idField;
    @FXML
    private ComboBox<Appareil> appareilComboBox; 
    @FXML
    private TextField descriptionField;
    @FXML
    private Spinner<Integer> nbHoursOfLaborSpinner; 

    private Order selectedOrder;
    private OrdresReparationController ordresReparationController;

    private static ObservableList<Appareil> appareilList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Remplir la liste déroulante avec les appareils disponibles
        populateAppareilComboBox();
        appareilComboBox.setConverter(new StringConverter<Appareil>() {
            @Override
            public String toString(Appareil appareil) {
                return appareil.getDescription();
            }

            @Override
            public Appareil fromString(String string) {
                return null;
            }
        });
    }

    public void setSelectedOrder(Order selectedOrder) {
        this.selectedOrder = selectedOrder;
        if (selectedOrder != null) {
            idField.setText(String.valueOf(selectedOrder.getId()));
            descriptionField.setText(selectedOrder.getProblemDescription());
            nbHoursOfLaborSpinner.getValueFactory().setValue((int) selectedOrder.getNbHoursOfLabor());
            appareilComboBox.setValue(selectedOrder.getAppareil()); // Mettre à jour la sélection de l'appareil
        }
    }

    public void setOrdresReparationController(OrdresReparationController controller) {
        this.ordresReparationController = controller;
    }

    @FXML
    private void handleUpdateOrder() {
        if (selectedOrder != null) {
            Appareil selectedAppareil = appareilComboBox.getValue(); // Récupérer l'appareil sélectionné

            if (selectedAppareil != null) {
                selectedOrder.setAppareil(selectedAppareil);
            }
            selectedOrder.setProblemDescription(descriptionField.getText());
            selectedOrder.setNbHoursOfLabor(Double.parseDouble(nbHoursOfLaborSpinner.getValue().toString()));

            ordresReparationController.getOrderDAO().updateOrder(selectedOrder);
            Stage stage = (Stage) idField.getScene().getWindow();
            stage.close();
            ordresReparationController.populateOrdresTable();
        }
    }

    private void populateAppareilComboBox() {
        // Vider la liste déroulante des appareils
        appareilComboBox.getItems().clear(); 
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/atelierreparation", "root", "");
            String query = "SELECT appareils.id, appareils.description, appareils.marque, appareils.categorie_id " +
                    "FROM appareils " +
                    "JOIN demandes_reparation ON appareils.id = demandes_reparation.appareil_id " +
                    "WHERE appareils.id NOT IN (SELECT appareil_id FROM orders)";

   
            PreparedStatement statement = connection.prepareStatement(query);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Appareil appareil = new Appareil(
                    resultSet.getInt("id"),
                    resultSet.getString("description"),
                    resultSet.getString("marque"),
                    resultSet.getInt("categorie_id")
                );
                appareilComboBox.getItems().add(appareil);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void setAppareilList(ObservableList<Appareil> list) {
        appareilList = list;
    }
}
