package OrderReparation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import gestionAppareils.Appareil;

public class OrderDAO {
    private Connection connection;
    
    public OrderDAO(Connection connection) {
        this.connection = connection;
    }
    public void deletePiecesByOrderId(int orderId) {
        String query = "DELETE FROM pieces_changees WHERE ordre_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, orderId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

 public void addOrder(Order order) {
        try {
            String query = "INSERT INTO Orders (appareil_id, problemDescription, nbHoursOfLabor) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, order.getAppareil().getId());
            statement.setString(2, order.getProblemDescription());
            statement.setDouble(3, order.getNbHoursOfLabor());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public ResultSet getAllOrders() {
        ResultSet resultSet = null;
        try {
            String query = "SELECT o.id, o.problemDescription, o.nbHoursOfLabor, " +
                           "a.id as appareil_id, a.description as appareil_description, " +
                           "a.marque as appareil_marque, a.categorie_id as appareil_categorieId " +
                           "FROM Orders o " +
                           "JOIN Appareils a ON o.appareil_id = a.id";
            PreparedStatement statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            if (resultSet != null) {
                System.out.println("Query executed successfully!");
            }
        } catch (SQLException e) {
            System.err.println("Error executing query: " + e.getMessage());
        }
        return resultSet;
    }

     public void updateOrder(Order order) {
        try {
            String query = "UPDATE Orders SET appareil_id = ?, problemDescription = ?, nbHoursOfLabor = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, order.getAppareil().getId());
            statement.setString(2, order.getProblemDescription());
            statement.setDouble(3, order.getNbHoursOfLabor());
            statement.setInt(4, order.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteOrder(int orderId) {
        deletePiecesByOrderId(orderId); // Supprimer d'abord les pièces changées
        String query = "DELETE FROM orders WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, orderId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Appareil getAppareilById(int id) {
        Appareil appareil = null;
        try {
            String query = "SELECT * FROM Appareil WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                appareil = add(new Appareil(
                        resultSet.getInt("id"),
                        resultSet.getString("description"),
                        resultSet.getString("marque"),
                        resultSet.getInt("categorie_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appareil;
    }
    public Appareil add(Appareil appareil) {
        try {
            String query = "INSERT INTO Appareil (description, autre_colonne, autre_colonne) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, appareil.getDescription());
            statement.setString(2, appareil.getMarque());
            statement.setDouble(3, appareil.getCategorieId());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating appareil failed, no rows affected.");
            }
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    appareil.setId((int) generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating appareil failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appareil;
    }


}
