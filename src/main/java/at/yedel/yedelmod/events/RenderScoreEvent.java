package at.yedel.yedelmod.events;



import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraftforge.fml.common.eventhandler.Event;



public class RenderScoreEvent extends Event {
    private final RenderPlayer renderPlayer;

    public RenderPlayer getRenderPlayer() {
        return renderPlayer;
    }

    private final AbstractClientPlayer player;

    public AbstractClientPlayer getPlayer() {
        return player;
    }

    private final double x;

    public double getX() {
        return x;
    }

    private final double y;

    public double getY() {
        return y;
    }

    private final double z;

    public double getZ() {
        return z;
    }

    private final String string;

    public String getString() {
        return string;
    }

    private final float increment;

    public float getIncrement() {
        return increment;
    }

    private final double distanceSqToEntity;

    public double getDistanceSqToEntity() {
        return distanceSqToEntity;
    }

    public RenderScoreEvent(RenderPlayer renderPlayer, AbstractClientPlayer player, double x, double y, double z, String string, float increment, double distanceSqToEntity) {
        this.renderPlayer = renderPlayer;
        this.player = player;
        this.x = x;
        this.y = y;
        this.z = z;
        this.string = string;
        this.increment = increment;
        this.distanceSqToEntity = distanceSqToEntity;
    }
}
