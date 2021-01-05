package bgu.spl.net.impl.echo;

import bgu.spl.net.api.MessageEncoderDecoder;


import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.function.DoubleToIntFunction;


public class SubmissionEncoderDecoder implements MessageEncoderDecoder<String> {
    private byte[] bytes = new byte[1 << 10];
    private int len = 0;

    @Override
    public String decodeNextByte(byte nextByte) {
        if (nextByte == '\0') {
            return popString();
        }
        addByte(nextByte);
        return null;
    }



    @Override
    public byte[] encode(String message) {//TODO: check if we need to send opCode as bytes or String as bytes
      byte[] output=new byte[1 << 10];
      output[0]=0;
      if (message.startsWith("ACK"))
      {
        output[1]='C';
        output[2]=0;
        message=message.substring(4);
        String opCode;
        if (message.charAt(1)<='9'&&message.charAt(1)>='0')//two digits number
        {
            opCode=message.substring(0,2);
            message=message.substring(2);
        }
        else{
            opCode=message.substring(0,1);
            message=message.substring(1);
        }
        byte[] opCodeBytes=fillBytes(opCode);//4 bytes of opCode
        for (int i=0;i<2;i++)
        {
            output[3+i]=opCodeBytes[i];
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
          output[1]='D';
          message=message.substring(6);
          String opCode;
          if (message.charAt(1)<='9'&&message.charAt(1)>='0')//two digits number
          {
              opCode=message.substring(0,2);
              message=message.substring(2);
          }
          else{
              opCode=message.substring(0,1);
              message=message.substring(1);
          }
          byte[] opCodeBytes=fillBytes(opCode);//4 bytes of opCode
          for (int i=0;i<2;i++)
          {
              output[3+i]=opCodeBytes[i];
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

        String result = new String(bytes, 0, len, StandardCharsets.UTF_8);
        String opCode = result.substring(0,result.indexOf(" "));
        result=result.substring(opCode.length());
        String output=translateOpCodeToName(opCode);

        output=output+result;
        len = 0;
        return output;

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
        else if (bytesString.equals("0003"))
        {
            return "LOGIN";
        }
        if (bytesString.equals("0004"))
        {
            return "LOGOUT";
        }
        else if (bytesString.equals("0005"))
        {
            return "COURSEREG";
        }
        else if (bytesString.equals("0006"))
        {
            return "KDAMCHECK";
        }
        else if (bytesString.equals("0007"))
        {
            return "COURSESTAT";
        }
        else if (bytesString.equals("0008"))
        {
            return "STUDENTSTAT";
        }
        else if (bytesString.equals("0009"))
        {
            return "ISREGISTERED";
        }
        else if (bytesString.equals("000A"))
        {
            return "UNREGISTER";
        }
        if (bytesString.equals("000B"))
        {
            return "MYCOURSES";
        }
        else if (bytesString.equals("000C"))
        {
            return "ACK";
        }
        else if (bytesString.equals("000D"))
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

}