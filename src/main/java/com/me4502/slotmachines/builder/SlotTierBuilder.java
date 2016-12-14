package com.me4502.slotmachines.builder;

import com.me4502.slotmachines.SlotTier;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.DataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.Optional;

public class SlotTierBuilder extends AbstractDataBuilder<SlotTier> implements DataBuilder<SlotTier> {

    protected SlotTierBuilder() {
        super(SlotTier.class, 1);
    }

    @Override
    protected Optional<SlotTier> buildContent(DataView container) throws InvalidDataException {
        BlockState blockState = container.getSerializable(DataQuery.of("BlockState"), BlockState.class).orElse(null);
        if (blockState == null) {
            return Optional.empty();
        }
        double multiplier = container.getDouble(DataQuery.of("Multiplier")).orElse(1.0);

        return Optional.of(new SlotTier(blockState, multiplier));
    }
}
