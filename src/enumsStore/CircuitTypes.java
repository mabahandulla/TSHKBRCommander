package enumsStore;

/**
 * The enum class for storing switches for choosing the type of circuit.
 * <p>
 * @author Aleksander Demichev
 * @version 1.0.0.0
 */

public enum CircuitTypes {
	
	PROM("промышленный контур"),
	TEST("тестовый контур");
	
	public String circuitType;
	
	CircuitTypes (String name) {
		this.circuitType = name;
	}
    
	public String getUpperCaseName() {
		return this.circuitType.toUpperCase();
	}
	
	public String getLowerCaseName() {
		return	circuitType.substring(0,1).toUpperCase() + circuitType.substring(1).toLowerCase();
	}
}
