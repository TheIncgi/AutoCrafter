package com.theincgi.autocrafter.network.packets;

import java.util.function.Supplier;

import com.theincgi.autocrafter.Utils;
import com.theincgi.autocrafter.tileEntity.AutoCrafterTile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

public class NotifyPlayerTargetChangedPacket {
	public ItemStack targetItem;
	public BlockPos blockPos;
	public int recipeNo;

	public NotifyPlayerTargetChangedPacket( ItemStack targetItem, int recipeNo, BlockPos blockPos ) {
		this.targetItem = targetItem;
		this.blockPos = blockPos;
		this.recipeNo = recipeNo;
	}
	
//	public void encode(PacketBuffer buffer) {
//		encode(this, buffer);
//	}
	
	public static void encode(NotifyPlayerTargetChangedPacket message, PacketBuffer buffer) {
		buffer.writeItemStack( message.targetItem );
		buffer.writeInt(message.recipeNo);
		buffer.writeBlockPos( message.blockPos );
	}
	
	public static NotifyPlayerTargetChangedPacket decode(PacketBuffer buffer) {
		return new NotifyPlayerTargetChangedPacket( buffer.readItemStack(), buffer.readInt(), buffer.readBlockPos() );
	}
	
	public static void handle( final NotifyPlayerTargetChangedPacket message, final Supplier<NetworkEvent.Context> context ) {
		NetworkEvent.Context ctx = context.get();
		ctx.enqueueWork(()->{
			@SuppressWarnings("resource")
			ClientPlayerEntity player = Minecraft.getInstance().player;
			World worldIn = player.world; //server world
			BlockPos blockPos = message.blockPos;
			
			TileEntity e = worldIn.getTileEntity(blockPos);
			if( e != null ) {
				if( e instanceof AutoCrafterTile ) {
					AutoCrafterTile eTile = (AutoCrafterTile) e;
					eTile.updateRecipes(message.targetItem, message.recipeNo, worldIn);
				}
			}
			
		});
		ctx.setPacketHandled(true);
	}
}
