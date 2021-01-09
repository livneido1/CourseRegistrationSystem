package bgu.spl.net.impl.Attachments;

import bgu.spl.net.api.MessagingProtocol;
import bgu.spl.net.Database.Database;


import java.util.*;

public class SubmissionProtocol implements MessagingProtocol<String> {
    String name;
    boolean shouldTerminate;
    int clientNumber;
    HashMap<String,String> opcodes ;



    @Override
    public String process(String msg){
        if (opcodes==null)
            initOpCodes();
        if (msg.length()>1) {
            LinkedList<String> splitted = splitMSG(msg);
            String opcode =  splitted.getFirst();
            if (opcodes.containsKey(opcode)){
                Database database =  Database.getInstance();

                // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>ADMINREG<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<,
                if (opcode.equals("ADMINREG")){
                    if (splitted.size() <3) // should contain opcode, Username, password
                        return errorMSG(opcodes.get(opcode));
                    String userName =splitted.get(1);
                    String password = splitted.get(2);
                    if (name==null) // the user has not been logged in yet
                    {
                        boolean success = database.regAdmin(userName,password);
                        if (success){
                            return successMSG(opcodes.get(opcode));
                        }
                        else
                            return errorMSG(opcodes.get(opcode));
                    }
                    else
                        return errorMSG(opcodes.get(opcode));
                }

                // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>StudentReg<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<,
                if (opcode.equals("STUDENTREG")){
                    if (splitted.size() <3) // should contain opcode, Username, password
                        return errorMSG(opcodes.get(opcode));
                    String userName =splitted.get(1);
                    String password = splitted.get(2);
                    if (name==null) // the user has not been logged in yet
                    {
                        boolean success = database.studentReg(userName,password);
                        if (success){
                            return successMSG(opcodes.get(opcode));
                        }
                        else
                            return errorMSG(opcodes.get(opcode));
                    }
                    else
                        return errorMSG(opcodes.get(opcode));
                }

                // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>LOGIN<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<,
                if (opcode.equals("LOGIN")){
                    if (splitted.size() <3) // should contain opcode, Username, password
                        return errorMSG(opcodes.get(opcode));
                    String userName =splitted.get(1);
                    String password = splitted.get(2);
                    if (name==null) // the user has not been logged in yet
                    {
                        int success = database.logIn(userName,password);
                        if (success!=-1){
                            name = userName; // updates the protocol to hold the right user
                            clientNumber=success;
                            return successMSG(opcodes.get(opcode));
                        }
                        else
                            return errorMSG(opcodes.get(opcode));
                    }
                    else
                        return errorMSG(opcodes.get(opcode));
                }
                // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Logout<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<,
                if (opcode.equals("LOGOUT")){
                    if (splitted.size() != 1) // should only contain opcode
                        return errorMSG(opcodes.get(opcode));

                    if (name==null) // the user has not been logged in yet
                    {
                        return errorMSG(opcodes.get(opcode));
                    }
                    else{
                        boolean success = database.logout(name);
                        if (success) {

                            name = null; // updates so it won't be able to work till logging in back
                            shouldTerminate =true;
                            return successMSG(opcodes.get(opcode));
                        }
                        else
                            return errorMSG(opcodes.get(opcode));

                    }
                }

                // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>COURSEREG <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<,
                if (opcode.equals("COURSEREG")){
                    if (splitted.size() != 2) // should only contain opcode
                        return errorMSG(opcodes.get(opcode));
                    if (name==null) // the user has not been logged in yet
                    {
                        return errorMSG(opcodes.get(opcode));
                    }
                    else{
                        String numTemp = splitted.get(1);
                        int courseNum;
                        try {
                            courseNum =  Integer.parseInt(numTemp);
                        } catch (NumberFormatException e) {
                            return errorMSG(opcodes.get(opcode));
                        }
                        boolean success = database.courseReg(name,courseNum);
                        if (success)
                            return successMSG(opcodes.get(opcode));
                        else
                            return errorMSG(opcodes.get(opcode));

                    }
                }

                // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>KDAMCHECK<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                if (opcode.equals("KDAMCHECK")){
                    if (splitted.size() != 2) // should only contain opcode and course num
                        return errorMSG(opcodes.get(opcode));
                    if (name==null) // the user has not been logged in yet
                    {
                        return errorMSG(opcodes.get(opcode));
                    }

                    else{
                        String numTemp = splitted.get(1);
                        int courseNum;
                        try {
                            courseNum =  Integer.parseInt(numTemp);
                        } catch (NumberFormatException e) {
                            return errorMSG(opcode);
                        }
                        try {
                            List<Integer> kdamCourses = database.kdamCheck(courseNum, name);
                            String output ="";
                            for (Integer curr: kdamCourses) {
                                output = output + "," + curr;
                            }

                            if (output.length()>1)
                                output= output.substring(1);
                            output = "[" + output;
                            output += "]";


                            output = successMSG(opcodes.get(opcode)) + "\n" + output;
                            return output;

                        } catch (NoSuchElementException | IllegalArgumentException e){
                            return errorMSG(opcodes.get(opcode));
                        }


                    }
                }
                // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>COURSESTAT<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                if (opcode.equals("COURSESTAT")){
                    if (splitted.size() != 2) // should only contain opcode and course num
                        return errorMSG(opcodes.get(opcode));
                    if (name==null) // the user has not been logged in yet
                    {
                        return errorMSG(opcodes.get(opcode));
                    }
                    else{
                        String numTemp = splitted.get(1);
                        int courseNum;
                        try {
                            courseNum =  Integer.parseInt(numTemp);
                        } catch (NumberFormatException e) {
                            return errorMSG(opcodes.get(opcode));
                        }
                        try {
                            String output = successMSG(opcodes.get(opcode)) +"\n";
                            output =output + database.getCourseStats(courseNum, name);

                            return output;
                        } catch (NoSuchElementException | IllegalArgumentException e){
                            return errorMSG(opcodes.get(opcode));
                        }
                    }
                }

                // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>STUDENTSTAT<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                if (opcode.equals("STUDENTSTAT")){
                    if (splitted.size() != 2) // should only contain opcode and student name
                        return errorMSG(opcodes.get(opcode));
                    if (name==null) // the user has not been logged in yet
                    {
                        return errorMSG(opcodes.get(opcode));
                    }
                    else{
                        String studentName = splitted.get(1);

                        try {
                            String output = successMSG(opcodes.get(opcode)) + "\n";
                            output = output + database.getStudentStats(name, studentName);
                            return output;
                        } catch (NoSuchElementException | IllegalArgumentException e){
                            return errorMSG(opcodes.get(opcode));
                        }
                    }
                }

                // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>ISREGISTERED<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                if (opcode.equals("ISREGISTERED")){
                    if (splitted.size() != 2) // should only contain opcode and course num
                        return errorMSG(opcodes.get(opcode));
                    if (name==null) // the user has not been logged in yet
                    {
                        return errorMSG(opcodes.get(opcode));
                    }
                    else{
                        String numTemp = splitted.get(1);
                        int courseNum;
                        try {
                            courseNum =  Integer.parseInt(numTemp);
                            boolean success = database.isRegistered(name, courseNum );
                            if (success) {
                                String output = successMSG(opcodes.get(opcode)) + "\n";
                                return  output + "REGISTERED";
                            }
                            else
                                return successMSG(opcode) +"\n" + "NOT REGISTERED";
                        } catch (NoSuchElementException | IllegalArgumentException e){
                            return errorMSG(opcodes.get(opcode));
                        }
                    }
                }

                // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>UNREGISTER<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                if (opcode.equals("UNREGISTER")){
                    if (splitted.size() != 2) // should only contain opcode and course num
                        return errorMSG(opcodes.get(opcode));
                    if (name==null) // the user has not been logged in yet
                    {
                        return errorMSG(opcodes.get(opcode));
                    }
                    else{
                        String numTemp = splitted.get(1);
                        int courseNum;

                        courseNum =  Integer.parseInt(numTemp);
                        boolean success = database.unregister(name, courseNum );
                        if (success)
                            return successMSG(opcodes.get(opcode));
                        else
                            return errorMSG(opcodes.get(opcode));

                    }
                }

                // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>MYCOURSES<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                if (opcode.equals("MYCOURSES")){
                    if (splitted.size() != 1) // should only contain opcode
                        return errorMSG(opcodes.get(opcode));
                    if (name==null) // the user has not been logged in yet
                    {
                        return errorMSG(opcodes.get(opcode));
                    }
                    else{
                        String output = "";
                        try {
                            List<Integer> courseList = database.getCourses(name);

                            for (Integer curr : courseList){
                                output = output+ "," + curr;
                            }
                            if (output.length()>1)
                                output= output.substring(1);
                            output = "[" + output;
                            output += "]";
                            return successMSG(opcodes.get(opcode))+ "\n" + output;
                        }catch (IllegalArgumentException e){
                            return errorMSG(opcodes.get(opcode));
                        }



                    }
                }



            }


        }
        else
            return errorMSG(msg);

        return errorMSG(msg);



    }



    private void initOpCodes() {
        opcodes =  new HashMap<>();
        opcodes.put("ADMINREG", "01");
        opcodes.put("STUDENTREG", "02");
        opcodes.put("LOGIN", "03");
        opcodes.put("LOGOUT", "04");
        opcodes.put("COURSEREG", "05");
        opcodes.put("KDAMCHECK", "06");
        opcodes.put("COURSESTAT", "07");
        opcodes.put("STUDENTSTAT", "08");
        opcodes.put("ISREGISTERED", "09");
        opcodes.put("UNREGISTER", "0a");
        opcodes.put("MYCOURSES", "0b");
        opcodes.put("ACK", "0c");
        opcodes.put("ERR", "0d");
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
        String[] temp = msg.split(" ");
        return new LinkedList<>(Arrays.asList(temp));



    }

    private String errorMSG(String opcode){
        return "ERROR " + opcode;
    }

    private String successMSG(String opCode){
        return "ACK " + opCode;
    }
}
