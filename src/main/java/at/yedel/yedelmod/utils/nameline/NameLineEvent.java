package at.yedel.yedelmod.utils.nameline;



import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.ArrayList;
import java.util.List;



public class NameLineEvent extends Event {
    private final Entity entity;
    private final List<NameLine> nameLines = new ArrayList<>();

    public NameLineEvent(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

    public NameLine newNameLine(String text) {
        NameLine nameLine = new NameLine().text(text);
        nameLines.add(nameLine);
        return nameLine;
    }

    public NameLine newNameLine() {
        NameLine nameLine = new NameLine();
        nameLines.add(nameLine);
        return nameLine;
    }

    public List<NameLine> getNameLines() {
        return nameLines;
    }
}
