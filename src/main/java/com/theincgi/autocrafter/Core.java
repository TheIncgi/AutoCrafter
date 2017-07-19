package com.theincgi.autocrafter;

import com.theincgi.autocrafter.blocks.BlockAutoCrafter;
import com.theincgi.autocrafter.proxy.CommonProxy;

import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

@Mod(modid = Core.MODID, version = Core.VERSION)
public class Core {
	public static final String
		MODID = "autocrafter",
		VERSION = "1.6";
	@Instance
	public static Core instance;
	@SidedProxy(clientSide = "com.theincgi.autocrafter.proxy.ClientProxy", serverSide = "com.theincgi.autocrafter.proxy.ServerProxy")
	public static CommonProxy proxy;
	
	public static BlockAutoCrafter blockAutoCrafter;
	public static ItemBlock itemAutoCrafter;
	public static SimpleNetworkWrapper network;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent e){
		proxy.preInit(e);
	}
	@EventHandler
	public void init(FMLInitializationEvent e){
		proxy.init(e);
	}
	@EventHandler
	public void postInit(FMLPostInitializationEvent e){
		proxy.postInit(e);
	}
}