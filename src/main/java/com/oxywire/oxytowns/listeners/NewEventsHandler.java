package com.oxywire.oxytowns.listeners;

import com.destroystokyo.paper.MaterialSetTag;
import com.destroystokyo.paper.MaterialTags;
import com.google.common.collect.Sets;
import com.oxywire.oxytowns.OxyTownsPlugin;
import com.oxywire.oxytowns.cache.TownCache;
import com.oxywire.oxytowns.config.Config;
import com.oxywire.oxytowns.config.Messages;
import com.oxywire.oxytowns.config.messaging.Message;
import com.oxywire.oxytowns.entities.impl.plot.Plot;
import com.oxywire.oxytowns.entities.impl.town.Town;
import com.oxywire.oxytowns.entities.types.PlotType;
import com.oxywire.oxytowns.entities.types.perms.Permission;
import com.oxywire.oxytowns.entities.types.settings.Setting;
import com.oxywire.oxytowns.utils.ChunkPosition;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Allay;
import org.bukkit.entity.Animals;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Enemy;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Hanging;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Vehicle;
import org.bukkit.entity.WaterMob;
import org.bukkit.entity.minecart.HopperMinecart;
import org.bukkit.entity.minecart.StorageMinecart;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityPlaceEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.bukkit.event.entity.SheepDyeWoolEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketEntityEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.event.player.PlayerTakeLecternBookEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerUnleashEntityEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.world.PortalCreateEvent;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiPredicate;

@SuppressWarnings("unused")
public class NewEventsHandler implements Listener {

    private final TownCache cache = OxyTownsPlugin.get().getTownCache();

    private static final Set<Material> CHEST_MATERIALS = EnumSet.of(Material.CHEST, Material.TRAPPED_CHEST, Material.BARREL, Material.CHISELED_BOOKSHELF);
    private static final Set<Material> FURNACE_MATERIALS = EnumSet.of(Material.FURNACE, Material.BLAST_FURNACE, Material.SMOKER, Material.CAMPFIRE,
        Material.SOUL_CAMPFIRE);
    private static final Set<Material> REDSTONE_MATERIALS = Sets.newHashSet(Material.DISPENSER, Material.DROPPER, Material.HOPPER, Material.DAYLIGHT_DETECTOR,
        Material.REPEATER, Material.COMPARATOR, Material.NOTE_BLOCK, Material.JUKEBOX, Material.CRAFTER);
    private static final Set<Material> DOOR_MATERIALS = EnumSet.noneOf(Material.class);
    private static final Set<Material> INTERACT_SETS = EnumSet.of(Material.PUMPKIN, Material.CAKE, Material.CAVE_VINES_PLANT, Material.CAVE_VINES, Material.SWEET_BERRY_BUSH, Material.RESPAWN_ANCHOR, Material.DECORATED_POT);

    static {
        CHEST_MATERIALS.addAll(Tag.SHULKER_BOXES.getValues());
        CHEST_MATERIALS.addAll(Tag.SHULKER_BOXES.getValues());
        DOOR_MATERIALS.addAll(Tag.DOORS.getValues());
        DOOR_MATERIALS.addAll(Tag.TRAPDOORS.getValues());
        DOOR_MATERIALS.addAll(Tag.FENCE_GATES.getValues());
        INTERACT_SETS.addAll(Tag.CANDLES.getValues());
        INTERACT_SETS.addAll(Tag.FLOWER_POTS.getValues());
        INTERACT_SETS.remove(Material.FLOWER_POT); // ?
        INTERACT_SETS.addAll(Tag.LOGS.getValues());
        INTERACT_SETS.add(Material.DRAGON_EGG);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!cache.isBypassing(event.getPlayer()) && canInteract(event.getPlayer(), event.getBlock().getLocation(), Permission.BLOCK_BREAK, event.getBlock())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak$0(BlockBreakEvent event) {
        handleFarmland(event.getPlayer(), event);
    }

    @EventHandler
    public void onHangingBreakByEntity(HangingBreakByEntityEvent event) {
        if (event.getRemover() instanceof Player player
            && !cache.isBypassing(player)
            && (event.getEntity() instanceof Hanging)
            && canInteract(player, event.getEntity().getLocation(), Permission.BLOCK_BREAK, event.getEntity())
        ) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onHangingBreakByEntity$1(HangingBreakByEntityEvent event) {
        if (event.getRemover() instanceof Projectile projectile
            && projectile.getShooter()  != null
            && projectile.getShooter() instanceof Player player
            && !cache.isBypassing(player)
            && canInteract(player, event.getEntity().getLocation(), Permission.BLOCK_BREAK, event.getEntity())
        ) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Player player = null;

        if (event.getDamager() instanceof Projectile projectile && projectile.getShooter() != null && projectile.getShooter() instanceof Player shooter) {
            player = shooter;
        }

        if (event.getDamager() instanceof Player attacker && !(event.getEntity() instanceof Player)) {
            player = attacker;
        }

        Town town = cache.getTownByLocation(event.getEntity().getLocation());
        if (town == null) {
            return;
        }

        Plot plot = town.getPlot(event.getEntity().getLocation());
        if ((plot == null || plot.getType() == PlotType.MOB_FARM) && event.getEntity() instanceof Enemy) {
            return;
        }

        if (player != null && !cache.isBypassing(player) && canInteract(player, event.getEntity().getLocation(), Permission.BLOCK_BREAK, event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        if (event.getEntity() instanceof Projectile projectile
            && projectile.getShooter() != null
            && projectile.getShooter() instanceof Player player
            && !cache.isBypassing(player)
            && Tag.CAMPFIRES.isTagged(event.getBlock().getType())
            && canInteract(player, event.getBlock().getLocation(), Permission.BLOCK_BREAK, event.getBlock())
        ) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBucketFill(PlayerBucketFillEvent event) {
        if (!cache.isBypassing(event.getPlayer())
            && canInteract(event.getPlayer(), event.getBlockClicked().getLocation(), Permission.BLOCK_BREAK, event.getBlock())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        if (!cache.isBypassing(event.getPlayer())
            && canInteract(event.getPlayer(), event.getBlockClicked().getLocation(), Permission.BLOCK_PLACE, event.getBlock())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getBlock().getType() == Material.LECTERN) {
            return;
        }

        if (!cache.isBypassing(event.getPlayer())
            && canInteract(event.getPlayer(), event.getBlock().getLocation(), Permission.BLOCK_PLACE, event.getBlockPlaced())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() != null
            && INTERACT_SETS.contains(event.getClickedBlock().getType())
            && !cache.isBypassing(event.getPlayer())
            && canInteract(event.getPlayer(), event.getClickedBlock().getLocation(), Permission.BLOCK_BREAK, event.getClickedBlock())
        ) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onHangingPlace(HangingPlaceEvent event) {
        if (event.getPlayer() != null
        && !cache.isBypassing(event.getPlayer())
        && canInteract(event.getPlayer(), event.getEntity().getLocation(), Permission.BLOCK_PLACE, event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof Hanging
        && !cache.isBypassing(event.getPlayer())
        && canInteract(event.getPlayer(), event.getRightClicked().getLocation(), Permission.BLOCK_PLACE, event.getRightClicked())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract$0(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK
            && (
                Tag.FLOWERS.isTagged(event.getClickedBlock().getType())
                    || (event.hasItem() && event.getMaterial() == Material.BONE_MEAL)
                    || MaterialSetTag.ALL_SIGNS.isTagged(event.getClickedBlock().getType())
            )
            && !cache.isBypassing(event.getPlayer())
            && canInteract(event.getPlayer(), event.getClickedBlock().getLocation(), Permission.BLOCK_PLACE, event.getClickedBlock())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
        if (!cache.isBypassing(event.getPlayer())
            && canInteract(event.getPlayer(), event.getBlockClicked().getLocation(), Permission.BLOCK_PLACE, event.getBlockClicked())) {
            event.setCancelled(true);
        }
    }

    // Chest Access
    @EventHandler
    public void onPlayerInteract$1(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK
            && CHEST_MATERIALS.contains(event.getClickedBlock().getType())
            && !cache.isBypassing(event.getPlayer())
            && canInteract(event.getPlayer(), event.getClickedBlock().getLocation(), Permission.CHESTS, event.getClickedBlock())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onChestCartInteract(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof StorageMinecart
            && !cache.isBypassing(event.getPlayer())
            && canInteract(event.getPlayer(), event.getRightClicked().getLocation(), Permission.CHESTS, event.getRightClicked())) {
            event.setCancelled(true);
        }
    }

    // Furnace Access
    @EventHandler
    public void onPlayerInteract$2(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK
            && FURNACE_MATERIALS.contains(event.getClickedBlock().getType())
            && !cache.isBypassing(event.getPlayer())
            && canInteract(event.getPlayer(), event.getClickedBlock().getLocation(), Permission.FURNACES, event.getClickedBlock())) {
            event.setCancelled(true);
        }
    }

    // Door Access
    @EventHandler
    public void onPlayerInteract$3(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK
            && DOOR_MATERIALS.contains(event.getClickedBlock().getType())
            && !cache.isBypassing(event.getPlayer())
            && canInteract(event.getPlayer(), event.getClickedBlock().getLocation(), Permission.DOORS, event.getClickedBlock())) {
            event.setCancelled(true);
        }
    }

    // Buttons & Lever Access
    @EventHandler
    public void onPlayerInteract$4(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK
            && (event.getClickedBlock().getType() == Material.LEVER || Tag.BUTTONS.isTagged(event.getClickedBlock().getType()))
            && !cache.isBypassing(event.getPlayer())
            && canInteract(event.getPlayer(), event.getClickedBlock().getLocation(), Permission.BUTTONS, event.getClickedBlock())) {
            event.setCancelled(true);
        }
    }

    // Pressure Plates
    @EventHandler
    public void onPlayerInteract$5(PlayerInteractEvent event) {
        if (event.getAction() == Action.PHYSICAL
            && Tag.PRESSURE_PLATES.isTagged(event.getClickedBlock().getType())
            && !cache.isBypassing(event.getPlayer())
            && canInteract(event.getPlayer(), event.getClickedBlock().getLocation(), Permission.PLATES, event.getClickedBlock())) {
            event.setCancelled(true);
        }
    }

    // Anvils
    @EventHandler
    public void onPlayerInteract$6(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK
            && event.getClickedBlock().getType() == Material.ANVIL
            && !cache.isBypassing(event.getPlayer())
            && canInteract(event.getPlayer(), event.getClickedBlock().getLocation(), Permission.ANVIL, event.getClickedBlock())) {
            event.setCancelled(true);
        }
    }

    // Brewing Access
    @EventHandler
    public void onPlayerInteract$7(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK
            && (event.getClickedBlock().getType() == Material.BREWING_STAND || Tag.CAULDRONS.isTagged(event.getClickedBlock().getType()))
            && !cache.isBypassing(event.getPlayer())
            && canInteract(event.getPlayer(), event.getClickedBlock().getLocation(), Permission.BREWING, event.getClickedBlock())) {
            event.setCancelled(true);
        }
    }

    // Animal Welfare

    @EventHandler
    public void onPlayerInteractEntity$0(PlayerInteractEntityEvent event) {
        if ((event.getRightClicked() instanceof Animals || event.getRightClicked() instanceof Allay && !(event.getRightClicked() instanceof Vehicle))
            && !cache.isBypassing(event.getPlayer())
            && canInteract(event.getPlayer(), event.getRightClicked().getLocation(), Permission.ANIMALS, event.getRightClicked())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onSheepDyeWool(SheepDyeWoolEvent event) {
        if (event.getPlayer() != null
            && !cache.isBypassing(event.getPlayer())
            && canInteract(event.getPlayer(), event.getEntity().getLocation(), Permission.ANIMALS, event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerLeashEntity(PlayerLeashEntityEvent event) {
        if (!cache.isBypassing(event.getPlayer())
            && canInteract(event.getPlayer(), event.getEntity().getLocation(), Permission.ANIMALS, event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerUnleashEntity(PlayerUnleashEntityEvent event) {
        if (!cache.isBypassing(event.getPlayer())
            && canInteract(event.getPlayer(), event.getEntity().getLocation(), Permission.ANIMALS, event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerShearEntity(PlayerShearEntityEvent event) {
        if (!cache.isBypassing(event.getPlayer())
            && canInteract(event.getPlayer(), event.getEntity().getLocation(), Permission.ANIMALS, event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity$0(EntityDamageByEntityEvent event) {
        if ((event.getEntity() instanceof Animals || event.getEntity() instanceof WaterMob)) {
            final Entity attacker = event.getDamager();
            final Entity target = event.getEntity();

            if (attacker instanceof Player player && !cache.isBypassing(player) && canInteract(player, target.getLocation(), Permission.ANIMALS, target)) {
                event.setCancelled(true);
            } else if (attacker instanceof Projectile projectile
                && projectile.getShooter() instanceof Player player
                && !cache.isBypassing(player)
                && canInteract(player, target.getLocation(), Permission.ANIMALS, target)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerInteract$8(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK
            && (event.getClickedBlock().getType() == Material.BEEHIVE || event.getClickedBlock().getType() == Material.BEE_NEST)
            && !cache.isBypassing(event.getPlayer())
            && canInteract(event.getPlayer(), event.getClickedBlock().getLocation(), Permission.ANIMALS, event.getClickedBlock())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract$14(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK
            && event.getItem() != null
            && MaterialTags.SPAWN_EGGS.isTagged(event.getItem())
            && !cache.isBypassing(event.getPlayer())
            && canInteract(event.getPlayer(), event.getClickedBlock().getLocation(), Permission.ANIMALS, event.getClickedBlock())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerBucketEntity(PlayerBucketEntityEvent event) {
        if (!cache.isBypassing(event.getPlayer())
            && canInteract(event.getPlayer(), event.getEntity().getLocation(), Permission.ANIMALS, event.getEntity())) {
            event.setCancelled(true);
        }
    }

    // Vehicles

    @EventHandler
    public void onVehicleEnter(VehicleEnterEvent event) {
        if (event.getEntered() instanceof Player player
            && !cache.isBypassing(player)
            && canInteract(player, event.getVehicle().getLocation(), Permission.VEHICLES, event.getEntered())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityPlace(EntityPlaceEvent event) {
        if ((event.getEntity() instanceof Boat
            || event.getEntity() instanceof Minecart)
            && event.getPlayer() != null
            && !cache.isBypassing(event.getPlayer())
            && canInteract(event.getPlayer(), event.getEntity().getLocation(), Permission.BLOCK_PLACE, event.getEntity())
        ) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onVehicleDamage(VehicleDamageEvent event) {
        if (event.getAttacker() instanceof Player player
            && !cache.isBypassing(player)
            && canInteract(player, event.getVehicle().getLocation(), Permission.VEHICLES, event.getVehicle())
        ) {
            event.setCancelled(true);
        }
    }

    //todo check rest of animal perms ?
    @EventHandler
    public void onPlayerInteractEntity$1(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof Animals
        && event.getRightClicked() instanceof Vehicle
        && !cache.isBypassing(event.getPlayer())
        && canInteract(event.getPlayer(), event.getRightClicked().getLocation(), Permission.VEHICLES, event.getRightClicked())) {
            event.setCancelled(true);
        }
    }

    // Redstone
    @EventHandler
    public void onPlayerInteract$9(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK
            && REDSTONE_MATERIALS.contains(event.getClickedBlock().getType())
            && !cache.isBypassing(event.getPlayer())
            && canInteract(event.getPlayer(), event.getClickedBlock().getLocation(), Permission.REDSTONE, event.getClickedBlock())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onHopperCartInteract(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof HopperMinecart
            && !cache.isBypassing(event.getPlayer())
            && canInteract(event.getPlayer(), event.getRightClicked().getLocation(), Permission.REDSTONE, event.getRightClicked())) {
            event.setCancelled(true);
        }
    }

    // Tripwires
    @EventHandler
    public void onPlayerInteract$10(PlayerInteractEvent event) {
        if (event.getAction() == Action.PHYSICAL
            && event.getClickedBlock().getType() == Material.TRIPWIRE
            && !cache.isBypassing(event.getPlayer())
            && canInteract(event.getPlayer(), event.getClickedBlock().getLocation(), Permission.REDSTONE, event.getClickedBlock())) {
            event.setCancelled(true);
        }
    }

    // Armor Stand

    @EventHandler
    public void onEntityDamageByEntity$1(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof ArmorStand
            && event.getDamager() instanceof Player player
            && !cache.isBypassing(player)
            && canInteract(player, event.getEntity().getLocation(), Permission.ARMOR_STAND, event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract$11(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK
            && event.getClickedBlock().getType() == Material.ARMOR_STAND
            && !cache.isBypassing(event.getPlayer())
            && canInteract(event.getPlayer(), event.getClickedBlock().getLocation(), Permission.ARMOR_STAND, event.getClickedBlock())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
        if (event.getRightClicked() instanceof ArmorStand
            && !cache.isBypassing(event.getPlayer())
            && canInteract(event.getPlayer(), event.getRightClicked().getLocation(), Permission.ARMOR_STAND, event.getRightClicked())) {
            event.setCancelled(true);
        }
    }

    // Composting
    @EventHandler
    public void onPlayerInteract$12(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK
            && event.getClickedBlock().getType() == Material.COMPOSTER
            && !cache.isBypassing(event.getPlayer())
            && canInteract(event.getPlayer(), event.getClickedBlock().getLocation(), Permission.COMPOSTING, event.getClickedBlock())) {
            event.setCancelled(true);
        }
    }

    // Beacon Access
    @EventHandler
    public void onPlayerInteract$13(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK
            && event.getClickedBlock().getType() == Material.BEACON
            && !cache.isBypassing(event.getPlayer())
            && canInteract(event.getPlayer(), event.getClickedBlock().getLocation(), Permission.BEACON, event.getClickedBlock())) {
            event.setCancelled(true);
        }
    }

    // Lectern
    @EventHandler
    public void onPlayerTakeLecternBook(PlayerTakeLecternBookEvent event) {
        if (!cache.isBypassing(event.getPlayer())
            && canInteract(event.getPlayer(), event.getLectern().getLocation(), Permission.LECTERN, event.getLectern())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace$0(BlockPlaceEvent event) {
        if (event.getBlock().getType() == Material.LECTERN
            && Tag.ITEMS_LECTERN_BOOKS.isTagged(event.getItemInHand().getType())
            && !cache.isBypassing(event.getPlayer())
            && canInteract(event.getPlayer(), event.getBlock().getLocation(), Permission.LECTERN, event.getBlock())
        ) {
            event.setCancelled(true);
        }
    }

    // Chunk region information
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        final Location from = event.getFrom();
        final Location to = event.getTo();
        if (to == null) {
            return;
        }

        if ((from.getBlockX() >> 4) != (to.getBlockX() >> 4) || (from.getBlockZ() >> 4) != (to.getBlockZ() >> 4) && !cache.isBypassing(event.getPlayer())) {
            final Player player = event.getPlayer();
            final Town oldTown = cache.getTownByLocation(event.getFrom());
            final Town newTown = cache.getTownByLocation(event.getTo());

            if (oldTown == null && newTown == null) { // Continuing in Wilderness
                return;
            }

            Messages messages = Messages.get();
            if (oldTown != null && newTown == null) { // Entering Wilderness
                messages.getNowEnteringWilderness().send(player);
                return;
            }

            if ((newTown != null && oldTown == null) || !newTown.equals(oldTown)) { // Entering new Territory
                if (newTown.getBannedUUIDs().contains(player.getUniqueId()) && !cache.isBypassing(player)) {
                    event.setCancelled(true);
                    messages.getPlayer().getBannedWarningTitle().send(player);
                    return;
                }
                messages.getNowEnteringTown().send(player, Placeholder.unparsed("town", newTown.getName()));
            }

            Plot plot = newTown.getPlot(event.getTo());
            if (plot != null) {
                messages.getTown().getPlot().getEnter().send(
                    player,
                    Placeholder.unparsed("plot", plot.getName()),
                    Placeholder.unparsed("type", plot.getType().name())
                );
                // Disable their fly if it is a PVP plot
                if (plot.getType() == PlotType.ARENA && !(player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR)) {
                    player.setAllowFlight(false);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        final Town town = cache.getTownByLocation(event.getTo());
        if (town != null && town.getBannedUUIDs().contains(event.getPlayer().getUniqueId()) && !cache.isBypassing(event.getPlayer())) {
            Messages.get().getPlayer().getBannedWarningTitle().send(event.getPlayer());
            event.setCancelled(true);
        }
    }

    // Border stuff

    @EventHandler
    public void onBlockPistonRetract(BlockPistonRetractEvent event) {
        if (borderCheck(event.getBlock(), event.getDirection().getOppositeFace(), event.getBlocks())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPistonExtend(BlockPistonExtendEvent event) {
        if (borderCheck(event.getBlock(), event.getDirection(), event.getBlocks())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockFromTo(BlockFromToEvent event) {
        if (crossesBorder(event.getBlock(), event.getToBlock())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        event.blockList().removeIf(block -> crossesBorder(event.getEntity().getLocation().getBlock(), block));
    }

    @EventHandler
    public void onPortalCreate(PortalCreateEvent event) {
        if (event.getBlocks().isEmpty()) {
            return;
        }

        BlockState first = event.getBlocks().get(0);
        if (event.getEntity() instanceof Player player
            && !cache.isBypassing(player)
            && canInteract(player, first.getLocation(), Permission.BLOCK_PLACE, first.getBlock().getType())
        ) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityBlockForm(EntityBlockFormEvent event) {
        if (event.getEntity() instanceof Player player
            && !cache.isBypassing(player)
            && canInteract(player, event.getBlock().getLocation(), Permission.BLOCK_PLACE, event.getBlock().getType())
        ) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        final Town town = this.cache.getTownByLocation(event.getPlayer().getLocation());
        if (town == null) {
            return;
        }

        final Plot plot = town.getPlot(event.getPlayer().getLocation());
        if (plot == null) {
            return;
        }

        if (plot.getType().isCommandBlacklisted(event.getMessage())) {
            Messages.get().getPlayer().getCantDoThatCommandInThisPlotType().send(event.getPlayer(), Placeholder.unparsed("type", Message.formatEnum(plot.getType())));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity$2(EntityDamageByEntityEvent event) {
        // Figure the attacker
        Player attacker = null;
        if (event.getDamager() instanceof Player player) attacker = player;
        else if (event.getDamager() instanceof Projectile projectile && projectile.getShooter() instanceof Player player) attacker = player;
        if (attacker == null) return;
        // Figure the victim
        if (!(event.getEntity() instanceof Player victim)) return;

        // Don't proc in non-whitelisted worlds
        if (Config.get().getBlacklistedWorlds().contains(victim.getWorld().getName())) return;

        ChunkPosition attackerLocation = ChunkPosition.chunkPosition(attacker.getLocation());
        ChunkPosition victimLocation = ChunkPosition.chunkPosition(victim.getLocation());
        Town attackerTown = cache.getTownByChunk(attackerLocation);
        Town victimTown = cache.getTownByChunk(victimLocation);

        // If both players are in wilderness, and we allow pvp in wilderness, allow it
        if (attackerTown == null && victimTown == null) {
            if (Config.get().isAllowPvpInWilderness()) return;

            event.setCancelled(true);
            return;
        }

        BiPredicate<Town, Location> permitsPvpHalfHalf = (town, location) -> {
            // We're expecting the other player to be in wilderness
            if (!Config.get().isAllowPvpInWilderness()) return false;

            Plot plot = town.getPlot(location);
            // If the plot was at all modified, only allow if it's an arena plot
            // Otherwise, check the town toggle
            if (plot != null && plot.isModified() && plot.getType() != PlotType.ARENA) return false;
            else return town.getToggle(Setting.PVP);
        };

        // If the attacker is in wilderness, and the victim is not
        if (attackerTown == null) {
            if (permitsPvpHalfHalf.test(victimTown, victim.getLocation())) return;

            event.setCancelled(true);
            return;
        }

        // If the attacker is in wilderness, and the victim is not
        if (victimTown == null) {
            if (permitsPvpHalfHalf.test(attackerTown, attacker.getLocation())) return;

            event.setCancelled(true);
            return;
        }

        // If both players are in a town
        Plot attackerPlot = attackerTown.getPlot(attacker.getLocation());
        Plot victimPlot = victimTown.getPlot(victim.getLocation());

        if (attackerPlot != null && attackerPlot.isModified() && attackerPlot.getType() == PlotType.ARENA
            && victimPlot != null && victimPlot.isModified() && victimPlot.getType() == PlotType.ARENA) return;
        else if (attackerTown.getToggle(Setting.PVP) && victimTown.getToggle(Setting.PVP)) return;

        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntitySpawn(final EntitySpawnEvent event) {
        if (!(event.getEntity() instanceof Enemy)) return;
        if (Config.get().getBlacklistedWorlds().contains(event.getLocation().getWorld().getName())) return;

        final Town town = cache.getTownByLocation(event.getLocation());
        if (town == null) return;

        final Plot plot = town.getPlot(event.getLocation());
        if (plot != null && plot.isModified()) {
            // If the plot was at all modified, only allow mobs if it's a mob farm plot
            if (plot.getType() != PlotType.MOB_FARM) {
                event.setCancelled(true);
            }
        } else if (!town.getToggle(Setting.MOBS)) {
            // Otherwise, check the town toggle
            event.setCancelled(true);
        }
    }

    private boolean borderCheck(final Block block, final BlockFace direction, final List<Block> blocks) {
        final Block testing = block.getRelative(direction);
        if (crossesBorder(block, testing)) {
            return true;
        }

        if (blocks.isEmpty()) {
            return false;
        }

        for (final Block blockTest : blocks) {
            if (crossesBorder(block, blockTest.getRelative(direction))) {
                return true;
            }
        }
        return false;
    }

    private boolean crossesBorder(final Block current, final Block target) {
        final Town currentTown = cache.getTownByLocation(current.getLocation());
        final Town targetTown = cache.getTownByLocation(target.getLocation());

        if (currentTown == null && targetTown == null) {
            return false;
        }

        return currentTown == null || targetTown == null;
    }


    /**
     * Check if a player can interact with something at a specific location
     *
     * @param player     the player to check
     * @param location   the location to check
     * @param permission the permission to check
     * @return if the player can interact at the specific location
     */
    private boolean canInteract(final Player player, final Location location, final Permission permission, Object queryObject) {
        return !OxyTownsPlugin.get().getOxyTownsApi().hasPermission(
            player.getUniqueId(),
            permission,
            ChunkPosition.chunkPosition(location),
            queryObject
        );
    }

    public <E extends BlockEvent & Cancellable> void handleFarmland(Player player, E event) {
        Location location = event.getBlock().getLocation();
        if (event.getBlock().getType() != Material.FARMLAND || cache.isBypassing(player)) return;
        Town town = cache.getTownByLocation(location);
        if (town == null) return;
        Plot plot = town.getPlot(location);

        if (plot != null
            && plot.getType() == PlotType.FARM
            && !plot.getAssignedMembers().contains(player.getUniqueId())
            && !town.hasPermission(player.getUniqueId(), Permission.PLOTS_MODIFY)
        ) {
            event.setCancelled(true);
        }
    }
}
