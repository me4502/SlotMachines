package com.me4502.slotmachines.data.builder;

import com.me4502.slotmachines.data.SlotMachineKeys;
import com.me4502.slotmachines.data.immutable.ImmutableSlotMachineOwnerData;
import com.me4502.slotmachines.data.mutable.SlotMachineOwnerData;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.Optional;
import java.util.UUID;

public class SlotMachineOwnerDataManipulatorBuilder extends AbstractDataBuilder<SlotMachineOwnerData> implements DataManipulatorBuilder<SlotMachineOwnerData, ImmutableSlotMachineOwnerData> {

    public SlotMachineOwnerDataManipulatorBuilder() {
        super(SlotMachineOwnerData.class, 1);
    }

    @Override
    public SlotMachineOwnerData create() {
        return new SlotMachineOwnerData();
    }

    @Override
    public Optional<SlotMachineOwnerData> createFrom(DataHolder dataHolder) {
        return create().fill(dataHolder);
    }

    @Override
    protected Optional<SlotMachineOwnerData> buildContent(DataView container) throws InvalidDataException {
        return create().from(container.getContainer());
    }
}