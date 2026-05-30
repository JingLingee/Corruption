#version 150

uniform sampler2D DiffuseSampler;
uniform float GameTime;
uniform float Intensity; // 서버에서 받은 value 값 (0.0 ~ 1.0 예상)

in vec2 texCoord;
out vec4 fragColor;

float rand(vec2 co) {
    return fract(sin(dot(co.xy, vec2(12.9898, 78.233))) * 43758.5453);
}

void main() {

    if (Intensity < 0.001) {
            fragColor = texture(DiffuseSampler, texCoord);
            return;
        }

    vec2 uv = texCoord;
    float time = GameTime * 0.5;

    // --- 1. 화면 깨짐 (Glitch) 강도 조절 ---
    // Intensity가 높을수록 더 자주(확률 증가), 더 멀리(거리 증가) 깨집니다.
    float noise = rand(vec2(floor(uv.y * (20.0 / (Intensity + 0.1))), time));

    // Intensity가 0.5라면 0.95 이상의 확률로, 1.0이라면 0.9 이상의 확률로 깨짐
    float threshold = 1.0 - (Intensity * 0.1);
    if (noise > threshold) {
        uv.x += (rand(vec2(time, uv.y)) - 0.5) * Intensity * 0.2;
    }

    // --- 2. RGB 색상 분리 (Chromatic Aberration) 강도 조절 ---
    // Intensity에 비례해서 색상 분리 거리가 길어집니다.
    float shift = (0.01 + 0.01 * sin(time * 10.0)) * Intensity;

    float r = texture(DiffuseSampler, uv + vec2(-shift, shift)).r;
    float g = texture(DiffuseSampler, uv).g;
    float b = texture(DiffuseSampler, uv + vec2(shift, -shift)).b;

    vec3 color = vec3(r, g, b);

    // --- 3. 추가 노이즈 (Intensity가 높을수록 지직거림) ---
    float scanline = sin(uv.y * 800.0) * 0.04 * Intensity;
    color -= scanline;

    fragColor = vec4(color, 1.0);
}