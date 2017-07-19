package com.theincgi.autocrafter.tileEntity;

import java.util.List;

import com.theincgi.autocrafter.Recipe;
import com.theincgi.autocrafter.Utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;

public class TileAutoCrafter extends TileEntity implements ITickable, ISidedInventory{
	public static final int OUTPUT_SLOT = 9, TARGET_SLOT = 10;
	private static final int[] INPUT_SLOTS = new int[]{0,1,2,3,4,5,6,7,8};
	private static final int[] OUTPUT_SLOTS = new int[]{0,1,2,3,4,5,6,7,8,OUTPUT_SLOT};
	NonNullList<ItemStack> inventory = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);
	private Recipe recipe = new Recipe();
	private ItemStack crafts = ItemStack.EMPTY;

	private String customName;
	private List<IRecipe> recipes;

	private ItemStackHandlerAutoCrafter ishac;

	public TileAutoCrafter() {
		ishac = new ItemStackHandlerAutoCrafter(this);
	}


	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		if(hasCustomName())
			compound.setString("customName", customName);
		compound.setTag("inventory", ItemStackHelper.saveAllItems(new NBTTagCompound(), inventory));
		compound.setTag("recipe", recipe.getNBT());
		compound.setTag("crafts", crafts.serializeNBT());
		return compound;
	}
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if(compound.hasKey("customName"))
			customName = compound.getString("customName");
		if(compound.hasKey("inventory"))
			ItemStackHelper.loadAllItems(compound.getCompoundTag("inventory"), inventory);
		if(compound.hasKey("recipe"))
			recipe = Recipe.fromNBT(compound.getTagList("recipe", 10));
		if(compound.hasKey("crafts"))
			crafts = new ItemStack(compound.getCompoundTag("crafts"));
	}


	@Override
	public int getSizeInventory() {
		return 11;
	}

	@Override
	public boolean isEmpty() {
		for (int i = 0; i < inventory.size(); i++) {
			if(!inventory.get(i).isEmpty()){
				return false;
			}
		};
		return true;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		if(index<0 || index >= getSizeInventory()) return ItemStack.EMPTY;
		return inventory.get(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		ItemStack s = ItemStackHelper.getAndSplit(inventory, index, count);
		if(getStackInSlot(index).getCount()==0){setInventorySlotContents(index, ItemStack.EMPTY);}
		return s;
	}
	public ItemStack SIMULATEdecrStackSize(int index, int count) {
		ItemStack temp = getStackInSlot(index).copy();
		return temp.splitStack(count);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return ItemStackHelper.getAndRemove(inventory, index);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		ItemStack itemstack = (ItemStack)this.inventory.get(index);
		this.inventory.set(index, stack);

		if (stack.getCount() > this.getInventoryStackLimit())
		{
			stack.setCount(this.getInventoryStackLimit());
		}

		this.markDirty();
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return world.getTileEntity(getPos()) == this && 
				player.getDistanceSq(pos.add(.5, .5, .5)) <= 64;
	}

	@Override
	public void openInventory(EntityPlayer player) {
	}

	@Override
	public void closeInventory(EntityPlayer player) {
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return true;
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
		for(int i = 0; i<inventory.size(); i++){
			inventory.set(i, ItemStack.EMPTY);
		}
	}

	@Override
	public String getName() {
		return hasCustomName()?customName:"Auto Crafter";
	}

	@Override
	public boolean hasCustomName() {
		return customName!=null;
	}
	@Override
	public ITextComponent getDisplayName() {
		return Utils.IText(getName());
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		if(side.equals(EnumFacing.DOWN)){return OUTPUT_SLOTS;}
		return INPUT_SLOTS;
	}


	public boolean isSlotAllowed(int index, ItemStack itemStack){
		//System.out.printf("isSlotAllowed: %d %s %s\n",index, itemStack.getItem().getRegistryName().toString(), (index<9 && recipe.matchesRecipe(index, itemStack))+"");
		return index<9 && recipe.matchesRecipe(index, itemStack);
	}
	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		//System.out.printf("canInsertItem: %d %s %s\n",index, itemStackIn.getItem().getRegistryName().toString(), isSlotAllowed(index, itemStackIn) && nextHasSameOrMore(index, itemStackIn));
		return isSlotAllowed(index, itemStackIn);
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		return index==OUTPUT_SLOT || (index<9&&!recipe.matchesRecipe(index, stack));
	}



	public void setCustomName(String displayName) {
		this.customName = displayName;
		markDirty();
	}
	public void setRecipe(IRecipe recipe) {
		this.recipe.setRecipe(recipe);
		markDirty();
	}
	public void setRecipe(NBTTagList recipeTag){
		recipe = Recipe.fromNBT(recipeTag);
	}
	public void updateRecipes(ItemStack crafts, int index) {
		this.crafts = crafts;
		recipes = Utils.getValid(crafts);
		currentRecipeIndex = index%Math.max(1,recipes.size());
		if(recipes.size()>0)
			setRecipe(recipes.get(currentRecipeIndex));
		else
			recipe.clearRecipe();
		markDirty();
	}


	public Recipe getRecipe() {
		return recipe;
	}
	/**whatever the target item is*/
	public ItemStack getCrafts() {
		return crafts;
	}

	private int currentRecipeIndex = 0;

	/**Called from server packet, then client is notified to do the same*/
	public void nextRecipe() {

		if(recipes==null){updateRecipes(getCrafts(), currentRecipeIndex);}
		if(recipes.size()==0){return;}
		currentRecipeIndex++;
		currentRecipeIndex%=recipes.size();
		setRecipe(recipes.get(currentRecipeIndex));
	}
	/**Called from server packet, then client is notified to do the same*/
	public void prevRecipe(){
		if(recipes==null){updateRecipes(getCrafts(), currentRecipeIndex);}
		if(recipes.size()==0){return;}
		currentRecipeIndex--;
		if(currentRecipeIndex<0){
			currentRecipeIndex=recipes.size()-1;
		}
		setRecipe(recipes.get(currentRecipeIndex));
	}
	public int getCurrentRecipeIndex() {
		return currentRecipeIndex;
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return (T) ishac;
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public void update() {
		//return if powered by redstone
		if(world.isBlockPowered(pos)||world.isBlockIndirectlyGettingPowered(pos)>0){return;}
		
		if(recipe.getOutput().isEmpty()){return;}
		if(getStackInSlot(OUTPUT_SLOT).getCount()+recipe.getOutput().getCount()>recipe.getOutput().getMaxStackSize()){return;}
		if(!Recipe.matches(getStackInSlot(OUTPUT_SLOT), recipe.getOutput()) && !getStackInSlot(OUTPUT_SLOT).isEmpty()){return;}

		distibuteItems();

		for(int i = 0; i<9; i++){
			if(!recipe.matchesRecipe(i, inventory.get(i))){
				return;}
		}
		NonNullList<ItemStack> leftovers = recipe.getLeftovers(inventory, 0, 9); //9, exclusive
		for(int i = 0; i<9; i++){
			inventory.get(i).shrink(1);
			if(inventory.get(i).getCount()<=0){
				setInventorySlotContents(i, ItemStack.EMPTY);
			}
			if(!leftovers.get(i).isEmpty()){
				if(inventory.get(i).isEmpty()){
					setInventorySlotContents(i, leftovers.get(i));
				}else{
					InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), leftovers.get(i)); //drop in world if no space
					//This is incase something happens like a stack of 16 milk buckets finds its way into the inventory
				}
			}
		}
		if(getStackInSlot(OUTPUT_SLOT).isEmpty()){
			setInventorySlotContents(OUTPUT_SLOT, recipe.getOutput());
		}else{
			getStackInSlot(OUTPUT_SLOT).grow(recipe.getOutput().getCount());
		}
		markDirty();
	}
	private void distibuteItems() {
		for(int i = 0; i<9; i++){
			ItemStack current = getStackInSlot(i);
			if(current.isEmpty())continue;

			int nextMatch = nextMatch(i);
			if(nextMatch<0)continue;
			if(getStackInSlot(nextMatch).isEmpty()){
				if(current.getCount()>=2){
					setInventorySlotContents(nextMatch, current.splitStack(1));
				}
			}else{
				if(current.getCount()>getStackInSlot(nextMatch).getCount()){
					current.shrink(1);
					getStackInSlot(nextMatch).grow(1);
				}
			}
		}
	}
	private int nextMatch(int j){
		ItemStack is = getStackInSlot(j);
		for(int i = 0; i<9; i++){
			int c = (i+j+1)%9;
			if(Recipe.matches(is, getStackInSlot(c)) || (getStackInSlot(c).isEmpty() && recipe.matchesRecipe(c, is))){
				if(c==j)return -1;
				return c;
			}
		}
		return -1;
	}


	


	public void setCurrentRecipeIndex(int integer) {
		currentRecipeIndex = integer;
	}

	/**for rendering*/
	public void setCrafts(ItemStack itemStack) {
		this.crafts = itemStack;
	}


	




}
