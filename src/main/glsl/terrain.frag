#version 400
#include commons

in vec2 passTextureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector;
in vec3 toCameraVector;
in float fogFactor;

uniform sampler2D textureSampler;

out vec4 out_Colour;

uniform vec3 lightColor;
uniform float shineDamper;
uniform float reflectivity;
uniform Fog fog;

void main(void){

    vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitToLight = normalize(toLightVector);

    float nDot = dot(unitNormal, unitToLight);
    float britghtness = max(nDot, 0.2);

    vec3 diffuse = britghtness * lightColor;

    out_Colour = vec4(diffuse, 1.0) * texture(textureSampler, passTextureCoords);
    out_Colour = applyFog(fog, out_Colour, fogFactor);
}
