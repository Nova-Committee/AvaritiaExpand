package committee.nova.avaritia_expand.client.shader;

import com.mojang.blaze3d.shaders.AbstractUniform ;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import committee.nova.avaritia_expand.AvaritiaExpand;
import net.minecraft.client.renderer.ShaderInstance;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;

public class AEShaders {
    public static ShaderInstance GLOW_SHADER;

    public static AbstractUniform glowIntensity;
    public static AbstractUniform glowColor;
    public static void onRegisterShaders(RegisterShadersEvent event){
        try {

            event.registerShader(new ShaderInstance(event.getResourceProvider(), AvaritiaExpand.rl("glow"), DefaultVertexFormat.BLOCK), shader -> {
                GLOW_SHADER = shader;
                glowIntensity = GLOW_SHADER.safeGetUniform("glowIntensity");
                glowColor = GLOW_SHADER.safeGetUniform("glowColor");
                GLOW_SHADER.apply();
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
