package org.chaoscoders.konraid.party;

/**
 * Created by konraid on 15.05.16.
 */
public class PartyManager {

    static String PLAYER;
    static BungeeParty PARTY;

    public static BungeeParty getParty(String player) {
        PLAYER = player;
        return (BungeeParty) Main.PARTY.get(Main.GETPARTY.get(player));
    }

    public static void inviteToParty(String player, BungeeParty party) {
        PLAYER = player;
        PARTY = party;
        Main.INVITEPARTY.put(player, party.getOwner().getName());
    }

    public static void addToParty(String player, BungeeParty party) {
        party.getMembers().add(player);
    }


    public static void removeFromParty(String player, BungeeParty party){
        PLAYER = player;
        PARTY = party;
        party.getMembers().remove(player);
    }
}
