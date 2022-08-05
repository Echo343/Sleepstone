package com.blargsworkshop.sleepstone.common.text;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.entity.player.Player;

public class Chat {
	// TODO overload to take Components
	
	public static void addChatMessage(Player player, String messageKey, Object... args) {
		player.sendSystemMessage(Component.translatable(messageKey, args));
	}

	public static void addUnlocalizedChatMessage(Player player, String message) {
		player.sendSystemMessage(Component.literal(message));
	}

	public static void showStatusMessage(Player player, TextColor color, String messageKey, Object... args) {
		MutableComponent text = Component.translatable(messageKey, args);
		Style style = text.getStyle()
				.withColor(color);  //set Color
		text.setStyle(style);
		player.displayClientMessage(text, true);
	}

	public static void clearStatusMessage(Player player) {
		player.displayClientMessage(Component.literal(""), true);
	}
}
