package enumsStore;

/**
 * The enum class for storing the names of image files that are located in the resource directory.
 * <p>
 * @author Aleksander Demichev
 * @version 1.0.0.0
 */

public enum ImagesStore {
	
	LOGO ("matrix_smart_folder_32x32.ico"),
	BUTTON_KEY ("key_button.ico"),
	CB_LOGO ("cb_eagle.ico"),
	ERROR_ICON ("error_icon.ico");

	private String imageIcon;

    private ImagesStore(String imageIcon) {
        this.imageIcon = imageIcon;
    }
    
	public String getImageIcon() {
		return this.imageIcon;
	}
}
