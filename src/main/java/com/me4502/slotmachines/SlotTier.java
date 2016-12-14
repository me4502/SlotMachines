package com.me4502.slotmachines;

import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.data.*;

public class SlotTier implements DataSerializable {

    private BlockState blockState;
    private double multiplier;

    public SlotTier(BlockState blockState, double multiplier) {
        this.blockState = blockState;
        this.multiplier = multiplier;
    }

    @Override
    public int getContentVersion() {
        return 1;
    }

    @Override
    public DataContainer toContainer() {
        return new MemoryDataContainer()
                .set(Queries.CONTENT_VERSION, getContentVersion())
                .set(DataQuery.of("BlockState"), this.blockState)
                .set(DataQuery.of("Multiplier"), this.multiplier);
    }

    public BlockState getBlockState() {
        return this.blockState;
    }

    public double getMultiplier() {
        return this.multiplier;
    }
}
