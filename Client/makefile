CFLAGS:=-c -Wall -Weffc++ -g -std=c++11 -Iinclude
LDFLAGS:=-lboost_system -lpthread

all: submission
	g++ -o bin/BGRSclient bin/SubmissionEncoderDecoder.o bin/ConnectionHandler.o bin/BGRSclient.o  $(LDFLAGS)

submission: bin/SubmissionEncoderDecoder.o bin/ConnectionHandler.o bin/BGRSclient.o

bin/SubmissionEncoderDecoder.o: src/SubmissionEncoderDecoder.cpp
	g++ $(CFLAGS) -o bin/SubmissionEncoderDecoder.o src/SubmissionEncoderDecoder.cpp $(LDFLAGS)

bin/ConnectionHandler.o: src/ConnectionHandler.cpp
	g++ $(CFLAGS) -o bin/ConnectionHandler.o src/ConnectionHandler.cpp $(LDFLAGS)

bin/BGRSclient.o: src/BGRSclient.cpp
	g++ $(CFLAGS) -o bin/BGRSclient.o src/BGRSclient.cpp $(LDFLAGS)


.PHONY: clean
clean:
	rm -f bin/*
