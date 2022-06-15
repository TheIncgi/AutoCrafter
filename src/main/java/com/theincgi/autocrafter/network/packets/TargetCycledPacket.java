package com.theincgi.autocrafter.network.packets;

import java.util.function.Supplier;

import com.theincgi.autocrafter.network.ModNetworkChannels;
import com.theincgi.autocrafter.tileEntity.AutoCrafterTile;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

public class TargetCycledPacket {
	
	public enum CycleDir {PREV, NEXT}
	public CycleDir cycleDir;
	public BlockPos blockPos;

	public TargetCycledPacket( CycleDir cycleDir, BlockPos blockPos ) {
		this.cycleDir = cycleDir;
		this.blockPos = blockPos;
	}
	
//	public void encode(PacketBuffer buffer) {
//		encode(this, buffer);
//	}
	
	public static void encode(TargetCycledPacket message, PacketBuffer buffer) {
		buffer.writeEnumValue( message.cycleDir );
		buffer.writeBlockPos( message.blockPos );
	}
	
	public static TargetCycledPacket decode(PacketBuffer buffer) {
		return new TargetCycledPacket( buffer.readEnumValue(CycleDir.class), buffer.readBlockPos() );
	}
	
	public static void handle( final TargetCycledPacket message, final Supplier<NetworkEvent.Context> context ) {
		NetworkEvent.Context ctx = context.get();
		ctx.enqueueWork(()->{
			ServerPlayerEntity player = ctx.getSender();
			World worldIn = player.world; //server world
			BlockPos blockPos = message.blockPos;
			
			TileEntity e = worldIn.getTileEntity(blockPos);
			if( e != null ) {
				if( e instanceof AutoCrafterTile ) {
					AutoCrafterTile eTile = (AutoCrafterTile) e;
					if(message.cycleDir.equals(CycleDir.NEXT))
						eTile.nextRecipe(worldIn);
					else if(message.cycleDir.equals(CycleDir.PREV))
						eTile.prevRecipe(worldIn);
					NotifyPlayerTargetChangedPacket update = new NotifyPlayerTargetChangedPacket(eTile.getCrafts(), eTile.getCurrentRecipeIndex(), blockPos);
					ModNetworkChannels.CHANNEL.sendTo(update, player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
					//TODO broadcast?
				}
			}
			
		});
		ctx.setPacketHandled(true);
	}
}
