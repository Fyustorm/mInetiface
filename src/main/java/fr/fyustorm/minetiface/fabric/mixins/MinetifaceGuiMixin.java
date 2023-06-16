package fr.fyustorm.minetiface.fabric.mixins;

import net.minecraft.client.gui.DrawContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import fr.fyustorm.minetiface.gui.GuiMinetiface;
import net.minecraft.client.gui.hud.InGameHud;

@Mixin(InGameHud.class)
public abstract class MinetifaceGuiMixin {

    private static GuiMinetiface gui;

    @Inject(method = "render", at = @At("RETURN"), cancellable = true)
    public void render(DrawContext context, float tickDelta, CallbackInfo ci) {
        if (gui == null)
            gui = new GuiMinetiface();
        gui.onRender(context);
    }
}
