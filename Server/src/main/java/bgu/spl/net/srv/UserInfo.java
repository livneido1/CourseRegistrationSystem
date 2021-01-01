package bgu.spl.net.srv;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.HashMap;
import java.util.List;

public class UserInfo {
    private HashMap<Integer,String> myCourses;
    private String password;
    private  boolean isAdmin;

    public void registerToCourse(int courseNum, String courseName)
    {
        throw new NotImplementedException();
    }

    public String getPassword() {
        return password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }
    public List<Integer> getCourses()
    {
        throw  new NotImplementedException();
    }
}
