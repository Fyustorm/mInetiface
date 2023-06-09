package fr.fyustorm.minetiface;

import fr.fyustorm.minetiface.common.StartupCommon;
import fr.fyustorm.minetiface.commons.config.MinetifaceConfig;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

@Mod(MinetifaceMod.MOD_ID)
public class MinetifaceMod
{
    public static final String MOD_ID = "assets/minetiface";

    private static final Logger LOGGER = LoggerFactory.getLogger(MinetifaceMod.class);

    public MinetifaceMod() {
        MinecraftForge.EVENT_BUS.register(this);

        Path configPath = FMLPaths.CONFIGDIR.get();
        MinetifaceConfig.loadConfig(configPath);

        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.register(StartupCommon.class);

        LOGGER.info("Minetiface init");
    }
}
