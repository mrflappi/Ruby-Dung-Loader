package net.murfgames.rdloader.mod.builtin.advanced;

import net.murfgames.rdloader.KeyboardEventHandler;
import net.murfgames.rdloader.agent.intercept.TileDestroyIntercept;
import net.murfgames.rdloader.agent.wrapper.PlayerWrapper;
import net.murfgames.rdloader.agent.wrapper.RubyDungWrapper;
import net.murfgames.rdloader.audio.AudioMaster;
import net.murfgames.rdloader.audio.PositionalAudioSource;
import net.murfgames.rdloader.entity.component.EntityComponent;
import net.murfgames.rdloader.gamemode.GameMode;
import net.murfgames.rdloader.level.CustomTile;
import net.murfgames.rdloader.level.LevelData;
import net.murfgames.rdloader.level.TileRegistry;
import net.murfgames.rdloader.mod.ModEntryPoint;
import net.murfgames.rdloader.mod.ModPrinter;
import net.murfgames.rdloader.util.AccumulativeMap;
import net.murfgames.rdloader.util.Identifier;
import net.murfgames.rdloader.util.event.TerminalEvent;

import java.io.IOException;
import java.util.Map;

public class AdvancedModEntryPoint implements ModEntryPoint {

    public static final String MOD_ID = "rd-loader-advanced";
    public static final ModPrinter PRINTER = new ModPrinter("Ruby Dung Advanced Features");

    public static final SurvivalGameMode SURVIVAL_GAMEMODE = new SurvivalGameMode(new Identifier(AdvancedModEntryPoint.MOD_ID, "survival"));
    public static final CreativeGameMode CREATIVE_GAME_MODE = new CreativeGameMode(new Identifier(AdvancedModEntryPoint.MOD_ID, "creative"));

    public static final CustomTile BEDROCK_TILE = new CustomTile(new Identifier(MOD_ID, "bedrock"), 17);

    private static final int PLAYER_INVENTORY_SIZE = 10;
    public static final AdvancedEntityInventory PLAYER_INVENTORY = new AdvancedEntityInventory(PLAYER_INVENTORY_SIZE);

    private static PlayerWrapper playerWrapper;

    @Override
    public void onInitialise() {
        registerTiles();
        registerGameModes();

        PlayerWrapper.INITIALISE_SIGNAL.connect(AdvancedModEntryPoint::onPlayerInitialise);
        PlayerWrapper.GET_COMPONENTS_SIGNAL.connect(AdvancedModEntryPoint::onPlayerGetComponents);
        LevelData.DATA_BUILD_SIGNAL.connect(AdvancedModEntryPoint::onLevelDataBuild);
        TileDestroyIntercept.DESTROY_SIGNAL.connect(AdvancedModEntryPoint::onTileDestroy);

        GameMode.setGameMode(CREATIVE_GAME_MODE);

        PRINTER.println("Advanced Features initialised");
    }

    @Override
    public void onTick() {
        // Tick
    }

    private static void registerTiles() {
        TileRegistry.registerTile(BEDROCK_TILE);
    }

    private static void registerGameModes() {
        GameMode.registerGameMode(SURVIVAL_GAMEMODE);
        GameMode.registerGameMode(CREATIVE_GAME_MODE);
    }

    private static void onPlayerInitialise(PlayerWrapper playerWrapper) {
        try {
            Map<Identifier, Object> levelData = RubyDungWrapper.getLevelWrapper().levelData.getData();

            float x = (float) levelData.getOrDefault(new Identifier(MOD_ID, "playerX"), playerWrapper.player.x);
            float y = (float) levelData.getOrDefault(new Identifier(MOD_ID, "playerY"), playerWrapper.player.y);
            float z = (float) levelData.getOrDefault(new Identifier(MOD_ID, "playerZ"), playerWrapper.player.z);
            float xRot = (float) levelData.getOrDefault(new Identifier(MOD_ID, "playerXRot"), playerWrapper.player.xRot);
            float yRot = (float) levelData.getOrDefault(new Identifier(MOD_ID, "playerYRot"), playerWrapper.player.yRot);

            playerWrapper.setPos(x, y, z);
            playerWrapper.player.xRot = xRot;
            playerWrapper.player.yRot = yRot;

            AdvancedModEntryPoint.playerWrapper = playerWrapper;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void onPlayerGetComponents(AccumulativeMap<Identifier, EntityComponent> map) {
        map.put(new Identifier(MOD_ID, "inventory"), PLAYER_INVENTORY);
    }

    private static void onLevelDataBuild(LevelData.Builder levelDataBuilder) {
        PRINTER.println("Saving player data");
        levelDataBuilder.registerData(new Identifier(MOD_ID, "playerX"), playerWrapper.player.x);
        levelDataBuilder.registerData(new Identifier(MOD_ID, "playerY"), playerWrapper.player.y);
        levelDataBuilder.registerData(new Identifier(MOD_ID, "playerZ"), playerWrapper.player.z);
        levelDataBuilder.registerData(new Identifier(MOD_ID, "playerXRot"), playerWrapper.player.xRot);
        levelDataBuilder.registerData(new Identifier(MOD_ID, "playerYRot"), playerWrapper.player.yRot);
    }

    private static void onTileDestroy(TileDestroyIntercept.DestroyData destroyData) {
        PlayerWrapper playerWrapper = AdvancedModEntryPoint.playerWrapper;
        float playerX = playerWrapper.player.x;
        float playerY = playerWrapper.player.y;
        float playerZ = playerWrapper.player.z;
        float playerYaw = playerWrapper.player.yRot;

        PositionalAudioSource source = new PositionalAudioSource();
        try {
            Identifier tileId = TileRegistry.convertId(destroyData.tile.id);

            int buffer = AudioMaster.loadAudio(new Identifier(tileId.namespace, String.format("audio/block/%s/destroy.wav", tileId.path)));
            source.playAt(buffer, (destroyData.x + 0.5f), (destroyData.y + 0.5f), (destroyData.z + 0.5f), playerX, playerY, playerZ, playerYaw);
        } catch (IOException e) {
            PRINTER.printerr("Failed to load audio", e);
        }
    }
}
