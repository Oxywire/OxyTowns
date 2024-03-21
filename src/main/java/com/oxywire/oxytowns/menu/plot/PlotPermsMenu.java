package com.oxywire.oxytowns.menu.plot;

import com.oxywire.oxytowns.config.Menus;
import com.oxywire.oxytowns.entities.impl.plot.Plot;
import com.oxywire.oxytowns.entities.impl.town.Town;
import com.oxywire.oxytowns.entities.types.Role;
import com.oxywire.oxytowns.entities.types.perms.Permission;
import com.oxywire.oxytowns.entities.types.perms.PermissionType;
import com.oxywire.oxytowns.menu.Menu;
import com.oxywire.oxytowns.menu.MenuElement;
import com.oxywire.oxytowns.menu.PagedMenu;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import lombok.AllArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@AllArgsConstructor
public class PlotPermsMenu extends PagedMenu {

    private final Town town;
    private final Plot plot;
    private final Role role;

    public static void open(final Player player, final Town town, final Plot plot, final Role role) {
        Menu.builder(new PlotPermsMenu(town, plot, role)).build().open(player);
    }

    @Override
    public ClickableItem[] createPaged(final Player player, final InventoryContents contents) {
        final Map<String, MenuElement> elements = getConfig().getElements();

        for (Role role : Role.values()) {
            if (role != Role.MAYOR) Menu.set(contents, elements.get("role-" + role), e -> PlotPermsMenu.open(player, this.town, this.plot, role));
        }
        Menu.set(contents, elements.get("role-selected-" + this.role), e -> PlotPermsMenu.open(player, this.town, this.plot, this.role));

        Menu.set(contents, elements.get("go-home"), e -> PlotMenu.open(player, this.town, this.plot));

        return Arrays.stream(Permission.values())
            .filter(it -> it.getPermissionType() == PermissionType.GLOBAL)
            .map(permission -> {
                final TagResolver placeholder = Formatter.booleanChoice("status", this.plot.getPermission(this.role, permission));
                final ItemStack item = elements.get("permission-" + permission.name()).getItem(placeholder);
                final ItemMeta meta = item.getItemMeta();
                final List<Component> lore = Objects.requireNonNullElse(meta.lore(), new ArrayList<>());

                lore.addAll(Menus.get().getCommonPermissionLore().stream().map(it -> MenuElement.itemComponent(it, placeholder)).toList());
                meta.lore(lore);
                item.setItemMeta(meta);

                return ClickableItem.of(
                    item,
                    e -> {
                        this.plot.setPermission(this.role, permission, !this.plot.getPermission(this.role, permission));
                        PlotPermsMenu.open(player, this.town, this.plot, this.role);
                    });
            })
            .toArray(ClickableItem[]::new);
    }
}
