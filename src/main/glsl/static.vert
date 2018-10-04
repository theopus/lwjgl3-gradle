#version 400

layout (location=0) in vec3 position;
layout (location=1) in vec2 textureCoords;
layout (location=2) in vec3 normal;

out vec2 passTextureCoords;
out vec3 surfaceNormal;
out vec3 toLightVector;
out vec3 toCameraVector;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPosition;
uniform float useFakeLight;

const vec3 yNormal = vec3(0,1,0);

void main(void){
    vec4 worldPosition = transformationMatrix * vec4(position, 1.0);
    vec4 toCamPosition = viewMatrix * worldPosition;

    gl_Position = projectionMatrix * toCamPosition;

    vec3 acutalNormal = normal;

    if (useFakeLight > 0){
        acutalNormal = yNormal;
    }

    surfaceNormal = (transformationMatrix * vec4(acutalNormal, 0.0)).xyz;
    toLightVector = lightPosition - worldPosition.xyz;

    toCameraVector = (inverse(viewMatrix) * vec4(0.0,0.0,0.0,1.0)).xyz - worldPosition.xyz;

    passTextureCoords = textureCoords;

    float distance = length(toCamPosition.xyz);
}