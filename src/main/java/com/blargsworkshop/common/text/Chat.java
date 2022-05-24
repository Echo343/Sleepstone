package com.blargsworkshop.common.text;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public class Chat {

	public static void addChatMessage(PlayerEntity player, String messageKey, Object... args) {
		player.sendMessage(new TranslationTextComponent(messageKey, args), player.getUniqueID());
	}

	public static void addUnlocalizedChatMessage(PlayerEntity player, String message) {
		player.sendMessage(new StringTextComponent(message), player.getUniqueID());
	}
	
	public static void showStatusMessage(PlayerEntity player, TextFormatting color, String messageKey, Object... args) {
		IFormattableTextComponent text = new TranslationTextComponent(messageKey, args);
		Style style = text.getStyle()
				.setFormatting(color);  //set Color
		text.setStyle(style);
		player.sendStatusMessage(text, true);
	}
	
	public static void clearStatusMessage(PlayerEntity player) {
		player.sendStatusMessage(new StringTextComponent(""), true);
	}
}
