package com.theincgi.autocrafter.network;

import static com.theincgi.autocrafter.AutoCrafterMod.MODID;

import java.util.concurrent.atomic.AtomicInteger;

import com.theincgi.autocrafter.AutoCrafterMod;
import com.theincgi.autocrafter.network.packets.NotifyPlayerTargetChangedPacket;
import com.theincgi.autocrafter.network.packets.NotifyPlayerTileEntity;
import com.theincgi.autocrafter.network.packets.RequestState;
import com.theincgi.autocrafter.network.packets.TargetChangedPacket;
import com.theincgi.autocrafter.network.packets.TargetCycledPacket;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModNetworkChannels {
	
	private static final String PROTOCOL_VERSION = "1";
	private static int id = 0;
	
	public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel( 
		new ResourceLocation(MODID, "main"), 
		() -> PROTOCOL_VERSION, 
		PROTOCOL_VERSION::equals,  //client accepted version
		PROTOCOL_VERSION::equals  //server accepted version
	);
	
	public static void register() {
		CHANNEL.registerMessage(id++, TargetChangedPacket.class, TargetChangedPacket::encode, TargetChangedPacket::decode, TargetChangedPacket::handle);
		CHANNEL.registerMessage(id++, TargetCycledPacket.class, TargetCycledPacket::encode, TargetCycledPacket::decode, TargetCycledPacket::handle);
		CHANNEL.registerMessage(id++, NotifyPlayerTargetChangedPacket.class, NotifyPlayerTargetChangedPacket::encode, NotifyPlayerTargetChangedPacket::decode, NotifyPlayerTargetChangedPacket::handle);
		CHANNEL.registerMessage(id++, RequestState.class, RequestState::encode, RequestState::decode, RequestState::handle);
		CHANNEL.registerMessage(id++, NotifyPlayerTileEntity.class, NotifyPlayerTileEntity::encode, NotifyPlayerTileEntity::decode, NotifyPlayerTileEntity::handle);
	}
	
	
	
}
