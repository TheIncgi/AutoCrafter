package com.theincgi.autocrafter.gui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import com.theincgi.autocrafter.Core;
import com.theincgi.autocrafter.container.ContainerAutoCrafter;
import com.theincgi.autocrafter.packets.PacketClientChanged;
import com.theincgi.autocrafter.tileEntity.TileAutoCrafter;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GuiAutoCrafter extends GuiContainer{
	ResourceLocation background = new ResourceLocation(Core.MODID, "textures/gui/autocrafter_gui.png");
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
	
	private IInventory playerInv;
	private TileAutoCrafter tileAutoCrafter;
	private ContainerAutoCrafter container;
	public GuiAutoCrafter(IInventory playerInv, TileAutoCrafter te) {
		super(new ContainerAutoCrafter(playerInv, te));
		this.playerInv = playerInv;
		this.tileAutoCrafter = te;
		container = (ContainerAutoCrafter) super.inventorySlots;
	}
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	public void drawImage(ResourceLocation texture, int x, int y, int wid, int hei, float uMin, float vMin, float uMax, float vMax){
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		GlStateManager.enableAlpha();
		VertexBuffer buffer = Tessellator.getInstance().getBuffer();
		buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
		buffer.pos(x, y, 0).tex(uMin, vMin).endVertex();
		buffer.pos(x, y+hei, 0).tex(uMin, vMax).endVertex();
		buffer.pos(x+wid, y+hei, 0).tex(uMax, vMax).endVertex();
		buffer.pos(x+wid, y, 0).tex(uMax, vMin).endVertex();
		Tessellator.getInstance().draw();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		int x = width/2-GUIWID/2;
		int y = height/2-GUIHEI/2;
		Minecraft.getMinecraft().getTextureManager().bindTexture(background);
		drawImage(background, x, y, GUIWID, GUIHEI, 0, 0, uMax, vMax);
		prev.draw(mouseX, mouseY, x, y);
		next.draw(mouseX, mouseY, x, y);

		for(int i = 0; i<tileAutoCrafter.getRecipe().items.size(); i++){
			Slot s = container.getSlot(i);
			ItemStack req = tileAutoCrafter.getRecipe().getDisplayItem(i);
			
			RenderHelper.disableStandardItemLighting();
			RenderHelper.enableGUIStandardItemLighting();
			this.itemRender.renderItemAndEffectIntoGUI(req, s.xPos+x, s.yPos+y);
			GlStateManager.enableAlpha();
			GlStateManager.enableBlend();
			GlStateManager.disableDepth();
			
			RenderHelper.enableGUIStandardItemLighting();
			drawRect(s.xPos+x, s.yPos+y, s.xPos+x+16, s.yPos+y+16, req.isEmpty()?0xf0484848:0x708b8b8b);
			GlStateManager.enableDepth();
		}
		ItemStack target = tileAutoCrafter.getCrafts();
		Slot slot = container.targetSlot;
		//if((System.currentTimeMillis()/500)%2==0) //debug blink
		GlStateManager.disableDepth();
		GlStateManager.color(1f, 1f, 1f, 1f);
		this.itemRender.renderItemAndEffectIntoGUI(target, x+slot.xPos, y+slot.yPos);
		GlStateManager.enableDepth();
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String s = tileAutoCrafter.getDisplayName().getUnformattedText();
		fontRendererObj.drawString(s, 88-fontRendererObj.getStringWidth(s)/2, 6, 0x404040);
		this.fontRendererObj.drawString(this.playerInv.getDisplayName().getUnformattedText(), 8, 72, 0x404040);
	}
	
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
		public boolean isInBounds(int clickX, int clickY){
			if(clickX>x && clickY>y && clickX<(x+wid) && clickY<(y+hei))
				return true;
			return false;
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		int x = width/2-GUIWID/2;
		int y = height/2-GUIHEI/2;
		
		int playerDim = Minecraft.getMinecraft().player.dimension;
		if(prev.isInBounds(mouseX-x, mouseY-y)){
			PacketClientChanged packet = PacketClientChanged.nextRecipe(tileAutoCrafter.getPos());
			Core.network.sendToServer(packet);
		}else if(next.isInBounds(mouseX-x, mouseY-y)){
			PacketClientChanged packet = PacketClientChanged.prevRecipe(tileAutoCrafter.getPos());
			Core.network.sendToServer(packet);
		}else if(container.targetSlot.equals(getSlotUnderMouse())){
			Core.proxy.sendPacketServer(PacketClientChanged.targetChanged(tileAutoCrafter.getPos(), Minecraft.getMinecraft().player.inventory.getItemStack()));
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
			System.out.println(tileAutoCrafter.getRecipe()); 
	}

	
}
