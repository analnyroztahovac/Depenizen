package net.gnomeffinway.depenizen.extensions.worldedit;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import net.aufdemrand.denizen.objects.Element;
import net.aufdemrand.denizen.objects.dCuboid;
import net.aufdemrand.denizen.objects.dObject;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.tags.Attribute;
import net.gnomeffinway.depenizen.extensions.dObjectExtension;
import net.gnomeffinway.depenizen.support.Supported;
import org.bukkit.entity.Player;

public class WorldEditPlayerExtension extends dObjectExtension {

    public static boolean describes(dObject pl) {
        return pl instanceof dPlayer
                && ((dPlayer) pl).isOnline();
    }

    public static WorldEditPlayerExtension getFrom(dObject pl) {
        if (!describes(pl)) return null;
        else return new WorldEditPlayerExtension((dPlayer) pl);
    }

    private WorldEditPlayerExtension(dPlayer pl) {
        player = pl.getPlayerEntity();
    }

    Player player = null;

    @Override
    public String getAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <p@player.selected_region>
        // @returns dCuboid
        // @description
        // Returns the player's current region selection, as a dCuboid.
        // @Plugin WorldEdit
        // -->
        if (attribute.startsWith("selected_region")) {
            WorldEditPlugin worldEdit = Supported.get("WORLDEDIT").getPlugin();
            Selection selection = worldEdit.getSelection(player);
            if (selection != null)
                return new dCuboid(selection.getMinimumPoint(), selection.getMaximumPoint()).getAttribute(attribute.fulfill(1));
            else
                return Element.NULL.getAttribute(attribute.fulfill(1));
        }

        return null;

    }

}