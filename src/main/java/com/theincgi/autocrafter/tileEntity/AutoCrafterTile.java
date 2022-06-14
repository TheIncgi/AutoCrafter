package com.theincgi.autocrafter.tileEntity;

import java.util.List;

import com.theincgi.autocrafter.AutoCrafterMod;
import com.theincgi.autocrafter.Recipe;
import com.theincgi.autocrafter.Utils;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class AutoCrafterTile extends TileEntity implements ITickableTileEntity, ISidedInventory{
	public static final int OUTPUT_SLOT = 9, TARGET_SLOT = 10;
	private static final int[] INPUT_SLOTS = new int[]{0,1,2,3,4,5,6,7,8};
	private static final int[] OUTPUT_SLOTS = new int[]{0,1,2,3,4,5,6,7,8,OUTPUT_SLOT};
//	NonNullList<ItemStack> inventory = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);
	private Recipe recipe = new Recipe();
	private ItemStack crafts = ItemStack.EMPTY;

	private String customName;
	private List<IRecipe<?>> recipes;

	private ItemStackHandlerAutoCrafter ishac;
	private final LazyOptional<IItemHandler> handlerRestricted = LazyOptional.of(()-> new RestrictedItemStackHandler( ishac ));
	private final LazyOptional<IItemHandler> handler = LazyOptional.of(()-> ishac );
	
	public AutoCrafterTile(TileEntityType<?> tileEntityType) {
		super(tileEntityType);
		ishac = new ItemStackHandlerAutoCrafter(this);
	}
	
	public AutoCrafterTile() {
		this(ModTileEntities.AUTOCRAFTER_TILE.get());
	}
	
	@Override
	public boolean isUsableByPlayer(PlayerEntity player) {
		return true;
	}
	
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if( facing == null )
				return handler.cast();
			return handlerRestricted.cast();
		}
		
		return super.getCapability(capability, facing);
	}
	
	
	@Override
	public CompoundNBT write(CompoundNBT compound) {
		AutoCrafterMod.LOGGER.debug("WRITE NBT");
		compound = super.write(compound);
		if(customName!=null)
			compound.putString("customName", customName);
		
		
		compound.put("inventory", ishac.serializeNBT());
		compound.put("recipe", recipe.getNBT());
		compound.put("crafts", crafts.serializeNBT());
		return compound;
	}

	@Override
	public void read(BlockState state, CompoundNBT nbt) {
		super.read(state, nbt);
		AutoCrafterMod.LOGGER.debug("READ NBT");
		if(nbt.contains("customName"))
			customName = nbt.getString("customName");
		if(nbt.contains("inventory")) {
			ishac.deserializeNBT(nbt.getCompound("inventory"));
		}
		if(nbt.contains("recipe"))
			recipe = Recipe.fromNBT(nbt.getList("recipe", 10));
		if(nbt.contains("crafts"))
			crafts = ItemStack.read(nbt.getCompound("crafts"));
	}


//	@Override
//	public int getSizeInventory() {
//		return 11;
//	}

	@Override
	public boolean isEmpty() {
		for (int i = 0; i < ishac.getSlots(); i++) {
			if(!ishac.getStackInSlot(i).isEmpty()){
				return false;
			}
		};
		return true;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		if(index<0 || index >= getSizeInventory()) return ItemStack.EMPTY;
		return ishac.getStackInSlot(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		ItemStack s = ishac.getStackInSlot(index).split(count);
		if(getStackInSlot(index).getCount()==0){setInventorySlotContents(index, ItemStack.EMPTY);}
		this.markDirty();
		return s;
	}
	public ItemStack SIMULATEdecrStackSize(int index, int count) {
		ItemStack temp = getStackInSlot(index).copy();
		return temp.split(count);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		ItemStack s = ishac.getStackInSlot(index);
		ishac.setStackInSlot(index, ItemStack.EMPTY);
		this.markDirty();
		return s;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		this.ishac.setStackInSlot(index, stack);

		this.markDirty();
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public void clear() {
		
		for(int i = 0; i<ishac.getSlots(); i++){
			ishac.setStackInSlot(i, ItemStack.EMPTY);
		}
		this.markDirty();
	}
//
//	@Override
//	public String getName() {
//		return hasCustomName()?customName:"Auto Crafter";
//	}
//
//	@Override
//	public boolean hasCustomName() {
//		return customName!=null;
//	}
//	dis
//	@Override
//	public ITextComponent getDisplayName() {
//		return Utils.IText(customName);
//	}
//
	@Override
	public int[] getSlotsForFace(Direction side) {
		if(side.equals(Direction.DOWN)){return OUTPUT_SLOTS;}
		return INPUT_SLOTS;
	}


	public boolean isSlotAllowed(int index, ItemStack itemStack){
		//System.out.printf("isSlotAllowed: %d %s %s\n",index, itemStack.getItem().getRegistryName().toString(), (index<9 && recipe.matchesRecipe(index, itemStack))+"");
		return itemStack.isEmpty() || index<9 && recipe.matchesRecipe(index, itemStack);
	}
	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, Direction direction) {
		//System.out.printf("canInsertItem: %d %s %s\n",index, itemStackIn.getItem().getRegistryName().toString(), isSlotAllowed(index, itemStackIn) && nextHasSameOrMore(index, itemStackIn));
		return isSlotAllowed(index, itemStackIn);
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
		return index==OUTPUT_SLOT || (index<9&&!recipe.matchesRecipe(index, stack));
	}
	
	public boolean canExtractItem(int index, int amount, Direction direction) {
		ItemStack x = ishac.getStackInSlot(index).copy();
		x.setCount(amount);
		return canExtractItem(index, x, direction);
	}



	public void setCustomName(String displayName) {
		this.customName = displayName;
		markDirty();
	}
	public void setRecipe(IRecipe<?> recipe) {
		this.recipe.setRecipe(recipe);
		markDirty();
	}
	public void setRecipe(ListNBT recipeTag){
		recipe = Recipe.fromNBT(recipeTag);
		markDirty();
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
	public void tick() {
		//return if powered by redstone
		if(world.isBlockPowered(pos)||world.getRedstonePowerFromNeighbors(pos)>0){return;}
		
		if(recipe.getOutput().isEmpty()){return;}
		if(getStackInSlot(OUTPUT_SLOT).getCount()+recipe.getOutput().getCount()>recipe.getOutput().getMaxStackSize()){return;}
		if(!Recipe.matches(getStackInSlot(OUTPUT_SLOT), recipe.getOutput()) && !getStackInSlot(OUTPUT_SLOT).isEmpty()){return;}

		distibuteItems();

		for(int i = 0; i<9; i++){
			if(!recipe.matchesRecipe(i, ishac.getStackInSlot(i))){
				return;}
		}
		NonNullList<ItemStack> leftovers = recipe.getLeftovers(ishac, 0, 9); //9, exclusive
		for(int i = 0; i<9; i++){
			ishac.getStackInSlot(i).shrink(1);
			if(ishac.getStackInSlot(i).getCount()<=0){
				setInventorySlotContents(i, ItemStack.EMPTY);
			}
			if(!leftovers.get(i).isEmpty()){
				if(ishac.getStackInSlot(i).isEmpty()){
					ishac.setStackInSlot(i, leftovers.get(i));
				}else{
					InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), leftovers.get(i)); //drop in world if no space
					//This is incase something happens like a stack of 16 milk buckets finds its way into the inventory
				}
			}
		}
		if(getStackInSlot(OUTPUT_SLOT).isEmpty()){
			ishac.setStackInSlot(OUTPUT_SLOT, recipe.getOutput());
		}else{
			ishac.getStackInSlot(OUTPUT_SLOT).grow(recipe.getOutput().getCount());
		}
		markDirty();
	}
	private void distibuteItems() {
		boolean moved = false;
		for(int i = 0; i<9; i++){
			ItemStack current = getStackInSlot(i);
			if(current.isEmpty())continue;

			int nextMatch = nextMatch(i);
			if(nextMatch<0)continue;
			if(getStackInSlot(nextMatch).isEmpty()){
				if(current.getCount()>=2){
					ishac.setStackInSlot(nextMatch, current.split(1));
					moved = true;
				}
			}else{
				if(current.getCount()>getStackInSlot(nextMatch).getCount()){
					current.shrink(1);
					getStackInSlot(nextMatch).grow(1);
					moved = true;
				}
			}
		}
		if(moved) {
			//TODO play a shuffling sound
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
		//TODO save recipe options list as nbt?
		markDirty();
	}

	/**for rendering*/
	public void setCrafts(ItemStack itemStack) {
		this.crafts = itemStack;
		markDirty();
	}

	@Override
	public int getSizeInventory() {
		return 11;
	}

	public ItemStackHandlerAutoCrafter getItemStackHandler() {
		return ishac;
	}


	




}
