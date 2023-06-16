package fr.fyustorm.minetiface.gui;

import fr.fyustorm.minetiface.config.MinetifaceConfig;
import fr.fyustorm.minetiface.intiface.MinetifaceController;
import fr.fyustorm.minetiface.intiface.ToyController;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

import java.text.DecimalFormat;

public class GuiMinetiface {

    private final TextRenderer fontRenderer;

    public GuiMinetiface() {
        fontRenderer = MinecraftClient.getInstance().textRenderer;
    }

    public void onRender(DrawContext context) {
        if (!MinetifaceConfig.INSTANCE.showIntensity) {
            return;
        }

        MinetifaceController client = MinetifaceController.getInstance();


        DecimalFormat df = new DecimalFormat("0.00");
        int intensity = Math.round(client.getIntensity() * 100);
        String strIntensity = "Intensity " + intensity + "%";
        String strPosition = "Position " + df.format(ToyController.instance().getLinearPosition());

        context.drawTextWithShadow(fontRenderer, strIntensity, 2, 2, MinetifaceConfig.INSTANCE.intensityColor);
        context.drawTextWithShadow(fontRenderer, strPosition, 2, 12, MinetifaceConfig.INSTANCE.intensityColor);
    }
}
