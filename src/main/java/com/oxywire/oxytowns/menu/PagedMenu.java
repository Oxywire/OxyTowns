package com.oxywire.oxytowns.menu;

import com.oxywire.oxytowns.config.Menus;
import com.oxywire.oxytowns.utils.IntRange;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;
import fr.minuskube.inv.content.SlotPos;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public abstract class PagedMenu extends Menu {

    @Override
    public PagedMenu.Config getConfig() {
        return Menus.get().getPaged().get(getClass().getSimpleName());
    }

    @Override
    public void init(final Player player, final InventoryContents contents) {
        super.init(player, contents);

        final PagedMenu.Config config = getConfig();

        final SlotIterator iterator = contents.newIterator(SlotIterator.Type.HORIZONTAL, SlotPos.of(config.getFirstElementSlot() / 9, config.getFirstElementSlot() % 9))
            .allowOverride(false);
        config.getBlacklistedSlots().forEach(range ->
            IntStream.range(range.min(), range.max() + 1)
                .mapToObj(slot -> SlotPos.of(slot / 9, slot % 9))
                .forEach(iterator::blacklist)
        );

        final Pagination pagination = contents.pagination()
            .setItemsPerPage(config.getElementsPerPage())
            .setItems(createPaged(player, contents))
            .addToIterator(iterator);

        if (!pagination.isFirst()) {
            final MenuElement previousPage = config.getPreviousPageElement();
            previousPage.toSlotPos().forEach(pos -> contents.set(
                pos,
                ClickableItem.of(
                    previousPage.getItem(),
                    e -> Menu.INVENTORY_MANAGER.getInventory(((Player) e.getWhoClicked())).ifPresent(menu -> menu.open(player, pagination.previous().getPage()))
                )
            ));
        }

        if (!pagination.isLast()) {
            final MenuElement nextPage = config.getNextPageElement();
            nextPage.toSlotPos().forEach(pos -> contents.set(
                pos,
                ClickableItem.of(
                    nextPage.getItem(),
                    e -> Menu.INVENTORY_MANAGER.getInventory(((Player) e.getWhoClicked())).ifPresent(menu -> menu.open(player, pagination.next().getPage()))
                )
            ));
        }
    }

    @Override
    public void create(final Player player, final InventoryContents contents) {
    }

    public abstract ClickableItem[] createPaged(final Player player, final InventoryContents contents);

    @Getter
    @ConfigSerializable
    public static class Config extends Menu.Config {

        @Setting
        private int firstElementSlot = 9;
        @Setting
        private int elementsPerPage = 45;
        @Setting
        private MenuElement previousPageElement = new MenuElement().setMaterial(Material.ARROW).setSlot(18);
        @Setting
        private MenuElement nextPageElement = new MenuElement().setMaterial(Material.ARROW).setSlot(26);
        @Setting
        private List<IntRange> blacklistedSlots = new ArrayList<>();

        @Override
        public Config setTitle(final String title) {
            super.setTitle(title);
            return this;
        }

        @Override
        public Config setType(final InventoryType type) {
            super.setType(type);
            return this;
        }

        @Override
        public Config setRows(final int rows) {
            super.setRows(rows);
            return this;
        }

        @Override
        public Config setDecoratorElements(final List<MenuElement> decoratorElements) {
            super.setDecoratorElements(decoratorElements);
            return this;
        }

        @Override
        public Config setElements(final Map<String, MenuElement> elements) {
            super.setElements(elements);
            return this;
        }

        public Config setFirstElementSlot(final int firstElementSlot) {
            this.firstElementSlot = firstElementSlot;
            return this;
        }

        public Config setElementsPerPage(final int elementsPerPage) {
            this.elementsPerPage = elementsPerPage;
            return this;
        }

        public Config setPreviousPageElement(final MenuElement previousPageElement) {
            this.previousPageElement = previousPageElement;
            return this;
        }

        public Config setNextPageElement(MenuElement nextPageElement) {
            this.nextPageElement = nextPageElement;
            return this;
        }
    }
}
