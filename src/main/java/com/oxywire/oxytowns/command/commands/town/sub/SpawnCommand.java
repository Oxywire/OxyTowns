package com.oxywire.oxytowns.command.commands.town.sub;

import cloud.commandframework.annotations.Argument;
import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import com.oxywire.oxytowns.OxyTownsPlugin;
import com.oxywire.oxytowns.cache.TownCache;
import com.oxywire.oxytowns.config.Messages;
import com.oxywire.oxytowns.entities.impl.town.Town;
import com.oxywire.oxytowns.entities.types.settings.SpawnSetting;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Optional;

public final class SpawnCommand {

    @CommandMethod("town|t spawn [town]")
    @CommandDescription("Teleport to a town's spawn")
    public void onSpawn(final Player sender, final @Argument("town") @Nullable Town town) {
        teleport(sender, town);
    }

    public static void teleport(final Player sender, final @Nullable Town town) {
        final Messages messages = Messages.get();
        final TownCache townCache = OxyTownsPlugin.get().getTownCache();
        if (town != null) {
            if (town.getHome() == null) {
                messages.getTown().getNoSpawnSet().send(sender);
                return;
            }

            // Always allow the town leader to go
            if (town.getOwner().equals(sender.getUniqueId()) || townCache.isBypassing(sender)) {
                town.teleport(sender);
                return;
            }

            if (town.getSpawnSetting() == SpawnSetting.MEMBERS) {
                final Optional<Town> possibleTown = townCache.getTownByPlayer(sender);
                if (possibleTown.isEmpty() || !possibleTown.get().equals(town)) {
                    messages.getTown().getNoPermissionTeleport().send(sender);
                    return;
                }
            }

            if (town.getSpawnSetting() == SpawnSetting.TRUSTED && !town.isTrusted(sender.getUniqueId())) {
                messages.getTown().getNoPermissionTeleport().send(sender);
                return;
            }

            town.teleport(sender);
            return;
        }

        final Optional<Town> sendersTown = townCache.getTownByPlayer(sender.getUniqueId());

        if (sendersTown.isEmpty()) {
            messages.getTown().getNoTown().send(sender);
            return;
        }

        final Town presentTown = sendersTown.get();

        if (presentTown.getHome() == null) {
            messages.getTown().getNoSpawnSet().send(sender);
            return;
        }

        if (presentTown.getOwner().equals(sender.getUniqueId()) || townCache.isBypassing(sender)) {
            presentTown.teleport(sender);
            return;
        }

        presentTown.teleport(sender);
    }
}
