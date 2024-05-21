package connexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connexion {
	private static final String URL = "jdbc:mysql://localhost:3306/atelierreparation";
	    
	    
	    public static Connection obtenirConnexion() {
	    	Connection connection = null;
	        try {
	            Class.forName("com.mysql.cj.jdbc.Driver");
	            connection = DriverManager.getConnection( "jdbc:mysql://localhost:3306/atelierreparation","root","");
	            System.out.println("Connexion établie avec succès !");
	        } catch (ClassNotFoundException e) {
	            System.err.println("Erreur de chargement du driver MySQL : " + e.getMessage());
	        } catch (SQLException e) {
	            System.err.println("Erreur lors de la connexion à la base de données : " + e.getMessage());
	        }
	        return connection;
	    }
	
	    public static void fermerConnexion(Connection connexion) {
	        if (connexion != null) {
	            try {
	                connexion.close();
	                System.out.println("Connexion fermée avec succès !");
	            } catch (SQLException e) {
	                System.err.println("Erreur lors de la fermeture de la connexion : " + e.getMessage());
	            }
	        }
	    }
}



