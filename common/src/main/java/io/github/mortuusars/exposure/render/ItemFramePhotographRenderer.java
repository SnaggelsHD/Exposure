package io.github.mortuusars.exposure.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Either;
import com.mojang.math.Axis;
import io.github.mortuusars.exposure.ExposureClient;
import io.github.mortuusars.exposure.camera.infrastructure.FrameData;
import io.github.mortuusars.exposure.item.PhotographItem;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ItemFramePhotographRenderer {
    public static boolean render(ItemFrame itemFrameEntity, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        ItemStack itemStack = itemFrameEntity.getItem();
        if (!(itemStack.getItem() instanceof PhotographItem photographItem) || !FrameData.hasIdOrTexture(itemStack))
            return false;

        if (itemFrameEntity.getType() == EntityType.GLOW_ITEM_FRAME)
            packedLight = LightTexture.FULL_BRIGHT;

        poseStack.pushPose();

        String entityName = BuiltInRegistries.ENTITY_TYPE.getKey(itemFrameEntity.getType()).toString();
        if (entityName.equals("quark:glass_frame")) {
            poseStack.translate(0, 0, 0.475f);
        }

        // Snap to 90 degrees like a map.
        poseStack.mulPose(Axis.ZP.rotationDegrees(45 * itemFrameEntity.getRotation()));

        float size = ExposureClient.getExposureRenderer().getSize();

        float scale = 1f / size;
        float pixelScale = scale / 16f;
        scale -= pixelScale * 6;

        poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
        poseStack.scale(scale, scale, scale);
        poseStack.translate(-size / 2f, -size / 2f, 10);

        PhotographRenderer.renderPhotograph(photographItem, itemStack, false, false,
                poseStack, bufferSource, packedLight, 255, 255, 255, 255);

        poseStack.popPose();

        return true;
    }
}
