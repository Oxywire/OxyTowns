package com.oxywire.oxytowns.command.commands.town.sub;

import cloud.commandframework.annotations.Argument;
import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.Regex;
import com.oxywire.oxytowns.cache.TownCache;
import com.oxywire.oxytowns.command.annotation.MustBeInTown;
import com.oxywire.oxytowns.command.annotation.SendersTown;
import com.oxywire.oxytowns.config.Messages;
import com.oxywire.oxytowns.entities.impl.town.Town;
import com.oxywire.oxytowns.entities.types.perms.Permission;
import com.oxywire.oxytowns.utils.TownUtils;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;

public final class RenameCommand {

    private final TownCache townCache;

    public RenameCommand(final TownCache townCache) {
        this.townCache = townCache;
    }

    @CommandMethod("town|t rename <name>")
    @CommandDescription("Rename your town")
    @MustBeInTown
    public void onRename(final Player sender, final @SendersTown Town town, final @Argument("name") @Regex(TownUtils.VALID_NAME) String name) {
        final Messages messages = Messages.get();

        if (!town.hasPermission(sender.getUniqueId(), Permission.RENAME)) {
            messages.getTown().getRenameNoPermission().send(sender);
            return;
        }

        if (this.townCache.getTownByName(name).isPresent()) {
            messages.getTown().getCreationNameAlreadyExists().send(sender, Placeholder.unparsed("new-name", name));
            return;
        }

        town.setName(name);
        messages.getTown().getRenameBroadcast().send(town, Placeholder.unparsed("new-name", name));
    }
}
