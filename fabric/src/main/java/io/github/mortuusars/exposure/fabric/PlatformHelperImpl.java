package io.github.mortuusars.exposure.fabric;

import io.github.mortuusars.exposure.fabric.api.event.FrameAddedCallback;
import io.github.mortuusars.exposure.fabric.api.event.ModifyFrameDataCallback;
import io.github.mortuusars.exposure.fabric.api.event.ShutterOpeningCallback;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class PlatformHelperImpl {
    public static boolean canShear(ItemStack stack) {
        return stack.getItem() instanceof ShearsItem;
    }

    public static boolean canStrip(ItemStack stack) {
        return stack.getItem() instanceof AxeItem;
    }

    public static void openMenu(ServerPlayer serverPlayer, MenuProvider menuProvider, Consumer<FriendlyByteBuf> extraDataWriter) {
        ExtendedScreenHandlerFactory extendedScreenHandlerFactory = new ExtendedScreenHandlerFactory() {
            @Nullable
            @Override
            public AbstractContainerMenu createMenu(int i, @NotNull Inventory inventory, @NotNull Player player) {
                return menuProvider.createMenu(i, inventory, player);
            }

            @Override
            public @NotNull Component getDisplayName() {
                return menuProvider.getDisplayName();
            }

            @Override
            public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buffer) {
                extraDataWriter.accept(buffer);
            }
        };

        serverPlayer.openMenu(extendedScreenHandlerFactory);
    }

    public static List<String> getDefaultSpoutDevelopmentColorSequence() {
        return List.of(
                "{FluidName:\"create:potion\",Amount:27000,Tag:{Potion:\"minecraft:awkward\"}}",
                "{FluidName:\"create:potion\",Amount:27000,Tag:{Potion:\"minecraft:thick\"}}",
                "{FluidName:\"create:potion\",Amount:27000,Tag:{Potion:\"minecraft:mundane\"}}");
    }

    public static List<String> getDefaultSpoutDevelopmentBWSequence() {
        return List.of(
                "{FluidName:\"minecraft:water\",Amount:27000}");
    }

    public static boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    public static boolean fireShutterOpeningEvent(Player player, ItemStack cameraStack, int lightLevel, boolean shouldFlashFire) {
        return ShutterOpeningCallback.EVENT.invoker().onShutterOpening(player, cameraStack, lightLevel, shouldFlashFire);
    }

    public static void fireModifyFrameDataEvent(ServerPlayer player, ItemStack cameraStack, CompoundTag frame, List<Entity> entitiesInFrame) {
        ModifyFrameDataCallback.EVENT.invoker().modifyFrameData(player, cameraStack, frame, entitiesInFrame);
    }

    public static void fireFrameAddedEvent(ServerPlayer player, ItemStack cameraStack, CompoundTag frame) {
        FrameAddedCallback.EVENT.invoker().onFrameAdded(player, cameraStack, frame);
    }
}
