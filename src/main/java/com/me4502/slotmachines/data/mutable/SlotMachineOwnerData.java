/*
 * Copyright (c) 2016-2017 Me4502 (Matthew Miller)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
        this(new UUID(0, 0));
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
            return Optional.of(set(SlotMachineKeys.SLOT_MACHINE_OWNER, UUID.fromString(container.getString(SlotMachineKeys.SLOT_MACHINE_OWNER.getQuery()).orElse(getValue().toString()))));
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
        return super.toContainer().set(SlotMachineKeys.SLOT_MACHINE_OWNER.getQuery(), getValue().toString());
    }

    @Override
    public int getContentVersion() {
        return 1;
    }
}
