package com.oxywire.oxytowns.menu.town;

import com.oxywire.oxytowns.OxyTownsPlugin;
import com.oxywire.oxytowns.config.Messages;
import com.oxywire.oxytowns.entities.impl.town.Town;
import com.oxywire.oxytowns.entities.types.Upgrade;
import com.oxywire.oxytowns.menu.Menu;
import com.oxywire.oxytowns.menu.MenuElement;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.SlotPos;
import lombok.AllArgsConstructor;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@AllArgsConstructor
public final class UpgradeMenu extends Menu {

    private final Town town;
    private final Upgrade upgrade;

    public static void open(final Player player, final Town town, final Upgrade upgrade) {
        Menu.builder(new UpgradeMenu(town, upgrade)).build().open(player);
    }

    @Override
    protected void create(final Player player, final InventoryContents contents) {
        final Map<String, MenuElement> elements = getConfig().getElements();
        Menu.set(contents, elements.get("go-home"), e -> UpgradeMainMenu.open(player, this.town));
        Menu.set(contents, elements.get("upgrade-info-" + this.upgrade.name()));


        final AtomicInteger tier = new AtomicInteger(0);

        this.upgrade.getTiers().forEach((upgraded, price) -> {
            final int upgradeTier = tier.get();
            final int townTier = this.town.getUpgradeTier(this.upgrade);
            final boolean hasUnlocked = townTier >= upgradeTier;

            final MenuElement element = elements.get("upgrade-" + (hasUnlocked ? "unlocked" : "locked"));
            final Optional<SlotPos> slot = element.toSlotPos().stream().findFirst();
            if (slot.isEmpty()) return;

            contents.set(
                slot.get().getRow(), slot.get().getColumn() + upgradeTier,
                element.getElement(
                    e -> {
                        Messages messages = Messages.get();
                        if (hasUnlocked) {
                            messages.getTown().getUpgrade().getUnlockedAlready().send(player, this.upgrade.getPlaceholders(upgradeTier));
                            return;
                        }
                        if (this.town.getBankValue() < price) {
                            messages.getTown().getUpgrade().getCannotAfford().send(player, this.upgrade.getPlaceholders(upgradeTier));
                            return;
                        }
                        if (townTier != upgradeTier - 1) {
                            messages.getTown().getUpgrade().getUnlockFirst().send(player, this.upgrade.getPlaceholders(upgradeTier - 1));
                            return;
                        }

                        this.town.removeWorth(price);
                        this.town.upgradeTown(upgrade, 1);

                        if (this.upgrade == Upgrade.VAULT_AMOUNT) {
                            for (int i = this.town.getVaults().size(); i < this.upgrade.getTiers().keySet().toArray(Integer[]::new)[upgradeTier]; i++) {
                                this.town.getVaults().add(new VaultMenu(i, "Town Vault"));
                            }

                            OxyTownsPlugin.get().getTownCache().updateVaultLogic(this.town);
                        }

                        messages.getTown().getUpgrade().getUpgraded().send(player, this.upgrade.getPlaceholders(upgradeTier));
                        open(player, this.town, this.upgrade);
                    },
                    TagResolver.resolver(TagResolver.resolver(this.upgrade.getPlaceholders(upgradeTier)), TagResolver.resolver(this.town.getPlaceholders()))
                )
            );
            tier.getAndIncrement();
        });
    }
}
