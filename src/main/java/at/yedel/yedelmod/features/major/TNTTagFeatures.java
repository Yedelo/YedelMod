package at.yedel.yedelmod.features.major;



import at.yedel.yedelmod.config.YedelConfig;
import at.yedel.yedelmod.utils.Constants;
import at.yedel.yedelmod.utils.TextUtils;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.hypixel.modapi.HypixelModAPI;
import net.hypixel.modapi.packet.impl.clientbound.event.ClientboundLocationPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.InteractionResult;
import org.polyfrost.oneconfig.api.event.v1.events.ChatEvent;
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe;
import org.polyfrost.oneconfig.utils.v1.Multithreading;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class TNTTagFeatures {
    private static final TNTTagFeatures INSTANCE = new TNTTagFeatures();

    public static TNTTagFeatures getInstance() {
        return INSTANCE;
    }

    private static final String BOUNTY_HUNTING_LOGO = "§6§l- BountyHunting -";
    private static final Pattern YOU_TAGGED_PERSON_REGEX = Pattern.compile("You tagged (?<personThatYouTagged>[a-zA-Z0-9_]*)!");
    private static final Pattern PERSON_IS_IT_REGEX = Pattern.compile("(?<personThatIsIt>[a-zA-Z0-9_]*) is IT!");
    private static final Pattern PERSON_BLEW_UP_REGEX = Pattern.compile("(?<personThatBlewUp>[a-zA-Z0-9_]*) blew up!");

    private final ArrayList<String> players = new ArrayList<>();
    private final List<String> displayLines = new ArrayList<>();
    private String target;
    private boolean fightingTarget;
    private boolean dead;
    private String playerName;
    private boolean inTNTTag;

    private TNTTagFeatures() {
        HypixelModAPI.getInstance().registerHandler(ClientboundLocationPacket.class, this::handleLocationPacket);

        displayLines.add("§c§lBounty §f§lHunting");
        displayLines.add("§a" + YedelConfig.getInstance().bountyHuntingPoints + " points");
        displayLines.add("§a" + YedelConfig.getInstance().bountyHuntingKills + " kills");
        displayLines.add("");

        AttackEntityCallback.EVENT.register((player, level, hand, entity, result) -> {
            if (Objects.equals(entity.getName().toString(), target) && !dead) {
                fightingTarget = true;
            }
            return InteractionResult.PASS;
        });
    }

    private void handleLocationPacket(ClientboundLocationPacket packet) {
        // intended. reassign the variable and also check it
        if (inTNTTag = packet.getMode().isPresent() && packet.getMode().get().equals("TNTAG")) {
            onTNTTagJoin();
        }
    }

    public void onTNTTagJoin() {
        if (YedelConfig.getInstance().enabled && YedelConfig.getInstance().bountyHunting) {
            playerName = Minecraft.getInstance().getUser().getName();
            dead = false;
            target = null;
            displayLines.set(0, "§c§lBounty §f§lHunting");
            displayLines.set(1, "§a" + YedelConfig.getInstance().bountyHuntingPoints + " points");
            displayLines.set(2, "§a" + YedelConfig.getInstance().bountyHuntingKills + " kills");
            displayLines.set(3, "");
            if (YedelConfig.getInstance().firstTimeBountyHunting) {
                TextUtils.chat(BOUNTY_HUNTING_LOGO + " §eIf this is your first time using this mod and you're nicked, or you've changed your nick, you will have to set your currentNick with §n/setnick§r§3.");
                YedelConfig.getInstance().firstTimeBountyHunting = false;
                YedelConfig.getInstance().save();
            }
        }
    }

    @Subscribe
    public void handleRoundStarted(ChatEvent.Receive event) {
        if (inTNTTag && event.getFullyUnformattedMessage().endsWith("has started!")) {
            players.clear();
            for (AbstractClientPlayer player : Minecraft.getInstance().level.players()) {
                players.add(player.getName().toString());
            }
            players.remove(playerName);
            players.remove(YedelConfig.getInstance().currentNick);
            target = players.get((int) Math.floor(Math.random() * players.size()));
            if (YedelConfig.getInstance().enabled && YedelConfig.getInstance().bountyHunting && YedelConfig.getInstance().playHuntingSounds) {
                Constants.playPingSound(1, 0.8F);
            }
            displayLines.set(1, "§a" + YedelConfig.getInstance().bountyHuntingPoints + " points");
            displayLines.set(2, "§a" + YedelConfig.getInstance().bountyHuntingKills + " kills");
            displayLines.set(3, "§cYour next target is §f" + target + ".");
        }
    }

    @Subscribe
    public void handleFightMessages(ChatEvent.Receive event) {
        String msg = event.getFullyUnformattedMessage();
        Matcher tagOtherMatcher = YOU_TAGGED_PERSON_REGEX.matcher(msg);
        while (tagOtherMatcher.find()) {
            if (Objects.equals(tagOtherMatcher.group("personThatYouTagged"), target)) {
                fightingTarget = true;
            }
        }

        Matcher personIsItMatcher = PERSON_IS_IT_REGEX.matcher(msg);
        while (personIsItMatcher.find()) {
            if (Objects.equals(personIsItMatcher.group("personThatIsIt"), target) && !dead) {
                fightingTarget = false;
            }
        }
    }
    //@TODO actually render it
    //    @SubscribeEvent
    //    public void renderTargetLabel(RenderPlayerEvent.Pre event) {
    //        if (YedelConfig.getInstance().enabled && YedelConfig.getInstance().bountyHunting && YedelConfig.getInstance().highlightTargetAndShowDistance) {
    //            EntityPlayer targetPlayer = event.entityPlayer;
    //            EntityPlayerSP player = UPlayer.getPlayer();
    //            if (Objects.equals(targetPlayer.getName(), target) && !targetPlayer.isInvisible()) {
    //                String text = "§fDistance: " + (int) Math.floor(player.getDistanceToEntity(targetPlayer)) + " blocks";
    //                double sneakingInc = targetPlayer.isSneaking() ? -0.125 : 0;
    //                ((InvokerRender) event.renderer).yedelmod$renderLivingLabel(targetPlayer, text, event.x, event.y + 0.274 + sneakingInc, event.z, 64);
    //            }
    //        }
    //    }

    @Subscribe
    public void onBlastRadiusDeath(ChatEvent.Receive event) {
        if (event.getFullyUnformattedMessage().startsWith("You were blown up by")) {
            target = null;
            dead = true;
            displayLines.set(3, "");
        }
    }

    @Subscribe
    public void onRoundEnd(ChatEvent.Receive event) {
        String msg = event.getFullyUnformattedMessage();
        Matcher peopleDeathMatcher = PERSON_BLEW_UP_REGEX.matcher(msg);
        while (peopleDeathMatcher.find()) {
            String personDied = peopleDeathMatcher.group("personThatBlewUp");
            if (Objects.equals(personDied, playerName)) {
                dead = true;
                target = null;
                displayLines.set(3, "");
            }
            // now this is the part where i actually check for config
            // everything before this is for keeping state but this actually changes things
            // so i will check if mod and bounty hunting are enabled before going ahead and updating
            if (YedelConfig.getInstance().enabled && YedelConfig.getInstance().bountyHunting && Objects.equals(personDied, target) && fightingTarget) {
                Multithreading.schedule(() -> {
                    int pointIncrease = (int) Math.ceil(players.size() * 0.8);
                    if (dead) {
                        pointIncrease /= 2;
                    }
                    YedelConfig.getInstance().bountyHuntingPoints += pointIncrease;
                    YedelConfig.getInstance().bountyHuntingKills++;
                    YedelConfig.getInstance().save();
                    displayLines.set(1, "§a" + YedelConfig.getInstance().bountyHuntingPoints + " points (+" + pointIncrease + ")");
                    displayLines.set(2, "§a" + YedelConfig.getInstance().bountyHuntingKills + " kills (+1)");
                    displayLines.set(3, "§cYou killed your target!");
                    if (YedelConfig.getInstance().playHuntingSounds) {
                        Constants.playPingSound(1, 1.04F);
                    }
                }, 500, TimeUnit.MILLISECONDS);
            }
        }
    }

    @Subscribe
    public void onNickChange(ChatEvent.Receive event) {
        if (YedelConfig.getInstance().enabled && YedelConfig.getInstance().bountyHunting && Objects.equals(event.getFullyUnformattedMessage(), "Processing request. Please wait...")) {
            TextUtils.chat(BOUNTY_HUNTING_LOGO + " §ePlease set your nick with /setnick or in the config.");
        }
    }

    public List<String> getDisplayLines() {
        return displayLines;
    }

    public boolean isInTNTTag() {
        return inTNTTag;
    }
}
