package com.oxywire.oxytowns.cache;

import com.google.common.collect.Sets;
import com.oxywire.oxytowns.OxyTownsPlugin;
import com.oxywire.oxytowns.entities.impl.town.Town;
import com.oxywire.oxytowns.menu.town.VaultSelectorMenu;
import com.oxywire.oxytowns.storage.TownStorageManager;
import com.oxywire.oxytowns.utils.ChunkPosition;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public final class TownCache {

    private final TownStorageManager townDao;
    private final Set<Town> towns;
    private final Map<ChunkPosition, Town> townsMap;
    private final Set<UUID> bypass;

    public TownCache(final OxyTownsPlugin plugin) {
        this.townDao = new TownStorageManager(plugin);
        this.towns = Sets.newConcurrentHashSet(this.townDao.getAll());
        this.townsMap = new ConcurrentHashMap<>();
        this.bypass = Sets.newConcurrentHashSet();

        for (Town town : this.towns) {
            for (ChunkPosition position : town.getOutpostAndClaimedChunks()) {
                this.townsMap.put(position, town);
            }
        }

        this.updateVaultLogic();

        plugin.getLogger().info(() -> "Successfully loaded " + this.towns.size() + " towns!");
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::saveTowns, 12_000, 12_000); // 10 mins
    }

    /**
     * Helper method to save all the towns on the server.
     */
    public void saveTowns() {
        this.towns.forEach(this.townDao::save);
    }

    /**
     * Called on shutdown to save towns sync.
     */
    public void unloadTowns() {
        this.towns.forEach(this.townDao::unload);
    }

    /**
     * Get a town by its uuid.
     *
     * @param uuid the uuid to look for
     * @return town if found
     */
    public Optional<Town> getTownById(final UUID uuid) {
        return this.towns.stream().filter(town -> town.getTownId().equals(uuid)).findAny();
    }

    /**
     * Get a town by a player.
     *
     * @param player the player to check
     * @return town if found
     */
    public Optional<Town> getTownByPlayer(final Player player) {
        return this.getTownByPlayer(player.getUniqueId());
    }

    /**
     * Get a town by a player's uuid.
     *
     * @param uuid the uuid of the player
     * @return town if found
     */
    public Optional<Town> getTownByPlayer(final UUID uuid) {
        return this.towns.stream().filter(town -> town.isMemberOrOwner(uuid)).findAny();
    }

    /**
     * Get a town by the name.
     *
     * @param name the name of the town
     * @return town if found
     */
    public Optional<Town> getTownByName(final String name) {
        return this.towns.stream().filter(town -> town.getName().equalsIgnoreCase(name)).findAny();
    }

    /**
     * Get a town by the location.
     *
     * @param location the location to check
     * @return town if found
     */
    public Town getTownByLocation(final Location location) {
        return this.townsMap.get(ChunkPosition.chunkPosition(location));
    }

    /**
     * Get all towns in a set of chunks.
     *
     * @param chunks the chunks to check
     * @return the towns in the chunks
     */
    public Set<Town> getTownsInChunks(final Collection<ChunkPosition> chunks) {
        return chunks.stream().map(this.townsMap::get).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    /**
     * Get a town by a specific chunk.
     *
     * @param chunk the chunk to check
     * @return town if found
     */
    public Town getTownByChunk(final ChunkPosition chunk) {
        return this.townsMap.get(chunk);
    }

    /**
     * Helper method to create a town.
     *
     * @param town the town to add
     */
    public void createTown(final Town town) {
        this.towns.add(town);
        this.townsMap.putAll(town.getOutpostAndClaimedChunks().stream().collect(Collectors.toMap(chunk -> chunk, chunk -> town)));
    }

    /**
     * Helper method to get all towns.
     *
     * @return set of towns
     */
    public Set<Town> all() {
        return Set.copyOf(this.towns);
    }

    /**
     * Helper method to delete a town.
     *
     * @param town the town to delete
     */
    public synchronized void deleteTown(final Town town) {
        this.townDao.delete(town);
        this.towns.remove(town);
        this.townsMap.values().removeIf(it -> it == town);
    }

    /**
     * Update vault logic for a specific town.
     *
     * @param town the town to update the vault logic on
     */
    public void updateVaultLogic(final Town town) {
        town.getVaults().forEach(vault -> vault.setOutsideClickAction(event -> {
            event.setCancelled(true);
            VaultSelectorMenu.open(((Player) event.getWhoClicked()), town);
        }));
    }

    /**
     * Helper method to see if a player is in bypass mode.
     *
     * @param uuid the uuid to check
     * @return bypass mode or not
     */
    public boolean isBypassing(final UUID uuid) {
        return this.bypass.contains(uuid);
    }

    /**
     * Helper method to see if player is in bypass mode.
     *
     * @param player the player to check
     * @return bypass mode or not
     */
    public boolean isBypassing(final Player player) {
        return this.isBypassing(player.getUniqueId());
    }

    /**
     * Helper method to make a player start bypassing.
     *
     * @param player the player to start bypassing
     */
    public void startBypassing(final Player player) {
        this.bypass.add(player.getUniqueId());
    }

    /**
     * Helper method to make a player stop bypassing.
     *
     * @param player the player to stop bypassing
     */
    public void stopBypassing(final Player player) {
        this.bypass.remove(player.getUniqueId());
    }

    /**
     * This is ran after the cache is generated on startup.
     * <p>
     * Loop through all the vaults and add in an outside click action
     */
    public void updateVaultLogic() {
        Bukkit.getScheduler().runTaskAsynchronously(OxyTownsPlugin.get(), () -> this.all().forEach(this::updateVaultLogic));
    }

    public Set<UUID> getBypass() {
        return this.bypass;
    }

    public Set<Town> getTowns() {
        return towns;
    }

    public Map<ChunkPosition, Town> getTownsMap() {
        return townsMap;
    }
}
