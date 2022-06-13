package com.theincgi.autocrafter.network;

import static com.theincgi.autocrafter.AutoCrafterMod.MODID;

import java.util.concurrent.atomic.AtomicInteger;

import com.theincgi.autocrafter.AutoCrafterMod;
import com.theincgi.autocrafter.network.packets.TargetChangedPacket;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModNetworkChannels {
	
	private static final String PROTOCOL_VERSION = "1";
	private static final AtomicInteger ID = new AtomicInteger();
	
	public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel( 
		new ResourceLocation(MODID, "main"), 
		() -> PROTOCOL_VERSION, 
		PROTOCOL_VERSION::equals,  //client accepted version
		PROTOCOL_VERSION::equals  //server accepted version
	);
	
	public static void register() {
		CHANNEL.registerMessage(ID.getAndIncrement(), TargetChangedPacket.class, TargetChangedPacket::encode, TargetChangedPacket::decode, TargetChangedPacket::handle);
	}
	
	
	
}
