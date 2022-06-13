package com.theincgi.autocrafter.network.packets;

import java.util.function.Supplier;

import com.theincgi.autocrafter.Utils;
import com.theincgi.autocrafter.tileEntity.AutoCrafterTile;

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

	public NotifyPlayerTargetChangedPacket( ItemStack targetItem, BlockPos blockPos ) {
		this.targetItem = targetItem;
		this.blockPos = blockPos;
	}
	
//	public void encode(PacketBuffer buffer) {
//		encode(this, buffer);
//	}
	
	public static void encode(NotifyPlayerTargetChangedPacket message, PacketBuffer buffer) {
		buffer.writeItemStack( message.targetItem );
		buffer.writeBlockPos( message.blockPos );
	}
	
	public static NotifyPlayerTargetChangedPacket decode(PacketBuffer buffer) {
		return new NotifyPlayerTargetChangedPacket( buffer.readItemStack(), buffer.readBlockPos() );
	}
	
	public static void handle( final NotifyPlayerTargetChangedPacket message, final Supplier<NetworkEvent.Context> context ) {
		NetworkEvent.Context ctx = context.get();
		ctx.enqueueWork(()->{
			@SuppressWarnings("resource")
			ClientPlayerEntity player = Utils.getMinecraft().player;
			World worldIn = player.world; //server world
			BlockPos blockPos = message.blockPos;
			
			TileEntity e = worldIn.getTileEntity(blockPos);
			if( e != null ) {
				if( e instanceof AutoCrafterTile ) {
					AutoCrafterTile eTile = (AutoCrafterTile) e;
					eTile.updateRecipes(message.targetItem, 0);
				}
			}
			
		});
		ctx.setPacketHandled(true);
	}
}
