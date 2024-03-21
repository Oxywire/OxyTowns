package com.oxywire.oxytowns.config;

import com.oxywire.oxytowns.OxyTownsPlugin;
import com.oxywire.oxytowns.entities.types.PlotType;
import com.oxywire.oxytowns.entities.types.Role;
import com.oxywire.oxytowns.entities.types.Upgrade;
import com.oxywire.oxytowns.entities.types.perms.Permission;
import com.oxywire.oxytowns.menu.Menu;
import com.oxywire.oxytowns.menu.MenuElement;
import com.oxywire.oxytowns.menu.PagedMenu;
import com.oxywire.oxytowns.utils.IntRange;
import lombok.Getter;
import org.bukkit.Material;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
@ConfigSerializable
public final class Menus {

    @Setting
    private Map<String, PagedMenu.Config> paged = Map.ofEntries(
        Map.entry(
            "BansMenu",
            new PagedMenu.Config()
                .setTitle("Town Bans")
                .setRows(5)
                .setDecoratorElements(commonDecorators(5, Material.IRON_BARS))
                .setElements(
                    Map.of(
                        "go-home", new MenuElement()
                            .setMaterial(Material.RED_STAINED_GLASS_PANE)
                            .setSlot(36)
                            .setLore(
                                List.of(
                                    "",
                                    "<red>Click to return to the main menu."
                                )
                            ),
                        "ban-entry", new MenuElement()
                            .setMaterial(Material.PLAYER_HEAD)
                            .setDisplayName("<name>")
                            .setLore(
                                List.of(
                                    "<white>Banned on: <yellow><date>",
                                    "<white>Banned by: <yellow><executor>",
                                    "<white>Reason: <yellow><reason>"
                                )
                            ),
                        "trusted", new MenuElement()
                            .setMaterial(Material.ENDER_EYE)
                            .setSlot(41)
                            .setDisplayName("<gold><b>Town Trusts")
                            .setLore(
                                List.of(
                                    "<gray>List all your Town Trusts",
                                    "",
                                    "<yellow>■ Click to open the town trust menu."
                                )
                            ),
                        "members", new MenuElement()
                            .setMaterial(Material.ENDER_EYE)
                            .setSlot(40)
                            .setDisplayName("<gold><b>Town Members")
                            .setLore(
                                List.of(
                                    "<gray>List all your Town Members",
                                    "",
                                    "<yellow>■ Click to open the town member menu."
                                )
                            )
                    )
                )
        ),
        Map.entry(
            "TrustedMenu",
            new PagedMenu.Config()
                .setTitle("Town Trusts")
                .setRows(5)
                .setDecoratorElements(commonDecorators(5, Material.EMERALD))
                .setElements(
                    Map.of(
                        "go-home", new MenuElement()
                            .setMaterial(Material.RED_STAINED_GLASS_PANE)
                            .setSlot(36)
                            .setLore(
                                List.of(
                                    "",
                                    "<red>Click to return to the main menu."
                                )
                            ),
                        "ban", new MenuElement()
                            .setMaterial(Material.IRON_BARS)
                            .setSlot(40)
                            .setDisplayName("<gold><b>Town Bans")
                            .setLore(
                                List.of(
                                    "<gray>List all your Town Bans",
                                    "",
                                    "<yellow>■ Click to open the town ban menu."
                                )
                            ),
                        "members", new MenuElement()
                            .setMaterial(Material.ENDER_EYE)
                            .setSlot(41)
                            .setDisplayName("<gold><b>Town Members")
                            .setLore(
                                List.of(
                                    "<gray>List all your Town Members",
                                    "",
                                    "<yellow>■ Click to open the town member menu."
                                )
                            ),
                        "entry", new MenuElement()
                            .setMaterial(Material.PLAYER_HEAD)
                            .setDisplayName("<name>")
                            .setLore(
                                List.of(
                                    "<white>Trusted on: <yellow><date>",
                                    "<white>Trusted by: <yellow><executor>",
                                    "<white>Reason: <yellow><reason>"
                                )
                            )
                    )
                )
        ),
        Map.entry(
            "ClaimsMenu",
            new PagedMenu.Config()
                .setTitle("Claims")
                .setRows(5)
                .setDecoratorElements(commonDecorators(5))
                .setElements(
                    Map.of(
                        "claims", new MenuElement()
                            .setMaterial(Material.GRASS_BLOCK)
                            .setDisplayName("<gold>Chunk Coords: <yellow>x <x> <gray>- <yellow>z <z>")
                            .setLore(
                                List.of(
                                    "<gold>World: <yellow><world>",
                                    "",
                                    "<yellow>■ Click to teleport to this chunk. <gray>(Mayor only)"
                                )
                            )
                    )
                )
        ),
        Map.entry(
            "MembersMenu",
            new PagedMenu.Config()
                .setTitle("Town Residents")
                .setRows(5)
                .setDecoratorElements(commonDecorators(5))
                .setElements(
                    Map.of(
                        "go-home", new MenuElement()
                            .setMaterial(Material.RED_STAINED_GLASS_PANE)
                            .setSlot(36)
                            .setLore(
                                List.of(
                                    "",
                                    "<red>Click to return to the main menu."
                                )
                            ),
                        "ban", new MenuElement()
                            .setMaterial(Material.IRON_BARS)
                            .setSlot(40)
                            .setDisplayName("<gold><b>Town Bans")
                            .setLore(
                                List.of(
                                    "<gray>List all your Town Bans",
                                    "",
                                    "<yellow>■ Click to open the town ban menu."
                                )
                            ),
                        "trusted", new MenuElement()
                            .setMaterial(Material.ENDER_EYE)
                            .setSlot(41)
                            .setDisplayName("<gold><b>Town Trusts")
                            .setLore(
                                List.of(
                                    "<gray>List all your Town Trusts",
                                    "",
                                    "<yellow>■ Click to open the town trust menu."
                                )
                            ),
                        "member", new MenuElement()
                            .setMaterial(Material.PLAYER_HEAD)
                            .setDisplayName("<yellow><name>")
                            .setLore(
                                List.of(
                                    "<white>Role: <red><role>",
                                    "",
                                    "<green>▲ Promote <gray>(Left-Click)",
                                    "<red>▼ Demote <gray>(Right-Click)"
                                )
                            )
                    )
                )
        ),
        Map.entry(
            "PermissionMenu",
            new PagedMenu.Config()
                .setRows(6)
                .setTitle("Permissions")
                .setDecoratorElements(commonDecorators(6))
                .setElements(
                    Map.ofEntries(
                        Map.entry(
                            "go-home", new MenuElement()
                                .setMaterial(Material.RED_STAINED_GLASS_PANE)
                                .setSlot(45)
                                .setLore(
                                    List.of(
                                        "",
                                        "<red>Click to return to the main menu."
                                    )
                                )
                        ),
                        Map.entry(
                            "members", new MenuElement()
                                .setMaterial(Material.ENDER_EYE)
                                .setSlot(49)
                                .setDisplayName("<gold>Mod Perms")
                                .setLore(
                                    List.of(
                                        "",
                                        "<yellow>■ Click to change mod perms."
                                    )
                                )
                        ),
                        Map.entry(
                            "mod", new MenuElement()
                                .setMaterial(Material.ENDER_PEARL)
                                .setSlot(49)
                                .setDisplayName("<gold>Member Perms")
                                .setLore(
                                    List.of(
                                        "",
                                        "<yellow>■ Click to change member perms."
                                    )
                                )
                        ),
                        Map.entry(
                            "permission-" + Permission.BLOCK_BREAK, new MenuElement()
                                .setMaterial(Material.DIAMOND_PICKAXE)
                                .setDisplayName("<gold>Break Blocks")
                        ),
                        Map.entry(
                            "permission-" + Permission.BLOCK_PLACE, new MenuElement()
                                .setMaterial(Material.BRICKS)
                                .setDisplayName("<gold>Place Blocks")
                        ),
                        Map.entry(
                            "permission-" + Permission.CHESTS, new MenuElement()
                                .setMaterial(Material.CHEST)
                                .setDisplayName("<gold>Open Chests")
                                .setLore(
                                    List.of(
                                        "",
                                        "<gold>* <white>Chests",
                                        "<gold>* <white>Trapped Chests",
                                        "<gold>* <white>Barrels",
                                        "<gold>* <white>Shulker Boxes",
                                        ""
                                    )
                                )
                        ),
                        Map.entry(
                            "permission-" + Permission.FURNACES, new MenuElement()
                                .setMaterial(Material.FURNACE)
                                .setDisplayName("<gold>Open Furnaces")
                                .setLore(
                                    List.of(
                                        "",
                                        "<gold>* <white>Furnaces",
                                        "<gold>* <white>Blast Furnaces",
                                        "<gold>* <white>Smokers",
                                        "<gold>* <white>Camp Fires",
                                        ""
                                    )
                                )
                        ),
                        Map.entry(
                            "permission-" + Permission.DOORS, new MenuElement()
                                .setMaterial(Material.OAK_DOOR)
                                .setDisplayName("<gold>Open Doors")
                                .setLore(
                                    List.of(
                                        "",
                                        "<gold>* <white>Doors",
                                        "<gold>* <white>Trap Doors",
                                        "<gold>* <white>Fence Gates",
                                        ""
                                    )
                                )
                        ),
                        Map.entry(
                            "permission-" + Permission.BUTTONS, new MenuElement()
                                .setMaterial(Material.LEVER)
                                .setDisplayName("<gold>Switch Levers & Buttons")
                                .setLore(
                                    List.of(
                                        "",
                                        "<gold>* <white>Levers",
                                        "<gold>* <white>Buttons",
                                        ""
                                    )
                                )
                        ),
                        Map.entry(
                            "permission-" + Permission.PLATES, new MenuElement()
                                .setMaterial(Material.STONE_PRESSURE_PLATE)
                                .setDisplayName("<gold>Trigger Pressure Plates")
                                .setLore(
                                    List.of(
                                        "",
                                        "<gold>* <white>Stone Pressure Plates",
                                        "<gold>* <white>Wooden Pressure Plates",
                                        "<gold>* <white>Iron Pressure Plates",
                                        "<gold>* <white>Gold Pressure Plates",
                                        ""
                                    )
                                )
                        ),
                        Map.entry(
                            "permission-" + Permission.ANVIL, new MenuElement()
                                .setMaterial(Material.ANVIL)
                                .setDisplayName("<gold>Use Anvils")
                        ),
                        Map.entry(
                            "permission-" + Permission.BREWING, new MenuElement()
                                .setMaterial(Material.BREWING_STAND)
                                .setDisplayName("<gold>Brewing")
                                .setLore(
                                    List.of(
                                        "",
                                        "<gold>* <white>Brewing Stands",
                                        "<gold>* <white>Cauldrons",
                                        ""
                                    )
                                )
                        ),
                        Map.entry(
                            "permission-" + Permission.ANIMALS, new MenuElement()
                                .setMaterial(Material.PIG_SPAWN_EGG)
                                .setDisplayName("<gold>Animals")
                                .setLore(
                                    List.of(
                                        "",
                                        "<gold>* <white>Breed Animals",
                                        "<gold>* <white>Kill Animals",
                                        ""
                                    )
                                )
                        ),
                        Map.entry(
                            "permission-" + Permission.LEASH, new MenuElement()
                                .setMaterial(Material.LEAD)
                                .setDisplayName("<gold>Leash Mobs")
                        ),
                        Map.entry(
                            "permission-" + Permission.VEHICLES, new MenuElement()
                                .setMaterial(Material.MINECART)
                                .setDisplayName("<gold>Vehicles")
                        ),
                        Map.entry(
                            "permission-" + Permission.REDSTONE, new MenuElement()
                                .setMaterial(Material.REDSTONE)
                                .setDisplayName("<gold>Use Redstone")
                                .setLore(
                                    List.of(
                                        "",
                                        "<gold>* <white>Dispensers",
                                        "<gold>* <white>Droppers",
                                        "<gold>* <white>Hoppers",
                                        "<gold>* <white>Daylight Sensors",
                                        "<gold>* <white>Repeaters",
                                        "<gold>* <white>Comparators",
                                        "<gold>* <white>Tripwires",
                                        ""
                                    )
                                )
                        ),
                        Map.entry(
                            "permission-" + Permission.ARMOR_STAND, new MenuElement()
                                .setMaterial(Material.ARMOR_STAND)
                                .setDisplayName("<gold>Interact with armor stands")
                        ),
                        Map.entry(
                            "permission-" + Permission.COMPOSTING, new MenuElement()
                                .setMaterial(Material.COMPOSTER)
                                .setDisplayName("<gold>Composting")
                        ),
                        Map.entry(
                            "permission-" + Permission.BEACON, new MenuElement()
                                .setMaterial(Material.BEACON)
                                .setDisplayName("<gold>Beacons")
                        ),
                        Map.entry(
                            "permission-" + Permission.LECTERN, new MenuElement()
                                .setMaterial(Material.LECTERN)
                                .setDisplayName("<gold>Lecterns")
                        ),
                        Map.entry(
                            "permission-" + Permission.WITHDRAW, new MenuElement()
                                .setMaterial(Material.GOLD_INGOT)
                                .setDisplayName("<gold>Withdraw from the town bank")
                        ),
                        Map.entry(
                            "permission-" + Permission.INVITE, new MenuElement()
                                .setMaterial(Material.FILLED_MAP)
                                .setDisplayName("<gold>Invite members to the town")
                        ),
                        Map.entry(
                            "permission-" + Permission.KICK, new MenuElement()
                                .setMaterial(Material.BARRIER)
                                .setDisplayName("<gold>Kick members from the town")
                        ),
                        Map.entry(
                            "permission-" + Permission.SPAWN, new MenuElement()
                                .setMaterial(Material.BEACON)
                                .setDisplayName("<gold>Set the town spawn")
                        ),
                        Map.entry(
                            "permission-" + Permission.OUTPOST, new MenuElement()
                                .setMaterial(Material.DARK_OAK_DOOR)
                                .setDisplayName("<gold>Create Outposts")
                        ),
                        Map.entry(
                            "permission-" + Permission.VAULT, new MenuElement()
                                .setMaterial(Material.ENDER_CHEST)
                                .setDisplayName("<gold>Access town vaults")
                        ),
                        Map.entry(
                            "permission-" + Permission.PLOTS_ASSIGN, new MenuElement()
                                .setMaterial(Material.MAP)
                                .setDisplayName("<gold>Assign players to plots")
                        ),
                        Map.entry(
                            "permission-" + Permission.PLOTS_RENAME, new MenuElement()
                                .setMaterial(Material.NAME_TAG)
                                .setDisplayName("<gold>Rename plots")
                        ),
                        Map.entry(
                            "permission-" + Permission.PLOTS_EVICT, new MenuElement()
                                .setMaterial(Material.BARRIER)
                                .setDisplayName("<gold>Evict players from plots")
                        ),
                        Map.entry(
                            "permission-" + Permission.PLOTS_TYPE, new MenuElement()
                                .setMaterial(Material.HAY_BLOCK)
                                .setDisplayName("<gold>Modify plot types")
                        ),
                        Map.entry(
                            "permission-" + Permission.PLOTS_MODIFY, new MenuElement()
                                .setMaterial(Material.BRICKS)
                                .setDisplayName("<gold>Modify all plots")
                                .setLore(
                                    List.of(
                                        "",
                                        "<gold>* <white>This will allow players to modify",
                                        "<white>plots that are not theirs.",
                                        "<gray><i>(Good for builders)",
                                        ""
                                    )
                                )
                        ),
                        Map.entry(
                            "permission-" + Permission.RENAME, new MenuElement()
                                .setMaterial(Material.NAME_TAG)
                                .setDisplayName("<gold>Rename the town")
                        ),
                        Map.entry(
                            "permission-" + Permission.PUNISH, new MenuElement()
                                .setMaterial(Material.IRON_BARS)
                                .setDisplayName("<gold>Ban players")
                        ),
                        Map.entry(
                            "permission-" + Permission.UPGRADES, new MenuElement()
                                .setMaterial(Material.ANVIL)
                                .setDisplayName("<gold>Upgrade the town")
                        ),
                        Map.entry(
                            "permission-" + Permission.CLAIM_UNCLAIM, new MenuElement()
                                .setMaterial(Material.GRASS_BLOCK)
                                .setDisplayName("<gold>Manage claims")
                        ),
                        Map.entry(
                            "permission-" + Permission.TRUST, new MenuElement()
                                .setMaterial(Material.TOTEM_OF_UNDYING)
                                .setDisplayName("<gold>Manage Trust")
                        ),
                        Map.entry(
                            "role-" + Role.CO_MAYOR, new MenuElement()
                            .setMaterial(Material.NAME_TAG)
                            .setSlot(6)
                            .setDisplayName("<red>Co Mayor")
                            .setLore(
                                List.of(
                                    "<gray>■ <yellow>Click to manage permissions"
                                )
                            )
                        ),
                        Map.entry(
                            "role-" + Role.SENATOR, new MenuElement()
                            .setMaterial(Material.NAME_TAG)
                            .setSlot(5)
                            .setDisplayName("<red>Senator")
                            .setLore(
                                List.of(
                                    "<gray>■ <yellow>Click to manage permissions"
                                )
                            )
                        ),
                        Map.entry(
                            "role-" + Role.HELPER, new MenuElement()
                            .setMaterial(Material.NAME_TAG)
                            .setSlot(4)
                            .setDisplayName("<red>Helper")
                            .setLore(
                                List.of(
                                    "<gray>■ <yellow>Click to manage permissions"
                                )
                            )
                        ),
                        Map.entry(
                            "role-" + Role.BUILDER, new MenuElement()
                            .setMaterial(Material.NAME_TAG)
                            .setSlot(3)
                            .setDisplayName("<red>Builder")
                            .setLore(
                                List.of(
                                    "<gray>■ <yellow>Click to manage permissions"
                                )
                            )
                        ),
                        Map.entry(
                            "role-" + Role.MEMBER, new MenuElement()
                            .setMaterial(Material.NAME_TAG)
                            .setSlot(2)
                            .setDisplayName("<red>Member")
                            .setLore(
                                List.of(
                                    "<gray>■ <yellow>Click to manage permissions"
                                )
                            )
                        ),
                        Map.entry(
                            "role-" + Role.TRUSTED, new MenuElement()
                                .setMaterial(Material.NAME_TAG)
                                .setSlot(2)
                                .setDisplayName("<red>Trusted")
                                .setLore(
                                    List.of(
                                        "<gray>■ <yellow>Click to manage permissions"
                                    )
                                )
                        ),
                        Map.entry(
                            "role-" + Role.OUTSIDER, new MenuElement()
                            .setMaterial(Material.NAME_TAG)
                            .setSlot(0)
                            .setDisplayName("<red>Outsider")
                            .setLore(
                                List.of(
                                    "<gray>■ <yellow>Click to manage permissions"
                                )
                            )
                        )
                    )
                )
        ),
        Map.entry(
            "OutpostsMenu",
            new PagedMenu.Config()
                .setTitle("Outposts")
                .setRows(5)
                .setDecoratorElements(commonDecorators(5))
                .setElements(
                    Map.of(
                        "go-home", new MenuElement()
                            .setMaterial(Material.RED_STAINED_GLASS_PANE)
                            .setSlot(36)
                            .setLore(
                                List.of(
                                    "",
                                    "<red>Click to return to the main menu."
                                )
                            ),
                        "outpost", new MenuElement()
                            .setMaterial(Material.GRASS_BLOCK)
                            .setDisplayName("<gold>Chunks Coords: <yellow>x <x> <gray>- <yellow>z <z>")
                            .setLore(
                                List.of(
                                    "<gold>World: <yellow><world>",
                                    "",
                                    "<yellow>■ Click to teleport to this outpost."
                                )
                            )
                    )
                )
        ),
        Map.entry(
            "TownsMenu",
            new PagedMenu.Config()
                .setTitle("Towns")
                .setDecoratorElements(commonDecorators(5))
                .setElements(
                    Map.of(
                        "go-home", new MenuElement()
                            .setMaterial(Material.RED_STAINED_GLASS_PANE)
                            .setSlot(36)
                            .setLore(
                                List.of(
                                    "",
                                    "<red>Click to return to the main menu."
                                )
                            ),
                        "list", new MenuElement()
                            .setMaterial(Material.PAPER)
                            .setDisplayName("<gold><b><name>")
                            .setLore(
                                List.of(
                                    "<yellow>Town Mayor: <white><owner>",
                                    "<yellow>Town Founded: <white><age:'MMMM dd, yyyy'>",
                                    "<yellow>Residents: <white><members>",
                                    "<yellow>Land Owned: <white><claims>/<maxclaims>",
                                    "<yellow>Outposts Owned: <white><outposts>/<maxoutposts>",
                                    "<yellow>Town Balance: <white><worth>",
                                    "<yellow>Visits: <white><visits>"
                                )
                            ),
                        "comparator", new MenuElement()
                            .setMaterial(Material.COMPARATOR)
                            .setSlot(30)
                            .setDisplayName("<gold><b>Sort Towns")
                            .setLore(
                                List.of(
                                    "<white>Click to sort the towns.",
                                    "",
                                    "<white>Sorting by: <yellow><comparator>"
                                )
                            )
                    )
                )
        )
    );

    @Setting
    private Map<String, Menu.Config> singles = Map.ofEntries(
        Map.entry(
            "PlotTypeMenu",
            new Menu.Config()
                .setTitle("Plot Type")
                .setRows(4)
                .setDecoratorElements(commonDecorators(4))
                .setElements(
                    Map.of(
                        "go-home", new MenuElement()
                            .setMaterial(Material.RED_STAINED_GLASS_PANE)
                            .setSlot(27)
                            .setLore(
                                List.of(
                                    "",
                                    "<red>Click to return to the main menu."
                                )
                            ),
                        "plot-type-" + PlotType.DEFAULT, new MenuElement()
                            .setMaterial(Material.GRASS_BLOCK)
                            .setSlot(21)
                            .setDisplayName("<gold>Default Plot")
                            .setLore(
                                List.of(
                                    "",
                                    "<white>The default plot type."
                                )
                            ),
                        "plot-type-" + PlotType.FARM, new MenuElement()
                            .setMaterial(Material.HAY_BLOCK)
                            .setSlot(22)
                            .setDisplayName("<gold>Farm Plot")
                            .setLore(
                                List.of(
                                    "",
                                    "<white>Allow players to place/break farmables.",
                                    "<red>Warning: ALL players including outsiders will get access!"
                                )
                            ),
                        "plot-type-" + PlotType.MOB_FARM, new MenuElement()
                            .setMaterial(Material.CHICKEN)
                            .setSlot(23)
                            .setDisplayName("<gold>Mobfarm Plot")
                            .setLore(
                                List.of(
                                    "",
                                    "<white>Allow players to kill animals.",
                                    "<red>Warning: All players including outsiders will get access!"
                                )
                            ),
                        "plot-type-" + PlotType.ARENA, new MenuElement()
                            .setMaterial(Material.DIAMOND_SWORD)
                            .setSlot(24)
                            .setDisplayName("<gold>Arena Plot")
                            .setLore(
                                List.of(
                                    "",
                                    "<white>Plot where pvp is always enabled.",
                                    "<red>Warning: This has higher priority than the global town settings!"
                                )
                            )
                    )
                )
        ),
        Map.entry(
            "TownMainMenu",
            new Menu.Config()
                .setTitle("<town>")
                .setRows(5)
                .setDecoratorElements(commonDecorators(5))
                .setElements(
                    Map.of(
                        "residents", new MenuElement()
                            .setMaterial(Material.PLAYER_HEAD)
                            .setSlot(12)
                            .setDisplayName("<gold><b>Residents")
                            .setLore(
                                List.of(
                                    "<gray>List all your members.",
                                    "",
                                    "<yellow>■ Click to open the residents menu."
                                )
                            ),
                        "vault", new MenuElement()
                            .setMaterial(Material.ENDER_CHEST)
                            .setSlot(14)
                            .setDisplayName("<gold><b>Town Vaults")
                            .setLore(
                                List.of(
                                    "<gray>Your own private town vaults.",
                                    "",
                                    "<yellow>■ Click to open the town vault selector."
                                )
                            ),
                        "bank", new MenuElement()
                            .setMaterial(Material.GOLD_INGOT)
                            .setSlot(16)
                            .setDisplayName("<gold><b>Town Bank")
                            .setLore(
                                List.of(
                                    "<gray>Financial Overview.",
                                    "",
                                    "<gold>Town Balance: <yellow><worth>",
                                    "<gold>Town Upkeep: <yellow><upkeep>",
                                    "",
                                    "<yellow>/town deposit <number>",
                                    "<gray>Deposit money into the town bank.",
                                    "",
                                    "<yellow>/town withdraw <number>",
                                    "<gray>Withdraw money from the town bank."
                                )
                            ),
                        "info", new MenuElement()
                            .setMaterial(Material.TOTEM_OF_UNDYING)
                            .setSlot(19)
                            .setDisplayName("<gold><b>Town Information")
                            .setLore(
                                List.of(
                                    "<gray>General Information.",
                                    "",
                                    "<gold><b><name>",
                                    "",
                                    "<yellow>Town Mayor: <white><owner>",
                                    "<yellow>Town Founded: <white><age:'MMMM dd, yyyy'>",
                                    "<yellow>Residents: <white><members>",
                                    "<yellow>Land Owned: <white><claims>",
                                    "<yellow>Outposts Owned: <white><outposts>",
                                    "<yellow>Town Balance: <white><worth>"
                                )
                            ),
                        "upgrades", new MenuElement()
                            .setMaterial(Material.ANVIL)
                            .setSlot(30)
                            .setDisplayName("<gold><b>Town Upgrades")
                            .setLore(
                                List.of(
                                    "<gray>Enhance your town",
                                    "",
                                    "<yellow>■ Click to open the town upgrades."
                                )
                            ),
                        "outposts", new MenuElement()
                            .setMaterial(Material.DARK_OAK_DOOR)
                            .setSlot(32)
                            .setDisplayName("<gold><b>Town Outposts")
                            .setLore(
                                List.of(
                                    "<gray>Unlock outposts.",
                                    "",
                                    "<yellow>■ Click to open the town outposts menu."
                                )
                            ),
                        "settings", new MenuElement()
                            .setMaterial(Material.COMPARATOR)
                            .setSlot(34)
                            .setDisplayName("<gold><b>Town Settings")
                            .setLore(
                                List.of(
                                    "<gray>Manage your town.",
                                    "",
                                    "<yellow>■ Click to open the town settings.",
                                    "",
                                    "<gray><i>This allows you to toggle settings",
                                    "<gray><i>and permissions for your members."
                                )
                            )
                    )
                )
        ),
        Map.entry(
            "TownSettingsMenu",
            new Menu.Config()
                .setTitle("Town Settings")
                .setRows(5)
                .setDecoratorElements(commonDecorators(5))
                .setElements(
                    Map.of(
                        "go-home", new MenuElement()
                            .setMaterial(Material.RED_STAINED_GLASS_PANE)
                            .setSlot(36)
                            .setLore(
                                List.of(
                                    "",
                                    "<red>Click to return to the main menu."
                                )
                            ),
                        "toggles", new MenuElement()
                            .setMaterial(Material.COMPARATOR)
                            .setSlot(21)
                            .setDisplayName("<gold><b>Town Settings")
                            .setLore(
                                List.of(
                                    "<gray>Manage your town.",
                                    "",
                                    "<yellow>■ Click to open the town settings.",
                                    "",
                                    "<gray><i>This allows you to toggle settings",
                                    "<gray><i>and permissions for your members."
                                )
                            ),
                        "roles", new MenuElement()
                            .setMaterial(Material.NAME_TAG)
                            .setSlot(23)
                            .setDisplayName("<gold><b>Town Roles")
                            .setLore(
                                List.of(
                                    "<gray>Manage your town.",
                                    "",
                                    "<yellow>■ Click to open the town settings.",
                                    "",
                                    "<gray><i>This allows you to toggle settings",
                                    "<gray><i>and permissions for your members."
                                )
                            )
                    )
                )
        ),
        Map.entry(
            "TownTogglesMenu",
            new Menu.Config()
                .setTitle("Town Toggles")
                .setRows(5)
                .setDecoratorElements(commonDecorators(5))
                .setElements(
                    Map.of(
                        "go-home", new MenuElement()
                            .setMaterial(Material.RED_STAINED_GLASS_PANE)
                            .setSlot(36)
                            .setLore(
                                List.of(
                                    "",
                                    "<red>Click to return to the main menu."
                                )
                            ),
                        "spawn-setting", new MenuElement()
                            .setMaterial(Material.RED_BED)
                            .setSlot(21)
                            .setDisplayName("Toggle Town Spawn Status")
                            .setLore(
                                List.of(
                                    "<gray>Status: <green><spawn-setting>",
                                    "",
                                    "<yellow>■ Click to switch!"
                                )
                            ),
                        "pvp", new MenuElement()
                            .setMaterial(Material.DIAMOND_SWORD)
                            .setSlot(22)
                            .setDisplayName("Toggle PVP")
                            .setLore(
                                List.of(
                                    "<gray>Status: <status:'1#\\'<green>\\'Enabled|0#\\'<red>\\'Disabled'>",
                                    "",
                                    "<yellow>■ Click to switch!"
                                )
                            ),
                        "join", new MenuElement()
                            .setMaterial(Material.BARRIER)
                            .setSlot(23)
                            .setDisplayName("Town Join Status")
                            .setLore(
                                List.of(
                                    "<gray>Status: <green><status:'1#Open|0#Closed'>",
                                    "",
                                    "<yellow>■ Click to switch!"
                                )
                            ),
                        "mobs", new MenuElement()
                            .setMaterial(Material.ZOMBIE_HEAD)
                            .setSlot(24)
                            .setDisplayName("Toggle Mobs")
                            .setLore(
                                List.of(
                                    "<gray>Status: <status:Enabled:Disabled>",
                                    "",
                                    "<yellow>■ Click to switch!"
                                )
                            )
                    )
                )
        ),
        Map.entry(
            "UpgradeMainMenu",
            new Menu.Config()
                .setTitle("Town Upgrades")
                .setRows(5)
                .setDecoratorElements(commonDecorators(5))
                .setElements(
                    Map.of(
                        "go-home", new MenuElement()
                            .setMaterial(Material.RED_STAINED_GLASS_PANE)
                            .setSlot(36)
                            .setLore(
                                List.of(
                                    "",
                                    "<red>Click to return to the main menu."
                                )
                            ),
                        "info", new MenuElement()
                            .setMaterial(Material.ANVIL)
                            .setSlot(4)
                            .setDisplayName("<gold><b>Town Upgrades")
                            .setLore(
                                List.of(
                                    "<gray>Enhance your town!",
                                    "",
                                    "<white>Here you can upgrade your town and",
                                    "<white>improve its traits. You'll need money",
                                    "<white>in your town bank in order to unlock stuff."
                                )
                            ),
                        "upgrade-" + Upgrade.CLAIMS, new MenuElement()
                            .setMaterial(Material.ANVIL)
                            .setSlot(19)
                            .setDisplayName("<gold><b>Claims")
                            .setLore(
                                List.of(
                                    "<gray>Expand your town!",
                                    "",
                                    "<white>Here you can unlock more claims.",
                                    "<white>This will allow you to claim more land."
                                )
                            ),
                        "upgrade-" + Upgrade.MEMBERS, new MenuElement()
                            .setMaterial(Material.ANVIL)
                            .setSlot(21)
                            .setDisplayName("<gold><b>Members")
                            .setLore(
                                List.of(
                                    "<gray>Expand your town!",
                                    "",
                                    "<white>Here you can unlock more member slots.",
                                    "<white>This will allow you to have more members."
                                )
                            ),
                        "upgrade-" + Upgrade.VAULT_AMOUNT, new MenuElement()
                            .setMaterial(Material.ANVIL)
                            .setSlot(23)
                            .setDisplayName("<gold><b>Vaults")
                            .setLore(
                                List.of(
                                    "<gray>Expand your vault!",
                                    "",
                                    "<white>Here you can unlock more vault space.",
                                    "<white>This will allow you to get more vaults."
                                )
                            ),
                        "upgrade-" + Upgrade.OUTPOSTS, new MenuElement()
                            .setMaterial(Material.ANVIL)
                            .setSlot(25)
                            .setDisplayName("<gold><b>Outposts")
                            .setLore(
                                List.of(
                                    "<gray>Expand your town!",
                                    "",
                                    "<white>Here you can unlock more outposts.",
                                    "<white>This will allow you to claim more outposts."
                                )
                            )
                    )
                )
        ),
        Map.entry(
            "UpgradeMenu",
            new Menu.Config()
                .setTitle("Town Upgrades")
                .setRows(5)
                .setDecoratorElements(commonDecorators(5))
                .setElements(
                    Map.of(
                        "go-home", new MenuElement()
                            .setMaterial(Material.RED_STAINED_GLASS_PANE)
                            .setSlot(36)
                            .setLore(
                                List.of(
                                    "",
                                    "<red>Click to return to the main menu."
                                )
                            ),
                        "upgrade-info-" + Upgrade.CLAIMS, new MenuElement()
                            .setMaterial(Material.GRASS_BLOCK)
                            .setSlot(4)
                            .setDisplayName("<gold>Claim Upgrades")
                            .setLore(
                                List.of(
                                    "",
                                    "<yellow>■ Click to upgrade amount of claims."
                                )
                            ),
                        "upgrade-info-" + Upgrade.MEMBERS, new MenuElement()
                            .setMaterial(Material.PLAYER_HEAD)
                            .setSlot(4)
                            .setDisplayName("<gold>Resident Upgrades")
                            .setLore(
                                List.of(
                                    "",
                                    "<yellow>■ Click to upgrade amount of residents."
                                )
                            ),
                        "upgrade-info-" + Upgrade.VAULT_AMOUNT, new MenuElement()
                            .setMaterial(Material.ENDER_CHEST)
                            .setSlot(4)
                            .setDisplayName("<gold>Vault Upgrades")
                            .setLore(
                                List.of(
                                    "",
                                    "<yellow>■ Click to upgrade amount of vaults."
                                )
                            ),
                        "upgrade-info-" + Upgrade.OUTPOSTS, new MenuElement()
                            .setMaterial(Material.DARK_OAK_DOOR)
                            .setSlot(4)
                            .setDisplayName("<gold>Outpost Upgrades")
                            .setLore(
                                List.of(
                                    "",
                                    "<yellow>■ Click to upgrade amount of outposts."
                                )
                            ),
                        "upgrade-unlocked", new MenuElement()
                            .setMaterial(Material.LIME_STAINED_GLASS_PANE)
                            .setSlot(19)
                            .setDisplayName("<green><b>Unlocked")
                            .setLore(
                                List.of(
                                    "",
                                    "Tier: <tier>",
                                    "Amount: <amount>"
                                )
                            ),
                        "upgrade-locked", new MenuElement()
                            .setMaterial(Material.RED_STAINED_GLASS_PANE)
                            .setSlot(19)
                            .setDisplayName("<red><b>Locked")
                            .setLore(
                                List.of(
                                    "<gray><upgrade>",
                                    "<gold>Tier: <white><tier>",
                                    "<gold>Amount: <white><amount>",
                                    "<gold>Price: <white><price>"
                                )
                            )
                    )
                )
        ),
        Map.entry(
            "VaultSelectorMenu",
            new Menu.Config()
                .setTitle("Vault Selector")
                .setRows(1)
                .setElements(
                    Map.of(
                        "vault", new MenuElement()
                            .setMaterial(Material.CHEST)
                            .setDisplayName("<gold><b>Vault #<number>")
                    )
                )
        )
    );

    @Setting
    private List<String> commonPermissionLore = List.of(
        "<status:'<green>Enabled':'<red>Disabled'>",
        "",
        "<yellow>■ Click to toggle."
    );

    private static List<MenuElement> commonDecorators(final int rows) {
        return commonDecorators(rows, Material.GRAY_STAINED_GLASS_PANE);
    }

    private static List<MenuElement> commonDecorators(final int rows, final Material material) {
        final List<MenuElement> elements = new ArrayList<>();
        final List<IntRange> border = new ArrayList<>();
        border.add(new IntRange(0, 9));
        border.add(new IntRange(18, 18));
        border.add(new IntRange(27, 27));
        border.add(new IntRange(36, 36));
        border.add(new IntRange(45, 45));
        border.add(new IntRange(17, 17));
        border.add(new IntRange(26, 26));
        border.add(new IntRange(35, 35));

        if (rows == 6) {
            border.add(new IntRange(44, 53));
        }

        elements.add(new MenuElement()
            .setMaterial(material)
            .setSlots(border)
        );

        return elements;
    }

    public static Menus get() {
        return OxyTownsPlugin.configManager.get(Menus.class);
    }
}
