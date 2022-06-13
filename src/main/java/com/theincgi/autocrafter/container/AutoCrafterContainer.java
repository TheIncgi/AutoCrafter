package com.theincgi.autocrafter.container;

import com.theincgi.autocrafter.block.ModBlocks;
import com.theincgi.autocrafter.tileEntity.AutoCrafterTile;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class AutoCrafterContainer extends Container {

	private final PlayerEntity playerEntity;
	private IItemHandler playerInventory;

	private AutoCrafterTile tileEntity;

	private ItemStack lastTarget = null;
	public SlotItemHandler targetSlot;



	public AutoCrafterContainer(int windowID, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
		super( ModContainers.AUTOCRAFTER_CONTAINER.get() , windowID );
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof AutoCrafterTile)
			this.tileEntity = (AutoCrafterTile) te;
		this.playerEntity = player;
		this.playerInventory = new InvWrapper(playerInventory);

		if(tileEntity != null) {
			tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
				int slot = 0;
				//0-9 crafting input // id 0-8
				for (int y = 0; y < 3; y++) {   
					for(int x = 0; x < 3; x++) {
						final int sloooooot = slot;
						SlotItemHandler itemSlot = new SlotItemHandler(h, slot++, 30+x*18, 17+y*18) {
							@Override
							public boolean isItemValid(ItemStack stack) {
								return tileEntity.isSlotAllowed(sloooooot, stack);
							}
						};
						addSlot( itemSlot );
					}
				}

				addSlot(new SlotItemHandler(h, slot++, 124	,49){
					@Override
					public boolean isItemValid(ItemStack stack) {
						return false;
					}
				}); //output slot //10

				addSlot(targetSlot = new SlotItemHandler(h, slot++ , 124	,18){
					@Override
					public int getItemStackLimit(ItemStack stack) {
						return 0;
					}
					@Override
					public void onSlotChange(ItemStack oldStackIn, ItemStack newStackIn) {
						super.onSlotChange(oldStackIn, newStackIn);
					}
					@Override
					public boolean canTakeStack(PlayerEntity playerIn) {
						return true;
					}
					public boolean isItemValid(ItemStack stack) {
						return false;
					}
				}); //target slot //11
				slot = 0;
				for(int x=0; x<9; x++){
					addSlot(new SlotItemHandler( AutoCrafterContainer.this.playerInventory , slot++, 8+x*18, 142));
				}
				slot = 9;
				for(int y=0; y<3; y++){
					for(int x = 0; x < 9; x++) {
						addSlot(new SlotItemHandler(AutoCrafterContainer.this.playerInventory, slot++, 8+x*18, 84+y*18)); 
					}
				}
			});
		}
		// TODO Auto-generated constructor stub
	}

	 
	
	public AutoCrafterTile getTileEntity() {
		return tileEntity;
	}


	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		return isWithinUsableDistance(IWorldPosCallable.of(tileEntity.getWorld(), tileEntity.getPos()), playerIn, ModBlocks.AUTOCRAFTER.get());
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
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
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
					if(tileEntity.getCrafts().isEmpty()){
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

	private ItemStack moveToCatagory(Slots catagory, PlayerInventory inventory, ItemStack containerStack){
		for(int i = catagory.getStart(); i<=catagory.getEnd(); i++){
			ItemStack invStack = inventory.getStackInSlot(i);
			if(invStack.equals(containerStack,false)){
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

	

	


}
