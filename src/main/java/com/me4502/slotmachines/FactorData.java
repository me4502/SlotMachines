package com.me4502.slotmachines;

import org.spongepowered.api.data.*;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.List;

public class FactorData implements DataSerializable {

    private ItemStack itemStack;
    private double multiplier;

    public FactorData(ItemStack itemStack, double multiplier) {
        this.itemStack = itemStack;
        this.multiplier = multiplier;
    }

    @Override
    public int getContentVersion() {
        return 1;
    }

    @Override
    public DataContainer toContainer() {
        return new MemoryDataContainer()
                .set(Queries.CONTENT_VERSION, getContentVersion())
                .set(DataQuery.of("ItemStack"), this.itemStack)
                .set(DataQuery.of("Multiplier"), this.multiplier);
    }

    public double getMultiplier() {
        return this.multiplier;
    }

    public boolean matches(List<ItemStack> items) {
        int amountRemaining = itemStack.getQuantity();
        ItemStack detection = itemStack.copy();
        detection.setQuantity(1);
        for (ItemStack item : items) {
            if (detection.equalTo(item)) {
                amountRemaining -= 1;
            }
        }

        return amountRemaining == 0;
    }
}
