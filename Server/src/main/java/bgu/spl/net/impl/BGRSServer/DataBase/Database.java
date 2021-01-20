package bgu.spl.net.impl.BGRSServer.DataBase;


import java.io.*;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
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
    private ConcurrentHashMap<String, UserInfo> userMap;
    private ConcurrentHashMap<Integer, CourseInfo> courseMap;
    private HashMap <String, Boolean> loggedInMap;
    private LinkedList<Integer> coursesOrder;
    private int clientCount;


    private static class DatabaseHolder{
        private static Database instance = new Database();
    }

    //to prevent user from creating new Database
    private Database() {
        userMap=new ConcurrentHashMap<>();
        courseMap=new ConcurrentHashMap<>();
        loggedInMap=new HashMap<>();
        coursesOrder =  new LinkedList<>();
        initialize(System.getProperty("user.dir"));
    }

    /**
     * Retrieves the single instance of this class.
     */

    public static Database getInstance() {
        return DatabaseHolder.instance;
    }

    /**
     * loades the courses from the file path specified
     * into the Database, returns true if successful.
     */
    public boolean initialize(String coursesFilePath) {
        try {
            coursesFilePath=coursesFilePath+ "/Courses.txt";
            File file=new File(coursesFilePath);
            Scanner scan=new Scanner(file);
            String s;
            while (scan.hasNextLine()){
                s=scan.nextLine();
                String[]afterSplit=new String[4];
                for (int i=0;i<3;i++)
                {
                    int index=s.indexOf("|");
                    String s1=s.substring(0,index);
                    s=s.substring(index+1);
                    afterSplit[i]=s1;
                }
                afterSplit[3]=s;
                LinkedList<Integer> kdam=new LinkedList<>();
                coursesOrder.addLast(Integer.parseInt(afterSplit[0]));
                if (afterSplit[2].length()>2) {
                    afterSplit[2] = afterSplit[2].substring(1, afterSplit[2].length() - 1);
                    String[] kdamCourses=afterSplit[2].split(",");
                    for (int i = 0; i < kdamCourses.length; i++) {
                        kdam.addLast(Integer.parseInt(kdamCourses[i]));
                    }
                }
                CourseInfo courseInfo=new CourseInfo
                        (Integer.parseInt(afterSplit[0]),afterSplit[1],Integer.parseInt(afterSplit[3]),kdam);
                courseMap.put(Integer.parseInt(afterSplit[0]),courseInfo);

            }


        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public synchronized int logIn(String userName, String password){
        if (userMap.containsKey(userName)&&userMap.get(userName).getPassword().equals(password)&&!loggedInMap.get(userName))
        {
            loggedInMap.put(userName,true);
            clientCount++;
            return clientCount;
        }
        return -1;
    }

    public boolean logout(String userName){
        if (loggedInMap.get(userName))
        {
            loggedInMap.put(userName,false);
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
        else if (!studentDidKdam(courseMap.get(courseNum).getKdamCourses(),userMap.get(userName).getCourses()))
        {
            return false;
        }
        else {

            UserInfo currentUser = userMap.get(userName);
            CourseInfo course = courseMap.get(courseNum);
            LinkedList<Integer> courses = currentUser.getCourses();
            if (!(courses.contains(courseNum))) {
                String courseName = course.getCourseName();
                boolean success = course.registerStudent(userName);
                if (success) {
                    currentUser.registerToCourse(courseNum, courseName);
                    return true;
                }
            }


            return false;
        }
    }




    public LinkedList<Integer> kdamCheck (int courseNum,String userName) {
        if (!courseMap.containsKey(courseNum)) {
            throw new NoSuchElementException();
        }
        if (userMap.get(userName).isAdmin()||!loggedInMap.get(userName))
        {
            throw new IllegalArgumentException();
        }
        LinkedList<Integer> afterOrder=OrderCourses(courseMap.get(courseNum).getKdamCourses());
        return afterOrder;
    }



    public String getCourseStats(int courseNum,String userName)
    {
        if (!courseMap.containsKey(courseNum))
        {
            throw new NoSuchElementException();
        }
        if (!userMap.get(userName).isAdmin()||!loggedInMap.get(userName))
        {
            throw new IllegalArgumentException();
        }
        CourseInfo courseInfo=courseMap.get(courseNum);
        String output="Course: ("+courseNum+") "+courseInfo.getCourseName()+"\n"
                +"Seats Available: "+courseInfo.getAvailableSeats()+"/"+courseInfo.getMaxNumOfSeats()+"\n"+
                "Students Registered: [";
        LinkedList<String> studentsInCourse=courseInfo.getRegisteredStudents();
        studentsInCourse=OrderAlphaBatic(studentsInCourse);
        for (String name:studentsInCourse)
        {
            output=output+name+", ";
        }
        if (!studentsInCourse.isEmpty())
        {output=output.substring(0,output.length()-2)+"]";}
        else{ output=output+"]";}
        return output;
    }




    public String getStudentStats(String userName,String studentName)
    {
        if (!userMap.get(userName).isAdmin()||!loggedInMap.get(userName) )
        {
            throw new IllegalArgumentException();
        }
        if (!userMap.containsKey(studentName)|| userMap.get(studentName).isAdmin())
        {
            throw new NoSuchElementException();
        }
        UserInfo studentInfo=userMap.get(studentName);
        String output="Student: "+studentName+"\n"+"Courses: [";
        LinkedList<Integer> orderedList=OrderCourses(studentInfo.getCourses());
        for (Integer num:orderedList)
        {
            output=output+courseMap.get(num).getCourseName()+", ";
        }
        if (!orderedList.isEmpty())
            output=output.substring(0,output.length()-2)+"]";
        else
            output=output+"]";
        return output;
    }
    public boolean isRegistered(String userName,int courseNum)
    {
        if (!loggedInMap.get(userName)||userMap.get(userName).isAdmin())
        {
            throw new IllegalArgumentException();
        }
        if (!courseMap.containsKey(courseNum))
        {
            throw new NoSuchElementException();
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

    public LinkedList<Integer> getCourses(String userName)
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
    private boolean studentDidKdam(LinkedList<Integer> kdamCourses, LinkedList<Integer> courses) {
        for (Integer num:kdamCourses)
        {
            if (!courses.contains(num))
            {
                return false;
            }
        }
        return true;
    }

}