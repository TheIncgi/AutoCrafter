package com.theincgi.autocrafter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

public class Utils {
	
	
	@SuppressWarnings("resource")
	public static RecipeManager getRecipeManager(World worldIn) {
		return worldIn.getRecipeManager();
	}
	
	@SuppressWarnings("resource")
	public static void log(String s, World worldIn){
		if( isClient(worldIn) )
		Minecraft.getInstance().ingameGUI.getChatGUI().printChatMessage(ITextComponent.Serializer.getComponentFromJson(
				"{\"text\":\""+s+"\"}"));
	}
	public static ITextComponent IText(String s){
		return ITextComponent.Serializer.getComponentFromJson(
				"{\"text\":\""+s+"\"}");
	}
	public static List<IRecipe<?>> getValid(ItemStack sItem, World worldIn){
		ArrayList<IRecipe<?>> out = new ArrayList<IRecipe<?>>();
		sItem = sItem.copy();
		sItem.setCount(1);
		Collection<IRecipe<?>> recipes = getRecipeManager(worldIn).getRecipes();
		for (IRecipe<?> iRecipe : recipes) {
			ItemStack is = iRecipe.getRecipeOutput();
			if(sItem.equals(is,false)){
				out.add(iRecipe);
			}
		}
		return out;
	}
	
	public static boolean isServer( World worldIn ) {
		return worldIn.isRemote == false;
	}
	public static boolean isClient( World worldIn ) {
		return worldIn.isRemote == true;
	}
	
	public static String itemStackToString(ItemStack temp) {
		return temp.getItem().getRegistryName().toString()+" x"+temp.getCount();
	}
}
