//
// Created by spl211 on 04/01/2021.
//

#ifndef CLIENT_CONNECTIONHANDLER_H
#define CLIENT_CONNECTIONHANDLER_H


#include <string>
#include <iostream>
#include <boost/asio.hpp>
#include "SubmissionEncoderDecoder.h"
#include <thread>
#include <shared_mutex>
#include <condition_variable>

using boost::asio::ip::tcp;

class ConnectionHandler {
private:
    const std::string host_;
    const short port_;
    boost::asio::io_service io_service_;   // Provides core I/O functionality
    tcp::socket socket_;
    SubmissionEncoderDecoder encoderDecoder;
    bool shouldTerminate;
    std::mutex lock;
    std::condition_variable cv;

public:
    ConnectionHandler(std::string host, short port);
    virtual ~ConnectionHandler();

    // Connect to the remote machine
    bool connect();

    void runRead();

    void runWrite();

    bool getBytes(char bytes[], unsigned int bytesToRead);


    bool sendBytes(const char bytes[], int bytesToWrite);


    bool getLine(std::string& line);


    bool sendLine(std::string& line);

    bool getFrameAscii(std::string& frame, char delimiter);

    bool sendFrameAscii(const std::string& frame, char delimiter);

    void realeseLock();

    void close();



};



#endif //CLIENT_CONNECTIONHANDLER_H
