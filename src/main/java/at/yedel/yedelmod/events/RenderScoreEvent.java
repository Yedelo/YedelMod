package at.yedel.yedelmod.events;



import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraftforge.fml.common.eventhandler.Event;



public class RenderScoreEvent extends Event {
    public RenderPlayer renderer;
    public AbstractClientPlayer entity;
    public double x;
    public double y;
    public double z;
    public String string;
    public float increment;
    public double distanceSqToEntity;

    public RenderScoreEvent(RenderPlayer renderer, AbstractClientPlayer entityIn, double x, double y, double z, String str, float p1770699, double p17706910) {
        this.renderer = renderer;
        this.entity = entityIn;
        this.x = x;
        this.y = y;
        this.z = z;
        this.string = str;
        this.increment = p1770699;
        this.distanceSqToEntity = p17706910;
    }
}
