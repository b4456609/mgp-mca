package soselab.easylearn.MCA.parser.model;

import java.lang.reflect.Member;
import java.util.Set;

/**
 * Created by bernie on 1/9/17.
 */
public class ClientCaller {
    private final ClientInfo clientInfo;
    private final Set<Member> members;

    public ClientCaller(ClientInfo clientInfo, Set<Member> members) {
        this.clientInfo = clientInfo;
        this.members = members;
    }

    public ClientInfo getClientInfo() {
        return clientInfo;
    }

    public Set<Member> getMembers() {
        return members;
    }

    @Override
    public String toString() {
        return "ClientCaller{" +
                "clientInfo=" + clientInfo +
                ", members=" + members +
                '}';
    }
}
