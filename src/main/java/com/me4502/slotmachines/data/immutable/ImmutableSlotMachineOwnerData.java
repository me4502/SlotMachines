/*
 * Copyright (c) Me4502 (Matthew Miller)
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
