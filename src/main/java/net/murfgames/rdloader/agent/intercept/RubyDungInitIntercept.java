package net.murfgames.rdloader.agent.intercept;

import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.implementation.bind.annotation.This;
import net.murfgames.rdloader.RubyDungLoader;
import net.murfgames.rdloader.util.event.Event;
import net.murfgames.rdloader.util.event.ProtectedEvent;

import java.util.concurrent.Callable;

public class RubyDungInitIntercept {

    private static final Event INITIALISE = new Event();
    public static final ProtectedEvent INITIALISE_EVENT = new ProtectedEvent(INITIALISE);


    public static void intercept(@SuperCall Callable<Void> original, @This Object instance) {
        try {
            RubyDungLoader.init(instance, INITIALISE_EVENT);

            RubyDungLoader.PRINTER.println("Initialising RubyDung...");
            original.call();
            INITIALISE.emit();
            RubyDungLoader.PRINTER.println("Initialisation complete");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}