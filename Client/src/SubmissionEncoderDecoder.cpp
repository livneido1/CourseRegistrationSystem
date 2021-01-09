//
// Created by spl211 on 07/01/2021.
//
#include <unordered_map>
#include <ios>
#include "SubmissionEncoderDecoder.h"

SubmissionEncoderDecoder::SubmissionEncoderDecoder(): shouldTerminate(false),opCodes(), msgLen() {
    initOpcodes();
}

std::string SubmissionEncoderDecoder::translateReceived(std::string msg)  {

    std::string delimiter =  "0";

    if (msg.length()>2) {
        std::string op(msg.substr(0, 2));
        if ((opCodes.find(op) != opCodes.end())) {

            msg.erase(0, 2);

            std::vector<std::string> splitted = splitString(delimiter, msg);
            splitted.at(0).erase(0,1);
            splitted.at(0) = translateOpcode( splitted.at(0));
            splitted.insert(splitted.begin(), op);

            std::string output("");

            std::unordered_map<std::string, std::string>::const_iterator got = opCodes.find(splitted.front());
            if (splitted.at(1) == "4" && op =="0c")
                shouldTerminate = true;


            if (!(got == opCodes.end())) {
                output = output + opCodes.at(splitted.front());

            } else {
                output = output + splitted.front();
            }

            splitted.erase(splitted.begin());
            while (!splitted.empty()) {

                output += " " + splitted.front();
                splitted.erase(splitted.begin());
            }
            return output;
        }
        else return msg.substr(1);
    }
    else
        return msg;



}
/**
 * this method receive the message and tranlsates it into hexa. also translates the comman to opCode
 * @param msg - the massage received from client's keyboard
 * @return  Hex values message, command as opcode;
 */
std::string SubmissionEncoderDecoder::translateSend(std::string msg) {
    std::string delimiter =  " ";
    std::vector<std::string> splitted = splitString(delimiter,msg);
    std::string output("");
    std::string command = splitted.front();


    if (!(opCodes.find(command)==opCodes.end())){
        std::string temp = opCodes.at(command);
        int msgSize =  getMsgSize(temp);
        int size =  splitted.size();
        if (msgSize != size)
            throw std:: invalid_argument("ERROR" + temp);
        output += temp;
    }
    else
        throw std:: invalid_argument("ERROR" + splitted.front());



    splitted.erase(splitted.begin());

    while (!splitted.empty()){
        output +=  splitted.front();
        output = output.append(1,'\0');
        splitted.erase(splitted.begin());
    }

    return output;

}


std::vector<std::string> SubmissionEncoderDecoder::splitString(std::string delimiter, std::string msg) {
    int pos(0);
    std::string token;
    std::vector<std::string> splitted;

    while ((pos = msg.find(delimiter)) >0) {
        token = msg.substr(0, pos);
        msg.erase(0, pos + 1);
        splitted.push_back(token);
    }
    if (msg.length()>0)
        splitted.push_back(msg);
    return splitted;
}

std::string SubmissionEncoderDecoder::translateOpcode(std::string opcode) {
    if (opcode == "a")
        return "10";
    else if (opcode=="b")
        return "11";
    else if (opcode=="c")
        return "12";
    else if (opcode=="d")
        return "13";
    else return opcode;
}

std::string SubmissionEncoderDecoder::stringToHex(std::string msg) {
    std::string output="";
    for (char c : msg){
        std::string temp;
        int curr (c);
        // now we turn c into hex
        int round(0);
        while(curr>15){
            curr = curr-16;
            round+=1;
        }
        output+= std::to_string(round);
        if (curr<10) {

            output += std::to_string( curr);
        }
        else{
            if (curr==10)
                output+='a';
            else if(curr ==11)
                output+='b';
            else if(curr ==12)
                output+='c';
            else if(curr ==13)
                output+='d';
            else if(curr ==14)
                output+='e';
            else
                output+='f';
        }
    }
    return output;
}



    int SubmissionEncoderDecoder::getMsgSize(std::string msg) {
    if ((msgLen.size()>0)==0)
        initMsgLen();
    return msgLen.at(msg);
}

    void SubmissionEncoderDecoder::initMsgLen() {
        msgLen["01"] = 3;
        msgLen["02"] = 3;
        msgLen["03"] = 3;
        msgLen["04"] = 1;
        msgLen["05"] = 2;
        msgLen["06"] = 2;
        msgLen["07"] = 2;
        msgLen["08"] = 2;
        msgLen["09"] = 2;
        msgLen["0a"] = 2;
        msgLen["0b"] = 1;
        msgLen["0c"] = 1;
        msgLen["0d"] = 1;
    }

    void SubmissionEncoderDecoder::initOpcodes() {
        opCodes ["01"] = "ADMINREG";
        opCodes ["02"] = "STUDENTREG";
        opCodes ["03"] = "LOGIN";
        opCodes ["04"] = "LOGOUT";
        opCodes ["05"] = "COURSEREG";
        opCodes ["06"] = "KDAMCHECK";
        opCodes ["07"] = "COURSESTAT";
        opCodes ["08"] = "STUDENTSTAT";
        opCodes ["09"] = "ISREGISTERED";
        opCodes ["0a"] = "UNREGISTER";
        opCodes ["0b"] = "MYCOURSES";
        opCodes ["0c"] = "ACK";
        opCodes ["0d"] = "ERROR";
        opCodes ["ADMINREG"] = "01";
        opCodes ["STUDENTREG"] = "02";
        opCodes ["LOGIN"] = "03";
        opCodes ["LOGOUT"] = "04";
        opCodes ["COURSEREG"] = "05";
        opCodes ["KDAMCHECK"] = "06";
        opCodes ["COURSESTAT"] = "07";
        opCodes ["STUDENTSTAT"] = "08";
        opCodes ["ISREGISTERED"] = "09";
        opCodes ["UNREGISTER"] = "0a";
        opCodes ["MYCOURSES"] = "0b";
        opCodes ["ACK"] = "0c";
        opCodes ["ERROR"] = "0d";
    }
    bool SubmissionEncoderDecoder::getShouldTerminate() const{
        bool test = shouldTerminate;
        return test;
}




