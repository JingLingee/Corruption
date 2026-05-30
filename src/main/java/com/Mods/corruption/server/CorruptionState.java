package com.Mods.corruption.server;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;

public class CorruptionState extends PersistentState {

    private static final String KEY = "corruption_state";
    private boolean wasLoadedFromDisk = false;
    private boolean helperShown = false;

    public static final Type<CorruptionState> TYPE =
            new Type<>(
                    CorruptionState::new,
                    CorruptionState::createFromNbt,
                    null
            );

    private float corruptionPercent = 0.0f;

    public CorruptionState() {}

    private static CorruptionState createFromNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup lookup) {
        CorruptionState state = new CorruptionState();
        state.corruptionPercent = nbt.getFloat("corruptionPercent");

        state.helperShown = nbt.getBoolean("helperShown"); // 추가

        state.wasLoadedFromDisk = true;
        return state;
    }
    @Override
    public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup lookup) {
        nbt.putFloat("corruptionPercent", corruptionPercent);

        nbt.putBoolean("helperShown", helperShown); // 추가

        return nbt;
    }

    public static CorruptionState get(MinecraftServer server) {

        PersistentStateManager manager =
                server.getOverworld().getPersistentStateManager();

        CorruptionState state = manager.getOrCreate(TYPE, KEY);

        // 파일이 없어서 새로 생성된 경우
        if (!state.wasLoadedFromDisk) {
            state.markDirty(); // 저장 플래그
        }

        return state;
    }

    // =======================
    // Getter / Setter
    // =======================

    public float getCorruptionPercent() {
        return corruptionPercent;
    }

    public void addCorruption(float amount) {
        corruptionPercent += amount;
        corruptionPercent = Math.max(0f, Math.min(100f, corruptionPercent));
        markDirty();
    }

    public void setCorruptionPercent(float value) {
        this.corruptionPercent = value;
        markDirty(); // 저장 플래그
    }

    public void setCorruption(float value) {
        corruptionPercent = Math.max(0f, Math.min(100f, value));
        markDirty();
    }

    public boolean isHelperShown() {
        return helperShown;
    }

    public void setHelperShown(boolean value) {
        this.helperShown = value;
        markDirty();
    }
}