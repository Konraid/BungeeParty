package org.chaoscoders.konraid.party;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;

/**
 * Created by konraid on 15.05.16.
 */
public class Commands extends Command{

    String prefix = "§6§2Party §8>> §r";

    public Commands() {
        super("party");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer) {
            ProxiedPlayer p = (ProxiedPlayer) sender;

            if(args.length == 0) {
                p.sendMessage("§6§2[]§8=================§6§5[ §f§2Party §6§5]§8=================§6§2[]");
                p.sendMessage("");
                p.sendMessage("§e/party invite <Name> §3Lade einen Spieler in die Party ein.");
                p.sendMessage("§e/party accept §3Nimmt eine Anfrage an.");
                p.sendMessage("§e/party deny §3Lehnt eine Anfrage ab.");
                p.sendMessage("§e/party kick <Name> §3Kicke einen Spieler aus deiner Party.");
                p.sendMessage("§e/party chat <Text...> §3Sende eine Nachricht an alle Party Mitglieder.");
                p.sendMessage("");
                p.sendMessage("§6§2[]§8=================§6§5[ §f§2Party §6§5]§8=================§6§2[]");
            } else {
                if(args.length >= 1) {
                    if(args[0].equalsIgnoreCase("invite")) {
                        if(PartyManager.getParty(p.getName()) == null) {
                            Main.PARTY.put(p.getName(), new BungeeParty(p, new ArrayList<String>()));
                        }
                        if(PartyManager.getParty(p.getName()).getOwner() == p) {
                            /*
                                TODO
                                Array out of Bound exeption

                             */
                            if(ProxyServer.getInstance().getPlayer(args[1]) == null) {
                                p.sendMessage(prefix + "§cFehler! Der Spieler ist nicht Online.");
                            } else {
                                ProxiedPlayer t = ProxyServer.getInstance().getPlayer(args[1]);

                                if(Main.GETPARTY.containsKey(t.getName())) {
                                    p.sendMessage(prefix + "§e" + t.getName()+ " §cist bereits in einer Party!");
                                } else {

                                    PartyManager.inviteToParty(t.getName(), PartyManager.getParty(p.getName()));
                                    t.sendMessage(prefix + "§e" + p.getName() +
                                            " §3hat dich in seine Party eingeladen!");
                                    t.sendMessage(prefix + "§a/party accept §7Um die Party von §e" +
                                            p.getName() + " §3 zu betreten.");
                                    p.sendMessage(prefix + "§3Du hast den Spieler §e" + t.getName() +
                                            " §3in deine Party eingeladen.");
                                }
                            }
                        } else {
                            p.sendMessage(prefix + "§cDu bist nicht der Partyleiter.");
                        }
                    } else if(args[0].equalsIgnoreCase("accept")) {
                        if(Main.PARTY.containsKey(Main.INVITEPARTY.get(p.getName()))) {
                            // Testen ob die maximale Mebmber Anzahl erreicht wurde
                            if(PartyManager.getParty((String)
                                    Main.INVITEPARTY.get(p.getName())).getMembers().size() >= Main.MAX_PARTY_MEMBERS) {
                                p.sendMessage(prefix + "§cFehler! Die Party ist voll.");
                            } else {
                                Main.GETPARTY.put(p.getName(), Main.INVITEPARTY.get(p.getName()));
                                PartyManager.addToParty(p.getName(), PartyManager.getParty(
                                        (String)Main.INVITEPARTY.get(p.getName())));
                                PartyManager.getParty(p.getName()).sendMessage(prefix +
                                "§e" + p.getName() + " §3ist der Party beigetreten.");
                            }
                        } else {
                            p.sendMessage(prefix + "§cFehler! Die Party wurde nicht gefunden.");
                        }
                    } else if(args[0].equalsIgnoreCase("deny")) {
                        if(Main.INVITEPARTY.containsKey(p.getName())) {
                            Main.INVITEPARTY.remove(p.getName());
                            PartyManager.getParty(p.getName()).sendMessage(prefix + "§e" + p.getName() +
                                    " §3hat die Partyanfrage abgelehnt.");
                        }
                    } else if(args[0].equalsIgnoreCase("leave")) {
                        if(Main.GETPARTY.containsKey(p.getName())) {
                            if(PartyManager.getParty(p.getName()).getOwner() == p) {
                                for (String all: PartyManager.getParty(p.getName()).getMembers()) {
                                    ProxyServer.getInstance().getPlayer(all).sendMessage(prefix + "§cDie Party von §e" +
                                            p.getName() + " §cwurde aufgelöst.");
                                    Main.GETPARTY.remove(all);
                                }
                            } else {
                                PartyManager.getParty(p.getName()).sendMessage(prefix + "§e" + p.getName()+
                                        " §chat die Party verlassen.");
                                PartyManager.removeFromParty(p.getName(), PartyManager.getParty(p.getName()));
                                p.sendMessage(prefix + "§3Du hast die Party von §e" +
                                        PartyManager.getParty(p.getName()).getOwner().getName() + " §3verlassen");
                                Main.GETPARTY.remove(p.getName());

                            }
                        } else {
                            p.sendMessage(prefix + "§cDu bist in keiner Party!");
                        }
                    } else if(args[0].equalsIgnoreCase("kick")) {
                        // Testen ob Spieler in einer Party ist
                        if(Main.GETPARTY.containsKey(p.getName())) {
                            // Testen ob Spieler Partyleiter ist
                            if(PartyManager.getParty(p.getName()).getOwner() == p) {
                                // Testen ob gewünschter Spieler in einer Party und wenn ja in der gleichen ist
                                if(PartyManager.getParty(p.getName()).getMembers().contains(args[1])) {
                                    PartyManager.removeFromParty(args[1], PartyManager.getParty(p.getName()));
                                    PartyManager.getParty(p.getName()).sendMessage(prefix + "§e" + args[1]
                                    + " §3wurde aus der Party gekickt!");
                                    ProxyServer.getInstance().getPlayer(args[1]).sendMessage(prefix +
                                    "§3Du wurdest von §e" + p.getName() + "§3 aus der Party gekickt.");
                                    Main.GETPARTY.remove(args[1]);
                                } else {
                                    p.sendMessage(prefix + "§e" + args[1] + "§c ist nicht in deiner Party.");
                                }
                            } else {
                                p.sendMessage(prefix + "§cDu bist nicht der Partyleiter.");
                            }
                        } else {
                            p.sendMessage(prefix + "§cDu bist in keiner Party.");
                        }
                    } else if(args[0].equalsIgnoreCase("chat")) {
                        if(Main.GETPARTY.containsKey(p.getName())) {
                            StringBuilder b = new StringBuilder();
                            for (int i = 1; i < args.length; i++) {
                                b.append(args[i]).append(" ");
                            }

                            if(PartyManager.getParty(p.getName()).getOwner() == p) {
                                PartyManager.getParty(p.getName()).sendMessage("§c" + p.getName() +
                                "§8>> §7" + b.toString());
                            } else {
                                PartyManager.getParty(p.getName()).sendMessage("§9" + p.getName() +
                                        "§8>> §7" + b.toString());
                            }
                        } else {
                            p.sendMessage(prefix + "§cDu bist in keiner Party.");
                        }
                    } else {
                        p.sendMessage(prefix + "§cDer Befehl §9" + args[0] +" §cexistiert nicht." +
                        "Verwende §e/party §c für eine Liste der verfügbaren Commands.");
                    }
                }
            }
        }
    }
}
