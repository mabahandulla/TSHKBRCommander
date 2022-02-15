package enumsStore;

/**
 * The enum class for storing the titles of the message box displayed to the user.
 * <p>
 * @author Aleksander Demichev
 * @version 1.0.0.0
 */

public enum MessageTypes {

	QUESTION ("ВОПРОС"),
	ERROR_QUESTION ("ОШИБКА"),
	WARNING_QUESTION ("ВНИМАНИЕ"),
	INFORMATION ("ОК"),
	ERROR ("ОШИБКА"),
	WARNING ("ВНИМАНИЕ");
	
	private String messageTitle;

    private MessageTypes(String messageTitle) {
        this.messageTitle = messageTitle;
    }
    
	public String getTitle() {
		return this.messageTitle;
	}
	
}
