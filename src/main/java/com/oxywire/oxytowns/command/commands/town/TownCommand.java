package com.oxywire.oxytowns.command.commands.town;

import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.suggestions.Suggestions;
import cloud.commandframework.context.CommandContext;
import com.oxywire.oxytowns.cache.TownCache;
import com.oxywire.oxytowns.command.annotation.MustBeInTown;
import com.oxywire.oxytowns.command.annotation.SendersTown;
import com.oxywire.oxytowns.entities.impl.town.Town;
import com.oxywire.oxytowns.entities.types.settings.Setting;
import com.oxywire.oxytowns.menu.town.TownMainMenu;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public final class TownCommand {

    private final TownCache townCache;

    public TownCommand(final TownCache townCache) {
        this.townCache = townCache;
    }

    @CommandMethod("town|t")
    @CommandDescription("View your town")
    @MustBeInTown
    public void onDefault(final Player sender, final @SendersTown Town town) {
        TownMainMenu.open(sender, town);
    }

    @Suggestions("town:bannable-members")
    public List<String> bannableMembers(final CommandContext<Player> context, final String input) {
        return Bukkit.getOnlinePlayers().stream()
            .filter(it ->
                !this.townCache.getTownByPlayer(context.getSender())
                    .map(Town::getBannedUUIDs)
                    .orElse(List.of())
                    .contains(it.getUniqueId())
            )
            .map(Player::getName)
            .toList();
    }

    @Suggestions("town:unbannable-members")
    public List<String> unbannableMembers(final CommandContext<Player> context, final String input) {
        return this.townCache.getTownByPlayer(context.getSender())
            .map(Town::getBanNames)
            .orElse(List.of());
    }

    @Suggestions("town:invitable-members")
    public List<String> invitableMembers(final CommandContext<Player> context, final String input) {
        return Bukkit.getOnlinePlayers().stream()
            .filter(it -> this.townCache.getTownByPlayer(it).isEmpty())
            .filter(it -> context.getSender().canSee(it))
            .map(Player::getName)
            .toList();
    }

    @Suggestions("town:set-mayor-members")
    public List<String> setMayorMembers(final CommandContext<Player> context, final String input) {
        return this.townCache.getTownByPlayer(context.getSender())
            .map(Town::getOwnerAndMemberNames)
            .orElse(List.of());
    }

    @Suggestions("town:members")
    public List<String> members(final CommandContext<Player> context, final String input) {
        return this.townCache.getTownByPlayer(context.getSender())
            .map(Town::getOwnerAndMemberNames)
            .orElse(List.of());
    }

    @Suggestions("town:joinable_towns")
    public List<String> joinableTowns(final CommandContext<Player> context, final String input) {
        if (townCache.getTownByPlayer(context.getSender()).isPresent()) {
            return Collections.emptyList();
        }

        return townCache.getTowns().stream()
            .filter(it -> it.getToggle(Setting.OPEN) || it.getInvitedPlayers().contains(context.getSender().getUniqueId()))
            .map(Town::getName)
            .toList();
    }
}
