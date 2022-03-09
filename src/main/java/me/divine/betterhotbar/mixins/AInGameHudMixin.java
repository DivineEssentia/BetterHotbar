package me.divine.betterhotbar.mixins;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.util.Objects;

@Mixin(InGameHud.class)
public abstract class AInGameHudMixin extends DrawableHelper {
    double interpolatedSlotValue = 5;
    @Shadow
    private int scaledHeight;
    @Shadow
    private int scaledWidth;
    @Inject(method = "renderHotbar", at = @At("HEAD"))
    public void renderHotbar(float tickDelta, MatrixStack matrices, CallbackInfo ci) {
        int i = this.scaledWidth / 2;
        double slotDiff = Objects.requireNonNull(MinecraftClient.getInstance().player).getInventory().selectedSlot - interpolatedSlotValue;
        slotDiff = Math.abs(slotDiff) < 0.07 ? (slotDiff * 5) : slotDiff;
        interpolatedSlotValue += (slotDiff / 5);
        DrawableHelper.fill(matrices, i - 91, MinecraftClient.getInstance().getWindow().getScaledHeight() - 23, i + 91, MinecraftClient.getInstance().getWindow()
                .getScaledHeight(), new Color(28, 28, 28, 170).getRGB());
        DrawableHelper.fill(matrices, (int) (i - 91 - 1 + interpolatedSlotValue * 20), this.scaledHeight - 23, (int) ((i - 91 - 1 + interpolatedSlotValue * 20) + 24), (this.scaledHeight), new Color(43, 43, 43, 150).brighter()
                .brighter().getRGB());
    }

    @Redirect(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V", ordinal = 0))
    private void blockTexture1(InGameHud inGameHud, MatrixStack matrices, int x, int y, int u, int v, int width, int height) {
        drawTexture(matrices, x, y, this.getZOffset(), (float) u, (float) v, width, height, 256, 256);
    }

    @Redirect(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V", ordinal = 1))
    private void blockTexture2(InGameHud inGameHud, MatrixStack matrices, int x, int y, int u, int v, int width, int height) {
        drawTexture(matrices, x, y, this.getZOffset(), (float) u, (float) v, width, height, 256, 256);
    }


}
