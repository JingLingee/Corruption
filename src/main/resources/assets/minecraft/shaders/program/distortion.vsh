#version 150

in vec4 Position;
in vec2 UV0;

out vec2 texCoord;

void main() {
    // 1. 화면을 꽉 채우는 설정 (성공했던 코드 유지)
    gl_Position = vec4(Position.xy * 2.0 - 1.0, 0.0, 1.0);

    // 2. UV 좌표 전달 (UV0가 작동하지 않을 경우를 대비해 직접 계산)
    // Position.xy가 0~1 범위라면 그대로 쓰고, -1~1 범위라면 보정합니다.
    texCoord = Position.xy;

    // 만약 화면이 뒤집혀 보인다면 아래 코드로 바꿔보세요:
    // texCoord = vec2(Position.x, 1.0 - Position.y);
}