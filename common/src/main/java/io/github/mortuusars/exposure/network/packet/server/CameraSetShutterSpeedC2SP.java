package io.github.mortuusars.exposure.network.packet.server;

import com.google.common.base.Preconditions;
import io.github.mortuusars.exposure.camera.infrastructure.ShutterSpeed;
import io.github.mortuusars.exposure.network.PacketDirection;
import io.github.mortuusars.exposure.network.packet.IPacket;
import io.github.mortuusars.exposure.util.CameraInHand;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public record CameraSetShutterSpeedC2SP(ShutterSpeed shutterSpeed) implements IPacket<CameraSetShutterSpeedC2SP> {
    public void toBuffer(FriendlyByteBuf buffer) {
        shutterSpeed.toBuffer(buffer);
    }

    public static CameraSetShutterSpeedC2SP fromBuffer(FriendlyByteBuf buffer) {
        return new CameraSetShutterSpeedC2SP(ShutterSpeed.fromBuffer(buffer));
    }

    @Override
    public boolean handle(PacketDirection direction, @Nullable Player player) {
        Preconditions.checkState(player != null, "Cannot handle packet: Player was null");

        CameraInHand camera = CameraInHand.getActive(player);
        if (!camera.isEmpty()) {
            camera.getItem().setShutterSpeed(camera.getStack(), shutterSpeed);
        }

        return true;
    }
}
