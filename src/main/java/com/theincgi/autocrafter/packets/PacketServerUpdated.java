package com.theincgi.autocrafter.packets;

import com.theincgi.autocrafter.Recipe;
import com.theincgi.autocrafter.Utils;
import com.theincgi.autocrafter.tileEntity.TileAutoCrafter;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**Used to tell the client that the server has changed something*/
public class PacketServerUpdated extends PacketTileChanged {

	public PacketServerUpdated() {
		super();
	}

	public PacketServerUpdated(BlockPos p, NBTTagCompound nbt) {
		super(p, nbt);
	}

	public static class Handler implements IMessageHandler<PacketServerUpdated, IMessage>{
		@Override
		public IMessage onMessage(final PacketServerUpdated message, final MessageContext ctx) {
			Minecraft.getMinecraft().addScheduledTask(new Runnable() {
				@Override
				public void run() {
					TileAutoCrafter ourTile = (TileAutoCrafter) Minecraft.getMinecraft().player.world.getTileEntity(message.p);
					if(message.nbt.hasKey("tile")){
						ourTile.readFromNBT(message.nbt.getCompoundTag("tile"));
						return;
					}
					if(message.nbt.hasKey("targetSlot")){
						ItemStack newTarget = new ItemStack(message.nbt.getCompoundTag("targetSlot"));
						if(!Recipe.matches(ourTile.getCrafts(), newTarget)){//target is new
							ourTile.setCrafts(newTarget);
						}
					}
					if(message.nbt.hasKey("recipe")){
						ourTile.setRecipe(message.nbt.getTagList("recipe",10));
					}
					
					if(message.nbt.hasKey("rIndex")){
						ourTile.setCurrentRecipeIndex(message.nbt.getInteger("rIndex"));
					}
				}
			});
			return null;
		}
	}
}
