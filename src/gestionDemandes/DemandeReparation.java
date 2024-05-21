package gestionDemandes;

import java.sql.Timestamp;

public class DemandeReparation {
    private int id;
    private int clientId;
    private String statut;
    private Timestamp dateDemande;
    private String description;
    private int appareilId;
    private String appareilDescription;
    private String clientNom;
    private String categorie; 
    public DemandeReparation(int id, int clientId, String statut, Timestamp dateDemande, String description, int appareilId, String appareilDescription, String clientNom) {
        this.id = id;
        this.clientId = clientId;
        this.statut = statut;
        this.dateDemande = dateDemande;
        this.description = description;
        this.appareilId = appareilId;
        this.appareilDescription = appareilDescription;
        this.clientNom = clientNom;
    }
    public DemandeReparation(int id, int clientId, String statut, Timestamp dateDemande, String description, int appareilId) {
        this.id = id;
        this.clientId = clientId;
        this.statut = statut;
        this.dateDemande = dateDemande;
        this.description = description;
        this.appareilId = appareilId;
       
    } 


    public DemandeReparation(int int1, int int2, int int3, String string, Timestamp timestamp, String string2) {
    	this.id = int1; this.appareilId = int2;
        this.clientId = int3;
        this.statut = string;
        this.dateDemande = timestamp;
        this.description = string2;
       
	}
    public DemandeReparation(int id, int clientId, String statut, Timestamp dateDemande, String description, int appareilId, String categorie) {
        this.id = id;
        this.clientId = clientId;
        this.statut = statut;
        this.dateDemande = dateDemande;
        this.description = description;
        this.appareilId = appareilId;
        this.categorie = categorie; // Initialisation de la catégorie
    }
 // Ajout de la méthode getCategorie() pour récupérer la catégorie
    public String getCategorie() {
        return categorie;
    }

    // Ajout de la méthode setCategorie() pour définir la catégorie
    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Timestamp getDateDemande() {
        return dateDemande;
    }

    public void setDateDemande(Timestamp dateDemande) {
        this.dateDemande = dateDemande;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAppareilId() {
        return appareilId;
    }

    public void setAppareilId(int appareilId) {
        this.appareilId = appareilId;
    }

    public String getAppareilDescription() {
        return appareilDescription;
    }

    public void setAppareilDescription(String appareilDescription) {
        this.appareilDescription = appareilDescription;
    }

    public String getClientNom() {
        return clientNom;
    }

    public void setClientNom(String clientNom) {
        this.clientNom = clientNom;
    }
	
}
