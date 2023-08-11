package dev.imprex.shieldedimpact.command.argument;

import org.bukkit.entity.Player;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import dev.imprex.shieldedimpact.api.ShieldedReference;
import dev.imprex.shieldedimpact.command.CommandInfo;

public class ArgumentTypes {

	public static LiteralArgumentBuilder<CommandInfo> literal(String literal) {
		return LiteralArgumentBuilder.literal(literal);
	}

	public static <T> RequiredArgumentBuilder<CommandInfo, T> argument(String name, ArgumentType<T> type) {
		return RequiredArgumentBuilder.argument(name, type);
	}

	/**
	 * Examples: "fairy", "circle", "two_point"
	 */
	public static ArgumentType<?> shieldedReference() {
		return ShieldedReferenceArgumentType.shieldReferenceArg();
	}

	public static ShieldedReference getShieldedReference(CommandContext<?> context, String name) throws CommandSyntaxException {
		return ShieldedReferenceArgumentType.getShieldReference(context, name);
	}

	/**
	 * Examples: "Player", "0123"
	 */
	public static ArgumentType<?> player() {
		return PlayerArgumentType.playerArg();
	}

	public static Player getPlayer(CommandContext<?> context, String name) throws CommandSyntaxException {
		return PlayerArgumentType.getPlayer(context, name);
	}

	/**
	 * Examples: "true", "false"
	 */
	public static BoolArgumentType bool() {
		return BoolArgumentType.bool();
	}

	public static boolean getBoolean(CommandContext<?> context, String name) {
		return BoolArgumentType.getBool(context, name);
	}

	/**
	 * Examples: "0", "1.2", ".5", "-1", "-.5", "-1234.56"
	 */
	public static DoubleArgumentType doubleArg() {
		return DoubleArgumentType.doubleArg();
	}

	/**
	 * Examples: "0", "1.2", ".5", "-1", "-.5", "-1234.56"
	 */
	public static DoubleArgumentType doubleArg(double min) {
		return DoubleArgumentType.doubleArg(min);
	}

	/**
	 * Examples: "0", "1.2", ".5", "-1", "-.5", "-1234.56"
	 */
	public static DoubleArgumentType doubleArg(double min, double max) {
		return DoubleArgumentType.doubleArg(min, max);
	}

	public static double getDouble(CommandContext<?> context, String name) {
		return DoubleArgumentType.getDouble(context, name);
	}

	/**
	 * Examples: "0", "1.2", ".5", "-1", "-.5", "-1234.56"
	 */
	public static FloatArgumentType floatArg() {
		return FloatArgumentType.floatArg();
	}

	/**
	 * Examples: "0", "1.2", ".5", "-1", "-.5", "-1234.56"
	 */
	public static FloatArgumentType floatArg(float min) {
		return FloatArgumentType.floatArg(min);
	}

	/**
	 * Examples: "0", "1.2", ".5", "-1", "-.5", "-1234.56"
	 */
	public static FloatArgumentType floatArg(float min, float max) {
		return FloatArgumentType.floatArg(min, max);
	}

	public static float getFloat(CommandContext<?> context, String name) {
		return FloatArgumentType.getFloat(context, name);
	}

	/**
	 * Examples: "0", "123", "-123"
	 */
	public static IntegerArgumentType integer() {
		return IntegerArgumentType.integer();
	}

	/**
	 * Examples: "0", "123", "-123"
	 */
	public static IntegerArgumentType integer(int min) {
		return IntegerArgumentType.integer(min);
	}

	/**
	 * Examples: "0", "123", "-123"
	 */
	public static IntegerArgumentType integer(int min, int max) {
		return IntegerArgumentType.integer(min, max);
	}

	public static int getInteger(CommandContext<?> context, String name) {
		return IntegerArgumentType.getInteger(context, name);
	}

	/**
	 * Examples: "0", "123", "-123"
	 */
	public static LongArgumentType longArg() {
		return LongArgumentType.longArg();
	}

	/**
	 * Examples: "0", "123", "-123"
	 */
	public static LongArgumentType longArg(long min) {
		return LongArgumentType.longArg(min);
	}

	/**
	 * Examples: "0", "123", "-123"
	 */
	public static LongArgumentType longArg(long min, long max) {
		return LongArgumentType.longArg(min, max);
	}

	public static long getLong(CommandContext<?> context, String name) {
		return LongArgumentType.getLong(context, name);
	}

	/**
	 * Examples: "word", "words with spaces", "\"and symbols\""
	 */
	public static StringArgumentType greedyString() {
		return StringArgumentType.greedyString();
	}

	/**
	 * Examples: "\"quoted phrase\"", "word", "\"\""
	 */
	public static StringArgumentType string() {
		return StringArgumentType.string();
	}

	/**
	 * Examples: "word", "words_with_underscores"
	 */
	public static StringArgumentType word() {
		return StringArgumentType.word();
	}

	public static String getString(CommandContext<?> context, String name) {
		return StringArgumentType.getString(context, name);
	}

	/**
	 * public enum Test {
	 * 	FOO,
	 *  BAR;
	 * }
	 * 
	 * Examples: "FOO", "BAR"
	 */
	public static <T extends Enum<?>> EnumArgumentType<T> enumType(Class<T> enumClass) {
		return EnumArgumentType.enumType(enumClass);
	}

	public static <T extends Enum<?>> T getEnum(CommandContext<?> context, String name) {
		return EnumArgumentType.getEnum(context, name);
	}
}