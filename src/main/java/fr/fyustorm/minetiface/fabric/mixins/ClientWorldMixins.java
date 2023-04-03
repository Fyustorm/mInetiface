package fr.fyustorm.minetiface.fabric.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import fr.fyustorm.minetiface.intiface.MinetifaceController;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;

@Mixin(ClientWorld.class)
public class ClientWorldMixins {

	@Inject(at = @At("TAIL"), method = "removeEntity", locals = LocalCapture.CAPTURE_FAILEXCEPTION)
	public void onRemoveEntity(int entityId, Entity.RemovalReason removalReason, CallbackInfo ci, Entity entity) {
		MinetifaceController.getInstance().onWorldExit(entity);
	}

	@Inject(at = @At("HEAD"), method = "addEntityPrivate")
	public void onEntityAdded(int id, Entity entity, CallbackInfo ci) {
		MinetifaceController.getInstance().onWorldEntry(entity);
	}
}
