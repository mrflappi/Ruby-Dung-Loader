package net.murfgames.rdloader.entity.component;

import net.murfgames.rdloader.agent.wrapper.EntityWrapper;

public interface EntityComponent {
    void onInstanceCreated();
    void onInitialise(EntityWrapper entity);
    void onTick(); // So far unused
}
