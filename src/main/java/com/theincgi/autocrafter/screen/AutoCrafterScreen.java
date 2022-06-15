package com.theincgi.autocrafter.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;
import com.theincgi.autocrafter.AutoCrafterMod;
import com.theincgi.autocrafter.container.AutoCrafterContainer;
import com.theincgi.autocrafter.network.ModNetworkChannels;
import com.theincgi.autocrafter.network.packets.TargetChangedPacket;
import com.theincgi.autocrafter.network.packets.TargetCycledPacket;
import com.theincgi.autocrafter.network.packets.TargetCycledPacket.CycleDir;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class AutoCrafterScreen extends ContainerScreen<AutoCrafterContainer>{
	ResourceLocation background = new ResourceLocation(AutoCrafterMod.MODID, "textures/gui/autocrafter_gui.png");
	private static final int GUIWID = 176, GUIHEI = 166;
	private static final int IMG_WID=256, IMG_HEI=256;
	private static final float uMax = GUIWID/(float)IMG_WID, vMax = GUIHEI/(float)IMG_HEI;
	private static final float leftUMin = 176/256f,
			leftUMax = 187/256f,
			rightUMin = 187/256f,
			rightUMax = 198/256f,
			buttonVMin = 0,
			buttonVMax = 36/256f;
	Button prev = new Button(108, 17, 11, 18, 176/255f, 0, 186/255f,  18/255f, background);
	Button next = new Button(145, 17, 11, 18, 186/255f, 0, 198/255f,  18/255f, background);
	
	private AutoCrafterContainer container;
	
	
	public AutoCrafterScreen(AutoCrafterContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
		container = (AutoCrafterContainer) super.container;
		// TODO Auto-generated constructor stub
	}
	@Override
	public boolean isPauseScreen() {
		return false;
	}
	
	public void drawImage(ResourceLocation texture, int x, int y, int wid, int hei, float uMin, float vMin, float uMax, float vMax){
		getMinecraft().getTextureManager().bindTexture(texture);
//		com.mojang.blaze3d.platform.GlStateManager.enableAlpha();
		BufferBuilder buffer = Tessellator.getInstance().getBuffer();
		buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
		buffer.pos(x, y, 0).tex(uMin, vMin).endVertex();
		buffer.pos(x, y+hei, 0).tex(uMin, vMax).endVertex();
		buffer.pos(x+wid, y+hei, 0).tex(uMax, vMax).endVertex();
		buffer.pos(x+wid, y, 0).tex(uMax, vMin).endVertex();
		Tessellator.getInstance().draw();
	}
	public void drawRect(int x, int y, int wid, int hei, int color){
		
//		com.mojang.blaze3d.platform.GlStateManager.enableAlpha();
		float r = ((color>>16)&0xFF) / 255f;
		float g = ((color>>8)&0xFF)  /255f;
		float b = (color & 0xFF)     /255f;
		float a = ((color>>24)&0xFF) /255f;
		BufferBuilder buffer = Tessellator.getInstance().getBuffer();
		buffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
		buffer.pos(x, y, 0).color(r, g, b, a).endVertex();
		buffer.pos(x, y+hei, 0).color(r, g, b, a).endVertex();
		buffer.pos(x+wid, y+hei, 0).color(r, g, b, a).endVertex();
		buffer.pos(x+wid, y, 0).color(r, g, b, a).endVertex();
		Tessellator.getInstance().draw();
	}

	
	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
		
	}
	
	
	
//	@Override
//	protected void drawGuiContainerForegroundLayer(MatrixStack ms, int mouseX, int mosueY) {
//		int x = width/2-GUIWID/2;
//		int y = height/2-GUIHEI/2;
////		getMinecraft().getTextureManager().bind(background);
////		drawImage(background, x, y, GUIWID, GUIHEI, 0, 0, uMax, vMax);
////		prev.draw(mouseX, mouseY, x, y);
////		next.draw(mouseX, mouseY, x, y);
//
//
//		String s = tileAutoCrafter.getDisplayName().getString(); //?
//		this.font.drawString(ms,s, 88-font.getStringWidth(s)/2, 6, 0x404040);
////		this.font.draw(this.playerInv.getDisplayName().getUnformattedText(), 8, 72, 0x404040);
//	}
	
	class Button{
		int x, y, wid, hei;
		float umin, umax,vmin,vmax;
		ResourceLocation tex;
		public Button(int x, int y, int wid, int hei, float umin,  float vmin, float umax, float vmax,
				ResourceLocation tex) {
			this.x = x;
			this.y = y;
			this.wid = wid;
			this.hei = hei;
			this.umin = umin;
			this.umax = umax;
			this.vmin = vmin;
			this.vmax = vmax;
			this.tex = tex;
		}
		public void draw(int mouseX, int mouseY, int dx, int dy){
			float yOffset = isInBounds(mouseX-dx, mouseY-dy)?hei/255f:0;
			drawImage(tex, x+dx, y+dy, wid, hei, umin, vmin+yOffset, umax, vmax+yOffset);
		}
		public boolean isInBounds(double clickX, double clickY){
			if(clickX>x && clickY>y && clickX<(x+wid) && clickY<(y+hei))
				return true;
			return false;
		}
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		
		int x = width/2-GUIWID/2;
		int y = height/2-GUIHEI/2;
		
		if(prev.isInBounds(mouseX-x, mouseY-y)){
			ModNetworkChannels.CHANNEL.sendToServer(new TargetCycledPacket(CycleDir.PREV, container.getTileEntity().getPos()));
			return true;
		}else if(next.isInBounds(mouseX-x, mouseY-y)){
			ModNetworkChannels.CHANNEL.sendToServer(new TargetCycledPacket(CycleDir.NEXT, container.getTileEntity().getPos()));
			return true;
		}else if(container.targetSlot.equals(getSlotUnderMouse())){
			@SuppressWarnings("resource")
			ItemStack held = getMinecraft().player.inventory.getItemStack();
			ModNetworkChannels.CHANNEL.sendToServer(new TargetChangedPacket(held, container.getTileEntity().getPos()));
			return true;
		}
		
		return super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
		int x = width/2-GUIWID/2;
		int y = height/2-GUIHEI/2;
		getMinecraft().getTextureManager().bindTexture(background);
		drawImage(background, x, y, GUIWID, GUIHEI, 0, 0, uMax, vMax);
		prev.draw(mouseX, mouseY, x, y);
		next.draw(mouseX, mouseY, x, y);
		
		for(int i = 0; i<container.getTileEntity().getRecipe().items.size(); i++){
			Slot s = container.getSlot(i);
			ItemStack req = container.getTileEntity().getRecipe().getDisplayItem(i);
			
			RenderHelper.disableStandardItemLighting();
			RenderHelper.enableStandardItemLighting();
//			RenderHelper.setupForFlatItems();
			this.itemRenderer.renderItemAndEffectIntoGUI(req, s.xPos + x, s.yPos + y);  //ItemAndEffectIntoGUI(req, s.x + x, s.y + y);
//			GlStateManager.enableAlpha();
//			GlStateManager.enableBlend();
//			GlStateManager.disableDepthTest();
			
			RenderHelper.enableStandardItemLighting();
			GlStateManager.blendFuncSeparate(SourceFactor.ONE_MINUS_SRC_COLOR.param, SourceFactor.DST_COLOR.param, DestFactor.ONE_MINUS_SRC_ALPHA.param, DestFactor.DST_ALPHA.param);
			if( container.getTileEntity().getItemStackHandler().getStackInSlot(i).isEmpty() )
				drawRect(s.xPos+x, s.yPos+y, 16, 16, req.isEmpty()?0xf0484848:0x708b8b8b);
			
//			GlStateManager.enableDepth();
		}
		ItemStack target = container.getTileEntity().getCrafts();
		Slot slot = container.targetSlot;
		//if((System.currentTimeMillis()/500)%2==0) //debug blink
//		GlStateManager.disableDepthTest();
//		GlStateManager.color4f(1f, 1f, 1f, 1f);
		this.itemRenderer.renderItemAndEffectIntoGUI(target, x+slot.xPos, y+slot.yPos);
//		GlStateManager.enableDepthTest();
	}

	
}
