//
// Created by spl211 on 07/01/2021.
//

#ifndef SPL3NEW3_SUBMISSIONENCODERDECODER_H
#define SPL3NEW3_SUBMISSIONENCODERDECODER_H


#include <string>
#include <unordered_map>
#include <vector>

class SubmissionEncoderDecoder {
private:
    bool shouldTerminate;
    std::unordered_map<std::string,std:: string> opCodes;
    std:: unordered_map<std::string , int> msgLen ;
    void initOpcodes();
    void initMsgLen();
    std::string translateOpcode(std::string opcode);

public:
    SubmissionEncoderDecoder();
    std::string translateReceived(std::string msg);
    std::string translateSend(std::string msg);
    std::vector<std::string> splitString(std::string delimiter, std::string msg);

    bool getShouldTerminate() const;

    std::string stringToHex(std::string msg);
    int getMsgSize(std::string msg);

};



#endif //SPL3NEW3_SUBMISSIONENCODERDECODER_H
