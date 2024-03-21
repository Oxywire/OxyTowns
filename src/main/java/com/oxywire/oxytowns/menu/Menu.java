package com.oxywire.oxytowns.menu;

import com.oxywire.oxytowns.OxyTownsPlugin;
import com.oxywire.oxytowns.config.Menus;
import com.oxywire.oxytowns.config.messaging.Message;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.InventoryManager;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public abstract class Menu implements InventoryProvider {

    public static final InventoryManager INVENTORY_MANAGER = new InventoryManager(OxyTownsPlugin.get());

    public static SmartInventory.Builder builder(final Menu receiver, final TagResolver... placeholders) {
        Config config = receiver.getConfig();
        return SmartInventory.builder()
            .manager(INVENTORY_MANAGER)
            .title(LegacyComponentSerializer.legacySection().serialize(Message.MINI_MESSAGE.deserialize(config.getTitle(), placeholders)))
            .type(config.getType())
            .provider(receiver)
            .size(config.getRows(), 9);
    }

    public static void open(final Player receiver, final SmartInventory inventory, final int page) {
        Bukkit.getScheduler().runTask(OxyTownsPlugin.get(), () -> inventory.open(receiver, page));
    }

    public static void open(final Player receiver, final SmartInventory inventory) {
        open(receiver, inventory, 0);
    }

    public static void set(final InventoryContents receiver, final MenuElement element, final Consumer<InventoryClickEvent> clickHandler, final TagResolver... placeholders) {
        if (element == null) {
            return;
        }

        element.toSlotPos().forEach(pos -> receiver.set(pos, element.getElement(clickHandler, placeholders)));
    }

    public static void set(final InventoryContents receiver, final MenuElement element, final TagResolver... placeholders) {
        if (element == null) {
            return;
        }

        element.toSlotPos().forEach(pos -> receiver.set(pos, element.getElement(placeholders)));
    }

    public Config getConfig() {
        return Menus.get().getSingles().get(getClass().getSimpleName());
    }

    @Override
    public void init(final Player player, final InventoryContents contents) {
        final List<MenuElement> items = this.getConfig().getDecoratorElements();
        if (items != null && !items.isEmpty()) {
            this.getConfig().getDecoratorElements().forEach(decorator -> {
                decorator.toSlotPos().forEach(slot -> {
                    contents.set(slot, ClickableItem.empty(decorator.getItem(TagResolver.empty())));
                });
            });
        }
        create(player, contents);
    }

    @Override
    public void update(final Player player, final InventoryContents contents) {
    }

    protected abstract void create(final Player player, final InventoryContents contents);

    @ConfigSerializable
    public static class Config {

        @Setting
        private String title = "Menu";
        @Setting
        private InventoryType type = InventoryType.CHEST;
        @Setting
        private int rows = 6;
        @Setting
        private List<MenuElement> decoratorElements = List.of();
        @Setting
        private Map<String, MenuElement> elements = Map.of();

        public String getTitle() {
            return this.title;
        }

        public Config setTitle(String title) {
            this.title = title;
            return this;
        }

        public InventoryType getType() {
            return this.type;
        }

        public Config setType(final InventoryType type) {
            this.type = type;
            return this;
        }

        public int getRows() {
            return this.rows;
        }

        public Config setRows(final int rows) {
            this.rows = rows;
            return this;
        }

        public List<MenuElement> getDecoratorElements() {
            return this.decoratorElements;
        }

        public Config setDecoratorElements(final List<MenuElement> decoratorElements) {
            this.decoratorElements = decoratorElements;
            return this;
        }

        public Map<String, MenuElement> getElements() {
            return this.elements;
        }

        public Config setElements(final Map<String, MenuElement> elements) {
            this.elements = elements;
            return this;
        }
    }
}
