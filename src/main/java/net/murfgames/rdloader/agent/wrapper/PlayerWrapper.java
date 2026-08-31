package net.murfgames.rdloader.agent.wrapper;

import com.mojang.minecraft.Player;
import net.murfgames.rdloader.entity.component.EntityComponent;
import net.murfgames.rdloader.util.AccumulativeMap;
import net.murfgames.rdloader.util.Identifier;
import net.murfgames.rdloader.util.event.ProtectedSignal;
import net.murfgames.rdloader.util.event.Signal;

import java.util.Map;

public final class PlayerWrapper extends EntityWrapper {

    public final Player player;

    private static final Signal<PlayerWrapper> INITIALISE = new Signal<>();
    public static final ProtectedSignal<PlayerWrapper> INITIALISE_SIGNAL = new ProtectedSignal<>(INITIALISE);

    private static final Signal<AccumulativeMap<Identifier, EntityComponent>> GET_COMPONENTS = new Signal<>();
    public static final ProtectedSignal<AccumulativeMap<Identifier, EntityComponent>> GET_COMPONENTS_SIGNAL = new ProtectedSignal<>(GET_COMPONENTS);


    private static final int INVENTORY_SIZE = 10;

    static {
        try {

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public PlayerWrapper(Object instance) {
        super(instance);
        player = (Player) instance;
    }

    @Override
    protected void onInitialise() {
        super.onInitialise();
        INITIALISE.emit(this);
    }

    @Override
    protected Map<Identifier, EntityComponent> registerComponents() {
        AccumulativeMap<Identifier, EntityComponent> map = new AccumulativeMap<>();
        GET_COMPONENTS.emit(map);
        return map;
    }
}
