package facturation;

import java.util.List;

public class Facture {
	private int id;
    private String appareil;
    private String description;
    private double nbHoursOfLabor;
    private double total;

    public Facture(int id, String appareil, String description, double nbHoursOfLabor, double total) {
        this.id = id;
        this.appareil = appareil;
        this.description = description;
        this.nbHoursOfLabor = nbHoursOfLabor;
        this.total = total;
    }

    private List<String> piecesChangees;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAppareil() {
		return appareil;
	}

	public void setAppareil(String appareil) {
		this.appareil = appareil;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getNbHoursOfLabor() {
		return nbHoursOfLabor;
	}

	public void setNbHoursOfLabor(double nbHoursOfLabor) {
		this.nbHoursOfLabor = nbHoursOfLabor;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public List<String> getPiecesChangees() {
		return piecesChangees;
	}

	public void setPiecesChangees(List<String> piecesChangees) {
		this.piecesChangees = piecesChangees;
	}

    // Getters and setters for all fields

    // Client information
   
}
