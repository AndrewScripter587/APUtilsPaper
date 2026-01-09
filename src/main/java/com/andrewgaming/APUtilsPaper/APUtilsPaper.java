package com.andrewgaming.APUtilsPaper;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

public final class APUtilsPaper extends JavaPlugin {
    static Server GAME_SERVER;
    @Override
    public void onEnable() {
        // Plugin startup logic
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS,
                commands -> commands.registrar().register(SetupCommands.registerCommands()
        ));

        GAME_SERVER = this.getServer();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public void onLoad() {
        super.onLoad();
    }
}
