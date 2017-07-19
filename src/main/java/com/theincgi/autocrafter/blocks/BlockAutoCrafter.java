package com.theincgi.autocrafter.blocks;

import com.theincgi.autocrafter.Core;
import com.theincgi.autocrafter.GuiHandler;
import com.theincgi.autocrafter.packets.PacketClientChanged;
import com.theincgi.autocrafter.tileEntity.TileAutoCrafter;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockAutoCrafter extends BlockContainer implements ITileEntityProvider{
	public BlockAutoCrafter() {
		super(Material.ROCK);
		setUnlocalizedName("autocrafter");
		setRegistryName("autocrafter");
		setHardness(1.5f);
		setResistance(10);
		setCreativeTab(CreativeTabs.REDSTONE);
		setLightLevel(1);
	}
	public static void addRecipe(){
		GameRegistry.addRecipe(new ItemStack(Core.blockAutoCrafter,1), 
				"LHL",
				"GCG",
				"RHR",
				'L', Items.GLOWSTONE_DUST,
				'H', Blocks.HOPPER,
				'G', Items.GOLD_INGOT,
				'C', Blocks.CRAFTING_TABLE,
				'R', Items.REDSTONE
				);
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		TileAutoCrafter tac = (TileAutoCrafter) worldIn.getTileEntity(pos);
		if(stack.hasDisplayName())
			tac.setCustomName(stack.getDisplayName());
	}
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		super.breakBlock(worldIn, pos, state);
		
		
	}
	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
		super.onBlockHarvested(worldIn, pos, state, player);
		
		TileAutoCrafter tac =  (TileAutoCrafter) worldIn.getTileEntity(pos);
		if(!worldIn.isRemote && !Core.proxy.isClient()){
			//dropBlockAsItem(worldIn, pos, state, 0); //Handled by server
			InventoryHelper.dropInventoryItems(worldIn, pos, tac);
			System.out.println("Droped item");
		}
		//InventoryHelper.dropInventoryItems(worldIn, pos, tac);
	}
	@Override
	public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return true;
	}
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(!worldIn.isRemote){
			
			playerIn.openGui(Core.instance, GuiHandler.AUTOCRAFTER_GUI	, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}else{
			Core.network.sendToServer(PacketClientChanged.requestAll(pos));
		}
		return true;
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileAutoCrafter();
	}
}
