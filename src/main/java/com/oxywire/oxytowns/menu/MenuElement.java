package com.oxywire.oxytowns.menu;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.oxywire.oxytowns.config.messaging.Message;
import com.oxywire.oxytowns.utils.IntRange;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.SlotPos;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.IntStream;

// TODO: Migrate to builder
@Getter
@ConfigSerializable
public class MenuElement {

    @Setting
    private Material material = Material.DIRT;
    @Setting
    private Integer amount = null;
    @Setting
    private String displayName = null;
    @Setting
    private List<String> lore = null;
    @Setting
    private Integer customModelData = null;
    @Setting
    private ItemFlag[] itemFlags = null;
    @Setting
    private String skullTexture = null;
    @Setting
    private List<IntRange> slots = new ArrayList<>();

    public MenuElement setMaterial(final Material material) {
        this.material = material;
        return this;
    }

    public MenuElement setAmount(final Integer amount) {
        this.amount = amount;
        return this;
    }

    public MenuElement setDisplayName(final String displayName) {
        this.displayName = displayName;
        return this;
    }

    public MenuElement setLore(final List<String> lore) {
        this.lore = lore;
        return this;
    }

    public MenuElement setCustomModelData(final Integer customModelData) {
        this.customModelData = customModelData;
        return this;
    }

    public MenuElement setItemFlags(final ItemFlag[] itemFlags) {
        this.itemFlags = itemFlags;
        return this;
    }

    public MenuElement setSkullTexture(final String skullTexture) {
        this.skullTexture = skullTexture;
        return this;
    }

    public MenuElement setSlots(final List<IntRange> slots) {
        this.slots = slots;
        return this;
    }

    public MenuElement setSlot(final int slot) {
        this.slots = Collections.singletonList(new IntRange(slot, slot));
        return this;
    }

    public List<SlotPos> toSlotPos() {
        if (this.slots == null) {
            return Collections.emptyList();
        }

        return this.slots.stream().map(range ->
                IntStream.range(range.min(), range.max() + 1)
                    .mapToObj(slot -> SlotPos.of(slot / 9, slot % 9))
                    .toList()
            )
            .flatMap(Collection::stream)
            .toList();
    }

    public ItemStack getItem(final TagResolver... placeholders) {
        ItemStack itemStack = new ItemStack(this.material);

        if (this.amount != null) itemStack.setAmount(this.amount);

        ItemMeta meta = itemStack.getItemMeta();
        if (this.displayName != null) meta.displayName(itemComponent(this.displayName, placeholders));
        if (this.lore != null) meta.lore(this.lore.stream().map(it -> itemComponent(it, placeholders)).toList());
        if (this.customModelData != null) meta.setCustomModelData(this.customModelData);
        if (this.itemFlags != null) {
            for (ItemFlag itemFlag : this.itemFlags) {
                if (itemFlag == ItemFlag.HIDE_ATTRIBUTES) {
                    // Add some dummy attribute so we can hide all attributes
                    meta.addAttributeModifier(Attribute.GENERIC_BURNING_TIME, new AttributeModifier(new NamespacedKey("oxywire", "dummy"), 0, AttributeModifier.Operation.ADD_SCALAR));
                }

                meta.addItemFlags(itemFlag);
            }
        }

        if (this.material == Material.PLAYER_HEAD && this.skullTexture != null) {
            final PlayerProfile pp = Bukkit.createProfile(UUID.randomUUID());
            pp.setProperty(new ProfileProperty("textures", this.skullTexture));
            itemStack.editMeta(SkullMeta.class, skullMeta -> skullMeta.setPlayerProfile(pp));
        }

        itemStack.setItemMeta(meta);

        return itemStack;
    }

    public ClickableItem getElement(final Consumer<InventoryClickEvent> clickHandler, final TagResolver... placeholders) {
        return ClickableItem.of(this.getItem(placeholders), clickHandler);
    }

    public ClickableItem getElement(final TagResolver... placeholders) {
        return ClickableItem.of(this.getItem(placeholders), e -> {});
    }

    public static Component itemComponent(String text, TagResolver... placeholders) {
        return Message.MINI_MESSAGE.deserialize(text, placeholders)
                .decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);
    }
}
