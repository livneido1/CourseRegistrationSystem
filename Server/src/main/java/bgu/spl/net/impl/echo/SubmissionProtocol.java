package bgu.spl.net.impl.echo;

import bgu.spl.net.api.MessagingProtocol;
import jdk.internal.loader.AbstractClassLoaderValue;
import org.graalvm.compiler.replacements.nodes.CStringConstant;

public class SubmissionProtocol implements MessagingProtocol<String> {
    String name;

    public SubmissionProtocol(){
        this.name=null;
    }


    @Override
    public String process(String msg) {
        if (msg.startsWith("ADMINREG"))


        }
        if ()
    }


    private String adminRegProcess(String msg){

    }

    @Override
    public boolean shouldTerminate() {
        return false;
    }
}
