package enumsStore;


/**
 * This enum class contains switches to select the mode of the running application.
 * <p>
 * @author Aleksander Demichev
 * @version 1.0.0.0
 */

public enum ApplicationMode {
	
	COMMANDER("ТШ КБР Commander"),
	NOTIFIER("ТШ КБР Notifier");

	public final String APP_TYPE;
	
	ApplicationMode (String name) {
		this.APP_TYPE = name;
	}
    
	public String getName() {
		return this.APP_TYPE;
	}
}
