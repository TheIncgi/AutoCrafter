package com.theincgi.autocrafter.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;

public abstract class PacketTileChanged extends TilePacket{
	NBTTagCompound nbt;
	public PacketTileChanged() {
	}
	public PacketTileChanged(BlockPos p, NBTTagCompound nbt) {
		super(p);
		this.nbt = nbt;
	}
	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		nbt = ByteBufUtils.readTag(buf);
	}
	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
		ByteBufUtils.writeTag(buf, nbt);
	}
	
	
}
