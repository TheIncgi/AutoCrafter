package com.theincgi.autocrafter.item;

import static com.theincgi.autocrafter.AutoCrafterMod.MODID;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
	
	public static final RegistryObject<Item> COMPONENT = ITEMS.register("component", ()-> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
	
	
//	private static <T extends Item> RegistryObject<T> registerItem(String name, Supplier<T> item) {
//		RegistryObject<T> out = ITEMS.register(name, item);
//		return out;
//	}
//	
//	private static <T extends Item> void registerItem(String name, RegistryObject<T> item) {
//		
//	}
	
	public static void register(IEventBus eventBus) {
		ITEMS.register(eventBus);
	}
}
