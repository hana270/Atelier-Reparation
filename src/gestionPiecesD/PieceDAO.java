package gestionPiecesD;

import connexion.Connexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PieceDAO {

    public List<Piece> obtenirToutesPieces() {
        List<Piece> pieces = new ArrayList<>();
        Connection connexion = Connexion.obtenirConnexion();
        try {
            String sql = "SELECT * FROM pieces";
            Statement statement = connexion.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String description = resultSet.getString("description");
                double prixHT = resultSet.getDouble("prixHT");
                pieces.add(new Piece(id, description, prixHT));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Connexion.fermerConnexion(connexion);
        }
        return pieces;
    }

    public void ajouterPiece(Piece piece) {
        Connection connexion = Connexion.obtenirConnexion();
        try {
            String sql = "INSERT INTO pieces (description, prixHT) VALUES (?, ?)";
            PreparedStatement preparedStatement = connexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, piece.getDescription());
            preparedStatement.setDouble(2, piece.getPrixHT());
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                piece.setId(generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Connexion.fermerConnexion(connexion);
        }
    }

    public void modifierPiece(Piece piece) {
        Connection connexion = Connexion.obtenirConnexion();
        try {
            String sql = "UPDATE pieces SET description = ?, prixHT = ? WHERE id = ?";
            PreparedStatement preparedStatement = connexion.prepareStatement(sql);
            preparedStatement.setString(1, piece.getDescription());
            preparedStatement.setDouble(2, piece.getPrixHT());
            preparedStatement.setInt(3, piece.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Connexion.fermerConnexion(connexion);
        }
    }

    public void supprimerPiece(int id) {
        Connection connexion = Connexion.obtenirConnexion();
        try {
            String sql = "DELETE FROM pieces WHERE id = ?";
            PreparedStatement preparedStatement = connexion.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Connexion.fermerConnexion(connexion);
        }
    }
}