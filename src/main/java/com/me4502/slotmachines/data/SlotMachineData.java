/*
 * Copyright (c) Me4502 (Madeline Miller)
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
package com.me4502.slotmachines.data;

import com.me4502.slotmachines.SlotMachines;
import com.me4502.slotmachines.data.builder.SlotMachineOwnerDataManipulatorBuilder;
import com.me4502.slotmachines.data.immutable.ImmutableSlotMachineOwnerData;
import com.me4502.slotmachines.data.mutable.SlotMachineOwnerData;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataRegistration;

public class SlotMachineData {

    public static void registerData() {
        new SlotMachineKeys(); // Required to early-load keys

        DataRegistration<SlotMachineOwnerData, ImmutableSlotMachineOwnerData> slowMachineOwnerData =
                DataRegistration.<SlotMachineOwnerData, ImmutableSlotMachineOwnerData>builder()
                        .dataClass(SlotMachineOwnerData.class)
                        .immutableClass(ImmutableSlotMachineOwnerData.class)
                        .builder(new SlotMachineOwnerDataManipulatorBuilder())
                        .manipulatorId("slot_machine_owner")
                        .dataName("SlotMachineOwner")
                        .buildAndRegister(SlotMachines.instance.container);

        Sponge.getDataManager().registerLegacyManipulatorIds("com.me4502.slotmachines.data.mutable.SlotMachineOwnerData", slowMachineOwnerData);
    }
}
