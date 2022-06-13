package com.theincgi.autocrafter.network.packets;

import java.util.function.Supplier;

import com.theincgi.autocrafter.network.ModNetworkChannels;
import com.theincgi.autocrafter.tileEntity.AutoCrafterTile;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

public class RequestState {
	
	public BlockPos blockPos;

	public RequestState( BlockPos blockPos ) {
		this.blockPos = blockPos;
	}
	
//	public void encode(PacketBuffer buffer) {
//		encode(this, buffer);
//	}
	
	public static void encode(RequestState message, PacketBuffer buffer) {
		buffer.writeBlockPos( message.blockPos );
	}
	
	public static RequestState decode(PacketBuffer buffer) {
		return new RequestState( buffer.readBlockPos() );
	}
	
	public static void handle( final RequestState message, final Supplier<NetworkEvent.Context> context ) {
		NetworkEvent.Context ctx = context.get();
		ctx.enqueueWork(()->{
			ServerPlayerEntity player = ctx.getSender();
			World worldIn = player.world; //server world
			BlockPos blockPos = message.blockPos;
			
			TileEntity e = worldIn.getTileEntity(blockPos);
			if( e != null ) {
				if( e instanceof AutoCrafterTile ) {
					AutoCrafterTile eTile = (AutoCrafterTile) e;
					CompoundNBT nbt = new CompoundNBT();
					nbt = eTile.write(nbt);
					NotifyPlayerTileEntity update = new NotifyPlayerTileEntity(nbt, blockPos);
					ModNetworkChannels.CHANNEL.sendTo(update, player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
					//TODO broadcast?
				}
			}
			
		});
		ctx.setPacketHandled(true);
	}
}
