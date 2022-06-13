package com.theincgi.autocrafter.tileEntity;

import com.theincgi.autocrafter.Recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.items.ItemStackHandler;

public class ItemStackHandlerAutoCrafter extends ItemStackHandler {
	private AutoCrafterTile tac;
	public ItemStackHandlerAutoCrafter(AutoCrafterTile tac) {
		super(11);
		this.tac = tac;
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		if(tac.canInsertItem(slot, stack, Direction.UP)){
			int space = getSpaceFor(slot, stack);

			if(simulate){
				if(stack.getCount()<=space)
					return ItemStack.EMPTY; //remainder none
				ItemStack m = stack.copy();
				m.shrink(space);
				return m;
			}else{
				//System.out.printf("Inserting Item %d %s %s\t\n",slot, stack.getItem().getRegistryName().toString(), "x"+stack.getCount());
				int newCount = getStackInSlot(slot).getCount()+stack.getCount();
				newCount = Math.min(newCount, getStackInSlot(slot).getMaxStackSize());
				if(getStackInSlot(slot).isEmpty()){
					setStackInSlot(slot, stack.copy().split(newCount));
				}else{
					getStackInSlot(slot).setCount(newCount);
				}
				ItemStack temp = stack.copy();
				temp.shrink(space);
				return temp;
			}

		}else{
			return stack;
		}
	}

	@Override
	protected void onContentsChanged(int slot) {
		tac.markDirty();
	}
	@Override
	public boolean isItemValid(int slot, ItemStack stack) {
//		return true;
		return getSpaceFor(slot, stack) > 0;
	}
	
	
	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		ItemStack stackIn = getStackInSlot(slot);
		if(tac.canExtractItem(slot, stackIn, Direction.DOWN)){
			if(simulate){
				return tac.SIMULATEdecrStackSize(slot, amount);
			}else{
				return tac.decrStackSize(slot, amount);
			}
		}
		return ItemStack.EMPTY;
	}

	public int getSpaceFor(int indx, ItemStack s){
		if(!tac.getRecipe().matchesRecipe(indx, s)){
			int x = 0;
		}
		if(tac.getRecipe().matchesRecipe(indx, s)){
			if(Recipe.matches(s, getStackInSlot(indx))){
				return s.getMaxStackSize()-getStackInSlot(indx).getCount();
			}
			if(getStackInSlot(indx).isEmpty())
				return s.getMaxStackSize();
		}
		return 0;
	}

	@Override
	public int getSlotLimit(int slot) {
		return getStackInSlot(slot).getMaxStackSize();
	}

	

}
