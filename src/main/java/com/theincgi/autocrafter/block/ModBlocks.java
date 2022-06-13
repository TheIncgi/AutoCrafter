package com.theincgi.autocrafter.block;



import static com.theincgi.autocrafter.AutoCrafterMod.MODID;

import java.util.function.Supplier;

import com.theincgi.autocrafter.item.ModItems;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBlocks {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
	
	public static final RegistryObject<Block> AUTOCRAFTER = registerBlock("autocrafter", ()-> new BlockAutoCrafter() );
	
	
	
	
	
	
	private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block){
		RegistryObject<T> out = BLOCKS.register(name, block);
		registerBlockItem( name, out );
		
		return out;
	}
	
	private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block) {
	ModItems.ITEMS.register(name, ()->{
			return new BlockItem(
				block.get(), 
				new Item.Properties().group(ItemGroup.REDSTONE)
			);
		});
	}
	
	public static void register(IEventBus eventBus) {
		BLOCKS.register(eventBus);
	}
}
