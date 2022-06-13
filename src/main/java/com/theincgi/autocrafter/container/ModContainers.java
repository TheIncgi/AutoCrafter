package com.theincgi.autocrafter.container;

import com.theincgi.autocrafter.AutoCrafterMod;

import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModContainers {
	public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, AutoCrafterMod.MODID);
	
	public static final RegistryObject<ContainerType<AutoCrafterContainer>> AUTOCRAFTER_CONTAINER = CONTAINERS.register("autocrafter_container",
		()->{
			return IForgeContainerType.create( (windowId, inv, data) ->{
				BlockPos pos = data.readBlockPos();
				World world = inv.player.getEntityWorld();
				return new AutoCrafterContainer(windowId, world, pos, inv, inv.player);
			} );
		}
	);
			
	
	public static void register(IEventBus eventBus) {
		CONTAINERS.register(eventBus);
	}
	
}
