package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.impl.Attachments.SubmissionEncoderDecoder;
import bgu.spl.net.impl.Attachments.SubmissionProtocol;
import bgu.spl.net.srv.Server;

public class ReactorMain {

    public static void main(String[] args) {

        int nThreads =  Integer.parseInt(args[1]);
        int port = Integer.parseInt(args[0]);

        Server.reactor(
               // Runtime.getRuntime().availableProcessors(),
                nThreads,
                port,
                () ->  new SubmissionProtocol(), //protocol factory
                SubmissionEncoderDecoder::new //message encoder decoder factory
        ).serve();

    }
}
