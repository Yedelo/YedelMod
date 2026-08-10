package at.yedel.yedelmod.utils;



import net.minecraft.client.entity.AbstractClientPlayer;

import java.util.ArrayList;
import java.util.List;



public class NameLineEvent {
    private final NameRenderingMethod method;
    private final AbstractClientPlayer player;
    private final double distanceSquared;
    private final List<String> nameLines = new ArrayList<>();
    private double verticalAdjustment = 0;

    public NameLineEvent(NameRenderingMethod method, AbstractClientPlayer player, double distanceSquared) {
        this.method = method;
        this.player = player;
        this.distanceSquared = distanceSquared;
    }

    public NameRenderingMethod getMethod() {
        return method;
    }

    public AbstractClientPlayer getPlayer() {
        return player;
    }

    public double getDistanceSquared() {
        return distanceSquared;
    }

    public List<String> getNameLines() {
        return nameLines;
    }

    public void addNameLine(String nameLine) {
        nameLines.add(nameLine);
    }

    public double getVerticalAdjustment() {
        return verticalAdjustment;
    }

    public void addVerticalAdjustment(double verticalAdjustment) {
        this.verticalAdjustment += verticalAdjustment;
    }

    public enum NameRenderingMethod {
        STANDARD,
        SNEAKING
    }
}
