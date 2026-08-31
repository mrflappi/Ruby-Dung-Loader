package net.murfgames.rdloader.mod.builtin.advanced;

import net.murfgames.rdloader.RubyDungLoader;
import net.murfgames.rdloader.entity.component.EntityInventory;
import net.murfgames.rdloader.level.TileRegistry;
import net.murfgames.rdloader.util.Identifier;

public class AdvancedEntityInventory extends EntityInventory {

    private static final boolean DEBUG = RubyDungLoader.DEBUG;

    public AdvancedEntityInventory(int size) {
        super(size);
    }

    @Override
    public void onInstanceCreated() {
        super.onInstanceCreated();

        if (DEBUG) {
            addItem(new Identifier("rock"));
            AdvancedModEntryPoint.PRINTER.println(toString());
        }
    }

    public int getItemTile(int itemIndex) {
        try {
            Identifier id = getItemId(itemIndex);
            return TileRegistry.convertId(id);
        } catch (Exception e) {
            AdvancedModEntryPoint.PRINTER.printerr("Error getting item tile " + itemIndex, e);
            return 0;
        }

    }
}
