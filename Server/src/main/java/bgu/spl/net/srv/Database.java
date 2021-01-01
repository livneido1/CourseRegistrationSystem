package bgu.spl.net.srv;

import bgu.spl.net.srv.CourseInfo;
import bgu.spl.net.srv.UserInfo;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import javax.xml.crypto.Data;
import java.util.HashMap;

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
	private HashMap<String, UserInfo> userMap;
	private ConcurrentHashMap<String, CourseInfo> courseMap;
	private HashMap<String, Boolean> loggedInMap;




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

	boolean regAdmin(String userName, String password){
		throw new NotImplementedException();
	}

	boolean studentReg(String userName, String password){
		throw new NotImplementedException();
	}

	synchronized boolean  logIn(String userName, String password){
		throw new NotImplementedException();
	}

	boolean logout(String userName){
		throw new NotImplementedException();
	}
	boolean courseReg(String userName , int CourseNum)
	{
		throw new NotImplementedException();
	}
	List<Integer> kdamCheck (int courseNum)
	{
		throw new NotImplementedException();
	}
	List<Integer> getCourseStats(int courseNum)
	{
		throw new NotImplementedException();
	}
	UserInfo getStudentStats(String userName,String studentName)
	{
		throw new NotImplementedException();
	}
	boolean isRegistered(String userName,int courseNum)
	{
		throw new NotImplementedException();
	}
	boolean unregister(String userName,int courseNum)
	{
		throw new NotImplementedException();
	}
	List<String> getCourses(String userName)
	{
		throw new NotImplementedException();
	}
	String getPassword(String userName)
	{
		throw new NotImplementedException();
	}



}
