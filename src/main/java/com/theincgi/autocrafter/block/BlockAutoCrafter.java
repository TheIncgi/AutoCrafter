package com.theincgi.autocrafter.block;

import com.theincgi.autocrafter.container.AutoCrafterContainer;
import com.theincgi.autocrafter.tileEntity.AutoCrafterTile;
import com.theincgi.autocrafter.tileEntity.ModTileEntities;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;

public class BlockAutoCrafter extends ContainerBlock{
	
	public BlockAutoCrafter() {
		super( AbstractBlock.Properties
				 .create(Material.IRON)
		         .harvestLevel(0)
		         .harvestTool(ToolType.PICKAXE)
		         .hardnessAndResistance(1.5f, 10) //hardness, resistance? 
		         .setLightLevel( s->{ return 1; } )
		); 
	}
	
	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return ModTileEntities.AUTOCRAFTER_TILE.get().create();
	}
	@Override
	public TileEntity createNewTileEntity(IBlockReader worldIn) {
		return ModTileEntities.AUTOCRAFTER_TILE.get().create();
	}
	
	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player,
			Hand handIn, BlockRayTraceResult hit) {
		
		if(!worldIn.isRemote) {
			TileEntity entity = worldIn.getTileEntity(pos);
			if(entity instanceof AutoCrafterTile) {
				INamedContainerProvider containerProvider = createContainerProvider(worldIn, pos);
				
				NetworkHooks.openGui((ServerPlayerEntity)player, containerProvider, entity.getPos());
			}else{
				throw new IllegalStateException("Incorrect tile entity!");
			}
			
		}else {
			
		}
////		return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
//		if(worldIn.isRemote) {
////			AutoCrafterMod.network.sendToServer(PacketClientChanged.requestAll(pos));			
//		}else {
//			Utils.getMinecraft().displayGuiScreen(  );
////			player.openGui(AutoCrafterMod.instance, GuiHandler.AUTOCRAFTER_GUI, world, pos.getX(), pos.getY(), pos.getZ());
//		}
		return ActionResultType.SUCCESS;
	}
	
	private INamedContainerProvider createContainerProvider(World worldIn, BlockPos pos) {
		return new INamedContainerProvider() {
			
			@Override
			public Container createMenu(int window, PlayerInventory inv, PlayerEntity entity) {
				return new AutoCrafterContainer(window, worldIn, pos, inv, entity);
			}
			
			@Override
			public ITextComponent getDisplayName() {
				return new TranslationTextComponent("screen.autocrafter.autocrafter");
			}
		};
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		AutoCrafterTile tac = (AutoCrafterTile) worldIn.getTileEntity(pos);
//		if(stack.hasDisplayName())
//			tac.setCustomName(stack.getDisplayName());
	}
	
	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
		super.onBlockHarvested(worldIn, pos, state, player);
		
		AutoCrafterTile tac =  (AutoCrafterTile) worldIn.getTileEntity(pos);
		if(!worldIn.isRemote){
			//dropBlockAsItem(worldIn, pos, state, 0); //Handled by server
			InventoryHelper.dropInventoryItems(worldIn, pos, tac);
			System.out.println("Droped item");
		}
		//InventoryHelper.dropInventoryItems(worldIn, pos, tac);
	}
	
	@Override
	public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, Direction side) {
		return true;
	}

	
	
	


//	@Override
//	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
//		return LazyOptional.empty(); //TODO
//	}


//	@Override
//	public TileEntity newBlockEntity(IBlockReader p_196283_1_) {
//		return new AutoCrafterTile();
//	}


}
