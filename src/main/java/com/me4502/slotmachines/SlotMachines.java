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
package com.me4502.slotmachines;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import com.me4502.slotmachines.builder.FactorDataBuilder;
import com.me4502.slotmachines.builder.SlotTierBuilder;
import com.me4502.slotmachines.data.SlotMachineData;
import com.me4502.slotmachines.data.SlotMachineKeys;
import com.me4502.slotmachines.data.mutable.SlotMachineOwnerData;
import com.me4502.slotmachines.util.ConfigValue;
import com.me4502.slotmachines.util.LocationUtil;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.block.tileentity.Sign;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.hanging.ItemFrame;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.entity.InteractEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.service.economy.transaction.ResultType;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.Location;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Plugin(
        id = "slotmachines",
        name = "SlotMachines",
        description = "SlotMachines for Sponge",
        authors = {
                "Me4502"
        }
)
public class SlotMachines {

    @Inject
    private Logger logger;

    @Inject
    public PluginContainer container;

    @Inject
    @DefaultConfig(sharedRoot = false)
    private Path defaultConfig;

    @Inject
    @DefaultConfig(sharedRoot = false)
    private ConfigurationLoader<CommentedConfigurationNode> configManager;

    public static SlotMachines instance;

    public static final UUID ADMIN_UUID = new UUID(0, 0);

    private ConfigValue<List<ItemStack>> items = new ConfigValue<>("items", "The items that can appear on the slot machine", Lists.newArrayList(
            ItemStack.of(ItemTypes.GOLD_INGOT, 1), ItemStack.of(ItemTypes.GOLDEN_APPLE, 1), ItemStack.of(ItemTypes.DIAMOND_ORE, 1), ItemStack.of(ItemTypes.EMERALD_ORE, 1),
            ItemStack.of(ItemTypes.GOLD_ORE, 1), ItemStack.of(ItemTypes.IRON_ORE, 1), ItemStack.of(ItemTypes.COAL_ORE, 1), ItemStack.of(ItemTypes.APPLE, 1), ItemStack.of(ItemTypes.STICK, 1),
            ItemStack.of(ItemTypes.FISH, 1), ItemStack.of(ItemTypes.DIRT, 1)
    ), new TypeToken<List<ItemStack>>() {});

    private ConfigValue<List<FactorData>> factors = new ConfigValue<>("factors", "The factors", Lists.newArrayList(
        new FactorData(ItemStack.of(ItemTypes.GOLD_INGOT, 3), 1666.0), new FactorData(ItemStack.of(ItemTypes.GOLDEN_APPLE, 3), 300.0),
        new FactorData(ItemStack.of(ItemTypes.DIAMOND_ORE, 3), 100.0), new FactorData(ItemStack.of(ItemTypes.EMERALD_ORE, 3), 50.0),
        new FactorData(ItemStack.of(ItemTypes.GOLD_ORE, 3), 25.0), new FactorData(ItemStack.of(ItemTypes.IRON_ORE, 3), 12.0),
        new FactorData(ItemStack.of(ItemTypes.APPLE, 3), 12.0), new FactorData(ItemStack.of(ItemTypes.APPLE, 2), 6.0),
        new FactorData(ItemStack.of(ItemTypes.APPLE, 1), 1.0)
    ), new TypeToken<List<FactorData>>() {});

    private ConfigValue<List<SlotTier>> slotTiers = new ConfigValue<>("tiers", "The slot tiers", Lists.newArrayList(
            new SlotTier(BlockState.builder().blockType(BlockTypes.IRON_BLOCK).build(), 1.0d),
            new SlotTier(BlockState.builder().blockType(BlockTypes.GOLD_BLOCK).build(), 2.5d),
            new SlotTier(BlockState.builder().blockType(BlockTypes.DIAMOND_BLOCK).build(), 5.0d)
    ), new TypeToken<List<SlotTier>>() {});

    private ConfigurationNode messagesNode;
    private ConfigurationNode defaultNode;

    private Set<Location<?>> lockedMachines = Sets.newHashSet();

    @Listener
    public void onServerStarting(GamePreInitializationEvent event) {
        instance = this;
        SlotMachineData.registerData();
        Sponge.getDataManager().registerBuilder(FactorData.class, new FactorDataBuilder());
        Sponge.getDataManager().registerBuilder(SlotTier.class, new SlotTierBuilder());
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        URL jarConfigFile = this.getClass().getResource("messages.conf");

        ConfigurationOptions options = ConfigurationOptions.defaults().setShouldCopyDefaults(true);

        ConfigurationLoader<CommentedConfigurationNode> loader = HoconConfigurationLoader.builder().setURL(jarConfigFile).setDefaultOptions(options).build();
        ConfigurationLoader<CommentedConfigurationNode> fileLoader = HoconConfigurationLoader.builder().setFile(new File(defaultConfig.toFile().getParentFile(), "messages.conf")).setDefaultOptions(options).build();
        try {
            messagesNode = fileLoader.load(options);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            defaultNode = loader.load(options);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            ConfigurationNode node = configManager.load(options);

            items.load(node);
            factors.load(node);
            slotTiers.load(node);

            configManager.save(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Listener
    public void onServerStopping(GameStoppingServerEvent event) {
        ConfigurationOptions options = ConfigurationOptions.defaults().setShouldCopyDefaults(true);
        ConfigurationLoader<CommentedConfigurationNode> fileLoader = HoconConfigurationLoader.builder().setFile(new File(defaultConfig.toFile().getParentFile(), "messages.conf")).setDefaultOptions(options).build();

        try {
            fileLoader.save(messagesNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Listener
    public void onEntityInteract(InteractEntityEvent event, @First Player player) {
        if (event.getTargetEntity() instanceof ItemFrame) {
            for (Location<?> location : lockedMachines) {
                if (location.getBlockPosition().distance(event.getTargetEntity().getLocation().getBlockPosition()) < 5) {
                    event.setCancelled(true);
                    break;
                }
            }
        }
    }

    @Listener
    public void onInteract(InteractBlockEvent.Secondary.MainHand event, @First Player player) {
        event.getTargetBlock().getLocation().filter(location -> location.getBlockType() == BlockTypes.STONE_BUTTON || location.getBlockType() == BlockTypes.WOODEN_BUTTON).ifPresent(location -> {
            getTier(LocationUtil.getBackBlock(location)).ifPresent(slotTier -> {
                if (lockedMachines.contains(location)) {
                    player.sendMessage(getMessage("slots.in-use"));
                    return;
                }
                boolean failed = false;
                boolean message = false;
                Location<?> newLocation = LocationUtil.getBackBlock(location);
                Direction xDirection = LocationUtil.getRight(location);
                Direction xOpposite = xDirection.getOpposite();
                Direction yDirection = Direction.UP;

                List<ItemFrame> frames = new ArrayList<>();
                Location<?> lampLocation = null;
                Sign topLeftSign = null;
                Sign topRightSign = null;

                for (int y = 0; y < 3; y++) {
                    if (failed) {
                        break;
                    }
                    for (int x = 0; x < 3; x++) {
                        Location<?> frontLocation = newLocation.getRelative(LocationUtil.getFront(location));

                        if (x == 1 && y == 2) {
                            if (newLocation.getBlockType() != BlockTypes.REDSTONE_LAMP) {
                                failed = true;
                                logger.debug("Missing lamp. Got " + newLocation.getBlockType().getName() + " at " + newLocation.toString() + " Grid: " + x + "," + y);
                                break;
                            } else {
                                lampLocation = newLocation;
                            }
                        } else if (y == 1) {
                            ItemFrame frame = getItemFrame(frontLocation);
                            if (frame != null) {
                                frames.add(frame);
                            } else {
                                failed = true;
                                logger.debug("Missing frame. Got " + frontLocation.getBlockType().getName() + " at " + frontLocation.toString() + " Grid: " + x + "," + y);
                                break;
                            }
                        } else {
                            if (!newLocation.getBlock().equals(slotTier.getBlockState())) {
                                failed = true;
                                logger.debug("Missing tier. Got " + newLocation.getBlockType().getName() + " at " + newLocation.toString() + " Grid: " + x + "," + y);
                                break;
                            }
                        }

                        if (x == 0 && y == 2) {
                            // Find sign.
                            topRightSign = (Sign) frontLocation.getTileEntity().filter(tileEntity -> tileEntity instanceof Sign).orElse(null);
                            if (topRightSign == null) {
                                logger.debug("Missing right sign. Got " + frontLocation.getBlockType().getName() + " at " + frontLocation.toString() + " Grid: " + x + "," + y);
                                failed = true;
                                break;
                            }
                        } else if (x == 2 && y == 2) {
                            // Find sign.
                            topLeftSign = (Sign) frontLocation.getTileEntity().filter(tileEntity -> tileEntity instanceof Sign).orElse(null);
                            if (topLeftSign == null) {
                                logger.debug("Missing left sign. Got " + frontLocation.getBlockType().getName() + " at " + frontLocation.toString() + " Grid: " + x + "," + y);
                                failed = true;
                                break;
                            }
                        }

                        newLocation = newLocation.getRelative(xDirection);
                    }
                    newLocation = newLocation.getRelative(xOpposite).getRelative(xOpposite).getRelative(xOpposite).getRelative(yDirection);
                }

                if (failed) {
                    if (message) {
                        player.sendMessage(getMessage("slots.invalid-slotmachine"));
                    }
                } else {
                    if (topLeftSign.lines().get(0).equals(getMessage("slots.sign.title"))) {
                        frames = Lists.reverse(frames);
                        UUID ownerUUID = topRightSign.get(SlotMachineKeys.SLOT_MACHINE_OWNER).orElse(null);
                        if (ownerUUID == null) {
                            player.sendMessage(getMessage("slots.missing-data"));
                            return;
                        }

                        if (player.getUniqueId().equals(ownerUUID)) {
                            player.sendMessage(getMessage("slots.own-machine"));
                            return;
                        }

                        double price = 0;
                        try {
                            price = Double.parseDouble(LocationUtil.getTextRaw(topRightSign, 1));
                        } catch (Exception e) {
                        }
                        if (price <= 0) {
                            player.sendMessage(getMessage("slots.missing-price"));
                        } else {
                            EconomyService economyService = Sponge.getServiceManager().getRegistration(EconomyService.class).get().getProvider();

                            double lowerLimit = 0;
                            try {
                                lowerLimit = Double.parseDouble(LocationUtil.getTextRaw(topRightSign, 3));
                            } catch (Exception e) {
                            }
                            lowerLimit = Math.max(lowerLimit, 0);

                            double maxPrize = price * slotTier.getMultiplier() * getHighestFactor().getMultiplier();
                            UniqueAccount ownerAccount = economyService.getOrCreateAccount(ownerUUID).get();
                            if (!ownerUUID.equals(ADMIN_UUID)) {
                                if (ownerAccount.getBalance(economyService.getDefaultCurrency()).doubleValue() - maxPrize < lowerLimit) {
                                    player.sendMessage(getMessage("slots.out-of-money"));
                                    return;
                                }
                            }

                            UniqueAccount account = economyService.getOrCreateAccount(player.getUniqueId()).get();

                            if (account.withdraw(economyService.getDefaultCurrency(), BigDecimal.valueOf(price), Cause.source(container).build()).getResult() != ResultType.SUCCESS) {
                                double finalPrice = price;
                                player.sendMessage(getMessage("slots.insufficient-funds", string -> string.replace("{amount}", String.valueOf(finalPrice))));
                            } else {
                                if (!ownerUUID.equals(ADMIN_UUID)) {
                                    ownerAccount.deposit(economyService.getDefaultCurrency(), BigDecimal.valueOf(price), Cause.source(container).build());
                                }

                                lockedMachines.add(location);
                                List<ItemFrame> finalFrames = frames;
                                double finalPrice1 = price;
                                Sponge.getScheduler().createTaskBuilder().intervalTicks(8).execute(new Consumer<Task>() {
                                    int startingFrame = 0;
                                    int timeSinceChange = 0;

                                    @Override
                                    public void accept(Task task) {
                                        if (startingFrame == finalFrames.size()) {
                                            // End
                                            task.cancel();
                                            List<ItemStack> results = finalFrames.stream().map(itemFrame -> itemFrame.get(Keys.REPRESENTED_ITEM).orElse(null)).filter(Objects::nonNull).map(ItemStackSnapshot::createStack).collect(Collectors.toList());
                                            FactorData data = getFactor(results).orElse(null);
                                            if (data == null) {
                                                player.sendMessage(getMessage("slots.better-luck"));
                                            } else {
                                                double prize = finalPrice1 * slotTier.getMultiplier() * data.getMultiplier();
                                                player.sendMessage(
                                                        getMessage("slots.you-won", string -> string.replace("{prize}", String.valueOf(prize))));

                                                EconomyService economyService =
                                                        Sponge.getServiceManager().getRegistration(EconomyService.class).get().getProvider();
                                                UniqueAccount account = economyService.getOrCreateAccount(player.getUniqueId()).get();
                                                account.deposit(economyService.getDefaultCurrency(), BigDecimal.valueOf(prize),
                                                        Cause.source(container).build());
                                                if (!ownerUUID.equals(ADMIN_UUID)) {
                                                    ownerAccount.withdraw(economyService.getDefaultCurrency(), BigDecimal.valueOf(prize), Cause.source(container).build());
                                                }
                                            }

                                            Sponge.getScheduler().createTaskBuilder().delayTicks(20 * 3).execute(task1 -> {
                                                finalFrames.forEach(frame -> frame.remove(Keys.REPRESENTED_ITEM));
                                                lockedMachines.remove(location);
                                            }).submit(container);
                                        } else {
                                            for (int i = startingFrame; i < finalFrames.size(); i++) {
                                                ItemFrame frame = finalFrames.get(i);
                                                frame.offer(Keys.REPRESENTED_ITEM, items.getValue().get(ThreadLocalRandom.current().nextInt(items.getValue().size())).createSnapshot());
                                                if (timeSinceChange >= 10 || (timeSinceChange > 5 && ThreadLocalRandom.current().nextInt(5) == 0)) {
                                                    startingFrame ++;
                                                    timeSinceChange = 0;
                                                }
                                                timeSinceChange ++;
                                            }
                                        }
                                    }
                                }).submit(container);

                                topLeftSign.offer(Keys.SIGN_LINES, Lists.newArrayList(getMessage("slots.sign.title"), Text.EMPTY, getMessage("slots.sign.last-player"), Text.of(TextColors.DARK_RED, player.getName())));
                            }
                        }
                    } else {
                        if (!player.hasPermission("slots.create")) {
                            player.sendMessage(getMessage("slots.permission.create"));
                        } else {
                            double price = 0;
                            try {
                                price = Double.parseDouble(LocationUtil.getTextRaw(topRightSign, 0));
                            } catch (Exception e) {
                            }
                            double lowerLimit = 0;
                            try {
                                lowerLimit = Double.parseDouble(LocationUtil.getTextRaw(topRightSign, 3));
                            } catch (Exception e) {
                            }
                            lowerLimit = Math.max(0, lowerLimit);
                            if (price <= 0) {
                                player.sendMessage(getMessage("slots.missing-price"));
                            } else {
                                double paymentPrice = price * slotTier.getMultiplier();
                                EconomyService economyService = Sponge.getServiceManager().getRegistration(EconomyService.class).get().getProvider();
                                UniqueAccount account = economyService.getOrCreateAccount(player.getUniqueId()).get();
                                boolean isAdmin = topLeftSign.lines().get(1).toPlain().equals("ADMIN");
                                if (isAdmin && !player.hasPermission("slots.create.admin")) {
                                    player.sendMessage(getMessage("slots.permission.admin"));
                                    return;
                                }

                                if (!isAdmin && account.withdraw(economyService.getDefaultCurrency(), BigDecimal.valueOf(paymentPrice), Cause.source(container).build()).getResult() != ResultType.SUCCESS) {
                                    player.sendMessage(getMessage("slots.insufficient-funds", string -> string.replace("{amount}", String.valueOf(paymentPrice))));
                                } else {
                                    topRightSign.offer(Keys.SIGN_LINES, Lists.newArrayList(getMessage("slots.sign.price"), Text.of(TextColors.RED, price), getMessage("slots.sign.limit"), Text.of(TextColors.RED, lowerLimit)));
                                    topLeftSign.offer(Keys.SIGN_LINES, Lists.newArrayList(getMessage("slots.sign.title"), Text.EMPTY, Text.EMPTY, Text.EMPTY));

                                    SlotMachineOwnerData slotMachineOwnerData = new SlotMachineOwnerData(isAdmin ? ADMIN_UUID : player.getUniqueId());
                                    topRightSign.offer(slotMachineOwnerData);
                                }
                            }
                        }
                    }
                }
            });
        });
    }

    private ItemFrame getItemFrame(Location<?> location) {
        return location.getExtent().getEntities().stream()
                .filter(entity -> entity.getLocation().getBlockPosition().equals(location.getBlockPosition()))
                .filter(entity -> entity.getType() == EntityTypes.ITEM_FRAME)
                .map(entity -> (ItemFrame) entity).findFirst().orElse(null);
    }

    private Optional<SlotTier> getTier(Location<?> location) {
        return slotTiers.getValue().stream().filter(tier -> location.getBlock().equals(tier.getBlockState())).findFirst();
    }

    private Optional<FactorData> getFactor(List<ItemStack> results) {
        FactorData bestMatch = null;
        for (FactorData data : factors.getValue()) {
            if (data.matches(results)) {
                if (bestMatch == null || bestMatch.getMultiplier() < data.getMultiplier()) {
                    bestMatch = data;
                }
            }
        }

        return Optional.ofNullable(bestMatch);
    }

    private FactorData getHighestFactor() {
        FactorData bestMatch = null;
        for (FactorData data : factors.getValue()) {
            if (bestMatch == null || bestMatch.getMultiplier() < data.getMultiplier()) {
                bestMatch = data;
            }
        }

        return bestMatch;
    }

    private Text getMessage(String message) {
        Object[] path = message.split("\\.");
        return TextSerializers.FORMATTING_CODE.deserialize(messagesNode.getNode(path).getString(defaultNode.getNode(path).getString("Unknown Message: " + message)));
    }

    private Text getMessage(String message, Function<String, String> replacer) {
        Object[] path = message.split("\\.");
        String replaced = replacer.apply(messagesNode.getNode(path).getString(defaultNode.getNode(path).getString("Unknown Message: " + message)));
        return TextSerializers.FORMATTING_CODE.deserialize(replaced);
    }
}
