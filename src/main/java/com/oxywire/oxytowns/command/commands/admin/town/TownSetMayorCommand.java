package com.oxywire.oxytowns.command.commands.admin.town;

import cloud.commandframework.annotations.Argument;
import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.CommandPermission;
import com.oxywire.oxytowns.OxyTownsPlugin;
import com.oxywire.oxytowns.config.Messages;
import com.oxywire.oxytowns.entities.impl.town.Town;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

public final class TownSetMayorCommand {

    @CommandMethod("townadmin|ta town <town> setmayor <player>")
    @CommandDescription("Force set a player as the mayor of a town")
    @CommandPermission("oxytowns.admin")
    public void onTownSetMayor(final CommandSender sender, final @Argument("town") Town town, final @Argument("player") OfflinePlayer member) {
        final Town targetsTown = OxyTownsPlugin.get().getTownCache().getTownByPlayer(member.getUniqueId()).orElse(null);
        if (targetsTown != null && targetsTown.getOwner().equals(member.getUniqueId())) {
            Messages.get().getAdmin().getTown().getPlayerIsMayor().send(sender);
            return;
        }
        if (targetsTown != null) {
            targetsTown.handleAdminLeave(member);
        }

        town.transferMayor(member.getUniqueId());
        Messages.get().getAdmin().getTown().getPlayerForceMayor().send(
            sender,
            Placeholder.unparsed("player", member.getName()),
            Placeholder.unparsed("town", town.getName())
        );
    }
}
