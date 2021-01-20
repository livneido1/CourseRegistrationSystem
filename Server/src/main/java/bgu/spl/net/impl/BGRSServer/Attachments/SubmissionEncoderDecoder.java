package bgu.spl.net.impl.BGRSServer.Attachments;



import bgu.spl.net.api.MessageEncoderDecoder;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;


public class SubmissionEncoderDecoder implements MessageEncoderDecoder<String> {
    private byte[] bytes = new byte[1 << 10];
    private int len = 0;
    private int sizeToSend=0;
    private int currentSize;
    private String currMsg="";
    private HashMap<String, String> opcodes;
    private HashMap<String,Integer> msgLen;

    @Override
    public String decodeNextByte(byte nextByte) {

        if (nextByte == '\0') {
            if (len != 0) {
                currMsg += popString();
                currentSize++;
                if (currentSize == sizeToSend) {
                    String toSend = currMsg;
                    currMsg = "";
                    currentSize = 0;
                    sizeToSend = 0;
                    return toSend;
                }
            } else
                return null;
        } else
            addByte(nextByte);


        return null;


    }



    @Override
    public byte[] encode(String message) {
        byte[] output=new byte[1 << 10];
        output[0]='0';
        if (message.startsWith("ACK"))
        {
            output[1]='c';
            message=message.substring(4);
            String opCode;

            opCode = message.substring(0,2);
            message = message.substring(2);
            byte[] temp= opCode.getBytes();
            for (int i =0; i< temp.length;i++){
                output[2+i] = temp[i];
            }


            byte[] everythingElse=message.getBytes();
            if (everythingElse.length>output.length-5)
            {
                output=Arrays.copyOf(output,everythingElse.length+10);
            }
            else {
                for(int i=0;i<everythingElse.length;i++)
                {
                    output[5+i]=everythingElse[i];
                }
            }
            return output;
        }
        else {
            output[1]='d';
            message=message.substring(6);
            String opCode;
            opCode = message.substring(0,2);
            message = message.substring(2);
            byte[] temp= opCode.getBytes();
            for (int i =0; i< temp.length;i++){
                output[2+i] = temp[i];
            }
            byte[] everythingElse=message.getBytes();
            if (everythingElse.length>output.length-5)
            {
                output=Arrays.copyOf(output,everythingElse.length+10);
            }
            else {
                for(int i=0;i<everythingElse.length;i++)
                {
                    output[5+i]=everythingElse[i];
                }
            }
            return output;
        }
    }



    private void addByte(byte nextByte) {
        if (len>=bytes.length)
        {
            Arrays.copyOf(bytes,2*len);
        }
        bytes[len++]=nextByte;
    }

    private String popString() {
        if (currentSize == 0) {
            if (opcodes == null)
                initOpCodes();
            if (msgLen == null)
                initMsgLen();
            String result = new String(bytes, 0, len, StandardCharsets.UTF_8);
            String opCode = result.substring(0, 2);
            opCode = translateOpCodeToName(opCode);
            sizeToSend = msgLen.get(opCode);
            result = result.substring(2);

            String output = opCode + " ";

            output += result;
            len = 0;
            return output.toString();
        }
        else{

            String res =  " " + new String(bytes,0,len,StandardCharsets.UTF_8);
            len =0;
            return  res;


        }

    }

    private String translateOpCodeToName(String bytesString) {

        if (bytesString.equals("01"))
        {
            return "ADMINREG";
        }
        else if (bytesString.equals("02"))
        {
            return "STUDENTREG";
        }
        else if (bytesString.equals("03"))
        {
            return "LOGIN";
        }
        if (bytesString.equals("04"))
        {
            return "LOGOUT";
        }
        else if (bytesString.equals("05"))
        {
            return "COURSEREG";
        }
        else if (bytesString.equals("06"))
        {
            return "KDAMCHECK";
        }
        else if (bytesString.equals("07"))
        {
            return "COURSESTAT";
        }
        else if (bytesString.equals("08"))
        {
            return "STUDENTSTAT";
        }
        else if (bytesString.equals("09"))
        {
            return "ISREGISTERED";
        }
        else if (bytesString.equals("0a"))
        {
            return "UNREGISTER";
        }
        if (bytesString.equals("0b"))
        {
            return "MYCOURSES";
        }
        else if (bytesString.equals("0c"))
        {
            return "ACK";
        }
        else if (bytesString.equals("0d"))
        {
            return "ERR";
        }
        else return bytesString;
    }
    private byte[] fillBytes(String s) {
        byte[] output=new byte[2];
        output[0]=0;
        if (s.equals("ADMINREG"))
        {
            output[1]=1;
        }
        else if (s.equals("STUDENTREG"))
        {
            output[1]=2;
        }
        else if (s.equals("LOGIN"))
        {
            output[1]=3;
        }
        if (s.equals("LOGOUT"))
        {
            output[1]=4;
        }
        else if (s.equals("COURSEREG"))
        {
            output[1]=5;
        }
        else if (s.equals("KDAMCHECK"))
        {
            output[1]=6;
        }
        else if (s.equals("COURSESTAT"))
        {
            output[1]=7;
        }
        else if (s.equals("STUDENTSTAT"))
        {
            output[1]=8;
        }
        else if (s.equals("ISREGISTERED"))
        {
            output[1]=9;
        }
        else if (s.equals("UNREGISTER"))
        {
            output[1]='A';
        }
        if (s.equals("MYCOURSES"))
        {
            output[1]='B';
        }
        else if (s.equals("ACK"))
        {
            output[1]='C';
        }
        else if (s.equals("ERR"))
        {
            output[1]='D';
        }
        return output;
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

    private void initMsgLen(){
        msgLen =  new HashMap<>();
        msgLen.put("ADMINREG", 2);
        msgLen.put("STUDENTREG", 2);
        msgLen.put("LOGIN", 2);
        msgLen.put("LOGOUT", 1);
        msgLen.put("COURSEREG", 1);
        msgLen.put("KDAMCHECK", 1);
        msgLen.put("STUDENTSTAT", 1);
        msgLen.put("ISREGISTERED", 1);
        msgLen.put("UNREGISTER", 1);
        msgLen.put("MYCOURSES", 1);
        msgLen.put("ACK", 1);
        msgLen.put("ERR", 1);
        msgLen.put("COURSESTAT",1);
    }

}