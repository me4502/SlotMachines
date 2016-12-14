package com.me4502.slotmachines.data.mutable;

import com.me4502.slotmachines.data.SlotMachineKeys;
import com.me4502.slotmachines.data.immutable.ImmutableSlotMachineOwnerData;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractSingleData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.value.mutable.Value;

import java.util.Optional;
import java.util.UUID;

public class SlotMachineOwnerData extends AbstractSingleData<UUID, SlotMachineOwnerData, ImmutableSlotMachineOwnerData> {

    public SlotMachineOwnerData() {
        this(UUID.randomUUID());
    }

    public SlotMachineOwnerData(UUID value) {
        super(value, SlotMachineKeys.SLOT_MACHINE_OWNER);
    }

    public Value<UUID> slotMachineOwner() {
        return Sponge.getRegistry().getValueFactory()
                .createValue(SlotMachineKeys.SLOT_MACHINE_OWNER, getValue());
    }

    @Override
    protected Value<UUID> getValueGetter() {
        return slotMachineOwner();
    }

    @Override
    public Optional<SlotMachineOwnerData> fill(DataHolder dataHolder, MergeFunction overlap) {
        dataHolder.get(SlotMachineOwnerData.class).ifPresent((data) -> {
            SlotMachineOwnerData finalData = overlap.merge(this, data);
            setValue(finalData.getValue());
        });
        return Optional.of(this);
    }

    @Override
    public Optional<SlotMachineOwnerData> from(DataContainer container) {
        if (container.contains(SlotMachineKeys.SLOT_MACHINE_OWNER.getQuery())) {
            return Optional.of(set(SlotMachineKeys.SLOT_MACHINE_OWNER, container.getObject(SlotMachineKeys.SLOT_MACHINE_OWNER.getQuery(), UUID.class).orElse(getValue())));
        }

        return Optional.empty();
    }

    @Override
    public SlotMachineOwnerData copy() {
        return new SlotMachineOwnerData(this.getValue());
    }

    @Override
    public ImmutableSlotMachineOwnerData asImmutable() {
        return new ImmutableSlotMachineOwnerData(this.getValue());
    }

    @Override
    public DataContainer toContainer() {
        return super.toContainer().set(SlotMachineKeys.SLOT_MACHINE_OWNER, getValue());
    }

    @Override
    public int getContentVersion() {
        return 1;
    }
}
