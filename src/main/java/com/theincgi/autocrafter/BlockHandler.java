package com.theincgi.autocrafter;

import com.theincgi.autocrafter.blocks.BlockAutoCrafter;
import com.theincgi.autocrafter.tileEntity.TileAutoCrafter;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockHandler {
	public static void init(){
		Core.blockAutoCrafter = new BlockAutoCrafter();
		Core.itemAutoCrafter  = new ItemBlock(Core.blockAutoCrafter){@Override public String getItemStackDisplayName(ItemStack stack){return "Auto Crafter";}};
	}
	public static void reg(){
		GameRegistry.register(Core.blockAutoCrafter);
		GameRegistry.register(Core.itemAutoCrafter, Core.blockAutoCrafter.getRegistryName());
		GameRegistry.registerTileEntity(TileAutoCrafter.class, "autocrafter_tile_entity");
	}
	public static void regRends(){
		regRenderer(Core.blockAutoCrafter);
	}
	private static void regRenderer(Block b){
    	Item i = Item.getItemFromBlock(b);
    	Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(i, 0, new ModelResourceLocation(b.getRegistryName(), "inventory"));
    }
}