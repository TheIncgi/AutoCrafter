package com.theincgi.autocrafter.packets;

import com.theincgi.autocrafter.Core;
import com.theincgi.autocrafter.Recipe;
import com.theincgi.autocrafter.tileEntity.TileAutoCrafter;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketClientChange extends PacketTileChanged{

	public PacketClientChange() {
		super();
	}

	public PacketClientChange(BlockPos p, NBTTagCompound nbt) {
		super(p, nbt);
	}

	
	/**This happens on server side*/
	public static class Handler implements IMessageHandler<PacketClientChange, PacketTileChanged>{
		@Override
		public PacketTileChanged onMessage(PacketClientChange message, MessageContext ctx) {
			final PacketClientChange fmsg = message;
			final MessageContext fctx = ctx;
			ctx.getServerHandler().playerEntity.getServerWorld().addScheduledTask(new Runnable() {
				@Override
				public void run() {
					TileAutoCrafter tac = (TileAutoCrafter) fctx.getServerHandler().playerEntity.getServerWorld().getTileEntity(fmsg.p);
					NBTTagCompound response = new NBTTagCompound();
					if(fmsg.nbt.hasKey("targetChanged")){
						NBTTagCompound how = fmsg.nbt.getCompoundTag("targetChanged");
						if(how.hasKey("next")){
							tac.nextRecipe();
						}else if(how.hasKey("prev")){
							tac.prevRecipe();
						}else if(how.hasKey("item")){
							//System.out.println("Updating craft target: ");
							//System.out.println(how.getCompoundTag("item"));
							tac.updateRecipes(new ItemStack(how.getCompoundTag("item")), 0);
						}else{
							//????
						}
						response.setTag("recipe", tac.getRecipe().getNBT());
						response.setTag("targetSlot", tac.getCrafts().serializeNBT());
						response.setInteger("rIndex", tac.getCurrentRecipeIndex());
					}else if(fmsg.nbt.hasKey("getAll")){
						response.setTag("tile", tac.serializeNBT());
					}else{
						response.setTag("recipe", tac.getRecipe().getNBT());
						response.setTag("targetSlot", tac.getStackInSlot(TileAutoCrafter.TARGET_SLOT).serializeNBT());
						response.setInteger("rIndex", tac.getCurrentRecipeIndex());
					}
					if(response.getKeySet().size()>0){
						TargetPoint target = new TargetPoint(fctx.getServerHandler().playerEntity.dimension, 
								fmsg.p.getX(), fmsg.p.getY(), fmsg.p.getZ(), 8);
						Core.network.sendToAllAround(new PacketServerUpdated(fmsg.p, response), target);
					}
				}
			});
			return null;
		}

	}

	public static PacketClientChange nextRecipe(BlockPos pos) {
		NBTTagCompound tag = new NBTTagCompound();
		NBTTagCompound chng =new NBTTagCompound();
		tag.setTag("targetChanged", chng);
		chng.setBoolean("next", true);
		return new PacketClientChange(pos, tag);
	}
	public static PacketClientChange prevRecipe(BlockPos pos) {
		NBTTagCompound tag = new NBTTagCompound();
		NBTTagCompound chng =new NBTTagCompound();
		tag.setTag("targetChanged", chng);
		chng.setBoolean("prev", true);
		return new PacketClientChange(pos, tag);
	}
	public static PacketClientChange requestAll(BlockPos pos) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setBoolean("getAll", true);
		return new PacketClientChange(pos, tag);
	}

	public static IMessage targetChanged(BlockPos pos, ItemStack heldItem) {
		NBTTagCompound tag = new NBTTagCompound(), tag2;
		tag.setTag("targetChanged", tag2=new NBTTagCompound());
		tag2.setTag("item", heldItem.serializeNBT());
		return new PacketClientChange(pos, tag);
	}

}
