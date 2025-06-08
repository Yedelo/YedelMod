package at.yedel.yedelmod.utils.nameline;



import at.yedel.yedelmod.mixins.InvokerRender;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.MinecraftForge;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;



public class NameLineImpl {
    private static final Map<Entity, Integer> labelCountMap = new HashMap<>();

    public static void incrementRenderedLabels(Entity entity) {
        int currentHeight = labelCountMap.getOrDefault(entity, 0);
        labelCountMap.put(entity, currentHeight + 1);
    }

    public static void renderNameLines(Entity entity, double x, double y, double z, Render<? extends Entity> entityRenderer) {
        NameLineEvent event = new NameLineEvent(entity);
        MinecraftForge.EVENT_BUS.post(event);
        List<NameLine> nameLines = event.getNameLines().stream().filter((nameLine) -> nameLine.shouldShow(entity)).collect(Collectors.toList());
        Collections.reverse(nameLines);
        if (labelCountMap.containsKey(entity)) {
            y += (labelCountMap.get(entity) - 1) * 0.276D;
        }
        for (NameLine nameLine : nameLines) {
            y += 0.276D;
            ((InvokerRender) entityRenderer).yedelmod$invokeRenderLabel(entity, nameLine.getText(), x, y, z, nameLine.getMaxDistance());
        }
        labelCountMap.clear();
    }
}
