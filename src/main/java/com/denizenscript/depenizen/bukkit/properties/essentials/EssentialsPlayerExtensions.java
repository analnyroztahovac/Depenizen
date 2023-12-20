package com.denizenscript.depenizen.bukkit.properties.essentials;

import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.core.DurationTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.core.MapTag;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.denizenscript.denizencore.utilities.debugging.SlowWarning;
import com.denizenscript.depenizen.bukkit.bridges.EssentialsBridge;
import com.earth2me.essentials.User;
import net.essentialsx.api.v2.services.mail.MailMessage;
import org.bukkit.Location;

import java.util.UUID;

public class EssentialsPlayerExtensions {

    public static User getUser(PlayerTag player) {
        return EssentialsBridge.essentialsInstance.getUser(player.getUUID());
    }

    public static SlowWarning oldHomesTag = new SlowWarning("essentialsListHomes", "The tag 'list_homes' from Depenizen/Essentials is deprecated: use 'essentials_homes' (now a MapTag).");

    public static void register() {

        // <--[tag]
        // @attribute <PlayerTag.god_mode>
        // @returns ElementTag(Boolean)
        // @mechanism PlayerTag.god_mode
        // @plugin Depenizen, Essentials
        // @description
        // Returns whether the player is currently in god mode.
        // -->
        PlayerTag.tagProcessor.registerTag(ElementTag.class, "god_mode", (attribute, player) -> {
            return new ElementTag(getUser(player).isGodModeEnabled());
        });

        // <--[tag]
        // @attribute <PlayerTag.has_home>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, Essentials
        // @description
        // Returns whether the player has set at least one home.
        // -->
        PlayerTag.tagProcessor.registerTag(ElementTag.class, "has_home", (attribute, player) -> {
            return new ElementTag(getUser(player).hasHome());
        });

        // <--[tag]
        // @attribute <PlayerTag.is_afk>
        // @returns ElementTag(Boolean)
        // @mechanism PlayerTag.is_afk
        // @plugin Depenizen, Essentials
        // @description
        // Returns whether the player is AFK.
        // -->
        PlayerTag.tagProcessor.registerTag(ElementTag.class, "is_afk", (attribute, player) -> {
            return new ElementTag(getUser(player).isAfk());
        });

        // <--[tag]
        // @attribute <PlayerTag.is_muted>
        // @returns ElementTag(Boolean)
        // @mechanism PlayerTag.is_muted
        // @plugin Depenizen, Essentials
        // @description
        // Returns whether the player is muted.
        // -->
        PlayerTag.tagProcessor.registerTag(ElementTag.class, "is_muted", (attribute, player) -> {
            return new ElementTag(getUser(player).isMuted());
        });

        // <--[tag]
        // @attribute <PlayerTag.is_vanished>
        // @returns ElementTag(Boolean)
        // @mechanism PlayerTag.vanish
        // @plugin Depenizen, Essentials
        // @description
        // Returns whether the player is vanished.
        // -->
        PlayerTag.tagProcessor.registerTag(ElementTag.class, "is_vanished", (attribute, player) -> {
            return new ElementTag(getUser(player).isVanished());
        });

        // <--[tag]
        // @attribute <PlayerTag.essentials_homes>
        // @returns MapTag
        // @plugin Depenizen, Essentials
        // @description
        // Returns a map of the homes of the player, with keys as the home name and values as the home location.
        // -->
        PlayerTag.tagProcessor.registerTag(MapTag.class, "essentials_homes", (attribute, player) -> {
            MapTag homes = new MapTag();
            for (String homeName : getUser(player).getHomes()) {
                try {
                    Location homeLoc = getUser(player).getHome(homeName);
                    if (homeLoc != null) { // home location can be null if the world isn't loaded
                        homes.putObject(homeName, new LocationTag(homeLoc));
                    }
                }
                catch (Exception e) {
                    if (!attribute.hasAlternative()) {
                        Debug.echoError(e);
                    }
                }
            }
            return homes;
        });

        PlayerTag.tagProcessor.registerTag(ListTag.class, "list_homes", (attribute, player) -> {
            oldHomesTag.warn(attribute.context);
            ListTag homes = new ListTag();
            User user = getUser(player);
            for (String homeName : user.getHomes()) {
                try {
                    homes.add(homeName + "/" + new LocationTag(user.getHome(homeName)).identifySimple());
                }
                catch (Exception e) {
                    if (!attribute.hasAlternative()) {
                        Debug.echoError(e);
                    }
                }
            }
            return homes;
        }, "homes_list");

        PlayerTag.tagProcessor.registerTag(ListTag.class, "list_home_locations", (attribute, player) -> {
            oldHomesTag.warn(attribute.context);
            ListTag homes = new ListTag();
            User user = getUser(player);
            for (String homeName : user.getHomes()) {
                try {
                    homes.addObject(new LocationTag(user.getHome(homeName)));
                }
                catch (Exception e) {
                    if (!attribute.hasAlternative()) {
                        Debug.echoError(e);
                    }
                }
            }
            return homes;
        }, "home_location_list");

        PlayerTag.tagProcessor.registerTag(ListTag.class, "list_home_names", (attribute, player) -> {
            oldHomesTag.warn(attribute.context);
            return new ListTag(getUser(player).getHomes());
        });

        // <--[tag]
        // @attribute <PlayerTag.ignored_players>
        // @returns ListTag(PlayerTag)
        // @plugin Depenizen, Essentials
        // @description
        // Returns a list of the ignored players of the player.
        // -->
        PlayerTag.tagProcessor.registerTag(ListTag.class, "ignored_players", (attribute, player) -> {
            ListTag players = new ListTag();
            for (UUID uuid : getUser(player)._getIgnoredPlayers()) {
                try {
                    players.addObject(new PlayerTag(uuid));
                }
                catch (Exception e) {
                    if (!attribute.hasAlternative()) {
                        Debug.echoError(e);
                    }
                }
            }
            return players;
        });

        // <--[tag]
        // @attribute <PlayerTag.list_mails>
        // @returns ListTag
        // @plugin Depenizen, Essentials
        // @description
        // Returns a list of mail the player currently has.
        // -->
        PlayerTag.tagProcessor.registerTag(ListTag.class, "list_mails", (attribute, player) -> {
            return new ListTag(getUser(player).getMailMessages().stream().map(MailMessage::getMessage).toList());
        }, "mail_list");

        // <--[tag]
        // @attribute <PlayerTag.mute_timeout>
        // @returns DurationTag
        // @plugin Depenizen, Essentials
        // @description
        // Returns how much time is left until the player is unmuted.
        // -->
        PlayerTag.tagProcessor.registerTag(DurationTag.class, "mute_timeout", (attribute, player) -> {
            return new DurationTag(getUser(player).getMuteTimeout() - System.currentTimeMillis());
        });

        // <--[tag]
        // @attribute <PlayerTag.socialspy>
        // @returns ElementTag(Boolean)
        // @mechanism PlayerTag.socialspy
        // @plugin Depenizen, Essentials
        // @description
        // Returns whether the player has SocialSpy enabled.
        // -->
        PlayerTag.tagProcessor.registerTag(ElementTag.class, "social_spy", (attribute, player) -> {
            return new ElementTag(getUser(player).isSocialSpyEnabled());
        });

        // <--[mechanism]
        // @object PlayerTag
        // @name is_afk
        // @input ElementTag(Boolean)
        // @plugin Depenizen, Essentials
        // @description
        // Sets whether the player is marked as AFK.
        // @tags
        // <PlayerTag.is_afk>
        // -->
        PlayerTag.tagProcessor.registerMechanism("is_afk", false, ElementTag.class, (player, mechanism, input) -> {
            if (mechanism.requireBoolean()) {
                getUser(player).setAfk(input.asBoolean());
            }
        }, "afk");

        // <--[mechanism]
        // @object PlayerTag
        // @name god_mode
        // @input ElementTag(Boolean)
        // @plugin Depenizen, Essentials
        // @description
        // Sets whether the player has god mode enabled.
        // @tags
        // <PlayerTag.god_mode>
        // -->
        PlayerTag.tagProcessor.registerMechanism("god_mode", false, ElementTag.class, (player, mechanism, input) -> {
            if (mechanism.requireBoolean()) {
                getUser(player).setGodModeEnabled(input.asBoolean());
            }
        });

        // <--[mechanism]
        // @object PlayerTag
        // @name is_muted
        // @input ElementTag(Boolean)(|DurationTag)
        // @plugin Depenizen, Essentials
        // @description
        // Sets whether the player is muted. Optionally, you may also
        // specify a duration to set how long they are muted for.
        // @tags
        // <PlayerTag.is_muted>
        // <PlayerTag.mute_timeout>
        // -->
        PlayerTag.tagProcessor.registerMechanism("is_muted", false, ElementTag.class, (player, mechanism, input) -> {
            if (mechanism.requireBoolean()) {
                ListTag split = mechanism.valueAsType(ListTag.class);
                getUser(player).setMuted(new ElementTag(split.get(0)).asBoolean());
                if (split.size() > 1) {
                    getUser(player).setMuteTimeout(System.currentTimeMillis() + DurationTag.valueOf(split.get(1), mechanism.context).getMillis());
                }
            }
        }, "muted");

        // <--[mechanism]
        // @object PlayerTag
        // @name remove_essentials_home
        // @input ElementTag
        // @plugin Depenizen, Essentials
        // @description
        // Removes the player's Essentials home that matches the specified name.
        // -->
        PlayerTag.tagProcessor.registerMechanism("remove_essentials_home", false, ElementTag.class, (player, mechanism, input) -> {
            try {
                if (getUser(player).hasHome(input.toString())) {
                    getUser(player).delHome(input.toString());
                }
                else {
                    mechanism.echoError("Invalid home name specified!");
                }
            }
            catch (Exception e) {
                Debug.echoError(e);
            }
        });

        // <--[mechanism]
        // @object PlayerTag
        // @name socialspy
        // @input ElementTag(Boolean)
        // @plugin Depenizen, Essentials
        // @description
        // Sets whether the player has SocialSpy enabled.
        // @tags
        // <PlayerTag.socialspy>
        // -->
        PlayerTag.tagProcessor.registerMechanism("socialspy", false, ElementTag.class, (player, mechanism, input) -> {
            if (mechanism.requireBoolean()) {
                getUser(player).setSocialSpyEnabled(input.asBoolean());
            }
        });

        // <--[mechanism]
        // @object PlayerTag
        // @name vanish
        // @input ElementTag(Boolean)
        // @plugin Depenizen, Essentials
        // @deprecated Use 'PlayerTag.is_vanished'
        // @description
        // Deprecated in favor of <@link mechanism PlayerTag.is_vanished>.
        // -->

        // <--[mechanism]
        // @object PlayerTag
        // @name is_vanished
        // @input ElementTag(Boolean)
        // @plugin Depenizen, Essentials
        // @description
        // Sets whether the player has vanish enabled.
        // @tags
        // <PlayerTag.is_vanished>
        // -->
        PlayerTag.tagProcessor.registerMechanism("is_vanished", false, ElementTag.class, (player, mechanism, input) -> {
            if (mechanism.requireBoolean()) {
                getUser(player).setVanished(input.asBoolean());
            }
        }, "vanish");

        // <--[mechanism]
        // @object PlayerTag
        // @name essentials_ignore
        // @input PlayerTag(|ElementTag(Boolean))
        // @plugin Depenizen, Essentials
        // @description
        // Sets whether the player should ignore another player.
        // Optionally, specify a boolean indicate whether to ignore (defaults to true).
        // @tags
        // <PlayerTag.ignored_players>
        // -->
        PlayerTag.tagProcessor.registerMechanism("essentials_ignore", false, ElementTag.class, (player, mechanism, input) -> {
            ListTag split = mechanism.valueAsType(ListTag.class);
            PlayerTag otherPlayer = PlayerTag.valueOf(split.get(0), mechanism.context);
            boolean shouldIgnore = split.size() < 2 || new ElementTag(split.get(1)).asBoolean();
            getUser(player).setIgnoredPlayer(getUser(otherPlayer), shouldIgnore);
        });

    }

}
