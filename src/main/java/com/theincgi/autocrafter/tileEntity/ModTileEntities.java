package com.theincgi.autocrafter.tileEntity;

import static com.theincgi.autocrafter.AutoCrafterMod.MODID;

import com.theincgi.autocrafter.block.ModBlocks;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModTileEntities {
	public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, MODID);
	
	public static final RegistryObject<TileEntityType<AutoCrafterTile>> AUTOCRAFTER_TILE = TILE_ENTITIES.register(
		"autocrafter_tile", 
		()-> {return TileEntityType.Builder.create(	AutoCrafterTile::new, ModBlocks.AUTOCRAFTER.get() ).build(null); }
	);
	
	public static void register(IEventBus eventBu) {
		TILE_ENTITIES.register(eventBu);
	}
}
