package committee.nova.avaritia_expand.client.shader;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import committee.nova.avaritia_expand.AvaritiaExpand;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;

import static net.minecraft.client.renderer.RenderStateShard.*;


public class AERenderTypes {

    public static RenderType GLOW_SHADER_TYPE = RenderType.create(
            AvaritiaExpand.rl("glow").toString(),
            DefaultVertexFormat.NEW_ENTITY,
            VertexFormat.Mode.QUADS, 256, true, true,
            RenderType.CompositeState.builder()
                    .setShaderState(new RenderStateShard.ShaderStateShard(() -> AEShaders.GLOW_SHADER))
                    .setTextureState(BLOCK_SHEET_MIPPED)
                    .setTransparencyState(RenderStateShard.LIGHTNING_TRANSPARENCY)
                    .setCullState(NO_CULL)
                    .setLightmapState(LIGHTMAP)
                    .setWriteMaskState(COLOR_WRITE)
                    .createCompositeState(true)
    );

}
