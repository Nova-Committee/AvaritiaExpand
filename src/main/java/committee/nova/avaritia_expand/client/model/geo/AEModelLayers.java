package committee.nova.avaritia_expand.client.model.geo;

import committee.nova.avaritia_expand.AvaritiaExpand;
import net.minecraft.client.model.geom.ModelLayerLocation;

public class AEModelLayers {

    public static final ModelLayerLocation EXTREME_WITHER =
            new ModelLayerLocation(
                    AvaritiaExpand.rl("extreme_wither"),
                    "main"
            );

    public static final ModelLayerLocation EXTREME_WITHER_ARMOR =
            new ModelLayerLocation(
                    AvaritiaExpand.rl("extreme_wither"),
                    "armor"
            );
}
