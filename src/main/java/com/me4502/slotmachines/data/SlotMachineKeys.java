package com.me4502.slotmachines.data;

import com.google.common.reflect.TypeToken;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.value.mutable.Value;

import java.util.UUID;

import static org.spongepowered.api.data.DataQuery.of;
import static org.spongepowered.api.data.key.KeyFactory.makeSingleKey;

public class SlotMachineKeys {

    public static Key<Value<UUID>> SLOT_MACHINE_OWNER = makeSingleKey(TypeToken.of(UUID.class),
            new TypeToken<Value<UUID>>(){}, of("SlotMachineOwner"), "slots:slotmachineowner", "SlotMachineOwner");
}
