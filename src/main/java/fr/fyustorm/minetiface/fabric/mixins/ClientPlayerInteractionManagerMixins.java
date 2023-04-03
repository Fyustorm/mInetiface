package fr.fyustorm.minetiface.fabric.mixins;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import fr.fyustorm.minetiface.intiface.MinetifaceController;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixins {
	@Shadow
	@Final
	public MinecraftClient client;

	@Inject(method = "breakBlock(Lnet/minecraft/util/math/BlockPos;)Z", at = @At("HEAD"))
	private void onBreak(BlockPos pos, CallbackInfoReturnable<Boolean> ci) {
		BlockState state = client.world.getBlockState(pos);
		MinetifaceController.getInstance().onBreak(client.player, state);
	}
}
