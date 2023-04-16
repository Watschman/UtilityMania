package com.watschman.utilitymania.common.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.watschman.utilitymania.UtilityMania;
import com.watschman.utilitymania.common.command.placeholder.PlayerPlaceholder;
import com.watschman.utilitymania.common.config.UtilityManiaConfig;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class MinifyCommand extends BaseCommand {

    private static final UUID MAX_HEALTH_MINIFY_MODIFIER_UUID = UUID.fromString("42be9bda-dbf1-48df-a67e-5fc64423e986");
    private static final AttributeModifier MAX_HEALTH_MINIFY_MODIFIER = new AttributeModifier(MAX_HEALTH_MINIFY_MODIFIER_UUID, "Minified", -0.6F, AttributeModifier.Operation.MULTIPLY_TOTAL);

    public static final String COMMAND_TAG = UtilityMania.MOD_ID + ".minify";
    private static final String commandSolo = "minify";
    private static final String commandWithPlayer = commandSolo + " #" + PlayerPlaceholder.PLACE_HOLDER_TEXT + "#";

    private static final String commandWithPlayerOn = commandWithPlayer + " on";
    private static final String commandWithPlayerOff = commandWithPlayer + " off";

    @Override
    protected List<String> getCommandPaths() {
        return Arrays.asList(commandSolo, commandWithPlayer, commandWithPlayerOn, commandWithPlayerOff);
    }

    @Override
    protected int executeCommand(String command, CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        switch (command) {
            case commandSolo -> {
                return this.executeSelf(context.getSource());
            }
            case commandWithPlayer -> {
                return this.executeWithArgument(context.getSource(), context.getArgument(PlayerPlaceholder.ARGUMENT_NAME, EntitySelector.class));
            }
            case commandWithPlayerOn -> {
                return this.executeWithArgument(context.getSource(), context.getArgument(PlayerPlaceholder.ARGUMENT_NAME, EntitySelector.class), true);
            }
            case commandWithPlayerOff -> {
                return this.executeWithArgument(context.getSource(), context.getArgument(PlayerPlaceholder.ARGUMENT_NAME, EntitySelector.class), false);
            }
        }
        throw ICommand.ERROR_COMMAND_NOT_IMPLEMENTED.create();
    }

    @Override
    public boolean isActive() {
        return UtilityManiaConfig.CONFIGS.isMinifyCommandEnabled.getConfigValue().get();
    }

    private static void minifyPlayer(Player player) {
        AttributeInstance maxHealth = player.getAttribute(Attributes.MAX_HEALTH);
        if (maxHealth == null || maxHealth.getModifier(MAX_HEALTH_MINIFY_MODIFIER_UUID) != null) {
            return;
        }
        maxHealth.addPermanentModifier(MAX_HEALTH_MINIFY_MODIFIER);
        player.setHealth(player.getMaxHealth());
        player.refreshDimensions();
    }

    private static void reverseMinificationForPlayer(Player player) {
        AttributeInstance maxHealth = player.getAttribute(Attributes.MAX_HEALTH);

        if (maxHealth == null || maxHealth.getModifier(MAX_HEALTH_MINIFY_MODIFIER_UUID) == null) {
            return;
        }
        maxHealth.removeModifier(MAX_HEALTH_MINIFY_MODIFIER_UUID);
        player.setHealth(player.getMaxHealth());
    }

    public static void handleLivingAttack(LivingAttackEvent event) {
        Entity player = event.getSource().getEntity();
        if (
            !(player instanceof Player)
                || event.getAmount() <= 1.0F
                || !(player.getTags().contains(MinifyCommand.COMMAND_TAG))
        ) {
            return;
        }
        event.getEntity().hurt(DamageSource.playerAttack((Player) player), 1.0F);
        event.setCanceled(true);
    }

    public static void handlePlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();
        player.getTags().remove(COMMAND_TAG);
    }

    public static void handleSizeEvent(EntityEvent.Size event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof Player)) {
            return;
        }
        if (entity.getTags().contains(COMMAND_TAG)) {
            event.setNewSize(new EntityDimensions(0.05F, 0.05F, false));
            event.setNewEyeHeight(0.05F);
        }
    }

    @Override
    protected boolean requiresAdmin() {
        return UtilityManiaConfig.CONFIGS.doesMinifyRequireAdminPrivileges.getConfigValue().get();
    }

    private int execute(CommandSourceStack commandSourceStack, ServerPlayer[] players, boolean activateMinify) {
        List<String> minifiedPlayer = new ArrayList<>();
        List<String> normalPlayers = new ArrayList<>();
        List<String> noChangedPlayers = new ArrayList<>();
        for (ServerPlayer player : players) {
            if (player.getTags().contains(COMMAND_TAG)) {
                if (!activateMinify) {
                    normalPlayers.add(player.getDisplayName().getString());
                    player.removeTag(COMMAND_TAG);
                    reverseMinificationForPlayer(player);
                } else {
                    noChangedPlayers.add(player.getDisplayName().getString());
                }
            } else {
                if (activateMinify) {
                    player.addTag(COMMAND_TAG);
                    minifiedPlayer.add(player.getDisplayName().getString());
                    minifyPlayer(player);
                } else {
                    noChangedPlayers.add(player.getDisplayName().getString());
                }
            }
        }

        if (minifiedPlayer.size() == 0 && normalPlayers.size() == 0) {
            commandSourceStack.sendSuccess(MutableComponent.create(new TranslatableContents("Players " + String.join(", ", noChangedPlayers) + " are already " + (activateMinify ? "minified" : "normal") + ".")), true);
        }
        if (minifiedPlayer.size() != 0) {
            commandSourceStack.sendSuccess(MutableComponent.create(new TranslatableContents("Minified the following Players: " + String.join(", ", minifiedPlayer))), true);
        }
        if (normalPlayers.size() != 0) {
            commandSourceStack.sendSuccess(MutableComponent.create(new TranslatableContents("Reversed minification for following Players: " + String.join(", ", normalPlayers))), true);
        }
        return 0;
    }

    private int execute(CommandSourceStack commandSourceStack, ServerPlayer[] players) {
        List<String> minifiedPlayer = new ArrayList<>();
        List<String> normalPlayers = new ArrayList<>();
        for (ServerPlayer player : players) {
            if (player.getTags().contains(COMMAND_TAG)) {
                normalPlayers.add(player.getDisplayName().getString());
                player.removeTag(COMMAND_TAG);
                reverseMinificationForPlayer(player);

            } else {
                player.addTag(COMMAND_TAG);
                minifiedPlayer.add(player.getDisplayName().getString());
                minifyPlayer(player);
            }
        }

        if (minifiedPlayer.size() != 0) {
            commandSourceStack.sendSuccess(MutableComponent.create(new TranslatableContents("Minified the following Players: " + String.join(", ", minifiedPlayer))), true);
        }
        if (normalPlayers.size() != 0) {
            commandSourceStack.sendSuccess(MutableComponent.create(new TranslatableContents("Reversed minification for following Players: " + String.join(", ", normalPlayers))), true);
        }


        return 0;
    }


    private int executeSelf(CommandSourceStack commandSourceStack) throws CommandSyntaxException {
        return this.execute(commandSourceStack, new ServerPlayer[]{commandSourceStack.getPlayerOrException()});
    }

    private int executeWithArgument(CommandSourceStack commandSourceStack, EntitySelector entitySelector) throws CommandSyntaxException {
        List<ServerPlayer> serverPlayers = entitySelector.findPlayers(commandSourceStack);
        if (serverPlayers.size() == 0) {
            throw CommandSourceStack.ERROR_NOT_PLAYER.create();
        }
        ServerPlayer[] serverPlayerArray = new ServerPlayer[serverPlayers.size()];
        serverPlayers.toArray(serverPlayerArray);
        return this.execute(commandSourceStack, serverPlayerArray);
    }

    private int executeWithArgument(CommandSourceStack commandSourceStack, EntitySelector entitySelector, boolean activateMinify) throws CommandSyntaxException {
        List<ServerPlayer> serverPlayers = entitySelector.findPlayers(commandSourceStack);
        if (serverPlayers.size() == 0) {
            throw CommandSourceStack.ERROR_NOT_PLAYER.create();
        }
        ServerPlayer[] serverPlayerArray = new ServerPlayer[serverPlayers.size()];
        serverPlayers.toArray(serverPlayerArray);
        return this.execute(commandSourceStack, serverPlayerArray, activateMinify);
    }
}
