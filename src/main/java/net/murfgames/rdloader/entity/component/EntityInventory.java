package net.murfgames.rdloader.entity.component;

import net.murfgames.rdloader.agent.wrapper.EntityWrapper;
import net.murfgames.rdloader.item.Inventory;
import net.murfgames.rdloader.util.Identifier;

public class EntityInventory extends Inventory implements EntityComponent {

    public EntityInventory() {
        super();
    }

    public EntityInventory(int size) {
        super(size);
    }

    @Override
    public void onInstanceCreated() {

    }

    @Override
    public void onInitialise(EntityWrapper entity) {

    }

    @Override
    public void onTick() {

    }
}
