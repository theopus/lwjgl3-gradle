#version 330

in vec2 passTextureCoords;

out vec4 color;


uniform sampler2D sampler;

void main(){
    color = texture(sampler, passTextureCoords);
}