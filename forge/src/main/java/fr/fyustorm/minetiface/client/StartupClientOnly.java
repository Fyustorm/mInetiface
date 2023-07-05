package fr.fyustorm.minetiface.client;

import fr.fyustorm.minetiface.common.RegisterCommandEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StartupClientOnly {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartupClientOnly.class);
    @SubscribeEvent
    public static void onClientSetupEvent(FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(RegisterCommandEvent.class);
        LOGGER.info("Minetiface client only setup");
    }
}
