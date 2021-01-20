package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.impl.BGRSServer.Attachments.SubmissionEncoderDecoder;
import bgu.spl.net.impl.BGRSServer.Attachments.SubmissionProtocol;
import bgu.spl.net.srv.Server;


public class TPCMain {
    public static void main(String[] args){
        int port = Integer.parseInt(args[0]);

        Server.threadPerClient(port,   () ->  new SubmissionProtocol(), //protocol factory
                SubmissionEncoderDecoder::new ).serve();


    }
}
