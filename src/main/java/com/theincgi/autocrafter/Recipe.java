package com.theincgi.autocrafter;

import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.BlastingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.items.ItemStackHandler;


public class Recipe {
	ItemStack output;
	public NonNullList<ItemOptions> items = NonNullList.withSize(9, ItemOptions.EMPTY);
	
	public void setRecipe(IRecipe<?> iRecipe, World worldIn){
		output = iRecipe.getRecipeOutput();
		for(int i = 0; i<items.size();i++){items.set(i, ItemOptions.EMPTY);}
		if(iRecipe instanceof ShapedRecipe){
			ShapedRecipe sr = (ShapedRecipe) iRecipe;
			NonNullList<Ingredient> ing = sr.getIngredients();
			for (int i = 0; i < ing.size(); i++) {
				ItemStack[] s = ing.get(i).getMatchingStacks();
				if(s==null){s=new ItemStack[0];}
				items.set(realIndx(i, sr.getRecipeWidth(), sr.getRecipeHeight()), new ItemOptions(s));
			}
//		}else if(iRecipe instanceof ShapedOreRecipe){
//			ShapedOreRecipe sor = (ShapedOreRecipe) iRecipe;
//			for(int i = 0; i<sor.getInput().length; i++){
//				Object o = sor.getInput()[i];
//				if(o==null){o=ItemStack.EMPTY;}
//				if(o instanceof List){
//					items.set(realIndx(i, sor.getWidth(), sor.getHeight()), new ItemOptions((List) o));
//				}
//				if(o instanceof ItemStack){
//					ItemStack is = (ItemStack) o;
//					items.set(realIndx(i, sor.getWidth(), sor.getHeight()), new ItemOptions(is));
//				}
//			}
		}else if(iRecipe instanceof ShapelessRecipe){
			ShapelessRecipe sr = (ShapelessRecipe) iRecipe;
			
			for(int i = 0; i<sr.getIngredients().size(); i++){
				items.set(i, new ItemOptions(sr.getIngredients().get(i).getMatchingStacks()));
			}
			
		if( iRecipe instanceof BlastingRecipe ) {
			output = ItemStack.EMPTY;
		}
//		}else if(iRecipe instanceof ShapelessOreRecipe){
//			ShapelessOreRecipe slor = (ShapelessOreRecipe) iRecipe;
//			for (int i = 0; i < slor.getInput().size(); i++) {
//				Object o = slor.getInput().get(i);
//				if(o==null){o=ItemStack.EMPTY;}
//				if(o instanceof List){
//					items.set(i, new ItemOptions((List) o));
//				}
//				if(o instanceof ItemStack){
//					ItemStack is = (ItemStack) o;
//					items.set(i, new ItemOptions(is));
//				}
//			}
		}else {
			Utils.log("It seems "+iRecipe.getClass().toGenericString() + " isn't a supported recipe type.", worldIn);
			output = ItemStack.EMPTY; //no free items for unsupported :c
		}
		
	}
	/**convert from grid size wid*hei to 3x3 for recipe*/
	protected int realIndx(int i, int wid, int hei){ //fixme for iron door
		int x = i%wid;
		int y = (i-x)/wid;
		return x+y*3;
	}
	
	public ItemStack getDisplayItem(int slot){
		ItemOptions opt = items.get(slot);
		if(opt.opts.size()==0){return ItemStack.EMPTY;}
		long mod = (System.currentTimeMillis()/500)%opt.opts.size();
		ItemStack is = opt.opts.get((int) mod);
		return new ItemStack(
			is.getItem(), 
			1
			//,	is.getItemDamage()==OreDictionary.WILDCARD_VALUE?(int)mod%Math.max(1,is.getMaxDamage()):is.getItemDamage()
		);
	}
	public boolean matchesRecipe(int slot, ItemStack itemStack){
		ItemOptions opt = items.get(slot);
		if(opt.opts.size()==0 && itemStack.isEmpty()){return true;}
		
		for(ItemStack i : opt.opts){
			if(matches(i, itemStack))
				return true;
		}
		return false;
	}
	
	public ListNBT getNBT(){
		ListNBT list = new ListNBT();
		list.add(getOutput().serializeNBT());
		for(int i = 0; i<items.size(); i++){
			CompoundNBT slot = new CompoundNBT();
			slot.put("item", items.get(i).getNBT());
			list.add(slot);
		}
		return list;
	}
	public static Recipe fromNBT(ListNBT tags){
		Recipe recipe = new Recipe();
		
		recipe.output = ItemStack.read(tags.getCompound(0));
		for(int i = 1; i<tags.size(); i++){
			CompoundNBT slot = tags.getCompound(i);
			int index = i;
			ItemOptions opts = ItemOptions.fromNBT(slot.getList("item", 10));
			recipe.items.set(index-1, opts);
		}
		return recipe;
	}
	
	public static class ItemOptions{
		private static final ItemOptions EMPTY = new ItemOptions();
		NonNullList<ItemStack> opts = NonNullList.create();
		public ItemOptions(){}
		
		public static ItemOptions fromNBT(ListNBT tagList) {
			ItemOptions opts = new ItemOptions();
			for(int i = 0; i<tagList.size(); i++){
				CompoundNBT itemTag = tagList.getCompound(i);
				opts.opts.add(ItemStack.read(itemTag));
			}
			return opts;
		}
		
		public NonNullList<ItemStack> getOpts() {
			return opts;
		}
		public ItemOptions(ItemStack itemStack){
			opts.add(itemStack);
		}
		public ItemOptions(NonNullList<ItemStack> opts){
			this.opts = opts;
		}
		public ItemOptions(List<ItemStack> opts){
			for (int i = 0; i < opts.size(); i++) {
				this.opts.add(opts.get(i));
			}
		}
		public ItemOptions(ItemStack[] opts){
			for (int i = 0; i < opts.length; i++) {
				this.opts.add(opts[i]);
			}
		}
		public ListNBT getNBT() {
			ListNBT list = new ListNBT();
			for(int i = 0; i<opts.size(); i++){
				list.add(opts.get(i).serializeNBT());
			}
			return list;
		}
		@Override
		public boolean equals(Object obj) {
			if(obj!=null && obj instanceof ItemOptions){
				ItemOptions io = (ItemOptions) obj;
				if(io.opts.size()!=this.opts.size()){return false;}
				for(int i = 0; i<io.opts.size(); i++){
					boolean found = false;
					ItemStack lookingFor = io.opts.get(i);
					for(int j = 0; j < this.opts.size(); j++){
						if(matches(lookingFor, this.opts.get(j))){
							found = true;
							break;
						}
					}
					if(!found){return false;}
				}
				return true;
			}
			return false;
		}
	}

	public void clearRecipe() {
		for (int i = 0; i < items.size(); i++) {
			items.set(i, ItemOptions.EMPTY);
		}
		output = ItemStack.EMPTY;
	}
	@Override
	public String toString() {
		String s = "Recipe:\n";
		for (int i = 0; i < items.size(); i++) {
			ItemOptions opts = items.get(i);
			s+="\ti: "+i+" = {";
			for (int j = 0; j < opts.opts.size(); j++) {
				s+= opts.opts.get(j).getItem().getRegistryName().toString() + " : ";
				s+= opts.opts.get(j).serializeNBT().toString();
				if(j<opts.opts.size()-1){
					s+=", ";
				}
			}
			s+="}";
			if(i!=items.size()-1){
				s+="\n";
			}
		}
		return s;
	}
	public static boolean matches(ItemStack crafts, ItemStack stack) {
		if(crafts.isEmpty()&&stack.isEmpty())return true;
		
		if(!crafts.isItemEqual(stack)) return false; //doesn't check nbt
		if(!ItemStack.areItemStackTagsEqual(crafts, stack)) return false;
		return true;
		
//		if(crafts.getItemDamage()==OreDictionary.WILDCARD_VALUE || stack.getItemDamage()==OreDictionary.WILDCARD_VALUE) return true;
//		return crafts.getItemDamage()==stack.getItemDamage();
//		return false; //TODO TODO TODO
	}
	@Override
	public boolean equals(Object obj) {
		if(obj==null)return false;
		if(!(obj instanceof Recipe))return false;
		Recipe r = (Recipe) obj;
		for(int i = 0; i<items.size(); i++){
			if(!r.items.get(i).equals(this.items.get(i))){return false;}
		}
		return true;
	}
	
	
	/**to is exclusive*/
	public NonNullList<ItemStack> getLeftovers(ItemStackHandler inv, int from, int to){
		NonNullList<ItemStack> ret = NonNullList.withSize(to-from, ItemStack.EMPTY);
        for (int i = from; i < to; i++)
        {
            ret.set(i, ForgeHooks.getContainerItem(inv.getStackInSlot(i)));
        }
        return ret;
	}
	public ItemStack getOutput() {
		if(output==null){ return ItemStack.EMPTY;}
		return output.copy();
	}
}
