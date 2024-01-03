package de.laura.lodepad;

import com.mojang.logging.LogUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.World;

import java.util.Objects;
import java.util.Set;

public class LodepadBlockEntity extends BlockEntity {
    GlobalPos pos;

    public LodepadBlockEntity(BlockPos pos, BlockState state) {
        super(Lodepad.LODEPAD_BLOCKENTITY, pos, state);
    }

    public void teleport(Entity entity) {
        if (this.pos == null) {
            return;
        }

        ServerWorld targetWorld = Objects.requireNonNull(Objects.requireNonNull(getWorld()).getServer()).getWorld(pos.getDimension());

        if (!targetWorld.getBlockState(pos.getPos()).getBlock().equals(Blocks.LODESTONE)) {
            world.playSound(null, BlockPos.ofFloored(entity.getPos()), SoundEvents.BLOCK_RESPAWN_ANCHOR_DEPLETE.value(), SoundCategory.PLAYERS, 1.0F, 1.0F);
            return;
        }

        world.playSound(null, BlockPos.ofFloored(entity.getPos()), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);
        entity.teleport(targetWorld, pos.getPos().getX() + 0.5, pos.getPos().getY() + 1, pos.getPos().getZ() + 0.5, Set.of(PositionFlag.X, PositionFlag.Y, PositionFlag.Z, PositionFlag.X_ROT, PositionFlag.Y_ROT), entity.getYaw(), entity.getPitch());
        entity.getWorld().playSound(null, BlockPos.ofFloored(entity.getPos()), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);

        if (this.pos == null) {
            return;
        }

        nbt.put("TargetPos", NbtHelper.fromBlockPos(pos.getPos()));
        World.CODEC.encodeStart(NbtOps.INSTANCE, pos.getDimension()).resultOrPartial(LogUtils.getLogger()::error).ifPresent(nbtElement -> nbt.put("TargetDimension", nbtElement));
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        if (nbt.contains("TargetPos") && nbt.contains("TargetDimension")) {
            World.CODEC.decode(NbtOps.INSTANCE, nbt.get("TargetDimension")).resultOrPartial(LogUtils.getLogger()::error).ifPresent(registryKeyNbtElementPair -> this.pos = GlobalPos.create(registryKeyNbtElementPair.getFirst(), NbtHelper.toBlockPos(nbt.getCompound("TargetPos"))));
        }
    }
}
