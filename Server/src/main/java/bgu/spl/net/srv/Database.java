import javax.xml.crypto.Data;

/**
 * Passive object representing the Database where all courses and users are stored.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add private fields and methods to this class as you see fit.
 */
public class Database {
	// TODO need to implement the Singelton implementation
	private final static Database instace=null;



	//to prevent user from creating new Database
	private Database() {
		// TODO: implement
	}

	/**
	 * Retrieves the single instance of this class.
	 */

	// TODO need to implement!!!
	public static Database getInstance() {
		return instace;
	}
	
	/**
	 * loades the courses from the file path specified 
	 * into the Database, returns true if successful.
	 */
	boolean initialize(String coursesFilePath) {
		// TODO: implement
		return false;
	}


}
