package com.blargsworkshop.sleepstone.spawn.gui;

import javax.annotation.Nullable;

import com.blargsworkshop.sleepstone.network.Networking;
import com.blargsworkshop.sleepstone.spawn.packet.SetSpawnPacket;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public class SetSpawnScreen extends Screen {

	private static final int WIDTH = 179;
	private static final int HEIGHT = 151;
	private final BlockPos newSpawnPos;
	private final ResourceKey<Level> newWorldKey;
	private final float direction;
	private final MutableComponent currentSpawnMsgKey;
	private int currentPosTextY = 0;

	public SetSpawnScreen(ResourceKey<Level> worldKey, @Nullable BlockPos spawnPos, ResourceKey<Level> newWorldKey, BlockPos newSpawnPos, float direction) {
		super(Component.translatable("text.gui.title.setspawn"));
		this.newWorldKey = newWorldKey;
		this.newSpawnPos = newSpawnPos;
		this.direction = direction;

		String worldStr = worldKey.location().getPath();
		MutableComponent worldStringComponent;
		Style worldColor;

		switch (worldStr) {
			case "overworld":
				worldStringComponent = Component.translatable("text.gui.overworld");
				worldColor = Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.DARK_GREEN));
				break;
			case "the_nether":
				worldStringComponent = Component.translatable("text.gui.nether");
				worldColor = Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.DARK_RED));
				break;
			default:
				worldStringComponent = Component.literal(worldStr);
				worldColor = Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.YELLOW));
				break;
		}

		worldStringComponent.setStyle(worldColor);
		if (spawnPos == null) {
			currentSpawnMsgKey = Component.translatable("text.gui.no_spawn_set");
		}
		else {
			currentSpawnMsgKey = Component.translatable("text.gui.current_spawn", worldStringComponent, spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
		}
		currentSpawnMsgKey.setStyle(currentSpawnMsgKey.getStyle().withColor(TextColor.fromLegacyFormat(ChatFormatting.GRAY)));
	}

	@Override
	protected void init() {
		int relX = (this.width - WIDTH) / 2;
		int relY = (this.height - HEIGHT) / 2;

		MutableComponent yes = Component.translatable("text.button.yes");
		yes.setStyle(yes.getStyle().withColor(TextColor.fromLegacyFormat(ChatFormatting.GREEN)));

		MutableComponent no = Component.translatable("text.button.no");
		no.setStyle(no.getStyle().withColor(TextColor.fromLegacyFormat(ChatFormatting.DARK_RED)));

		currentPosTextY = relY + 10;

        addRenderableWidget(new Button(relX + 10, relY + 37, 160, 20, yes, button -> setSpawn()));
        addRenderableWidget(new Button(relX + 10, relY + 64, 160, 20, no, button -> this.getMinecraft().setScreen(null)));
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	private void setSpawn() {
		Networking.INSTANCE.sendToServer(new SetSpawnPacket(newWorldKey, newSpawnPos, direction));
		this.getMinecraft().setScreen(null);
	}

	@Override
	public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		// TODO this changed, might have unexpected side effects
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

		this.renderBackground(matrixStack);
		GuiComponent.drawCenteredString(matrixStack, this.font, this.title, this.width / 2, 40, 16777215);
        GuiComponent.drawCenteredString(matrixStack, this.font, this.currentSpawnMsgKey, this.width / 2, currentPosTextY, 16777215);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
	}

	public static void open(ResourceKey<Level> worldKey, @Nullable BlockPos spawnPos, ResourceKey<Level> newWorldKey, BlockPos newSpawnPos, float direction) {
		Minecraft.getInstance().setScreen(new SetSpawnScreen(worldKey, spawnPos, newWorldKey, newSpawnPos, direction));
	}
}
