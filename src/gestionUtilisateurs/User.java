package gestionUtilisateurs;

public class User {
    private int id;
    private String nom;
    private String adresse;
    private String telephone;
    private String email;
    private String password;
    private String role; // New field for the role

    public User(int id, String nom, String adresse, String telephone, String email, String password, String role) {
    	this.id=id;
    	this.nom = nom;
        this.adresse = adresse;
        this.telephone = telephone;
        this.email = email;
        this.password = password;
        this.role = role; // Assigning role in the constructor
    }

    // Getters and setters for the new role field
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // Existing getters and setters for other fields
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
