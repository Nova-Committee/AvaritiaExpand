package committee.nova.avaritia_expand.client.model.geo;

import committee.nova.avaritia_expand.AvaritiaExpand;
import committee.nova.avaritia_expand.common.item.tool.infinity.InfinityBookItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class InfinityBookModel extends GeoModel<InfinityBookItem> {


    @Override
    public ResourceLocation getModelResource(InfinityBookItem animatable) {
        return AvaritiaExpand.rl("geo/item/infinity_book.geo.json");
    }

    @Override
    public ResourceLocation getAnimationResource(InfinityBookItem animatable) {
        return AvaritiaExpand.rl("animations/infinity_book.animation.json");
    }

    @Override
    public ResourceLocation getTextureResource(InfinityBookItem animatable) {
        return AvaritiaExpand.rl("textures/item/tools/infinity_book/layer_1.png");
    }
}
