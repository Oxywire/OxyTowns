package com.oxywire.oxytowns.command.commands.admin.town;

import cloud.commandframework.annotations.Argument;
import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.CommandPermission;
import cloud.commandframework.annotations.specifier.Range;
import com.oxywire.oxytowns.config.Messages;
import com.oxywire.oxytowns.entities.impl.town.Town;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;

@CommandPermission("oxytowns.admin")
public final class TownBankCommand {

    @CommandMethod("townadmin|ta town <town> deposit <amount>")
    @CommandDescription("Force add money to a town bank")
    public void onBankAdd(final CommandSender sender, final @Argument("town") Town town, final @Argument("amount") @Range(min = "0") int amount) {
        town.addWorth(amount);
        Messages.get().getAdmin().getTown().getBank().getAdded().send(
            sender,
            Formatter.number("amount", amount),
            Placeholder.unparsed("town", town.getName()),
            Formatter.number("balance", town.getBankValue())
        );
    }

    @CommandMethod("townadmin|ta town <town> balance")
    public void onBankBalance(final CommandSender sender, final @Argument("town") Town town) {
        Messages.get().getAdmin().getTown().getBank().getBalance().send(
            sender,
            Placeholder.unparsed("town", town.getName()),
            Formatter.number("balance", town.getBankValue())
        );
    }

    @CommandMethod("townadmin|ta town <town> withdraw <amount>")
    @CommandDescription("Force take money from a town bank")
    public void onBankRemove(final CommandSender sender, final @Argument("town") Town town, final @Argument("amount") @Range(min = "0") int amount) {
        final Messages messages = Messages.get();
        if (town.getBankValue() < amount) {
            messages.getAdmin().getTown().getBank().getNotEnough().send(sender);
            return;
        }

        town.removeWorth(amount);
        messages.getAdmin().getTown().getBank().getRemoved().send(
            sender,
            Formatter.number("amount", amount),
            Placeholder.unparsed("town", town.getName()),
            Formatter.number("balance", town.getBankValue())
        );
    }
}
