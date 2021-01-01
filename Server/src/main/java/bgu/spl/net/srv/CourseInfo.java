package bgu.spl.net.srv;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
public class CourseInfo {
    private int courseNumber;
    private String courseName;
    private int maxNumOfSeats;
    private int availableSeats;
    private List<String> registeredStudents;

    public boolean registerStudent(String studentName){
        throw new NotImplementedException();
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

    public List<String> getRegisteredStudents() {
        return registeredStudents;
    }''
}
