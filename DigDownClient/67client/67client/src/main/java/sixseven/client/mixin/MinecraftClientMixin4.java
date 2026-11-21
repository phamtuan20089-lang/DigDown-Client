package sixseven.client.mixin;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sixseven.client.PearlEscapeAutomation;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin4 {
  @Inject(
      method = {"handleInputEvents", "doItemUse"},
      at = @At("HEAD"),
      cancellable = true)
  public void apply(CallbackInfo callbackInfo) {
    if (PearlEscapeAutomation.test2()) {
      callbackInfo.cancel();
    }
  }

  @Inject(method = "doAttack", at = @At("HEAD"), cancellable = true)
  public void apply(CallbackInfoReturnable callbackInfoReturnable) {
    if (PearlEscapeAutomation.test2()) {
      callbackInfoReturnable.setReturnValue(false);
    }
  }

  @Inject(method = "handleBlockBreaking", at = @At("HEAD"), cancellable = true)
  public void apply(boolean flag, CallbackInfo callbackInfo) {
    if (PearlEscapeAutomation.test2()) {
      callbackInfo.cancel();
    }
  }
}
