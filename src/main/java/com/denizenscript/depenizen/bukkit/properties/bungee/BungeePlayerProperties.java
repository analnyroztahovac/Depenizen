package com.denizenscript.depenizen.bukkit.properties.bungee;

import com.denizenscript.depenizen.bukkit.bungee.BungeeHelpers;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.objects.Mechanism;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.objects.properties.Property;
import net.aufdemrand.denizencore.tags.Attribute;

public class BungeePlayerProperties implements Property {

        public static boolean describes(dObject entity) {
            return entity instanceof dPlayer;
        }

        public static BungeePlayerProperties getFrom(dObject player) {
            if (!describes(player)) {
                return null;
            }
            else {
                return new BungeePlayerProperties((dPlayer) player);
            }
        }

        public static final String[] handledTags = new String[] {
        };

        public static final String[] handledMechs = new String[] {
                "send_to"
        };


        ///////////////////
        // Instance Fields and Methods
        /////////////

        private BungeePlayerProperties(dPlayer plr) {
            player = plr;
        }

        dPlayer player;

        /////////
        // Property Methods
        ///////

        @Override
        public String getPropertyString() {
            return null;
        }

        @Override
        public String getPropertyId() {
            return "BungeePlayer";
        }


        ///////////
        // dObject Attributes
        ////////

        @Override
        public String getAttribute(Attribute attribute) {
            return null;
        }

        @Override
        public void adjust(Mechanism mechanism) {

            // <--[mechanism]
            // @object dPlayer
            // @name send_to
            // @input Element
            // @description
            // Sends the player to the specified Bungee server.
            // -->
            if ((mechanism.matches("send_to") && mechanism.hasValue())) {
                BungeeHelpers.sendPlayer(player.getOfflinePlayer().getUniqueId(), mechanism.getValue().asString());
            }
        }
    }
