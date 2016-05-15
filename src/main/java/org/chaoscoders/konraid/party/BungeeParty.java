package org.chaoscoders.konraid.party;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;
/**
 * Created by konraid on 15.05.16.
 */
public class BungeeParty {
    private ProxiedPlayer OWNER;
    private List<String> MEMBERS;

    public BungeeParty(ProxiedPlayer owner, List<String> members) {
        this.OWNER = owner;
        this.MEMBERS = members;

        MEMBERS.add(OWNER.getName());
        Main.GETPARTY.put(OWNER.getName(), OWNER.getName());
    }

    public void sendMessage(String message) {
        for(String member : MEMBERS) {
            ProxyServer.getInstance().getPlayer(member).sendMessage(message);
        }
    }

    public ProxiedPlayer getOwner() {
        return OWNER;
    }

    public List<String> getMembers() {
        return MEMBERS;
    }

    public void setOwner(ProxiedPlayer owner) {
        this.OWNER = owner;
    }

    public void setMembers(List<String> members) {
        this.MEMBERS = members;
    }
}
