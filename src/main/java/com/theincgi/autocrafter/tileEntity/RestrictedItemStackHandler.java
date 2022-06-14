package com.theincgi.autocrafter.tileEntity;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.items.ItemStackHandler;

public class RestrictedItemStackHandler extends ItemStackHandler {

	private ItemStackHandlerAutoCrafter ishac;

	public RestrictedItemStackHandler(ItemStackHandlerAutoCrafter ishac) {
		this.ishac = ishac;
	}
	
	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		if(ishac.tac.canExtractItem(slot, amount, Direction.DOWN))
			return ishac.extractItem(slot, amount, simulate);
		return ItemStack.EMPTY;
	}
	
	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		ishac.deserializeNBT(nbt);
	}

	@Override
	public void setSize(int size) {
		ishac.setSize(size);
	}

	@Override
	public void setStackInSlot(int slot, ItemStack stack) {
		ishac.setStackInSlot(slot, stack);
	}

	@Override
	public int getSlots() {
		return ishac.getSlots();
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return ishac.getStackInSlot(slot);
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		return ishac.insertItem(slot, stack, simulate);
	}

	@Override
	public int getSlotLimit(int slot) {
		return ishac.getSlotLimit(slot);
	}

	@Override
	public boolean isItemValid(int slot, ItemStack stack) {
		return ishac.isItemValid(slot, stack);
	}

	@Override
	public CompoundNBT serializeNBT() {
		return ishac.serializeNBT();
	}

	@Override
	protected void onContentsChanged(int slot) {
		ishac.onContentsChanged(slot);
	}

	@Override
	public boolean equals(Object obj) {
		return ishac.equals(obj);
	}

	@Override
	public int hashCode() {
		return ishac.hashCode();
	}

	@Override
	public String toString() {
		return ishac.toString();
	}
	
	

}
