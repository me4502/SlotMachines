package com.me4502.slotmachines.data;

import com.me4502.slotmachines.data.builder.SlotMachineOwnerDataManipulatorBuilder;
import com.me4502.slotmachines.data.immutable.ImmutableSlotMachineOwnerData;
import com.me4502.slotmachines.data.mutable.SlotMachineOwnerData;
import org.spongepowered.api.Sponge;

public class SlotMachineData {

    public static void registerData() {
        Sponge.getDataManager().register(SlotMachineOwnerData.class, ImmutableSlotMachineOwnerData.class, new SlotMachineOwnerDataManipulatorBuilder());
    }
}
