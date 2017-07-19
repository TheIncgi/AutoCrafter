package com.theincgi.autocrafter;

import com.theincgi.autocrafter.container.ContainerAutoCrafter;
import com.theincgi.autocrafter.gui.GuiAutoCrafter;
import com.theincgi.autocrafter.tileEntity.TileAutoCrafter;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler{
	public static final int AUTOCRAFTER_GUI = 0;
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID) {
		case AUTOCRAFTER_GUI:
			TileAutoCrafter tac = (TileAutoCrafter)world.getTileEntity(new BlockPos(x, y, z));
			return new ContainerAutoCrafter(player.inventory, tac);
		default:
			return null;
		}
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID) {
		case AUTOCRAFTER_GUI:
			TileAutoCrafter tac = (TileAutoCrafter) world.getTileEntity(new BlockPos(x,y,z));
			return new GuiAutoCrafter(player.inventory, tac);
		default:
			return null;
		}
	}

}
