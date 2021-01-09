#include <iostream>
#include <thread>
#include "../include/ConnectionHandler.h"



int main(int argc, char *argv[]) {
    if (argc < 3) {
        std::cerr << "Usage: " << argv[0] << " host port" << std::endl << std::endl;
        return -1;
    }
    std::string host = argv[1];
    short port = atoi(argv[2]);

    ConnectionHandler connectionHandler(host, port);
    if (!connectionHandler.connect()) {
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }


    std::thread readThread(&ConnectionHandler::runRead, &connectionHandler);
    std::thread writeThread(&ConnectionHandler::runWrite, &connectionHandler);

    writeThread.join();
    connectionHandler.realeseLock();
    readThread.join();




    return 0;
}