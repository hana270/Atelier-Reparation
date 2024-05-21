package OrderReparation;

import gestionAppareils.Appareil;
public class Order {
	private int id;
    private Appareil appareil;
    private String problemDescription;
    private double nbHoursOfLabor;

    public Order(Appareil appareil, String problemDescription, double nbHoursOfLabor) {
        this.appareil = appareil;
        this.problemDescription = problemDescription;
        this.nbHoursOfLabor = nbHoursOfLabor;
    }
    
    public Order(int id,Appareil appareil, String problemDescription, double nbHoursOfLabor) {
       this.id=id;
    	this.appareil = appareil;
        this.problemDescription = problemDescription;
        this.nbHoursOfLabor = nbHoursOfLabor;
    }
    public int getId() {
		return id;
	}
	public Appareil getAppareil() {
		return appareil;
	}

	public String getProblemDescription() {
		return problemDescription;
	}

	public double getNbHoursOfLabor() {
		return nbHoursOfLabor;
	}

	public void setAppareil(Appareil appareil) {
		this.appareil = appareil;
	}

	public void setProblemDescription(String problemDescription) {
		this.problemDescription = problemDescription;
	}

	public void setNbHoursOfLabor(double nbHoursOfLabor) {
		this.nbHoursOfLabor = nbHoursOfLabor;
	}



}
