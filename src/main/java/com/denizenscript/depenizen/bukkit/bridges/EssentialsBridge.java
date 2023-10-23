package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizencore.events.ScriptEvent;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.tags.PseudoObjectTagBase;
import com.denizenscript.denizencore.tags.TagManager;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.depenizen.bukkit.events.essentials.*;
import com.denizenscript.depenizen.bukkit.properties.essentials.EssentialsItemExtensions;
import com.denizenscript.depenizen.bukkit.properties.essentials.EssentialsPlayerExtensions;
import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.commands.WarpNotFoundException;
import net.ess3.api.InvalidWorldException;

public class EssentialsBridge extends Bridge {

    static class EssentialsTagBase extends PseudoObjectTagBase<EssentialsTagBase> {

        public static EssentialsTagBase instance;

        public EssentialsTagBase() {
            instance = this;
            TagManager.registerStaticTagBaseHandler(EssentialsTagBase.class, "essentials", (t) -> instance);
        }

        public void register() {

            // <--[tag]
            // @attribute <essentials.warp[<warp_name>]>
            // @returns LocationTag
            // @plugin Depenizen, Essentials
            // @description
            // Returns the location of the warp name.
            // -->
            tagProcessor.registerTag(LocationTag.class, ElementTag.class, "warp", (object, attribute, name) -> {
                try {
                    return new LocationTag(essentialsInstance.getWarps().getWarp(name.toString()));
                } catch (WarpNotFoundException e) {
                    Debug.echoError("Warp not found");
                } catch (InvalidWorldException e) {
                    Debug.echoError("Invalid world for getting warp");
                }
                return null;
            });

            // <--[tag]
            // @attribute <essentials.list_warps>
            // @returns ListTag
            // @plugin Depenizen, Essentials
            // @description
            // Returns a list of all Warp names.
            // -->
            tagProcessor.registerTag(ListTag.class, "list_warps", (object, attribute) -> {
                ListTag list = new ListTag();
                list.addAll(essentialsInstance.getWarps().getList());
                return list;
            });

        }

    }

    public static Essentials essentialsInstance;

    @Override
    public void init() {
        essentialsInstance = (Essentials) plugin;
        ScriptEvent.registerScriptEvent(PlayerAFKStatusScriptEvent.class);
        ScriptEvent.registerScriptEvent(PlayerGodModeStatusScriptEvent.class);
        ScriptEvent.registerScriptEvent(PlayerJailStatusScriptEvent.class);
        ScriptEvent.registerScriptEvent(PlayerMuteStatusScriptEvent.class);
        ScriptEvent.registerScriptEvent(PlayerBalanceChangeScriptEvent.class);
        EssentialsItemExtensions.register();
        EssentialsPlayerExtensions.register();
        new EssentialsTagBase();
    }

}
