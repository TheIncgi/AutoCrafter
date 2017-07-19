package com.theincgi.autocrafter.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public abstract class TilePacket implements IMessage{
	BlockPos p;
	public TilePacket() {
	}
	
	public TilePacket(BlockPos p) {
		super();
		this.p = p;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		p = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(p.getX());
		buf.writeInt(p.getY());
		buf.writeInt(p.getZ());
	}
	
}
