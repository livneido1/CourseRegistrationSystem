package bgu.spl.net.Database;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class UserInfo {
    private HashMap<Integer,String> myCourses;//<course Number,CourseName>
    private String password;
    private  boolean isAdmin;

    public UserInfo(String password,boolean isAdmin)//Called in register (Both for student and admin)
    {
        this.password=password;
        this.isAdmin=isAdmin;
        this.myCourses=new HashMap<>();
    }

    public void registerToCourse(int courseNum, String courseName)
    {
        myCourses.put(courseNum,courseName);
    }

    public String getPassword() {
        return password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public HashMap<Integer, String> getMyCourses() {
        return myCourses;
    }

    public LinkedList<Integer> getCourses()
    {
        LinkedList<Integer> courses=new LinkedList<>();
        myCourses.forEach((Integer,String)->courses.add(Integer));
        return courses;
    }
    public void unregister(Integer courseNum)
    {
        myCourses.remove(courseNum);
    }
}
