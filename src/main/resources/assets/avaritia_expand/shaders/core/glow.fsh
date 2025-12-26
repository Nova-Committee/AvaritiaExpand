#version 150

#define M_PI 3.1415926535897932384626433832795

#moj_import <fog.glsl>

uniform sampler2D Sampler0;

uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;

uniform float glowIntensity; // 发光强度
uniform vec3 glowColor;      // 发光颜色

in float vertexDistance;
in vec4 vertexColor;
in vec2 texCoord0;
in vec4 normal;
in vec3 fPos;

out vec4 fragColor;

void main() {
    // 获取原始纹理颜色
    vec4 textureColor = texture(Sampler0, texCoord0);

    // 计算基础颜色
    vec3 baseColor = textureColor.rgb * vertexColor.rgb;

    // 计算发光效果
    vec3 glow = glowColor * glowIntensity;

    // 组合基础颜色和发光效果
    vec3 finalColor = baseColor + glow;

    // 设置透明度
    float alpha = textureColor.a * vertexColor.a;

    // 创建发光效果 - 基于纹理的alpha值
    float edgeGlow = 1.0 - textureColor.a; // 使用反向alpha创建边缘发光
    vec3 edgeColor = glowColor * edgeGlow * glowIntensity * 2.0;

    finalColor = mix(finalColor, finalColor + edgeColor, edgeGlow);

    vec4 color = vec4(finalColor, alpha);

    fragColor = linear_fog(color * ColorModulator, vertexDistance, FogStart, FogEnd, FogColor);
}
