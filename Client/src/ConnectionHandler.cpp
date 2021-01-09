//
// Created by spl211 on 04/01/2021.
//

#include <boost/asio/ip/tcp.hpp>
#include <iostream>
#include "../include/ConnectionHandler.h"
#include "SubmissionEncoderDecoder.h"
#include <thread>
#include <shared_mutex>
#include <condition_variable>
using boost::asio::ip::tcp;

using std::cin;
using std::cout;
using std::cerr;
using std::endl;
using std::string;

ConnectionHandler::ConnectionHandler(string host, short port): host_(host), port_(port),
io_service_(), socket_(io_service_), encoderDecoder(),shouldTerminate(false) , lock(), cv()
{
    encoderDecoder =  SubmissionEncoderDecoder();


}

ConnectionHandler::~ConnectionHandler() {
    close();
}

bool ConnectionHandler::connect() {
    std::cout << "Starting connect to "
              << host_ << ":" << port_ << std::endl;
    try {
        tcp::endpoint endpoint(boost::asio::ip::address::from_string(host_), port_); // the server endpoint
        boost::system::error_code error;
        socket_.connect(endpoint, error);
        if (error)
            throw boost::system::system_error(error);
    }
    catch (std::exception& e) {
        std::cerr << "Connection failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}

void ConnectionHandler::runRead() {
    while(!shouldTerminate){
        const short bufsize = 1024;
        char buf[bufsize];
        std::cin.getline(buf, bufsize);
        std::string msg(buf);
        try {
            msg = encoderDecoder.translateSend(msg);

            std::string line(msg);
            if (!sendLine(line)) {
                std::cout << "Disconnected. Exiting...\n" << std::endl;
                break;
            }
            std::string logout = msg.substr(0,2);
            if (logout.compare("04") ==0){
                std:: unique_lock<std::mutex> lk(lock);
                cv.wait(lk);


            }
        }catch (std::invalid_argument& err){
            if (msg.length()>0) {
                std::string code=( encoderDecoder.splitString(" ", msg)).front();
                std::cout << "ERROR " + code<<endl;
            }
            else
                std::cout<< "ERROR"<<endl;
        }
    }
}

void ConnectionHandler::runWrite() {

    while(!shouldTerminate){

        string answer;
        if (!getLine(answer)) {
            std::cout << "Disconnected. Exiting...\n" << std::endl;
            break;
        }
        answer = encoderDecoder.translateReceived(answer);

        int len;


        len = answer.length();
        if(len>0) {
            std::cout << answer << std::endl;
            shouldTerminate = encoderDecoder.getShouldTerminate();


        }
        cv.notify_all();
    }
}

bool ConnectionHandler::getBytes(char bytes[], unsigned int bytesToRead) {
    size_t tmp = 0;
    boost::system::error_code error;
    try {
        while (!error && bytesToRead > tmp ) {
            int t = socket_.read_some(boost::asio::buffer(bytes+tmp, bytesToRead-tmp), error);
            tmp +=t;

        }
        if(error)
            throw boost::system::system_error(error);
    } catch (std::exception& e) {
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }


    return true;
}

bool ConnectionHandler::sendBytes(const char bytes[], int bytesToWrite) {
    int tmp = 0;
    boost::system::error_code error;
    try {
        while (!error && bytesToWrite > tmp ) {
            tmp += socket_.write_some(boost::asio::buffer(bytes + tmp, bytesToWrite - tmp), error);
        }
        if(error)
            throw boost::system::system_error(error);
    } catch (std::exception& e) {
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}

bool ConnectionHandler::getLine(std::string& line) {

    return getFrameAscii(line, '\0');
}
bool ConnectionHandler::sendLine(std::string& line) {

    return sendFrameAscii(line, '\0');
}


bool ConnectionHandler::getFrameAscii(std::string& frame, char delimiter) {
    char ch;
    // Stop when we encounter the null character.
    // Notice that the null character is not appended to the frame string.
    try {
        do{
            if(!getBytes(&ch, 1))
            {
                return false;
            }

            if(ch!='\0') {


                frame.append(1, ch);
            }
            else if (ch =='\n'){
                frame.append(1,'\n');
            }
            
        }while (delimiter != ch);
    } catch (std::exception& e) {
        std::cerr << "recv failed2 (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}


bool ConnectionHandler::sendFrameAscii(const std::string& frame, char delimiter) {
    bool result=sendBytes(frame.c_str(),frame.length());
    if(!result) return false;
    return sendBytes(&delimiter,1);
}

void ConnectionHandler::close() {
    try{
        socket_.close();
    } catch (...) {
        std::cout << "closing failed: connection already closed" << std::endl;
    }
}

void ConnectionHandler::realeseLock() {cv.notify_all();}