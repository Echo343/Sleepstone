package com.blargsworkshop.sleepstone.spawn.gui;

import javax.annotation.Nullable;

import com.blargsworkshop.sleepstone.network.Networking;
import com.blargsworkshop.sleepstone.spawn.packet.SetSpawnPacket;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class SetSpawnScreen extends Screen {
	
	private static final int WIDTH = 179;
	private static final int HEIGHT = 151;
	private final BlockPos newSpawnPos;
	private final RegistryKey<World> newWorldKey;
	private final float direction;
	private final IFormattableTextComponent currentSpawnMsgKey;
	private int currentPosTextY = 0;
	
	public SetSpawnScreen(RegistryKey<World> worldKey, @Nullable BlockPos spawnPos, RegistryKey<World> newWorldKey, BlockPos newSpawnPos, float direction) {
		super(new TranslationTextComponent("text.gui.title.setspawn"));
		this.newWorldKey = newWorldKey;
		this.newSpawnPos = newSpawnPos;
		this.direction = direction;
		
		String worldStr = worldKey.getLocation().getPath();
		IFormattableTextComponent worldStringComponent;
		Style worldColor;

		switch (worldStr) {
			case "overworld":
				worldStringComponent = new TranslationTextComponent("text.gui.overworld");
				worldColor = Style.EMPTY.setFormatting(TextFormatting.DARK_GREEN);
				break;
			case "the_nether":
				worldStringComponent = new TranslationTextComponent("text.gui.nether");
				worldColor = Style.EMPTY.setFormatting(TextFormatting.DARK_RED);
				break;
			default:
				worldStringComponent = new StringTextComponent(worldStr);
				worldColor = Style.EMPTY.setFormatting(TextFormatting.YELLOW);
				break;
		}
		
		worldStringComponent.setStyle(worldColor);
		if (spawnPos == null) {
			currentSpawnMsgKey = new TranslationTextComponent("text.gui.no_spawn_set");
		}
		else {
			currentSpawnMsgKey = new TranslationTextComponent("text.gui.current_spawn", worldStringComponent, spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());			
		}
		currentSpawnMsgKey.setStyle(currentSpawnMsgKey.getStyle().setFormatting(TextFormatting.GRAY));
	}
	
	@Override
	protected void init() {
		int relX = (this.width - WIDTH) / 2;
		int relY = (this.height - HEIGHT) / 2;
		
		IFormattableTextComponent yes = new TranslationTextComponent("text.button.yes");
		yes.setStyle(yes.getStyle().setFormatting(TextFormatting.GREEN));
		
		IFormattableTextComponent no = new TranslationTextComponent("text.button.no");
		no.setStyle(no.getStyle().setFormatting(TextFormatting.DARK_RED));
		
		currentPosTextY = relY + 10;
		
        addButton(new Button(relX + 10, relY + 37, 160, 20, yes, button -> setSpawn()));
        addButton(new Button(relX + 10, relY + 64, 160, 20, no, button -> this.getMinecraft().displayGuiScreen(null)));
	}
	
	@Override
	public boolean isPauseScreen() {
		return false;
	}
	
	private void setSpawn() {
		Networking.INSTANCE.sendToServer(new SetSpawnPacket(newWorldKey, newSpawnPos, direction));
		this.getMinecraft().displayGuiScreen(null);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);

		this.renderBackground(matrixStack);
        AbstractGui.drawCenteredString(matrixStack, this.font, this.title, this.width / 2, 40, 16777215);
        AbstractGui.drawCenteredString(matrixStack, this.font, this.currentSpawnMsgKey, this.width / 2, currentPosTextY, 16777215);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
	}
	
	public static void open(RegistryKey<World> worldKey, @Nullable BlockPos spawnPos, RegistryKey<World> newWorldKey, BlockPos newSpawnPos, float direction) {
		Minecraft.getInstance().displayGuiScreen(new SetSpawnScreen(worldKey, spawnPos, newWorldKey, newSpawnPos, direction));
	}
}
