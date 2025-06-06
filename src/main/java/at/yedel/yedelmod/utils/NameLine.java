package at.yedel.yedelmod.utils;



import com.google.common.base.Strings;
import net.minecraft.client.entity.AbstractClientPlayer;

import java.util.function.Predicate;



public class NameLine {
    private String text;
    private boolean showWhileSneaking = true;
    private boolean showWhileInvisible = false;
    private int maxDistance = 64;
    private final Predicate<AbstractClientPlayer> DEFAULT_SHOW_PREDICATE = (player) -> {
        if (Strings.isNullOrEmpty(text)) {
            return false;
        }
        if (!showWhileSneaking && player.isSneaking()) {
            return false;
        }
        if (!showWhileInvisible && player.isInvisible()) {
            return false;
        }
        // Max distance handled in the Render/RendererLivingEntity.renderLivingLabel method
        return true;
    };
    private Predicate<AbstractClientPlayer> shouldShow = DEFAULT_SHOW_PREDICATE;

    public NameLine text(String text) {
        this.text = text;
        return this;
    }

    public NameLine empty() {
        return text("");
    }

    public NameLine showWhileSneaking(boolean showWhileSneaking) {
        this.showWhileSneaking = showWhileSneaking;
        return this;
    }

    public NameLine showWhileSneaking() {
        return showWhileSneaking(true);
    }

    public NameLine hideWhileSneaking() {
        return showWhileSneaking(false);
    }

    public NameLine showWhileInvisible(boolean showWhileInvisible) {
        this.showWhileInvisible = showWhileInvisible;
        return this;
    }

    public NameLine showWhileInvisible() {
        return showWhileInvisible(true);
    }

    public NameLine hideWhileInvisible() {
        return showWhileInvisible(false);
    }

    public NameLine maxDistance(int maxDistance) {
        this.maxDistance = maxDistance;
        return this;
    }

    public NameLine shouldShow(Predicate<AbstractClientPlayer> shouldShow) {
        this.shouldShow = shouldShow;
        return this;
    }

    public String getText() {
        return text;
    }

    public int getMaxDistance() {
        return maxDistance;
    }

    public boolean shouldShow(AbstractClientPlayer player) {
        return shouldShow.test(player);
    }
}
