package io.github.mortuusars.exposure.forge.event;

import io.github.mortuusars.exposure.Exposure;
import io.github.mortuusars.exposure.command.ExposureCommand;
import io.github.mortuusars.exposure.command.ShaderCommand;
import io.github.mortuusars.exposure.command.TestCommand;
import io.github.mortuusars.exposure.data.Lenses;
import io.github.mortuusars.exposure.data.LensesDataLoader;
import io.github.mortuusars.exposure.network.forge.PacketsImpl;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class CommonEvents {
    public static class ModBus {
        @SubscribeEvent
        public static void commonSetup(FMLCommonSetupEvent event) {
            event.enqueueWork(() -> {
                PacketsImpl.register();
                Exposure.Advancements.register();
                Exposure.Stats.register();
            });
        }

        @SubscribeEvent
        public static void onCreativeTabsBuild(BuildCreativeModeTabContentsEvent event) {
            if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
                event.accept(Exposure.Items.CAMERA.get());
                event.accept(Exposure.Items.BLACK_AND_WHITE_FILM.get());
                event.accept(Exposure.Items.COLOR_FILM.get());
                event.accept(Exposure.Items.DEVELOPED_BLACK_AND_WHITE_FILM.get());
                event.accept(Exposure.Items.DEVELOPED_COLOR_FILM.get());
                event.accept(Exposure.Items.PHOTOGRAPH.get());
                event.accept(Exposure.Items.AGED_PHOTOGRAPH.get());
                event.accept(Exposure.Items.INTERPLANAR_PROJECTOR.get());
                event.accept(Exposure.Items.STACKED_PHOTOGRAPHS.get());
                event.accept(Exposure.Items.PHOTOGRAPH_FRAME.get());
                event.accept(Exposure.Items.ALBUM.get());
            }

            if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
                event.accept(Exposure.Items.LIGHTROOM.get());
            }
        }
    }

    public static class ForgeBus {
        @SubscribeEvent
        public static void serverStarting(ServerStartingEvent event) {
            Exposure.initServer(event.getServer());
        }

        @SubscribeEvent
        public static void addReloadListeners(AddReloadListenerEvent event) {
            event.addListener(new LensesDataLoader());
        }

        @SubscribeEvent
        public static void onDatapackSync(OnDatapackSyncEvent event) {
            Lenses.onDatapackSync(event.getPlayer());
        }

        @SubscribeEvent
        public static void registerCommands(RegisterCommandsEvent event) {
            ExposureCommand.register(event.getDispatcher());
            ShaderCommand.register(event.getDispatcher());
            TestCommand.register(event.getDispatcher());
        }
    }
}
