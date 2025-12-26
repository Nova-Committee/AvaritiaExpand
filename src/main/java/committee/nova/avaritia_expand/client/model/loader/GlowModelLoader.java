package committee.nova.avaritia_expand.client.model.loader;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import committee.nova.avaritia_expand.client.model.GlowBakeModel;
import committee.nova.mods.avaritia.client.model.loader.base.BaseGeometry;
import committee.nova.mods.avaritia.client.model.loader.base.BaseModelLoader;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;

import java.util.function.Function;

public class GlowModelLoader extends BaseModelLoader<GlowModelLoader.GlowGeometry> {
    public static final GlowModelLoader INSTANCE = new GlowModelLoader();

    @Override
    public GlowGeometry read(JsonObject modelContents, JsonDeserializationContext deserializationContext) {
        BlockModel baseModel = deserializationContext.deserialize(modelContents, BlockModel.class);
        return new GlowGeometry(baseModel);
    }

    public static class GlowGeometry extends BaseGeometry<GlowGeometry> {
        public GlowGeometry(BlockModel baseModel) {
            super(baseModel);
        }

        @Override
        public BakedModel bake(IGeometryBakingContext context, ModelBaker baker,
                               Function<Material, TextureAtlasSprite> spriteGetter,
                               ModelState modelState, ItemOverrides overrides) {
            BakedModel baseBakedModel = this.baseModel.bake(baker, this.baseModel, spriteGetter, modelState, true);
            return new GlowBakeModel(baseBakedModel);
        }
    }
}
