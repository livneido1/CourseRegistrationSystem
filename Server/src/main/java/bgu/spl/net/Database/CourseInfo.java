package bgu.spl.net.Database;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
public class CourseInfo {
    private int courseNumber;
    private String courseName;
    private int maxNumOfSeats;
    private int availableSeats;
    private LinkedList<String> registeredStudents;
    private LinkedList<Integer> kdamCourses;

    public CourseInfo(int courseNumber,String courseName,int maxNumOfSeats,LinkedList<Integer> kdamCourses){
        this.courseNumber=courseNumber;
        this.courseName=courseName;
        this.maxNumOfSeats=maxNumOfSeats;
        this.availableSeats=maxNumOfSeats;
        this.registeredStudents=new LinkedList<>();
        this.kdamCourses=kdamCourses;
    }

    public boolean registerStudent(String studentName){
        if(registeredStudents.contains(studentName)||availableSeats==0)
        {
            return false;
        }
        else {
            registeredStudents.add(studentName);
            availableSeats--;
            return true;
        }

    }

    public int getCourseNumber() {
        return courseNumber;
    }

    public String getCourseName() {
        return courseName;
    }

    public int getMaxNumOfSeats() {
        return maxNumOfSeats;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public LinkedList<String> getRegisteredStudents() {
        return registeredStudents;
    }

    public LinkedList<Integer> getKdamCourses() {
        return kdamCourses;
    }
    public void unregister(String studentName)
    {
        registeredStudents.remove(studentName);
        availableSeats++;
    }
}
