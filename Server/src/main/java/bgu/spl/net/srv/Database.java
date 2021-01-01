package bgu.spl.net.srv;

import bgu.spl.net.srv.CourseInfo;
import bgu.spl.net.srv.UserInfo;
import sun.awt.image.ImageWatched;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
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
	private ConcurrentHashMap<Integer, CourseInfo> courseMap;
	private HashMap<String, Boolean> loggedInMap;
	private LinkedList<Integer> coursesOrder;


	private static class DatabaseHolder{
		private static Database instance = new Database();
	}

	//to prevent user from creating new Database
	private Database() {
		userMap=new HashMap<>();
		courseMap=new ConcurrentHashMap<>();
		loggedInMap=new HashMap<>();
	}

	/**
	 * Retrieves the single instance of this class.
	 */

	// TODO need to implement!!!
	public static Database getInstance() {
		return DatabaseHolder.instance;
	}
	
	/**
	 * loades the courses from the file path specified 
	 * into the Database, returns true if successful.
	 */
	public boolean initialize(String coursesFilePath) {
		// TODO: implement
		return false;
	}

	public boolean regAdmin(String userName, String password){
		if (!userMap.containsKey(userName))
		{
			userMap.put(userName,new UserInfo(password,true));
			loggedInMap.put(userName,false);
			return true;
		}
		return false;
	}

	public boolean studentReg(String userName, String password){
		if (!userMap.containsKey(userName))
		{
			userMap.put(userName,new UserInfo(password,false));
			loggedInMap.put(userName,false);
			return true;
		}
		return false;
	}

	public synchronized boolean  logIn(String userName, String password){
		if (!userMap.containsKey(userName)||!userMap.get(userName).getPassword().equals(password)||!loggedInMap.get(userName))
		{
			loggedInMap.put(userName,true);
			return true;
		}
		return false;
	}

	public boolean logout(String userName){
		if (loggedInMap.containsKey(userName))
		{
			loggedInMap.remove(userName);
			return true;
		}
		return false;
	}
	public boolean courseReg(String userName , int courseNum)
	{
		//user isn't registered/ user isn't logged in /user is an admin/ course dont exist
		if(!userMap.containsKey(userName)||!loggedInMap.containsKey(userName)||userMap.get(userName).isAdmin()||!courseMap.containsKey(courseNum))
		{
			return false;
		}
		else {
			 return courseMap.get(courseNum).registerStudent(userName);
		}
	}
	public LinkedList<Integer> kdamCheck (int courseNum,String userName) throws Exception {
		if (!courseMap.containsKey(courseNum)) {
			throw new NoSuchElementException();
		}
		if (!userMap.get(userName).isAdmin()||!loggedInMap.get(userName))
		{
			throw new IllegalArgumentException();
		}
		LinkedList<Integer> afterOrder=OrderCourses(courseMap.get(courseNum).getKdamCourses());
		return afterOrder;
	}



	public String getCourseStats(int courseNum,String userName) throws Exception
	{
		if (!courseMap.containsKey(courseNum))
		{
			throw new NoSuchElementException();
		}
		if (userMap.get(userName).isAdmin()||!loggedInMap.get(userName))
		{
			throw new IllegalArgumentException();
		}
		CourseInfo courseInfo=courseMap.get(courseNum);
		String output="Course:("+courseNum+") "+courseInfo.getCourseName()+"\n"
				+"Seats Available:"+courseInfo.getAvailableSeats()+"/"+courseInfo.getMaxNumOfSeats()+"\n"+
				"Students Registered:[";
		LinkedList<String> studentsInCourse=courseInfo.getRegisteredStudents();
		studentsInCourse=OrderAlphaBatic(studentsInCourse);
		for (String name:studentsInCourse)
		{
			output=output+name;
		}
		output=output+"]";
		return output;
	}




	public String getStudentStats(String userName,String studentName) throws Exception
	{
		if (userMap.get(userName).isAdmin()||!loggedInMap.get(userName))
		{
			throw new IllegalArgumentException();
		}
		if (!userMap.containsKey(studentName))
		{
			throw new NoSuchElementException();
		}
		UserInfo studentInfo=userMap.get(studentName);
		String output="Student: "+studentName+"\n"+"Courses:[";
		LinkedList<Integer> orederedList=OrderCourses(studentInfo.getCourses());
		for (Integer num:orederedList)
		{
			output=output+courseMap.get(num).getCourseName();
		}
		output=output+"]";
		return output;
	}
	public boolean isRegistered(String userName,int courseNum) throws Exception
	{
		if (!loggedInMap.get(userName)||!courseMap.containsKey(courseNum))
		{
			throw new IllegalArgumentException();
		}
		return userMap.get(userName).getCourses().contains(courseNum);
	}
	public boolean unregister(String userName,int courseNum)
	{
		try {
			if(isRegistered(userName, courseNum))
			{
				courseMap.get(courseNum).unregister(userName);
				userMap.get(userName).unregister(courseNum);
				return true;
			}
			else return false;
		}
		catch (Exception e)
		{
			return false;
		}
	}

	public LinkedList<Integer> getCourses(String userName) throws Exception
	{
		if (userMap.get(userName).isAdmin())
		{
			throw new IllegalArgumentException();
		}
		return OrderCourses(userMap.get(userName).getCourses());

	}
	public String getPassword(String userName)
	{
		return userMap.get(userName).getPassword();
	}
	private LinkedList<Integer> OrderCourses(LinkedList<Integer> courseList) {
		LinkedList<Integer> result=new LinkedList<>();
		for (Integer curr : coursesOrder){
			if (courseList.contains(curr))
			{
				result.addLast(curr);
			}
		}
		return result;
	}
	private LinkedList<String> OrderAlphaBatic(LinkedList<String> studentsInCourse) {
		studentsInCourse.sort(String::compareTo);
		return studentsInCourse;
	}



}
