#version 400
#include commons

in vec2 passTextureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector;
in vec3 toCameraVector;

out vec4 out_Color;

uniform sampler2D textureSampler;
uniform vec3 lightColor;
uniform Material material;

const vec4 whiteColor = vec4(1,1,1,1);

void main(void){

    vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitToLight = normalize(toLightVector);
    float nDot = dot(unitNormal, unitToLight);
    float britghtness = max(nDot, 0.2);

    vec3 diffuse = britghtness * lightColor;

    vec3 unitVectorToCamera = normalize(toCameraVector);
    vec3 lightDirection = -unitToLight;
    vec3 reflectedDirection = reflect(lightDirection, unitNormal);

    float rDot = dot(reflectedDirection, unitVectorToCamera);
    float specularFactor = max(rDot, 0.0);

    float dampedFactor = pow(specularFactor, material.shineDamper);

    float finalSpecularFactor = dampedFactor * material.reflectivity;

    if (material.hasTexture > 0){
        vec4 textureColor = texture(textureSampler, passTextureCoords);

            if (material.hasTransparency > 0 && textureColor.a < 0.5){
                discard;
            }

            out_Color = vec4(diffuse, 1.0) *  textureColor + finalSpecularFactor * textureColor;
    } else {
        out_Color = vec4(diffuse, 1.0) * whiteColor + finalSpecularFactor * whiteColor;
    }

}
