package at.yedel.yedelmod.utils;



import com.google.common.base.Strings;
import net.minecraft.entity.Entity;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;



public class NameLine {
    private String text;
    private boolean showWhileSneaking = true;
    private boolean showWhileInvisible = false;
    private int maxDistance = 64;
    private final Predicate<Entity> DEFAULT_PREDICATE = (entity) -> {
        if (Strings.isNullOrEmpty(text)) {
            return false;
        }
        if (!showWhileSneaking && entity.isSneaking()) {
            return false;
        }
        if (!showWhileInvisible && entity.isInvisible()) {
            return false;
        }
        // Max distance handled in the Render/RendererLivingEntity.renderLivingLabel method
        return true;
    };
    private List<Predicate<Entity>> predicates = Collections.singletonList(DEFAULT_PREDICATE);

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

    public NameLine addPredicate(Predicate<Entity> predicate) {
        predicates.add(predicate);
        return this;
    }

    public NameLine predicate(Predicate<Entity> predicate) {
        predicates = Collections.singletonList(predicate);
        return this;
    }

    public String getText() {
        return text;
    }

    public int getMaxDistance() {
        return maxDistance;
    }

    public List<Predicate<Entity>> getPredicates() {
        return predicates;
    }

    public boolean shouldShow(Entity entity) {
        return predicates.stream().allMatch((predicate) -> predicate.test(entity));
    }
}
