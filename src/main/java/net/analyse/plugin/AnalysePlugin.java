package net.analyse.plugin;

import gnu.trove.map.hash.TCustomHashMap;
import gnu.trove.strategy.IdentityHashingStrategy;
import net.analyse.plugin.commands.AnalyseCommand;
import net.analyse.plugin.listener.PlayerActivityListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class AnalysePlugin extends JavaPlugin {
    private final Map<UUID, Date> activeJoinMap = new TCustomHashMap<>(new IdentityHashingStrategy<>());
    private final Map<UUID, String> playerDomainMap = new TCustomHashMap<>(new IdentityHashingStrategy<>());

    private boolean setup;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        // TODO: Send an api call after to check the token, and disable.
        setup = getConfig().getString("server-token") != null && !getConfig().getString("server-token").isEmpty();

        getCommand("analyse").setExecutor(new AnalyseCommand(this));
        Bukkit.getPluginManager().registerEvents(new PlayerActivityListener(this), this);

        if(!setup) {
            getLogger().info("Hey! I'm not yet set-up, please run the following command:");
            getLogger().info("/analyse setup <server-token>");
        }
    }

    @Override
    public void onDisable() {
        activeJoinMap.clear();
    }

    public Map<UUID, Date> getActiveJoinMap() {
        return activeJoinMap;
    }

    public Map<UUID, String> getPlayerDomainMap() {
        return playerDomainMap;
    }

    public boolean isSetup() {
        return setup;
    }

    public void setSetup(boolean setup) {
        this.setup = setup;
    }

    public String parse(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }
}
