package com.oxywire.oxytowns.menu.town;

import com.oxywire.oxytowns.entities.impl.town.Town;
import com.oxywire.oxytowns.entities.types.settings.Setting;
import com.oxywire.oxytowns.menu.Menu;
import com.oxywire.oxytowns.menu.MenuElement;
import fr.minuskube.inv.content.InventoryContents;
import lombok.AllArgsConstructor;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;

import java.util.Map;

@AllArgsConstructor
public final class TownTogglesMenu extends Menu {

    private final Town town;

    public static void open(final Player player, final Town town) {
        Menu.builder(new TownTogglesMenu(town)).build().open(player);
    }

    @Override
    protected void create(final Player player, final InventoryContents contents) {
        final Map<String, MenuElement> elements = getConfig().getElements();

        Menu.set(contents, elements.get("go-home"), e -> TownSettingsMenu.open(player, this.town));
        Menu.set(
            contents,
            elements.get("spawn-setting"),
            e -> {
                this.town.setNextSpawnSetting();
                open(player, this.town);
            },
            Placeholder.unparsed("status", this.town.getSpawnSetting().getName())
        );
        Menu.set(
            contents,
            elements.get("pvp"),
            e -> {
                this.town.toggleSetting(Setting.PVP);
                open(player, this.town);
            },
            Formatter.booleanChoice("status", this.town.getToggle(Setting.PVP))
        );
        Menu.set(
            contents,
            elements.get("join"),
            e -> {
                this.town.toggleSetting(Setting.OPEN);
                open(player, this.town);
            },
            Formatter.booleanChoice("status", this.town.getToggle(Setting.OPEN))
        );

        Menu.set(
            contents,
            elements.get("mobs"),
            e -> {
                this.town.toggleSetting(Setting.MOBS);
                open(player, this.town);
            },
            Formatter.booleanChoice("status", this.town.getToggle(Setting.MOBS))
        );
    }
}
