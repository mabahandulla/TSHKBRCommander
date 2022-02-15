package enumsStore;

import java.util.Arrays;

/* Get array with table column names */

/**
 * The enum class for storing the names of the columns "tsh_kbr_info" table.
 * <p>
 * @author Aleksander Demichev
 * @version 1.0.0.0
 */

public enum InformationFields {

	URL_SEND_WORK("url_send_wrk"),
	URL_RECIVE_WORK("url_recive_wrk"),
	URL_SEND_TEST("url_send_tst"),
	URL_RECIVE_TEST("url_recive_tst"),
	MAIN_IP_WORK("main_ip_wrk"),
	BACKUP_IP_WORK("reserved_ip_wrk"),
	MAIN_IP_TEST("main_ip_tst"),
	BACKUP_IP_TEST("reserved_ip_tst"),
	URL_LK_WORK("url_lk_wrk"),
	URL_LK_TEST("url_lk_tst");

	public static final String TABLE_NAME = "tsh_kbr_info";

	public final String COLUMN_NAME;

	InformationFields(String name) {
		this.COLUMN_NAME = name;
	}

	/* Get table column name */
	public String toString() {
		return this.COLUMN_NAME;
	}

	/* Get an array of table column names */
	public static String[] names() {
		return Arrays.toString(values()).replaceAll("\\[|]", "").split(", ");
	}
}
