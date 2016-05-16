package org.chaoscoders.konraid.party;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.event.EventHandler;

/**
 * Created by konraid on 15.05.16.
 */
public class Events implements net.md_5.bungee.api.plugin.Listener {

    String prefix = "§6§2Party §8>> §r";

    @EventHandler
    public void onPartyMemberDisconnect(PlayerDisconnectEvent ev) {
        if (Main.GETPARTY.containsKey(ev.getPlayer().getName())) {
            ProxiedPlayer p = (ProxiedPlayer) ev.getPlayer();
            /*
                TODO
                If PartyOwner Disconects make new Member owner

             */
            if (PartyManager.getParty(p.getName()).getOwner() == p) {
                for (String all : PartyManager.getParty(p.getName()).getMembers()) {
                    ProxyServer.getInstance().getPlayer(all).sendMessage(prefix + "§cDie Party von §e" +
                            p.getName() + " §cwurde aufgelöst.");
                    Main.GETPARTY.remove(all);
                }
            } else {
                PartyManager.getParty(p.getName()).sendMessage(prefix + "§e" + p.getName()+
                        " §chat die Party verlassen.");
                PartyManager.removeFromParty(p.getName(), PartyManager.getParty(p.getName()));
                Main.GETPARTY.remove(p.getName());
            }
        }
    }

    @EventHandler
    public void onSwitch(ServerSwitchEvent ev){
        ProxiedPlayer p = ev.getPlayer();
        if(Main.GETPARTY.containsKey(p.getName())){
            if(PartyManager.getParty(p.getName()).getOwner() == p){
                /*if(PartyManager.getParty(p.getName()).getMembers().size() >
                        p.getServer().getInfo().getPlayers().size()) {*/
                /*p.sendMessage(prefix + "§cFehler die Party ist zu groß und es können nicht " +
                        "alle Spieler teleportiert werden.");
                p.sendMessage(p.getServer().getInfo().getName());*/
                p.sendMessage(p.getServer().getInfo().getPlayers().size() + "");
                for (String members : PartyManager.getParty(p.getName()).getMembers()) {
                    ProxiedPlayer t = ProxyServer.getInstance().getPlayer(members);
                    if(t.getName() == p.getName()) {
                    }else if (t.getServer().getInfo() == p.getServer().getInfo()) {
                        t.sendMessage(prefix + "§3Du bist bereits auf dem Server.");
                    } else {
                        t.connect(p.getServer().getInfo());
                    }
                }
                PartyManager.getParty(p.getName()).sendMessage(prefix + "§aDie Party Betrit den Server §6" + p.getServer().getInfo().getName());
            }
        }
    }
}
