package committee.nova.avaritia_expand.client.shader;

import com.mojang.blaze3d.shaders.AbstractUniform ;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import committee.nova.avaritia_expand.AvaritiaExpand;
import net.minecraft.client.renderer.ShaderInstance;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;

public class AEShaders {
    public static AbstractUniform glowColor;
    public static AbstractUniform glowWidth;
    public static AbstractUniform glowTime;
    public static ShaderInstance GLOW_EDGE_SHADER;
    public static void onRegisterShaders(RegisterShadersEvent event){
        try {

            event.registerShader(new ShaderInstance(event.getResourceProvider(), AvaritiaExpand.rl("glow_edge"), DefaultVertexFormat.BLOCK), shader -> {
                GLOW_EDGE_SHADER = shader;
                glowColor = GLOW_EDGE_SHADER.safeGetUniform("glowColor");
                glowWidth = GLOW_EDGE_SHADER.safeGetUniform("glowWidth");
                glowTime = GLOW_EDGE_SHADER.safeGetUniform("time");

                GLOW_EDGE_SHADER.apply();
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
