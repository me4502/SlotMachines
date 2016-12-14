package com.me4502.slotmachines.builder;

import com.me4502.slotmachines.FactorData;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.DataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.Optional;

public class FactorDataBuilder extends AbstractDataBuilder<FactorData> implements DataBuilder<FactorData> {

    public FactorDataBuilder() {
        super(FactorData.class, 1);
    }

    @Override
    protected Optional<FactorData> buildContent(DataView container) throws InvalidDataException {
        ItemStack itemStack = container.getSerializable(DataQuery.of("ItemStack"), ItemStack.class).orElse(null);
        if (itemStack == null) {
            return Optional.empty();
        }
        double multiplier = container.getDouble(DataQuery.of("Multiplier")).orElse(1.0);

        return Optional.of(new FactorData(itemStack, multiplier));
    }
}
