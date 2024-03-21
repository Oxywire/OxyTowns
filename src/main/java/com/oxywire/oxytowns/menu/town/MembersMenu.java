package com.oxywire.oxytowns.menu.town;

import com.oxywire.oxytowns.config.Messages;
import com.oxywire.oxytowns.config.messaging.Message;
import com.oxywire.oxytowns.entities.impl.town.Town;
import com.oxywire.oxytowns.entities.types.Role;
import com.oxywire.oxytowns.menu.Menu;
import com.oxywire.oxytowns.menu.MenuElement;
import com.oxywire.oxytowns.menu.PagedMenu;
import com.oxywire.oxytowns.utils.OfflinePlayerIsOnlineComparator;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import lombok.AllArgsConstructor;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
public final class MembersMenu extends PagedMenu {

    private final Town town;

    public static void open(final Player player, final Town town) {
        Menu.builder(new MembersMenu(town))
            .build()
            .open(player, Menu.INVENTORY_MANAGER.getContents(player).map(i -> i.pagination().getPage()).orElse(0));
    }

    @Override
    public ClickableItem[] createPaged(final Player player, final InventoryContents contents) {
        final Map<String, MenuElement> elements = getConfig().getElements();

        Menu.set(contents, elements.get("go-home"), e -> TownMainMenu.open(player, this.town));
        Menu.set(contents, elements.get("ban"), e -> BansMenu.open(player, this.town));
        Menu.set(contents, elements.get("trusted"), e -> TrustedMenu.open(player, this.town));

        return this.town.getOwnerAndMembersWithRoles().entrySet().stream()
            .sorted((a, b) -> OfflinePlayerIsOnlineComparator.INSTANCE.compare(Bukkit.getOfflinePlayer(a.getKey()), Bukkit.getOfflinePlayer(b.getKey())))
            .map(member -> {
                final OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(member.getKey());
                final ItemStack item = elements.get("member").getItem(
                    Placeholder.unparsed("name", Objects.requireNonNullElse(offlinePlayer.getName(), "null")),
                    Placeholder.unparsed("role", Message.formatEnum(member.getValue())),
                    Formatter.booleanChoice("status", offlinePlayer.isOnline())
                );
                item.editMeta(SkullMeta.class, meta -> meta.setOwningPlayer(offlinePlayer));

                return ClickableItem.of(
                    item,
                    e -> role(
                        player,
                        this.town,
                        member.getKey(),
                        e.isLeftClick(),
                        e.isLeftClick() ? Messages.get().getTown().getBroadcastDemotion() : Messages.get().getTown().getBroadcastPromotion()
                    )
                );
            })
            .toArray(ClickableItem[]::new);
    }

    private static void role(final Player player, final Town town, final UUID target, final boolean promote, final Message message) {
        if (!town.getOwner().equals(player.getUniqueId())) {
            return;
        }

        if (target.equals(town.getOwner())) {
            return;
        }

        Role role = promote ? town.promotePlayer(target) : town.demotePlayer(target);
        if (role == null) {
            return;
        }

        open(player, town);
        message.send(
            town,
            Placeholder.unparsed("player", Bukkit.getOfflinePlayer(target).getName()),
            Placeholder.unparsed("role", Message.formatEnum(role))
        );
    }
}
