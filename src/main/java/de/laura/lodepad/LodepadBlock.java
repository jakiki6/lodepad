package de.laura.lodepad;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.CompassItem;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class LodepadBlock extends Block implements BlockEntityProvider {
    public static final BooleanProperty ENABLED = Properties.ENABLED;

    public LodepadBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(ENABLED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(ENABLED);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient() && player.getStackInHand(hand).getItem().equals(Items.COMPASS) && player.getStackInHand(hand).getOrCreateNbt().contains("LodestonePos")) {
            LodepadBlockEntity be = (LodepadBlockEntity) world.getBlockEntity(pos);
            assert be != null;
            be.pos = CompassItem.createLodestonePos(player.getStackInHand(hand).getOrCreateNbt());
            be.markDirty();
            world.setBlockState(pos, state.with(ENABLED, true));
            world.playSound(null, pos, SoundEvents.ITEM_LODESTONE_COMPASS_LOCK, SoundCategory.PLAYERS, 1.0F, 1.2F);
            return ActionResult.SUCCESS;
        }

        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        super.onSteppedOn(world, pos, state, entity);

        if (!world.isClient()) {
            if (entity instanceof PlayerEntity) {
                if (entity.isSneaking()) {
                    ((LodepadBlockEntity) Objects.requireNonNull(world.getBlockEntity(pos))).teleport(entity);
                }
            } else {
                ((LodepadBlockEntity) Objects.requireNonNull(world.getBlockEntity(pos))).teleport(entity);
            }
        }
    }

    @Nullable
    @Override
    public LodepadBlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new LodepadBlockEntity(pos, state);
    }
}
