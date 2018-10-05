#version 400
#include commons

in vec2 passTextureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector;
in vec3 toCameraVector;
in float fogFactor;

out vec4 out_Color;

uniform sampler2D textureSampler;
uniform vec3 lightColor;
uniform Material mat;
uniform Fog fog;

const vec4 whiteColor = vec4(1,1,1,1);
const float ambient = 0.2;

void main(void){
    vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitToLight = normalize(toLightVector);
    vec3 unitToCam = normalize(toCameraVector);

    vec4 diffuse = getDiffuse(unitNormal, unitToLight, lightColor);
    float specular = getSpecular(unitNormal, unitToLight, unitToCam, mat);

    vec4 textureColor;

    if (mat.hasTexture > 0){
        textureColor = texture(textureSampler, passTextureCoords);
            if (mat.hasTransparency > 0 && textureColor.a < 0.5){
                discard;
            }
    } else {
        textureColor = whiteColor;
    }

    out_Color = diffuse * textureColor +\
                specular * textureColor +\
                ambient * textureColor;

    out_Color = applyFog(fog, out_Color, fogFactor);
}
