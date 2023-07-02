package fr.fyustorm.minetiface.common;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StartupCommon {
    private static final Logger LOGGER = LoggerFactory.getLogger(StartupCommon.class);

    @SubscribeEvent
    public static void onCommonSetupEvent(FMLCommonSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(RegisterCommandEvent.class);
        LOGGER.info("Minetiface common setup");
    }
}
