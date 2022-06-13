package com.theincgi.autocrafter.packets;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;

public class PacketClientChanged/* extends PacketTileChanged */{
//
//	public PacketClientChanged() {
//		super();
//	}
//
//	public PacketClientChanged(BlockPos p, CompoundNBT nbt) {
//		super(p, nbt);
//	}
//
//	
//	/**This happens on server side*/
//	public static class Handler implements IMessageHandler<PacketClientChanged, PacketTileChanged>{
//		@Override
//		public PacketTileChanged onMessage(PacketClientChanged message, MessageContext ctx) {
//			final PacketClientChanged fmsg = message;
//			final MessageContext fctx = ctx;
//			ctx.getServerHandler().playerEntity.getServerWorld().addScheduledTask(new Runnable() {
//				@Override
//				public void run() {
//					AutoCrafterTile tac = (AutoCrafterTile) fctx.getServerHandler().playerEntity.getServerWorld().getTileEntity(fmsg.p);
//					NBTTagCompound response = new NBTTagCompound();
//					if(fmsg.nbt.hasKey("targetChanged")){
//						NBTTagCompound how = fmsg.nbt.getCompoundTag("targetChanged");
//						if(how.hasKey("next")){
//							tac.nextRecipe();
//						}else if(how.hasKey("prev")){
//							tac.prevRecipe();
//						}else if(how.hasKey("item")){
//							tac.updateRecipes(new ItemStack(how.getCompoundTag("item")), 0);
//						}else{
//							//this shouldn't occur
//							System.err.println("Unhandled case in PacketClientChanged");
//						}
//						response.setTag("recipe", tac.getRecipe().getNBT());
//						response.setTag("targetSlot", tac.getCrafts().serializeNBT());
//						response.setInteger("rIndex", tac.getCurrentRecipeIndex());
//					}else if(fmsg.nbt.hasKey("getAll")){
//						response.setTag("tile", tac.serializeNBT());
//					}else{
//						response.setTag("recipe", tac.getRecipe().getNBT());
//						response.setTag("targetSlot", tac.getStackInSlot(AutoCrafterTile.TARGET_SLOT).serializeNBT());
//						response.setInteger("rIndex", tac.getCurrentRecipeIndex());
//					}
//					if(response.getKeySet().size()>0){
//						TargetPoint target = new TargetPoint(fctx.getServerHandler().playerEntity.dimension, 
//								fmsg.p.getX(), fmsg.p.getY(), fmsg.p.getZ(), 8);
//						AutoCrafterMod.network.sendToAllAround(new PacketServerUpdated(fmsg.p, response), target);
//					}
//				}
//			});
//			return null;
//		}
//
//	}
//
//	public static PacketClientChanged nextRecipe(BlockPos pos) {
//		NBTTagCompound tag = new NBTTagCompound();
//		NBTTagCompound chng =new NBTTagCompound();
//		tag.setTag("targetChanged", chng);
//		chng.setBoolean("next", true);
//		return new PacketClientChanged(pos, tag);
//	}
//	public static PacketClientChanged prevRecipe(BlockPos pos) {
//		NBTTagCompound tag = new NBTTagCompound();
//		NBTTagCompound chng =new NBTTagCompound();
//		tag.setTag("targetChanged", chng);
//		chng.setBoolean("prev", true);
//		return new PacketClientChanged(pos, tag);
//	}
//	public static PacketClientChanged requestAll(BlockPos pos) {
//		NBTTagCompound tag = new NBTTagCompound();
//		tag.setBoolean("getAll", true);
//		return new PacketClientChanged(pos, tag);
//	}
//
//	public static IMessage targetChanged(BlockPos pos, ItemStack heldItem) {
//		NBTTagCompound tag = new NBTTagCompound(), tag2;
//		tag.setTag("targetChanged", tag2=new NBTTagCompound());
//		tag2.setTag("item", heldItem.serializeNBT());
//		return new PacketClientChanged(pos, tag);
//	}

}
