package fr.fyustorm.minetiface.common;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegisterCommandEvent {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterCommandEvent.class);

    @SubscribeEvent
    public static void onRegisterCommandEvent(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> commandDispatcher = event.getDispatcher();
        MinetifaceCommand.register(commandDispatcher);
        LOGGER.info("Minetiface register commands");
    }
}
