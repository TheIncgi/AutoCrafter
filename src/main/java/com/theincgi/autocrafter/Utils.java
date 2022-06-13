package com.theincgi.autocrafter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.text.ITextComponent;

public class Utils {
	public static Minecraft getMinecraft() {
		return Minecraft.getInstance();
	}
	
	@SuppressWarnings("resource")
	public static RecipeManager getRecipeManager() {
		return getMinecraft().world.getRecipeManager();
	}
	
	@SuppressWarnings("resource")
	public static void log(String s){
		getMinecraft().ingameGUI.getChatGUI().printChatMessage(ITextComponent.Serializer.getComponentFromJson(
				"{\"text\":\""+s+"\"}"));
	}
	public static ITextComponent IText(String s){
		return ITextComponent.Serializer.getComponentFromJson(
				"{\"text\":\""+s+"\"}");
	}
	public static List<IRecipe<?>> getValid(ItemStack sItem){
		ArrayList<IRecipe<?>> out = new ArrayList<IRecipe<?>>();
		
		Collection<IRecipe<?>> recipes = getRecipeManager().getRecipes();
		for (IRecipe<?> iRecipe : recipes) {
			ItemStack is = iRecipe.getRecipeOutput();
			if(sItem.equals(is,false)){
				out.add(iRecipe);
			}
		}
		return out;
	}
	public static String itemStackToString(ItemStack temp) {
		return temp.getItem().getRegistryName().toString()+" x"+temp.getCount();
	}
}
