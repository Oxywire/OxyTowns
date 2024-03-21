package com.oxywire.oxytowns.menu.town;

import com.oxywire.oxytowns.config.Menus;
import com.oxywire.oxytowns.entities.impl.town.Town;
import com.oxywire.oxytowns.entities.types.Role;
import com.oxywire.oxytowns.entities.types.perms.Permission;
import com.oxywire.oxytowns.entities.types.perms.PermissionType;
import com.oxywire.oxytowns.events.TownPermissionChangeEvent;
import com.oxywire.oxytowns.menu.Menu;
import com.oxywire.oxytowns.menu.MenuElement;
import com.oxywire.oxytowns.menu.PagedMenu;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import lombok.AllArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@AllArgsConstructor
public final class PermissionMenu extends PagedMenu {

    private final Town town;
    private final Role role;
    private final PermissionType permissionType;

    public static void open(final Player player, final Town town, final Role role, final PermissionType permissionType) {
        Menu.builder(new PermissionMenu(town, role, permissionType)).build().open(player);
    }

    @Override
    public ClickableItem[] createPaged(final Player player, final InventoryContents contents) {
        final Map<String, MenuElement> elements = getConfig().getElements();

        Menu.set(contents, elements.get("go-home"), e -> TownSettingsMenu.open(player, this.town));

        if (this.permissionType == PermissionType.GLOBAL && PermissionType.MODERATOR.inherits(this.role)) {
            Menu.set(contents, elements.get("members"), e -> PermissionMenu.open(player, this.town, this.role, PermissionType.MODERATOR));
        } else if (this.permissionType == PermissionType.MODERATOR) {
            Menu.set(contents, elements.get("mod"), e -> PermissionMenu.open(player, this.town, this.role, PermissionType.GLOBAL));
        }

        for (Role role : Role.values()) {
            if (role != Role.MAYOR) {
                Menu.set(
                    contents,
                    elements.get("role-" + role),
                    e -> PermissionMenu.open(player, this.town, role, this.permissionType)
                );
            }
        }
        Menu.set(contents, elements.get("role-selected-" + this.role), e -> PermissionMenu.open(player, this.town, role, this.permissionType));

        return Arrays.stream(Permission.values())
            .filter(permission -> permission.getPermissionType() == this.permissionType)
            .filter(permission -> permission.getPermissionType().inherits(this.role))
            .map(permission -> {
                final TagResolver placeholder = Formatter.booleanChoice("status", this.town.getPermission(this.role, permission));
                final ItemStack item = elements.get("permission-" + permission.name()).getItem(placeholder);
                final ItemMeta meta = item.getItemMeta();
                final List<Component> lore = Objects.requireNonNullElse(meta.lore(), new ArrayList<>());

                lore.addAll(Menus.get().getCommonPermissionLore().stream().map(it -> MenuElement.itemComponent(it, placeholder)).toList());
                meta.lore(lore);
                item.setItemMeta(meta);

                return ClickableItem.of(
                    item,
                    e -> {
                        this.town.setPermission(this.role, permission, !this.town.getPermission(this.role, permission));
                        firePermissionChangeEvent(permission, this.role, this.town);
                        PermissionMenu.open(player, this.town, this.role, this.permissionType);
                    });
            })
            .toArray(ClickableItem[]::new);
    }

    private static void firePermissionChangeEvent(final Permission permission, final Role role, final Town town) {
        final TownPermissionChangeEvent event = new TownPermissionChangeEvent(town, role, permission);
        Bukkit.getPluginManager().callEvent(event);

        if (permission == Permission.VAULT) {
            town.getVaults().forEach(vault -> vault.getInventory().getViewers().forEach(viewer -> {
                if (!town.hasPermission(viewer.getUniqueId(), Permission.VAULT)) {
                    vault.close(viewer);
                }
            }));
        }
    }
}
