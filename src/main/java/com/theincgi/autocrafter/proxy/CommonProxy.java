package com.theincgi.autocrafter.proxy;

import com.theincgi.autocrafter.BlockHandler;
import com.theincgi.autocrafter.Core;
import com.theincgi.autocrafter.GuiHandler;
import com.theincgi.autocrafter.blocks.BlockAutoCrafter;
import com.theincgi.autocrafter.packets.PacketClientChanged;
import com.theincgi.autocrafter.packets.PacketServerUpdated;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;

public class CommonProxy {
	public void preInit(FMLPreInitializationEvent e) {
		
    }

    public void init(FMLInitializationEvent e) {
    	BlockHandler.init();
    	BlockHandler.reg();
    	BlockAutoCrafter.addRecipe();
    	NetworkRegistry.INSTANCE.registerGuiHandler(Core.instance, new GuiHandler());
    	
    	PacketClientChanged.Handler clientHandler = new PacketClientChanged.Handler();
    	PacketServerUpdated.Handler serverHandler = new PacketServerUpdated.Handler();
    	
   	
    	Core.network = NetworkRegistry.INSTANCE.newSimpleChannel(Core.MODID);
    	Core.network.registerMessage(clientHandler, PacketClientChanged.class, 0, Side.SERVER);
    	Core.network.registerMessage(serverHandler, PacketServerUpdated.class, 1, Side.CLIENT);
    }

    public void postInit(FMLPostInitializationEvent e) {

    }
    
    public boolean isClient(){
    	return this instanceof ClientProxy;
    }
    public boolean isServer(){
    	return this instanceof ServerProxy;
    }
    //Send packet to server if client side was loaded
	public void sendPacketServer(IMessage packetTargetChanged) {}
}
