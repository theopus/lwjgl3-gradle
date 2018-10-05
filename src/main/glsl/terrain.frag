#version 400
#include commons

in vec2 passTextureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector;
in vec3 toCameraVector;
in float fogFactor;

uniform sampler2D blendMapTexture;
uniform sampler2D bgTexture;
uniform sampler2D rTexture;
uniform sampler2D gTexture;
uniform sampler2D bTexture;

out vec4 out_Colour;

uniform vec3 lightColor;
uniform float shineDamper;
uniform float reflectivity;
uniform Fog fog;

void main(void){

    vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitToLight = normalize(toLightVector);

    vec2 tiledTexutreCoords = passTextureCoords * 40.0;

    vec4 mapColor = texture(blendMapTexture, passTextureCoords);
    vec4 bgColor = texture(rTexture, tiledTexutreCoords) * clamp((1 - (mapColor.r + mapColor.g + mapColor.b)), 0.0, 1.0);
    vec4 redTcolor = texture(bgTexture, tiledTexutreCoords) * mapColor.r;
    vec4 greenTcolor = texture(gTexture, tiledTexutreCoords) * mapColor.g;
    vec4 blueTcolor = texture(bTexture, tiledTexutreCoords) * mapColor.b;

    vec4 texColor = bgColor + redTcolor + greenTcolor + blueTcolor;

    float nDot = dot(unitNormal, unitToLight);
    float britghtness = max(nDot, 0.2);

    vec3 diffuse = britghtness * lightColor;

    out_Colour = vec4(diffuse, 1.0) * texColor;
    out_Colour = applyFog(fog, out_Colour, fogFactor);
}
