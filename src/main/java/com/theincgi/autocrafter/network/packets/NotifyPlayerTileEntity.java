package com.theincgi.autocrafter.network.packets;

import java.util.function.Supplier;

import com.theincgi.autocrafter.Utils;
import com.theincgi.autocrafter.tileEntity.AutoCrafterTile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

public class NotifyPlayerTileEntity {
	CompoundNBT nbt;
	BlockPos pos;
	
	public NotifyPlayerTileEntity( CompoundNBT nbt, BlockPos pos ) {
		this.nbt = nbt;
		this.pos = pos;
	}
	
//	public void encode(PacketBuffer buffer) {
//		encode(this, buffer);
//	}
	
	public static void encode(NotifyPlayerTileEntity message, PacketBuffer buffer) {
		buffer.writeCompoundTag(message.nbt);
		buffer.writeBlockPos(message.pos);
	}
	
	public static NotifyPlayerTileEntity decode(PacketBuffer buffer) {
		return new NotifyPlayerTileEntity( buffer.readCompoundTag(), buffer.readBlockPos() );
	}
	
	public static void handle( final NotifyPlayerTileEntity message, final Supplier<NetworkEvent.Context> context ) {
		NetworkEvent.Context ctx = context.get();
		ctx.enqueueWork(()->{
			@SuppressWarnings("resource")
			ClientPlayerEntity player = Minecraft.getInstance().player;
			World worldIn = player.world; //server world
			BlockPos blockPos = message.pos;
			
			TileEntity e = worldIn.getTileEntity(blockPos);
			if( e != null ) {
				if( e instanceof AutoCrafterTile ) {
					AutoCrafterTile eTile = (AutoCrafterTile) e;
					eTile.read(eTile.getBlockState(), message.nbt);
				}
			}
			
		});
		ctx.setPacketHandled(true);
	}
}
