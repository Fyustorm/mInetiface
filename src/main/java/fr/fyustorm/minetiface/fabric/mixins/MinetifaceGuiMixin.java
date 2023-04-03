package fr.fyustorm.minetiface.fabric.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import fr.fyustorm.minetiface.gui.GuiMinetiface;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;

@Mixin(InGameHud.class)
public abstract class MinetifaceGuiMixin {

    private static GuiMinetiface gui;

    @Inject(method = "render", at = @At("RETURN"), cancellable = true) 
    private void onRender(MatrixStack stack, float tickDelta, CallbackInfo ci) {
        if (gui == null)
            gui = new GuiMinetiface();
        gui.onRender(stack, tickDelta);
    }
}
