package at.yedel.yedelmod.utils;



import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.polyfrost.oneconfig.api.event.v1.events.Event;

import java.util.ArrayList;
import java.util.List;



//@TODO implement this
public class NameLineEvent implements Event {
    private final Entity entity;
    private final boolean isPlayer;
    private final double distanceSquared;
    private final List<Component> nameLines = new ArrayList<>();
    private double verticalAdjustment = 0;

    public NameLineEvent(Entity entity, double distanceSquared) {
        this.entity = entity;
        this.isPlayer = entity instanceof Player;
        this.distanceSquared = distanceSquared;
    }

    public Entity getEntity() {
        return entity;
    }

    public boolean isPlayer() {
        return isPlayer;
    }

    public double getDistanceSquared() {
        return distanceSquared;
    }

    public List<Component> getNameLines() {
        return nameLines;
    }

    public void addNameLine(Component nameLine) {
        nameLines.add(nameLine);
    }

    public double getVerticalAdjustment() {
        return verticalAdjustment;
    }

    public void addVerticalAdjustment(double verticalAdjustment) {
        this.verticalAdjustment += verticalAdjustment;
    }
}