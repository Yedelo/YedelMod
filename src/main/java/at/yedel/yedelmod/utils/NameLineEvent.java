package at.yedel.yedelmod.utils;



//? if legacy {
//import net.minecraft.client.entity.AbstractClientPlayer;
//?} else {
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
//?}
//? if v1
import org.polyfrost.oneconfig.api.event.v1.events.Event;

import java.util.ArrayList;
import java.util.List;


//~ if modern 'String' -> 'Component' {
public class NameLineEvent /*? if v1 {*/implements Event/*?}*/ {
    //? if legacy {
    /*private final NameRenderingMethod method;
    private final AbstractClientPlayer player;
    *///?} else {
    private final Entity entity;
    private final boolean isPlayer;
    //?}
    private final double distanceSquared;
    private final List<Component> nameLines = new ArrayList<>();
    private double verticalAdjustment = 0;

    //? if forge {
    /*public NameLineEvent(NameRenderingMethod method, AbstractClientPlayer player, double distanceSquared) {
        this.method = method;
        this.player = player;
        this.distanceSquared = distanceSquared;
    }
    *///?} else {

    public NameLineEvent(Entity entity, double distanceSquared) {
        this.entity = entity;
        this.isPlayer = entity instanceof Player;
        this.distanceSquared = distanceSquared;
    }
    //?}

    //? if legacy {
    /*public NameRenderingMethod getMethod() {
        return method;
    }

    public AbstractClientPlayer getPlayer() {
        return player;
    }
    *///?} else {
    
    public Entity getEntity() {
        return entity;
    }

    public boolean isPlayer() {
        return isPlayer;
    }
    //?}

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

    //? if forge {
    /*public enum NameRenderingMethod {
        STANDARD,
        SNEAKING
    }
    *///?}
}
//~}