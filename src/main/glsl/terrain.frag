#version 400

in vec2 passTextureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector;
in vec3 toCameraVector;

uniform sampler2D textureSampler;

out vec4 out_Colour;

uniform vec3 lightColor;
uniform float shineDamper;
uniform float reflectivity;

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

    float dampedFactor = pow(specularFactor, shineDamper);

    vec3 finalSpecularFactor = dampedFactor * reflectivity * lightColor;

    out_Colour = vec4(diffuse, 1.0) * texture(textureSampler, passTextureCoords) + vec4(finalSpecularFactor, 1.0);
}
