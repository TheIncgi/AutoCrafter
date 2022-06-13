package com.theincgi.autocrafter.packets;

/**Used to tell the client that the server has changed something*/
public class PacketServerUpdated {}
//extends PacketTileChanged {
//
//	public PacketServerUpdated() {
//		super();
//	}
//
//	public PacketServerUpdated(BlockPos p, NBTTagCompound nbt) {
//		super(p, nbt);
//	}
//
//	public static class Handler implements IMessageHandler<PacketServerUpdated, IMessage>{
//		@Override
//		public IMessage onMessage(final PacketServerUpdated message, final MessageContext ctx) {
//			Minecraft.getMinecraft().addScheduledTask(new Runnable() {
//				@Override
//				public void run() {
//					AutoCrafterTile ourTile = (AutoCrafterTile) Minecraft.getMinecraft().player.world.getTileEntity(message.p);
//					if(message.nbt.hasKey("tile")){
//						ourTile.readFromNBT(message.nbt.getCompoundTag("tile"));
//						return;
//					}
//					if(message.nbt.hasKey("targetSlot")){
//						ItemStack newTarget = new ItemStack(message.nbt.getCompoundTag("targetSlot"));
//						if(!Recipe.matches(ourTile.getCrafts(), newTarget)){//target is new
//							ourTile.setCrafts(newTarget);
//						}
//					}
//					if(message.nbt.hasKey("recipe")){
//						ourTile.setRecipe(message.nbt.getTagList("recipe",10));
//					}
//					
//					if(message.nbt.hasKey("rIndex")){
//						ourTile.setCurrentRecipeIndex(message.nbt.getInteger("rIndex"));
//					}
//				}
//			});
//			return null;
//		}
//	}
//}
