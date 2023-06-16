package fr.fyustorm.minetiface.gui;

import fr.fyustorm.minetiface.config.MinetifaceConfig;
import fr.fyustorm.minetiface.intiface.MinetifaceController;
import fr.fyustorm.minetiface.intiface.ToyController;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;

public class GuiMinetiface {
    private final MinecraftClient minecraft;
    private final TextRenderer fontRenderer;

    public GuiMinetiface() {
        minecraft = MinecraftClient.getInstance();
        fontRenderer = minecraft.textRenderer;
    }

    public void onRender(MatrixStack stack, float partialTicks) {
        if (!MinetifaceConfig.INSTANCE.showIntensity) {
            return;
        }

        MinetifaceController client = MinetifaceController.getInstance();
        

        int intensity = Math.round(client.getIntensity() * 100);
        String strIntensity = "Intensity " + intensity + "%";
        String strPosition = "Position " + ToyController.instance().getLinearPosition();

        stack.scale(0.8F, 0.8F, 0.8F);
        fontRenderer.draw(stack, strIntensity, 2, 2, MinetifaceConfig.INSTANCE.intensityColor);
        fontRenderer.draw(stack, strPosition, 2, 12, MinetifaceConfig.INSTANCE.intensityColor);        
        //fontRenderer.draw(stack, "Duration " + client.getSkipDownTicks() / 20 + "s", 2, 22, 0xFF00FF);
        stack.scale(1.25F, 1.25F, 1.25F);
    }
}
