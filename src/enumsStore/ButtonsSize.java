package enumsStore;

/**
 * The enum class for storing the width and height of the buttons of the whole application.
 * <p>
 * @author Aleksander Demichev
 * @version 1.0.0.0
 */

public enum ButtonsSize {
	
	BUTTON_WIDTH(75),
	BUTTON_HEIGHT(25);

	public final int SIZE;

	ButtonsSize(int size) {
		this.SIZE = size;
	}

	public int size() {
		return SIZE;
	}
}
