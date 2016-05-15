package org.chaoscoders.konraid.party;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by konraid on 15.05.16.
 */
public class Main extends Plugin{
    //KONSTANTEN
    public final static int MAX_PARTY_MEMBERS = 4;

    public static Map<String, BungeeParty> PARTY = new HashMap<String, BungeeParty>();
    public static Map<String, String> GETPARTY = new HashMap<String, String>();
    public static Map<String, String> INVITEPARTY = new HashMap<String, String>();

    @Override
    public void onEnable() {
        PluginManager pm = ProxyServer.getInstance().getPluginManager();
        pm.registerCommand(this, new Commands());
        pm.registerListener(this, new Events());
    }

    /*
        TODO
        VIP's unendlich viele Members
     */
}
