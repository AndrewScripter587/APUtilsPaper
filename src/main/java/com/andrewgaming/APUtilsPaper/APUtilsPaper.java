package com.andrewgaming.APUtilsPaper;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;

public final class APUtilsPaper extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS,
                commands -> commands.registrar().register(SetupCommands.registerCommands()
        ));

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
