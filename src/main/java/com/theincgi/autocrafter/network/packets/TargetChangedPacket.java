package com.theincgi.autocrafter.network.packets;

import java.util.function.Supplier;

import com.theincgi.autocrafter.tileEntity.AutoCrafterTile;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

public class TargetChangedPacket {
	
	public ItemStack targetItem;
	public BlockPos blockPos;

	public TargetChangedPacket( ItemStack targetItem, BlockPos blockPos ) {
		this.targetItem = targetItem;
		this.blockPos = blockPos;
	}
	
//	public void encode(PacketBuffer buffer) {
//		encode(this, buffer);
//	}
	
	public static void encode(TargetChangedPacket message, PacketBuffer buffer) {
		buffer.writeItemStack( message.targetItem );
		buffer.writeBlockPos( message.blockPos );
	}
	
	public static TargetChangedPacket decode(PacketBuffer buffer) {
		return new TargetChangedPacket( buffer.readItemStack(), buffer.readBlockPos() );
	}
	
	public static void handle( final TargetChangedPacket message, final Supplier<NetworkEvent.Context> context ) {
		NetworkEvent.Context ctx = context.get();
		ctx.enqueueWork(()->{
			ServerPlayerEntity player = ctx.getSender();
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
