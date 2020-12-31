package bgu.spl.net.srv;

public class ProtocolEnvelope {
    private String massage;
    private String userName;

    public ProtocolEnvelope(String userName,String massage){
        this.massage = massage;
        this.userName = userName;
    }
}
