package net.heyzeer0.sd.mixins;

import net.heyzeer0.sd.ModCore;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public class MixinHeldItemRenderer {

    @Inject(at =
        @At(value = "HEAD"),
        method = "renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformation$Mode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
        cancellable = true
    )
    private void hideShield(LivingEntity entity, ItemStack stack, ModelTransformation.Mode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo callback) {
        if (!ModCore.getMain().getGeneralConfig().isEnabled || entity != MinecraftClient.getInstance().player) return;
        if (!MinecraftClient.getInstance().options.getPerspective().isFirstPerson() || stack.isEmpty() || entity.isUsingItem()) return;

        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player != null && player.getOffHandStack() != stack) return;
        if (!ModCore.getMain().getGeneralConfig().hiddenItems.contains(Registry.ITEM.getId(stack.getItem()).toString())) return;

        callback.cancel();
    }

}
