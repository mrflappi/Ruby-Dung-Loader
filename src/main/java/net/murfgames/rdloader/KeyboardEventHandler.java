package net.murfgames.rdloader;

import com.mojang.minecraft.character.Zombie;
import net.murfgames.rdloader.agent.wrapper.RubyDungWrapper;
import net.murfgames.rdloader.util.event.ProtectedTerminalSignal;
import net.murfgames.rdloader.util.event.TerminalSignal;
import org.lwjgl.input.Keyboard;

public abstract class KeyboardEventHandler {
    private static final TerminalSignal<Integer> EVENT_KEY = new TerminalSignal<>();
    public static final ProtectedTerminalSignal<Integer> EVENT_KEY_SIGNAL = new ProtectedTerminalSignal<>(EVENT_KEY);

    public static void onTick() {
        while (Keyboard.next()) {
            if (Keyboard.getEventKeyState()) {
                try {
                    int key = Keyboard.getEventKey();
                    if (EVENT_KEY.emit(key)) return;
                    defaultKeyControl(key);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private static void defaultKeyControl(int key) throws IllegalAccessException {
        switch (key) {
            case Keyboard.KEY_RETURN: {
                RubyDungWrapper.getLevelWrapper().level.save();
                break;
            }
            case Keyboard.KEY_1: {
                setPaintTexture(1);
                break;
            }
            case Keyboard.KEY_2: {
                setPaintTexture(3);
                break;
            }
            case Keyboard.KEY_3: {
                setPaintTexture(4);
                break;
            }
            case Keyboard.KEY_4: {
                setPaintTexture(5);
                break;
            }
            case Keyboard.KEY_6: {
                setPaintTexture(6);
                break;
            }
            case Keyboard.KEY_G: {
                RubyDungWrapper.getZombies().add(new Zombie(
                        RubyDungWrapper.getLevelWrapper().level,
                        RubyDungWrapper.getPlayerWrapper().player.x,
                        RubyDungWrapper.getPlayerWrapper().player.y,
                        RubyDungWrapper.getPlayerWrapper().player.z));
            }
        }
    }

    private static void setPaintTexture(int i) {
        try {
            RubyDungWrapper.setPaintTexture(i);
        } catch (IllegalAccessException ignored) {}
    }
}
