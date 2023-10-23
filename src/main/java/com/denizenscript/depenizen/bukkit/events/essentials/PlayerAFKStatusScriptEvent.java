package com.denizenscript.depenizen.bukkit.events.essentials;

import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import net.ess3.api.events.AfkStatusChangeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerAFKStatusScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // player goes afk
    // player returns from afk
    // player afk status changes
    //
    // @Regex ^on player (goes afk|returns from afk|afk status changes)$
    //
    // @Cancellable true
    //
    // @Triggers when a player's afk status changes.
    //
    // @Context
    // <context.status> Returns the player's afk status.
    //
    // @Plugin Depenizen, Essentials
    //
    // @Player Always.
    //
    // @Group Depenizen
    //
    // -->

    public AfkStatusChangeEvent event;

    public PlayerAFKStatusScriptEvent() {
        registerCouldMatcher("player goes afk");
        registerCouldMatcher("player returns from afk");
        registerCouldMatcher("player afk status changes");
    }

    @Override
    public boolean matches(ScriptPath path) {
        String status = path.eventArgLowerAt(1);
        if (status.equals("goes")) {
            if (!event.getValue()) {
                return false;
            }
        }
        else if (status.equals("returns")) {
            if (event.getValue()) {
                return false;
            }
        }
        else if (!status.equals("afk")) {
            return false;
        }
        return super.matches(path);
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(PlayerTag.mirrorBukkitPlayer(event.getAffected().getBase()), null);
    }

    @Override
    public ObjectTag getContext(String name) {
        return switch (name) {
            case "status" -> new ElementTag(event.getValue());
            default -> super.getContext(name);
        };
    }

    @EventHandler
    public void onPlayerAFKStatus(AfkStatusChangeEvent event) {
        this.event = event;
        fire(event);
    }
}
