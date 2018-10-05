struct Material {
    float hasTexture;
    float reflectivity;
    float shineDamper;
    float hasTransparency;
    float useFakeLight;
};

struct Fog {
    float enabled;
    vec3 color;
    float density;
    float gradient;
};

float getFogFactor(float distance, Fog fog){
    return clamp(1/pow(exp(distance * fog.density), fog.gradient), 0.0, 1.0);
}

float getSpecular(vec3 unitNormal, vec3 unitToLight, vec3 unitToCam, Material material){
    vec3 lightDirection = -unitToLight;
    vec3 reflectedDirection = reflect(lightDirection, unitNormal);

    float rDot = dot(reflectedDirection, unitToCam);
    float specularFactor = max(rDot, 0.0);

    float dampedFactor = pow(specularFactor, material.shineDamper);

    return dampedFactor * material.reflectivity;
}

vec4 getDiffuse(vec3 unitNormal, vec3 unitToLight, vec3 lightColor){
    float nDot = dot(unitNormal, unitToLight);
    float britghtness = max(nDot, 0.0);

    vec3 diffuse = britghtness * lightColor;
    return vec4(diffuse, 1.0);
}

vec4 applyFog(Fog fog, vec4 out_Color, float fogFactor){
    if (fog.enabled > 0) {
        return mix(vec4(fog.color, 1.0), out_Color, fogFactor);
    } else {
        return out_Color;
    }
}
