package com.theincgi.autocrafter;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.text.ITextComponent;

public class Utils {
	public static void log(String s){
		Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(ITextComponent.Serializer.jsonToComponent(
				"{\"text\":\""+s+"\"}"));
	}
	public static ITextComponent IText(String s){
		return ITextComponent.Serializer.jsonToComponent(
				"{\"text\":\""+s+"\"}");
	}
	public static List<IRecipe> getValid(ItemStack sItem){
		ArrayList<IRecipe> out = new ArrayList<IRecipe>();
		List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
		for (IRecipe iRecipe : recipes) {
			ItemStack is = iRecipe.getRecipeOutput();
			if(sItem.isItemEqual(is)){
				out.add(iRecipe);
			}
		}
		return out;
	}
	public static String itemStackToString(ItemStack temp) {
		return temp.getItem().getRegistryName().toString()+":"+temp.getItemDamage()+" x"+temp.getCount();
	}
}
