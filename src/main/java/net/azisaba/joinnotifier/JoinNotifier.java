package net.azisaba.joinnotifier;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class JoinNotifier extends JavaPlugin implements Listener {
    private final List<UUID> notifyList = new ArrayList<>();

    public static JoinNotifier getInstance() {
        return JavaPlugin.getPlugin(JoinNotifier.class);
    }

    @Override
    public void onEnable() {
        for (String s : getConfig().getStringList("notifyList")) {
            try {
                notifyList.add(UUID.fromString(s));
            } catch (IllegalArgumentException ex) {
                getLogger().warning("Invalid UUID while reading notifyList: " + s);
            }
        }
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (notifyList.contains(e.getPlayer().getUniqueId())) {
            WebhookUtil.sendDiscordWebhook(
                    "webhookURL",
                    getConfig().getString("webhookUsername"),
                    getConfig().getString("webhookContentPrefix", "") + e.getPlayer().getName() + "が戦場に参戦！");
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        if (notifyList.contains(e.getPlayer().getUniqueId())) {
            WebhookUtil.sendDiscordWebhook(
                    "webhookURL",
                    getConfig().getString("webhookUsername"),
                    getConfig().getString("webhookContentPrefix", "") + e.getPlayer().getName() + "が戦場から脱走！");
        }
    }
}
