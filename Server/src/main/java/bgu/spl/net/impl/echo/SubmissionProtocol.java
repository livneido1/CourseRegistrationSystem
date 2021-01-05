package bgu.spl.net.impl.echo;

import bgu.spl.net.api.MessagingProtocol;
import bgu.spl.net.srv.Database;


import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

public class SubmissionProtocol implements MessagingProtocol<String> {
    String name;
    boolean shouldTerminate;
    int clientNumber;

  /*  public SubmissionProtocol(){
        this.name=null;
    }*/


    @Override
    public String process(String msg) {
        LinkedList<String> splitted = splitMSG(msg);
        String opCode =  splitted.getFirst();
        // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>ADMINREG<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<,
        if (opCode.equals("ADMINREG")){
            if (splitted.size() <3) // should contain opcode, Username, password
                return errorMSG(opCode);
            String userName =splitted.get(1);
            String password = splitted.get(2);
            if (name==null) // the user has not been logged in yet
            {
                Database database = Database.getInstance();
                boolean success = database.regAdmin(userName,password);
                if (success){
                    return successMSG(opCode);
                }
                else
                    return errorMSG(opCode);
            }
            else
                return errorMSG(opCode);
        }

        // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>StudentReg<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<,
        if (opCode.equals("STUDENTREG")){
            if (splitted.size() <3) // should contain opcode, Username, password
                return errorMSG(opCode);
            String userName =splitted.get(1);
            String password = splitted.get(2);
            if (name==null) // the user has not been logged in yet
            {
                Database database = Database.getInstance();
                boolean success = database.studentReg(userName,password);
                if (success){
                    return successMSG(opCode);
                }
                else
                    return errorMSG(opCode);
            }
            else
                return errorMSG(opCode);
        }

        // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>LOGIN<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<,
        if (opCode.equals("LOGIN")){
            if (splitted.size() <3) // should contain opcode, Username, password
                return errorMSG(opCode);
            String userName =splitted.get(1);
            String password = splitted.get(2);
            if (name==null) // the user has not been logged in yet
            {
                Database database = Database.getInstance();
                int success = database.logIn(userName,password);
                if (success!=-1){
                    name = userName; // updates the protocol to hold the right user
                    clientNumber=success;
                    return successMSG(opCode);
                }
                else
                    return errorMSG(opCode);
            }
            else
                return errorMSG(opCode);
        }
        // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Logout<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<,
        if (opCode.equals("LOGOUT")){
            if (splitted.size() != 1) // should only contain opcode
                return errorMSG(opCode);

            if (name==null) // the user has not been logged in yet
            {
               return errorMSG(opCode);
            }
            else{
                Database database = Database.getInstance();
                boolean success = database.logout(name);
                if (success) {

                    name = null; // updates so it won't be able to work till logging in back
                    shouldTerminate =true;
                    return successMSG(opCode);
                }
                else
                    return errorMSG(opCode);

            }
        }

        // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>COURSEREG <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<,
        if (opCode.equals("COURSEREG")){
            if (splitted.size() != 2) // should only contain opcode
                return errorMSG(opCode);
            if (name==null) // the user has not been logged in yet
            {
                return errorMSG(opCode);
            }
            else{
                String numTemp = splitted.get(1);
                int courseNum;
                try {
                    courseNum =  Integer.parseInt(numTemp);
                } catch (NumberFormatException e) {
                    return errorMSG(opCode);
                }
                Database database = Database.getInstance();
                boolean success = database.courseReg(name,courseNum);
                if (success)
                    return successMSG(opCode);
                else
                    return errorMSG(opCode);

            }
        }

        // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>KDAMCHECK<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        if (opCode.equals("KDAMCHECK")){
            if (splitted.size() != 2) // should only contain opcode and course num
                return errorMSG(opCode);
            if (name==null) // the user has not been logged in yet
            {
                return errorMSG(opCode);
            }

            else{
                String numTemp = splitted.get(1);
                int courseNum;
                try {
                    courseNum =  Integer.parseInt(numTemp);
                } catch (NumberFormatException e) {
                    return errorMSG(opCode);
                }
                Database database = Database.getInstance();
                try {
                    List<Integer> kdamCourses = database.kdamCheck(courseNum, name);
                    String output =  "[";
                    for (Integer curr: kdamCourses) {
                        output =  output +  ", "+ curr ;
                    }
                    output = "]";


                    output = successMSG((opCode)) + "\n" + output;
                    return output;

                } catch (NoSuchElementException | IllegalArgumentException e){
                    errorMSG(opCode);
                }


            }
        }
        // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>COURSESTAT<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        if (opCode.equals("COURSESTAT")){
            if (splitted.size() != 2) // should only contain opcode and course num
                return errorMSG(opCode);
            if (name==null) // the user has not been logged in yet
            {
                return errorMSG(opCode);
            }
            else{
                String numTemp = splitted.get(1);
                int courseNum;
                try {
                    courseNum =  Integer.parseInt(numTemp);
                } catch (NumberFormatException e) {
                    return errorMSG(opCode);
                }
                Database database = Database.getInstance();
                try {
                    String output = successMSG((opCode)) +"\n";
                    output =output + database.getCourseStats(courseNum, name);

                    return output;
                } catch (NoSuchElementException | IllegalArgumentException e){
                    errorMSG(opCode);
                }
            }
        }

        // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>STUDENTSTAT<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        if (opCode.equals("STUDENTSTAT")){
            if (splitted.size() != 2) // should only contain opcode and student name
                return errorMSG(opCode);
            if (name==null) // the user has not been logged in yet
            {
                return errorMSG(opCode);
            }
            else{
                String studentName = splitted.get(1);

                Database database = Database.getInstance();
                try {
                    String output = successMSG(opCode) + "\n";
                    output = output + database.getStudentStats(name, studentName);
                    return output;
                } catch (NoSuchElementException | IllegalArgumentException e){
                    errorMSG(opCode);
                }
            }
        }

        // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>ISREGISTERED<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        if (opCode.equals("ISREGISTERED")){
            if (splitted.size() != 2) // should only contain opcode and course num
                return errorMSG(opCode);
            if (name==null) // the user has not been logged in yet
            {
                return errorMSG(opCode);
            }
            else{
                String numTemp = splitted.get(1);
                int courseNum;
                try {
                    courseNum =  Integer.parseInt(numTemp);
                    Database database = Database.getInstance();
                    boolean success = database.isRegistered(name, courseNum );
                    if (success) {
                        String output = successMSG(opCode) + "\n";
                        return  output + "REGISTERED";
                    }
                    else
                        return "NOT REGISTERED";
                } catch (NoSuchElementException | IllegalArgumentException e){
                    errorMSG(opCode);
                }
            }
        }

        // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>UNREGISTER<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        if (opCode.equals("UNREGISTER")){
            if (splitted.size() != 2) // should only contain opcode and course num
                return errorMSG(opCode);
            if (name==null) // the user has not been logged in yet
            {
                return errorMSG(opCode);
            }
            else{
                String numTemp = splitted.get(1);
                int courseNum;

                courseNum =  Integer.parseInt(numTemp);
                Database database = Database.getInstance();
                boolean success = database.unregister(name, courseNum );
                if (success)
                    return successMSG(opCode);
                else
                    return errorMSG(opCode);

            }
        }

        // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>MYCOURSES<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        if (opCode.equals("MYCOURSES")){
            if (splitted.size() != 1) // should only contain opcode
                return errorMSG(opCode);
            if (name==null) // the user has not been logged in yet
            {
                return errorMSG(opCode);
            }
            else{
                Database database =  Database.getInstance();
                String output =  successMSG(opCode) + "\n";
                output =output + "[";
                try {
                    List<Integer> courseList = database.getCourses(name);

                    for (Integer curr : courseList){
                        output = output+  ", " + curr;
                    }
                     output = output + "]";
                     return output;
                }catch (IllegalArgumentException e){
                    return errorMSG(opCode);
                }
            }
        }

        return errorMSG(opCode);




    }




    @Override
    public boolean shouldTerminate() {
        return  shouldTerminate;


    }

    /**
     *
     * @param msg -  the arrived message from the user
     * @return returns a linked list, splliting the arrived message where " " (space) is found
     */
    private LinkedList<String> splitMSG(String msg){
        LinkedList<String > output = new LinkedList<String >();
        String temp = new String(msg);
        while (temp.contains(" ")){
            int index = temp.indexOf(" ");
            String cut =  temp.substring(0,index);
            if (temp.length()>index)
                temp =  temp.substring((index+1));
            else
                temp = "";
            output.add(cut);
        }
        output.add(temp);
      return output;
    }

    private String errorMSG(String opcode){
        return "ERR " + opcode;
    }

    private String successMSG(String opCode){
        return "ACK " + opCode;
    }
}
