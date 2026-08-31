package net.murfgames.rdloader.agent.wrapper;

import com.mojang.minecraft.Entity;
import net.murfgames.rdloader.RubyDungLoader;
import net.murfgames.rdloader.entity.component.EntityComponent;
import net.murfgames.rdloader.util.Identifier;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class EntityWrapper {
    public final Entity entity;
    public final Map<Identifier, EntityComponent> components;

    private static final Field x;
    private static final Field y;
    private static final Field z;

    private static final Method setPos;

    static {
        try {
            x = Entity.class.getDeclaredField("x");
            x.setAccessible(true);

            y = Entity.class.getDeclaredField("y");
            y.setAccessible(true);

            z = Entity.class.getDeclaredField("z");
            z.setAccessible(true);

            setPos = Entity.class.getDeclaredMethod("setPos", float.class, float.class, float.class);
            setPos.setAccessible(true);

        } catch (NoSuchFieldException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public EntityWrapper(Object instance) {
        this.entity = (Entity) instance;
        components = Collections.unmodifiableMap(registerComponents());
        RubyDungLoader.INITIALISE_EVENT.connect(this::onInitialise);
        onInstanceCreated();
    }

    protected void onInitialise() {
        components.forEach((id, component) -> {
            if (component != null)
                component.onInitialise(this);
        });
    }

    protected void onInstanceCreated() {
        components.forEach((id, component) -> {
            if (component != null)
                component.onInstanceCreated();
        });
    }

    protected Map<Identifier, EntityComponent> registerComponents() {
        return new HashMap<>();
    }

    public float getX() throws IllegalAccessException {
        return x.getFloat(entity);
    }

    public float getY() throws IllegalAccessException {
        return y.getFloat(entity);
    }

    public float getZ() throws IllegalAccessException {
        return z.getFloat(entity);
    }

    public void setPos(float x, float y, float z) throws InvocationTargetException, IllegalAccessException {
        setPos.invoke(entity, x, y, z);
    }
}
