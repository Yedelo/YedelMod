package at.yedel.yedelmod.event.events;



import at.yedel.yedelmod.utils.NameLine;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.ArrayList;
import java.util.List;



public class NameLineEvent extends Event {
    private final List<NameLine> nameLines = new ArrayList<>();

    public List<NameLine> getNameLines() {
        return nameLines;
    }

    public NameLine newNameLine(String text) {
        NameLine nameLine = new NameLine().text(text);
        nameLines.add(nameLine);
        return nameLine;
    }
}
