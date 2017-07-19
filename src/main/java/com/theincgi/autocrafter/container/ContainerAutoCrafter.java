package com.theincgi.autocrafter.container;

import com.theincgi.autocrafter.Core;
import com.theincgi.autocrafter.packets.PacketClientChanged;
import com.theincgi.autocrafter.tileEntity.TileAutoCrafter;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerAutoCrafter extends Container {

	private IInventory playerInv;
	private TileAutoCrafter tileAutoCrafter;
	private ItemStack lastTarget = null;
	public Slot targetSlot;
	public ContainerAutoCrafter(IInventory playerInv, TileAutoCrafter te) {
		this.playerInv = playerInv;
		this.tileAutoCrafter = te;

		int slot = 0;
		//0-9 crafting input // id 0-8
		for (int y = 0; y < 3; y++) {   
			for(int x = 0; x < 3; x++) {
				final int sloooooot = slot;
				addSlotToContainer(new Slot(tileAutoCrafter, slot++, 30+x*18, 17+y*18){
					@Override
					public boolean isItemValid(ItemStack stack) {
						return tileAutoCrafter.isSlotAllowed(sloooooot, stack);
					}
				});
			}
		}
		addSlotToContainer(new Slot(tileAutoCrafter, slot++, 124	,49){
			@Override
			public boolean isItemValid(ItemStack stack) {
				return false;
			}
		}); //output slot //10
		addSlotToContainer(targetSlot = new Slot(tileAutoCrafter, slot++ , 124	,18){@Override
			public int getItemStackLimit(ItemStack stack) {return 0;}
			@Override
			public void onSlotChanged() {
				super.onSlotChanged();
			}
			@Override
				public boolean canTakeStack(EntityPlayer playerIn) {
					return true;
				}
			@Override
				public boolean isItemValid(ItemStack stack) {
					return false;
				}
		}); //target slot //11

		slot = 0;
		for(int x=0; x<9; x++){
			addSlotToContainer(new Slot(playerInv, slot++, 8+x*18, 142));
		}
		slot = 9;
		for(int y=0; y<3; y++){
			for(int x = 0; x < 9; x++) {
				addSlotToContainer(new Slot(playerInv, slot++, 8+x*18, 84+y*18)); 
			}
		}
	}

	private enum Slots{
		CRAFTING(0,8),
		OUTPUT(9),
		TARGET(10),
		PLAYERINV(20,46),
		HOTBAR(11,19);
		int x,y;
		private Slots(int x){
			this(x, x);
		}
		private Slots(int x, int y){
			this.x = x;
			this.y = y;
		}
		public int getStart(){
			return x;
		}
		/**inclusive*/
		public int getEnd(){
			return y;
		}
		public static Slots getCatagory(int index){
			for(Slots s : values()){
				if(s.getStart()<=index && index<=s.getEnd())
					return s;
			}
			return null;
		}
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		ItemStack previous = ItemStack.EMPTY;
		Slot slot = (Slot) this.inventorySlots.get(index);
		Slots catagory = Slots.getCatagory(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack current = slot.getStack();
			previous = current.copy();

 			if(catagory!=null){
				switch (catagory) {
				case CRAFTING:
				case OUTPUT:
				//case TARGET: target doesnt hold anything anymore
					if(!mergeItemStack(current, Slots.PLAYERINV.getStart(), Slots.PLAYERINV.getEnd()+1, false)) return ItemStack.EMPTY;
					if(!mergeItemStack(current, Slots.HOTBAR.getStart(), Slots.HOTBAR.getEnd()+1, false)) return ItemStack.EMPTY;
					break;
				case HOTBAR:
				case PLAYERINV:
					if(tileAutoCrafter.getCrafts().isEmpty()){
						if(!mergeItemStack(current, Slots.TARGET.getStart(), Slots.TARGET.getEnd()+1, false)) return ItemStack.EMPTY;
					}else{
						if(!mergeItemStack(current, Slots.CRAFTING.getStart(), Slots.CRAFTING.getEnd()+1, false)) return ItemStack.EMPTY;
					}
				default:
					break;
				}
			}
			if (current.getCount() <= 0)
				slot.putStack(ItemStack.EMPTY);

			if (current.getCount() == previous.getCount())//amount in the slot didn't change
				return ItemStack.EMPTY;

			slot.onTake(playerIn, current);
		}
		return previous;
	}

	private ItemStack moveToCatagory(Slots catagory, InventoryPlayer inventory, ItemStack containerStack){
		for(int i = catagory.getStart(); i<=catagory.getEnd(); i++){
			ItemStack invStack = inventory.getStackInSlot(i);
			if(invStack.isItemEqual(containerStack)){
				int addable = Math.min(invStack.getMaxStackSize()-invStack.getCount(), containerStack.getCount());
				if(addable>0){
					invStack.setCount(invStack.getCount()+addable);
					containerStack.setCount(containerStack.getCount()-addable);
					if(containerStack.getCount()==0)
						return ItemStack.EMPTY;
					return containerStack;
				}
			}
		}
		for(int i = catagory.getStart(); i<=catagory.getEnd(); i++){
			ItemStack invStack = inventory.getStackInSlot(i);
			if(invStack.isEmpty()){
				inventory.mainInventory.set(i-Slots.PLAYERINV.getStart(), containerStack);
				containerStack.setCount(0);
				if(containerStack.getCount()==0)
					return ItemStack.EMPTY;
				return containerStack;
			}
		}
		return containerStack;
	}



	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}

}
