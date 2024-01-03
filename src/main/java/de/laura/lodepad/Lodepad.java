package de.laura.lodepad;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.Instrument;
import net.minecraft.entity.EyeOfEnderEntity;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class Lodepad implements ModInitializer {
    public static final Block LODEPAD_BLOCK  = new LodepadBlock(FabricBlockSettings.create().mapColor(MapColor.BLACK).instrument(Instrument.BASEDRUM).requiresTool().strength(50.0F, 1200.0F).luminance(state -> state.get(LodepadBlock.ENABLED) ? 15 : 0));
    public static final BlockEntityType<LodepadBlockEntity> LODEPAD_BLOCKENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier("lodepad", "lodepad"), FabricBlockEntityTypeBuilder.create(LodepadBlockEntity::new, LODEPAD_BLOCK).build());

    @Override
    public void onInitialize() {
        Registry.register(Registries.BLOCK, new Identifier("lodepad", "lodepad"), LODEPAD_BLOCK);
        Registry.register(Registries.ITEM, new Identifier("lodepad", "lodepad"), new BlockItem(LODEPAD_BLOCK, new FabricItemSettings()));
    }
}
