package com.me4502.slotmachines.data.immutable;

import com.me4502.slotmachines.data.SlotMachineKeys;
import com.me4502.slotmachines.data.mutable.SlotMachineOwnerData;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableSingleData;
import org.spongepowered.api.data.value.immutable.ImmutableValue;

import java.util.UUID;

public class ImmutableSlotMachineOwnerData extends AbstractImmutableSingleData<UUID, ImmutableSlotMachineOwnerData, SlotMachineOwnerData> {

    public ImmutableSlotMachineOwnerData() {
        this(UUID.randomUUID());
    }

    public ImmutableSlotMachineOwnerData(UUID value) {
        super(value, SlotMachineKeys.SLOT_MACHINE_OWNER);
    }

    @Override
    protected ImmutableValue<UUID> getValueGetter() {
        return Sponge.getRegistry().getValueFactory()
                .createValue(SlotMachineKeys.SLOT_MACHINE_OWNER, getValue())
                .asImmutable();
    }

    @Override
    public SlotMachineOwnerData asMutable() {
        return new SlotMachineOwnerData(this.getValue());
    }

    @Override
    public DataContainer toContainer() {
        return super.toContainer().set(SlotMachineKeys.SLOT_MACHINE_OWNER.getQuery(), getValue().toString());
    }

    @Override
    public int getContentVersion() {
        return 1;
    }
}
