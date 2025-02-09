package io.github.mortuusars.exposure.network.packet.server;

import com.google.common.base.Preconditions;
import io.github.mortuusars.exposure.Exposure;
import io.github.mortuusars.exposure.camera.Camera;
import io.github.mortuusars.exposure.camera.infrastructure.CompositionGuide;
import io.github.mortuusars.exposure.network.PacketDirection;
import io.github.mortuusars.exposure.network.packet.IPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public record CameraSetCompositionGuideC2SP(CompositionGuide guide) implements IPacket {
    public static final ResourceLocation ID = Exposure.resource("camera_set_composition_guide");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public FriendlyByteBuf toBuffer(FriendlyByteBuf buffer) {
        guide.toBuffer(buffer);
        return buffer;
    }

    public static CameraSetCompositionGuideC2SP fromBuffer(FriendlyByteBuf buffer) {
        return new CameraSetCompositionGuideC2SP(CompositionGuide.fromBuffer(buffer));
    }

    @Override
    public boolean handle(PacketDirection direction, @Nullable Player player) {
        Preconditions.checkState(player != null, "Cannot handle packet {}: Player was null", ID);
        Camera.getCamera(player).ifPresent(c -> c.get().getItem().setCompositionGuide(c.get().getStack(), guide));
        return true;
    }
}
