package facturation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FactureDAO {
    private Connection connection;

    public FactureDAO(Connection connection) {
        this.connection = connection;
    }

    public Facture getFactureByOrderId(int orderId) throws SQLException {
        String query = "SELECT orders.id, appareils.description AS appareil, orders.problemDescription AS description, orders.nbHoursOfLabor, SUM(pieces.prixHT * pieces_a_changer.quantite) AS total " +
                       "FROM orders " +
                       "JOIN appareils ON orders.appareil_id = appareils.id " +
                       "JOIN pieces_a_changer ON orders.id = pieces_a_changer.ordre_id " +
                       "JOIN pieces ON pieces_a_changer.piece_id = pieces.id " +
                       "WHERE orders.id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, orderId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String appareil = resultSet.getString("appareil");
                    String description = resultSet.getString("description");
                    double nbHoursOfLabor = resultSet.getDouble("nbHoursOfLabor");
                    double total = resultSet.getDouble("total");
                    
                    return new Facture(id, appareil, description, nbHoursOfLabor, total);
                }
            }
        }
        return null;
    }

    /*
    private void fetchClientAndAppareilInfo(Facture facture) throws SQLException {
        String clientSql = "SELECT * FROM clients WHERE id = ?";
        try (PreparedStatement clientStatement = connection.prepareStatement(clientSql)) {
            clientStatement.setInt(1, facture.getClientId());
            try (ResultSet clientResultSet = clientStatement.executeQuery()) {
                if (clientResultSet.next()) {
                    facture.setClientName(clientResultSet.getString("nom"));
                    facture.setClientAddress(clientResultSet.getString("adresse"));
                    facture.setClientPhone(clientResultSet.getString("telephone"));
                }
            }
        }

        String appareilSql = "SELECT * FROM appareils WHERE id = ?";
        try (PreparedStatement appareilStatement = connection.prepareStatement(appareilSql)) {
            appareilStatement.setInt(1, facture.getAppareilId());
            try (ResultSet appareilResultSet = appareilStatement.executeQuery()) {
                if (appareilResultSet.next()) {
                    facture.setAppareilDescription(appareilResultSet.getString("description"));
                    facture.setAppareilMarque(appareilResultSet.getString("marque"));
                    int categorieId = appareilResultSet.getInt("categorie_id");

                    String categorieSql = "SELECT * FROM categories WHERE id = ?";
                    try (PreparedStatement categorieStatement = connection.prepareStatement(categorieSql)) {
                        categorieStatement.setInt(1, categorieId);
                        try (ResultSet categorieResultSet = categorieStatement.executeQuery()) {
                            if (categorieResultSet.next()) {
                                facture.setCategorieLibelle(categorieResultSet.getString("libelle"));
                                facture.setCategorieTarif(categorieResultSet.getDouble("tarif"));
                            }
                        }
                    }
                }
            }
        }
    }*/

    private List<String> getPiecesChangees(int orderId) throws SQLException {
        List<String> pieces = new ArrayList<>();
        String sql = "SELECT * FROM pieces_a_changer WHERE ordre_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, orderId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    pieces.add(resultSet.getString("piece"));
                }
            }
        }
        return pieces;
    }
    
    public double getPriceForPiece(int pieceId) throws SQLException {
        double price = 0.0;
        String sql = "SELECT prixHT FROM pieces WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, pieceId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    price = resultSet.getDouble("prixHT");
                }
            }
        }
        return price;
    }
}
