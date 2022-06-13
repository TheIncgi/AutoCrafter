package com.theincgi.autocrafter.proxy;

public class CommonProxy {
//	public void preInit(FMLPreInitializationEvent e) {
//		
//    }
//
//    public void init(FMLInitializationEvent e) {
//    	BlockHandler.init();
//    	BlockHandler.reg();
//    	BlockAutoCrafter.addRecipe();
//    	NetworkRegistry.INSTANCE.registerGuiHandler(AutoCrafterMod.instance, new GuiHandler());
//    	
//    	PacketClientChanged.Handler clientHandler = new PacketClientChanged.Handler();
//    	PacketServerUpdated.Handler serverHandler = new PacketServerUpdated.Handler();
//    	
//   	
//    	AutoCrafterMod.network = net.minecraftforge.fml.network.NetworkRegistry.newSimpleChannel(AutoCrafterMod.MODID);
//    	AutoCrafterMod.network.registerMessage(clientHandler, PacketClientChanged.class, 0, Side.SERVER);
//    	AutoCrafterMod.network.registerMessage(serverHandler, PacketServerUpdated.class, 1, Side.CLIENT);
//    }
//
//    public void postInit(FMLPostInitializationEvent e) {
//
//    }
//    
//    public boolean isClient(){
//    	return this instanceof ClientProxy;
//    }
//    public boolean isServer(){
//    	return this instanceof ServerProxy;
//    }
//    //Send packet to server if client side was loaded
//	public void sendPacketServer(IMessage packetTargetChanged) {}
}
