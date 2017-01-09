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
        if (container.contains(SlotMachineKeys.SLOT_MACHINE_OWNER.getQuery())) {
            UUID value = container.getString(SlotMachineKeys.SLOT_MACHINE_OWNER.getQuery()).map(UUID::fromString).orElse(new UUID(0, 0));
            return Optional.of(new SlotMachineOwnerData(value));
        }
        return Optional.empty();
    }
}