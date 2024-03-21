package com.oxywire.oxytowns.command.commands.admin.town;

import cloud.commandframework.annotations.Argument;
import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.CommandPermission;
import cloud.commandframework.annotations.Regex;
import com.oxywire.oxytowns.cache.TownCache;
import com.oxywire.oxytowns.config.Messages;
import com.oxywire.oxytowns.entities.impl.town.Town;
import com.oxywire.oxytowns.utils.TownUtils;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;

public final class TownRenameCommand {

    private final TownCache townCache;

    public TownRenameCommand(final TownCache townCache) {
        this.townCache = townCache;
    }

    @CommandMethod("townadmin|ta town <town> rename <name>")
    @CommandDescription("Force rename a town")
    @CommandPermission("oxytowns.admin")
    public void onRename(final Player sender, final @Argument("town") Town town, final @Argument("name") @Regex(TownUtils.VALID_NAME) String name) {
        final Messages messages = Messages.get();
        if (this.townCache.getTownByName(name).isPresent()) {
            messages.getTown().getCreationNameAlreadyExists().send(sender, Placeholder.unparsed("new-name", name));
            return;
        }

        town.setName(name);
        messages.getTown().getRenameBroadcast().send(town, Placeholder.unparsed("new-name", name));
    }
}
