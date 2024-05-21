module AtelierReparation {
	requires javafx.controls;
    requires javafx.fxml;

    requires java.sql;
    requires javafx.graphics;
	requires javafx.base;

    
    opens application to javafx.graphics, javafx.fxml;
    
    exports authentification to javafx.fxml;
    exports gestionAppareils;
    exports gestioncategories;
    exports client; 
    exports gestionDemandes; 
    exports demandereparationclient; 
    exports gestionPiecesD; 
    exports OrderReparation; 
    exports facturation; 

    
    opens authentification to javafx.fxml;
    opens technicien to javafx.fxml;
    opens client to javafx.fxml;
    opens gestionUtilisateurs to javafx.base;
    opens gestionAppareils to javafx.fxml;
    opens gestioncategories to javafx.fxml;
    opens demandereparationclient to javafx.fxml; 
    opens gestionDemandes to javafx.fxml;
    opens gestionPiecesD to javafx.fxml;
    opens OrderReparation to javafx.fxml;

    opens gestionsPiecesC to javafx.fxml;
    exports gestionsPiecesC;
    
    opens facturation to javafx.fxml;
   }
