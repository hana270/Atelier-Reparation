package gestionAppareils;


public class Appareil {
    private int id;
    private String description;
    private String marque;
    private int categorieId;
 private String categorie; 

    public Appareil(int id, String description, String marque, String categorie) {
        this.id = id;
        this.description = description;
        this.marque = marque;
        this.categorie = categorie;
    }
    
public Appareil(int id, String description, String marque, int categorieId) {
		this.id = id;
		this.description = description;
		this.marque = marque;
	
		this.categorieId = categorieId;
	}
	public Appareil( String description, String marque, int categorieId) {
		this.description = description;
		this.marque = marque;
		this.categorieId = categorieId;
	}	
	

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }
	
	
	public int getId() {
		return id;
	}
	public String getDescription() {
		return description;
	}
	public String getMarque() {
		return marque;
	}
	
	public int getCategorieId() {
		return categorieId;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setMarque(String marque) {
		this.marque = marque;
	}
	
	public void setCategorieId(int categorieId) {
		this.categorieId = categorieId;
	}
	

    
    
}
