package com.blargsworkshop.common.text;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;

public class Chat {

	public static void addChatMessage(Player player, String messageKey, Object... args) {
		player.sendMessage(new TranslatableComponent(messageKey, args), player.getUUID());
	}

	public static void addUnlocalizedChatMessage(Player player, String message) {
		player.sendMessage(new TextComponent(message), player.getUUID());
	}
	
	public static void showStatusMessage(Player player, TextColor color, String messageKey, Object... args) {
		MutableComponent text = new TranslatableComponent(messageKey, args);
		Style style = text.getStyle()
				.withColor(color);  //set Color
		text.setStyle(style);
		player.displayClientMessage(text, true);
	}
	
	public static void clearStatusMessage(Player player) {
		player.displayClientMessage(new TextComponent(""), true);
	}
}
