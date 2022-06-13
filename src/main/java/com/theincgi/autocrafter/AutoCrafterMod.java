package com.theincgi.autocrafter;

import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.theincgi.autocrafter.block.ModBlocks;
import com.theincgi.autocrafter.container.ModContainers;
import com.theincgi.autocrafter.item.ModItems;
import com.theincgi.autocrafter.network.ModNetworkChannels;
import com.theincgi.autocrafter.screen.AutoCrafterScreen;
import com.theincgi.autocrafter.tileEntity.ModTileEntities;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(AutoCrafterMod.MODID)
public class AutoCrafterMod {
	public static final String
		MODID = "autocrafter",
		VERSION = "1.6";
	private static final Logger LOGGER = LogManager.getLogger();
	
	
//	public static AutoCrafterMod instance;
//	@SidedProxy(clientSide = "com.theincgi.autocrafter.proxy.ClientProxy", serverSide = "com.theincgi.autocrafter.proxy.ServerProxy")
//	public static CommonProxy proxy;
//	
//	public static BlockAutoCrafter blockAutoCrafter;
//	public static BlockItem itemAutoCrafter;
//	public static SimpleNetworkWrapper network;
	
//	@EventHandler
//	public void preInit(FMLPreInitializationEvent e){
//		
//	}
//	@EventHandler
//	public void init(FMLInitializationEvent e){
//		proxy.init(e);
//	}
//	@EventHandler
//	public void postInit(FMLPostInitializationEvent e){
//		proxy.postInit(e);
//	}
//	
	
	public AutoCrafterMod() {
		// Register the setup method for modloading
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModTileEntities.register(modEventBus);
        ModContainers.register(modEventBus);
        
		modEventBus.addListener(this::setup);
        // Register the enqueueIMC method for modloading
        modEventBus.addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        modEventBus.addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        modEventBus.addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
	}
	
	private void setup(final FMLCommonSetupEvent event)
    {
        ModNetworkChannels.register();
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
//        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().options);
    	
    	event.enqueueWork(()->{
    		ScreenManager.registerFactory(ModContainers.AUTOCRAFTER_CONTAINER.get(), AutoCrafterScreen::new);
    	});
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo("examplemod", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event)
    {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m->m.getMessageSupplier().get()).
                collect(Collectors.toList()));
    }
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            LOGGER.info("HELLO from Register Block");
        }
    }
}