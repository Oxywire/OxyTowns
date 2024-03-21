package com.oxywire.oxytowns.menu.town;

import com.oxywire.oxytowns.entities.impl.town.Town;
import com.oxywire.oxytowns.entities.types.Role;
import com.oxywire.oxytowns.entities.types.perms.PermissionType;
import com.oxywire.oxytowns.menu.Menu;
import com.oxywire.oxytowns.menu.MenuElement;
import fr.minuskube.inv.content.InventoryContents;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

import java.util.Map;

@AllArgsConstructor
public final class TownSettingsMenu extends Menu {

    private final Town town;

    public static void open(final Player player, final Town town) {
        Menu.builder(new TownSettingsMenu(town)).build().open(player);
    }

    @Override
    protected void create(final Player player, final InventoryContents contents) {
        final Map<String, MenuElement> config = getConfig().getElements();

        Menu.set(contents, config.get("go-home"), e -> TownMainMenu.open(player, this.town));
        Menu.set(contents, config.get("toggles"), e -> TownTogglesMenu.open(player, this.town));
        Menu.set(contents, config.get("roles"), e -> PermissionMenu.open(player, this.town, Role.OUTSIDER, PermissionType.GLOBAL));
    }
}
