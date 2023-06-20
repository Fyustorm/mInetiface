package fr.fyustorm.minetiface.mixin;

import fr.fyustorm.minetiface.commons.intiface.MinetifaceController;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixins {

    private final static Logger LOGGER = LoggerFactory.getLogger(ClientPlayerInteractionManagerMixins.class);

    @Shadow
    @Final
    private MinecraftClient client;
    @Inject(method = "breakBlock(Lnet/minecraft/util/math/BlockPos;)Z", at = @At("HEAD"))
    private void onBreak(BlockPos pos, CallbackInfoReturnable<Boolean> ci) {
        World world = client.world;
        ClientPlayerEntity player = client.player;
        if (world == null || player == null) {
            return;
        }

        BlockState state = world.getBlockState(pos);
        MinetifaceController.getInstance().onBreak(state.getBlock().getTranslationKey());
    }
}
