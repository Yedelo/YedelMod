package at.yedel.yedelmod.features.major;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.events.JoinGamePacketEvent;
import at.yedel.yedelmod.events.RenderScoreEvent;
import at.yedel.yedelmod.mixins.net.minecraft.client.renderer.entity.InvokerRender;
import at.yedel.yedelmod.utils.KillMessages;
import at.yedel.yedelmod.utils.ScoreboardName;
import at.yedel.yedelmod.utils.typeutils.NumberUtils;
import com.google.common.collect.Maps;
import gg.essential.api.utils.Multithreading;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;



public class StrengthIndicators {
    public static StrengthIndicators instance = new StrengthIndicators();
    private final Map<String, Double> strengthPlayers = Maps.newHashMap();
    private final ArrayList<String> startStrengthPlayers = new ArrayList<>();
    private final ArrayList<String> endStrengthPlayers = new ArrayList<>();

    private final Map<Integer, String> colorMap = new HashMap<>(); // Config array values -> color codes

    public StrengthIndicators() {
        colorMap.put(0, "§4");
        colorMap.put(1, "§c");
        colorMap.put(2, "§6");
        colorMap.put(3, "§e");
        colorMap.put(4, "§2");
        colorMap.put(5, "§a");
        colorMap.put(6, "§b");
        colorMap.put(7, "§3");
        colorMap.put(8, "§1");
        colorMap.put(9, "§9");
        colorMap.put(10, "§d");
        colorMap.put(11, "§5");
        colorMap.put(12, "§f");
        colorMap.put(13, "§7");
        colorMap.put(14, "§8");
        colorMap.put(15, "§0");
        Multithreading.INSTANCE.schedule(() -> {
            if (!YedelConfig.strengthIndicators || !ScoreboardName.inSkywars) return;
            for (Map.Entry<String, Double> entry: strengthPlayers.entrySet()) {
                String player = entry.getKey();
                Double seconds = entry.getValue();
                strengthPlayers.put(player, NumberUtils.round(seconds - 0.1, 1));
                if (seconds == 0.5) {
                    startStrengthPlayers.remove(player);
                    endStrengthPlayers.add(player);
                }
                else if (seconds == 0) {
                    startStrengthPlayers.remove(player);
                    endStrengthPlayers.remove(player);
                }
            }
        }, 0, 100, TimeUnit.MILLISECONDS);
    }

    @SubscribeEvent
    public void onServerChange(JoinGamePacketEvent event) {
        strengthPlayers.clear();
        startStrengthPlayers.clear();
        endStrengthPlayers.clear();
    }

    @SubscribeEvent
    public void onKillMessage(ClientChatReceivedEvent event) {
        if (!YedelConfig.strengthIndicators || !ScoreboardName.inSkywars) return;
        String message = event.message.getUnformattedText();
        for (String killMessage: KillMessages.killMessages) {
            Matcher messageMatcher = Pattern.compile(killMessage).matcher(message);
            if (messageMatcher.find()) {
                triggerKill(messageMatcher.group("killed"), messageMatcher.group("killer"));
            }
        }
    }

    public void triggerKill(String killed, String killer) {
        strengthPlayers.put(killer, NumberUtils.round(5.5, 1));
        if (!startStrengthPlayers.contains(killer)) startStrengthPlayers.add(killer);
        endStrengthPlayers.remove(killer);

        strengthPlayers.put(killed, (double) 0);
        startStrengthPlayers.remove(killed);
        endStrengthPlayers.remove(killed);
    }

    @SubscribeEvent
    public void onRenderStrengthPlayer_HealthTag(RenderScoreEvent event) {
        if (!YedelConfig.strengthIndicators || !ScoreboardName.inSkywars) return;
        EntityPlayer entityPlayer = event.entity;
        String entityName = entityPlayer.getName();
        boolean inStart = startStrengthPlayers.contains(entityName);
        RenderPlayer renderer = event.renderer;
        if (!inStart && !endStrengthPlayers.contains(entityName)) return;
        String color = inStart ? colorMap.get(YedelConfig.startStrengthColor) : colorMap.get(YedelConfig.endStrengthColor);
        String text = color + "Strength - " + strengthPlayers.get(entityName) + "s";
        ((InvokerRender) renderer).yedelmod$invokeRenderLabel(entityPlayer, text, event.x, event.y + 0.55, event.z, 64);
    }
}
