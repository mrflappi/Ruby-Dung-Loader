package net.murfgames.rdloader.mod;

public class ModPrinter {

    private final String id;

    public ModPrinter(String id) {
        this.id = id;
    }

    public final void println(Object message) {
        System.out.printf("[%s] %s%n", id, message.toString());
    }

    public final void printerr(Object message, Exception... exceptions) {
        println(String.format("ERROR: %s", message.toString()));
        for (Exception e: exceptions)
            e.printStackTrace();
    }
}
