package gestionsPiecesC;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import gestionPiecesD.Piece;

public class PieceChangeeDAO {
    private Connection connection;

// Correction de la requête SQL pour la création de la table pieces_changees
    private static final String CREATE_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS pieces_changees (id INT PRIMARY KEY AUTO_INCREMENT, ordre_id INT, piece_id INT, quantite INT, description VARCHAR(255))";

    public PieceChangeeDAO(Connection connection) {
        this.connection = connection;
        try {
            Statement statement = connection.createStatement();
            statement.execute(CREATE_TABLE_QUERY);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addPieceChangee(PieceChangee pieceChangee) throws SQLException {
        String query = "INSERT INTO pieces_changees (ordre_id, piece_id, quantite, description) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, pieceChangee.getOrdreId());
            statement.setInt(2, pieceChangee.getPieceId());
            statement.setInt(3, pieceChangee.getQuantite());
            statement.setString(4, pieceChangee.getDescription());

            System.out.println("Executing query: " + statement); // Ajoutez cette ligne pour le débogage
            statement.executeUpdate();
        }
    }

    public void updatePieceChangee(PieceChangee pieceChangee) throws SQLException {
        String query = "UPDATE pieces_changees SET piece_id = ?, quantite = ?, description = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, pieceChangee.getPieceId());
            statement.setInt(2, pieceChangee.getQuantite());
            statement.setString(3, pieceChangee.getDescription());
            statement.setInt(4, pieceChangee.getId());
            statement.executeUpdate();
        }
    }

    public void deletePieceChangee(int id) throws SQLException {
        String query = "DELETE FROM pieces_changees WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    public List<PieceChangee> getPiecesChangeesByOrdreId(int ordreId) throws SQLException {
        List<PieceChangee> piecesChangees = new ArrayList<>();
        String query = "SELECT pc.*, p.description, p.prixHT FROM pieces_changees pc INNER JOIN Pieces p ON pc.piece_id = p.id WHERE ordre_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, ordreId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Piece piece = new Piece(resultSet.getInt("piece_id"), resultSet.getString("description"), resultSet.getDouble("prixHT"));
                PieceChangee pieceChangee = new PieceChangee(resultSet.getInt("id"), 
                        resultSet.getInt("ordre_id"), 
                        resultSet.getInt("piece_id"), 
                        resultSet.getInt("quantite"), 
                        null, // Ou mettez un objet Piece approprié ici
                        resultSet.getString("description"));
piecesChangees.add(pieceChangee);
            }
        }
        return piecesChangees;
    }

    public List<Piece> getAllPieces() throws SQLException {
        List<Piece> pieces = new ArrayList<>();
        String query = "SELECT * FROM Pieces";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                pieces.add(new Piece(resultSet.getInt("id"), resultSet.getString("description"), resultSet.getDouble("prixHT")));
            }
        }
        return pieces;
    }
    public List<Piece> getAlPieces() throws SQLException {
        List<Piece> pieces = new ArrayList<>();
        String query = "SELECT * FROM Pieces";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                pieces.add(new Piece(resultSet.getInt("id"), resultSet.getString("description"), resultSet.getDouble("prixHT")));
            }
        }
        return pieces;
        }

}
