package com.oxywire.oxytowns.entities.impl.town;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.oxywire.oxytowns.OxyTownsPlugin;
import com.oxywire.oxytowns.config.Messages;
import com.oxywire.oxytowns.entities.impl.BanEntry;
import com.oxywire.oxytowns.entities.impl.TrustedEntry;
import com.oxywire.oxytowns.entities.impl.plot.Plot;
import com.oxywire.oxytowns.entities.model.Named;
import com.oxywire.oxytowns.entities.model.Organisation;
import com.oxywire.oxytowns.entities.types.PlotType;
import com.oxywire.oxytowns.entities.types.Role;
import com.oxywire.oxytowns.entities.types.Upgrade;
import com.oxywire.oxytowns.entities.types.perms.Permission;
import com.oxywire.oxytowns.entities.types.settings.Setting;
import com.oxywire.oxytowns.entities.types.settings.SpawnSetting;
import com.oxywire.oxytowns.menu.town.VaultMenu;
import com.oxywire.oxytowns.runnable.TaxSchedule;
import com.oxywire.oxytowns.entities.model.CreatedDateHolder;
import com.oxywire.oxytowns.utils.ChunkPosition;
import com.oxywire.oxytowns.utils.Placeholdered;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.ZoneId;
import java.util.Date;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Getter
public final class Town implements CreatedDateHolder, Organisation<UUID>, ForwardingAudience, Placeholdered, Named {

    private final UUID townId;
    @Setter
    private String name;
    private UUID owner;
    @Setter
    private Map<UUID, Role> members;
    private final Set<TrustedEntry> trusted;
    private final Map<Role, Set<Permission>> permissionSets;
    // private final Set<ChunkPosition> claimedChunks;
    private final Set<Location> outpostChunks;
    private final Map<ChunkPosition, Plot> playerPlots;
    private Location spawnPosition;
    @Setter
    private double bankValue;
    private final Map<Upgrade, Integer> townUpgrades;
    private final List<VaultMenu> vaults;
    private final Set<BanEntry> bans;
    private SpawnSetting spawnSetting;
    private final Map<Setting, Boolean> townToggles;
    @Getter
    private final Date creationDate;

    private transient Cache<UUID, UUID> invitedPlayers = CacheBuilder.newBuilder()
        .expireAfterWrite(5, TimeUnit.MINUTES)
        .build();

    public Town(final UUID townId, final String name, final UUID owner) {
        this.townId = townId;
        this.name = name;
        this.owner = owner;
        this.members = new ConcurrentHashMap<>();
        this.permissionSets = OxyTownsPlugin.get().getOxyTownsApi().createDefault();
        // this.claimedChunks = Sets.newConcurrentHashSet();
        this.outpostChunks = Sets.newConcurrentHashSet();
        this.playerPlots = new ConcurrentHashMap<>();
        this.spawnPosition = null;
        this.bankValue = 0.0d;
        this.townUpgrades = Maps.newConcurrentMap();
        this.vaults = Lists.newArrayList(new VaultMenu(6, "Town Vault"));
        this.bans = Sets.newConcurrentHashSet();
        this.spawnSetting = SpawnSetting.MEMBERS;
        this.townToggles = new EnumMap<>(Map.of(Setting.PVP, false, Setting.OPEN, false));
        this.trusted = Sets.newConcurrentHashSet();
        this.creationDate = new Date();
    }

    /**
     * Returns whether or not the specified UUID is a member or owner of this town.
     *
     * @param uuid UUID of the queried person
     * @return Whether they're currently an owner or member
     */
    public boolean isMemberOrOwner(final UUID uuid) {
        return this.owner.equals(uuid) || this.isMember(uuid);
    }

    /**
     * Adds a chunk to the town's claimed chunks.
     *
     * @param chunkRegion Chunk regions to claim.
     */
    public void claimChunks(final ChunkPosition chunkRegion) {
        this.playerPlots.put(chunkRegion, new Plot(UUID.randomUUID(), PlotType.DEFAULT, chunkRegion, ""));
        OxyTownsPlugin.get().getTownCache().getTownsMap().put(chunkRegion, this);
    }

    /**
     * Helper method to get all members with roles.
     *
     * @return members with roles
     */
    public Map<UUID, Role> getOwnerAndMembersWithRoles() {
        final Map<UUID, Role> members = new HashMap<>(this.members);
        members.put(this.owner, Role.MAYOR);

        return members;
    }

    /**
     * Unclaims a chunk at a given position.
     *
     * @param chunkRegion the chunk to remove
     * @param player      the player unclaiming the chunk
     */
    public void unclaimChunk(final ChunkPosition chunkRegion, final Player player) {
        this.outpostChunks.removeIf(it -> ChunkPosition.chunkPosition(it).equals(chunkRegion));

        final Plot plot = this.playerPlots.remove(chunkRegion);
        if (plot == null) return;

        OxyTownsPlugin.get().getTownCache().getTownsMap().remove(chunkRegion);

        if (this.spawnPosition != null && chunkRegion.contains(this.spawnPosition)) {
            this.spawnPosition = null;
        }
    }

    /**
     * Claim an outpost at the given location.
     *
     * @param location the location to claim
     */
    public void claimOutpost(final Location location) {
        this.outpostChunks.add(location);
        OxyTownsPlugin.get().getTownCache().getTownsMap().put(ChunkPosition.chunkPosition(location), this);
    }

    /**
     * Returns whether or not the specified chunk is claimed by this town.
     *
     * @param location Location that they're currently at.
     * @return Whether or not the area is claimed by any Towny.
     */
    public boolean hasClaimed(final Location location) {
        return this.hasClaimed(ChunkPosition.chunkPosition(location));
    }

    /**
     * Helper method to get all outpost and claimed chunks.
     *
     * @return set of all chunks
     */
    public ImmutableSet<ChunkPosition> getOutpostAndClaimedChunks() {
        final Set<ChunkPosition> chunks = Sets.newHashSet();

        chunks.addAll(this.playerPlots.keySet());
        chunks.addAll(this.outpostChunks.stream().map(ChunkPosition::chunkPosition).collect(Collectors.toSet()));

        return ImmutableSet.copyOf(chunks);
    }

    /**
     * Check if a town has claimed a chunk or not.
     *
     * @param chunkPosition the chunk to check
     * @return claimed or not
     */
    public boolean hasClaimed(final ChunkPosition chunkPosition) {
        if (playerPlots.containsKey(chunkPosition)) {
            return true;
        }

        for (Location location : this.outpostChunks) {
            if (!location.isWorldLoaded()) {
                continue; // TODO: ???
            }

            if (location.getBlockX() >> 4 == chunkPosition.getX() && location.getBlockZ() >> 4 == chunkPosition.getZ() && location.getWorld().getName().equals(chunkPosition.getWorld())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Get a plot by a location.
     *
     * @param location the location to check
     * @return if a plot is there or not
     */
    public Plot getPlot(final Location location) {
        return this.getPlot(ChunkPosition.chunkPosition(location));
    }

    public Plot getPlot(final ChunkPosition position) {
        return this.playerPlots.computeIfAbsent(position, pos -> outpostChunks.stream().filter(pos::contains).findFirst().map(it -> new Plot(UUID.randomUUID(), PlotType.DEFAULT, pos, "")).orElse(null));
    }

    /**
     * Helper method to get a set of all invited players to the town.
     *
     * @return set of invited players
     */
    public synchronized ImmutableSet<UUID> getInvitedPlayers() {
        return ImmutableSet.copyOf(this.invitedPlayers().asMap().keySet());
    }

    public Cache<UUID, UUID> invitedPlayers() {
        if (this.invitedPlayers == null) {
            this.invitedPlayers = CacheBuilder.newBuilder()
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .build();
        }
        return this.invitedPlayers;
    }

    /**
     * Helper method to handle a player joining a town.
     *
     * @param player the player that joins
     */
    public void handleJoin(final Player player) {
        Messages messages = Messages.get();

        this.members.put(player.getUniqueId(), Role.MEMBER);
        this.trusted.removeIf(it -> it.getUser().equals(player.getUniqueId()));
        this.invitedPlayers().invalidate(player.getUniqueId());

        messages.getTown().getBroadcastMemberJoined().send(this, Placeholder.unparsed("player", player.getName()));
    }

    /**
     * Helper method called when an admin puts a player in a town.
     *
     * @param uuid thw uuid to add to the town
     */
    public void handleAdminJoin(final UUID uuid) {
        this.members.put(uuid, Role.MEMBER);
        this.trusted.removeIf(it -> it.getUser().equals(uuid));
    }

    /**
     * Helper method to remove a player from a town by admin force.
     *
     * @param member the player to remove
     */
    public void handleAdminLeave(final OfflinePlayer member) {
        this.removePlayer(member);
    }

    /**
     * Helper method to get all people in a town.
     *
     * @return set of all people in town
     */
    public Set<UUID> getOwnerAndMembers() {
        final Set<UUID> members = Sets.newHashSet();

        members.addAll(this.members.keySet());
        members.add(owner);

        return members;
    }

    /**
     * Helper method to get all of the members names in the town.
     *
     * @return list of all valid member names
     */
    public List<String> getOwnerAndMemberNames() {
        return this.getOwnerAndMembers().stream()
            .map(Bukkit::getOfflinePlayer)
            .map(OfflinePlayer::getName)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    /**
     * Get a list of all the banned names in the town.
     *
     * @return list of banned names
     */
    public List<String> getBanNames() {
        return this.bans.stream()
            .map(entry -> Bukkit.getOfflinePlayer(entry.getUser()))
            .map(OfflinePlayer::getName)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    /**
     * Get a list of all UUIDs that are banned from the town.
     *
     * @return list of all banned uuids
     */
    public List<UUID> getBannedUUIDs() {
        return this.bans.stream().map(BanEntry::getUser).collect(Collectors.toList());
    }

    /**
     * Helper method to handle a player leaving a town.
     *
     * @param player the player leaving
     */
    public void handleLeave(final OfflinePlayer player) {
        Messages messages = Messages.get();

        removePlayer(player);
        messages.getTown().getBroadcastMemberLeave().send(this, Placeholder.unparsed("player", player.getName()));
    }

    /**
     * Removes a player from a town.
     *
     * @param player the player to remove
     */
    public void removePlayer(final OfflinePlayer player) {
        this.members.remove(player.getUniqueId());
        this.playerPlots.values().forEach(plot -> plot.getAssignedMembers().remove(player.getUniqueId()));
    }

    /**
     * Helper method to kick a player from a town.
     *
     * @param player the player to kick
     */
    public void handleKick(final OfflinePlayer player) {
        this.removePlayer(player);

        if (player.isOnline()) {
            this.vaults.forEach(vault -> {
                if (vault.getInventory().getViewers().contains(player.getPlayer())) {
                    vault.close(Objects.requireNonNull(player.getPlayer()));
                }
            });
        }
    }

    /**
     * Get the upgrade value from an upgrade.
     *
     * @param upgrade the upgrade to check
     * @return the tier value of the upgrade
     */
    public int getUpgradeValue(final Upgrade upgrade) {
        int currentTier = this.getUpgradeTier(upgrade);

        if (currentTier == -1) {
            return upgrade.getDefaultValue();
        }

        return (int) upgrade.getTiers().keySet().toArray()[currentTier]; // TODO: I don't believe this is reliable
    }

    /**
     * Get the upgrade tier from an upgrade.
     *
     * @param upgrade the upgrade to check
     * @return the upgrade tier or default
     */
    public int getUpgradeTier(final Upgrade upgrade) {
        return this.townUpgrades.getOrDefault(upgrade, -1); // not upgraded yet
    }

    /**
     * Upgrade a town by a specific amount.
     *
     * @param upgrade the upgrade to apply
     * @param amount  the amount to apply
     */
    public void upgradeTown(final Upgrade upgrade, final int amount) {
        this.townUpgrades.put(upgrade, getUpgradeTier(upgrade) + amount);
    }

    /**
     * Helper method to invite a player to a town.
     *
     * @param invitee the uuid to invite
     * @param inviter the uuid that invited the other player
     * @return if the player was invited or not
     */
    public boolean invitePlayer(final UUID invitee, final UUID inviter) {
        if (this.invitedPlayers().asMap().containsKey(invitee)) {
            return false;
        }

        this.invitedPlayers().put(invitee, inviter);
        return true;
    }

    /**
     * Helper method to remove an invite for a player.
     *
     * @param player the player to remove the invite for
     */
    public boolean removeInvite(final UUID player) {
        if (this.invitedPlayers().asMap().containsKey(player)) {
            this.invitedPlayers().asMap().remove(player);
            return true;
        }
        return false;
    }

    /**
     * Teleports an entity to the town spawn.
     *
     * @param entity the entity
     */
    public void teleport(final Entity entity) {
        entity.teleportAsync(this.spawnPosition);
    }

    /**
     * Helper method to get all claimed chunks from a town.
     *
     * @return set of chunks
     */
    public ImmutableSet<ChunkPosition> getClaimedChunks() {
        return ImmutableSet.copyOf(this.playerPlots.keySet());
    }

    /**
     * Toggles the given setting.
     *
     * @param setting the setting to toggle
     */
    public void toggleSetting(final Setting setting) {
        this.setToggle(setting, !getToggle(setting));
    }

    /**
     * Sets the value of a toggle for the town.
     *
     * @param setting the setting to toggle
     * @param value   the value of the toggle
     */
    public void setToggle(final Setting setting, final boolean value) {
        this.townToggles.put(setting, value);
    }

    /**
     * Obtains the value of a toggle in the town.
     *
     * @param setting the setting to check
     * @return the value of the toggle
     */
    public boolean getToggle(final Setting setting) {
        return this.townToggles.getOrDefault(setting, false);
    }

    /**
     * Get the permission of a role.
     *
     * @param role       the role to check
     * @param permission the permission or not
     * @return role has perm or not
     */
    public boolean getPermission(final Role role, final Permission permission) {
        return this.permissionSets.getOrDefault(role, EnumSet.noneOf(Permission.class)).contains(permission);
    }

    /**
     * Set permissions for a role.
     *
     * @param permission the permission to set
     * @param value      the value of the permission
     */
    public void setPermission(final Role role, final Permission permission, final boolean value) {
        final Set<Permission> permissions = this.permissionSets.computeIfAbsent(role, k -> EnumSet.noneOf(Permission.class));
        if (value) {
            permissions.add(permission);
        } else {
            permissions.remove(permission);
        }
    }

    /**
     * Check if a uuid from a user has permission.
     *
     * @param uuid       the uuid to check
     * @param permission the permission to check
     * @return if they are permission or thoughts
     */
    public boolean hasPermission(final UUID uuid, final Permission permission) {
        if (this.owner.equals(uuid)) {
            return true;
        }
        return this.getPermission(getRole(uuid), permission);
    }

    /**
     * Check if a player has a specific role.
     *
     * @param uuid the uuid of the role to check
     * @param role the role to check
     * @return if the player has the role or not
     */
    public boolean hasRole(final UUID uuid, final Role role) {
        return this.getRole(uuid) == role;
    }

    /**
     * Get the role from a uuid.
     *
     * @param uuid the uuid of the role
     * @return the role or lowest
     */
    public Role getRole(final UUID uuid) {
        if (this.owner.equals(uuid)) {
            return Role.MAYOR;
        }

        return this.members.getOrDefault(uuid, this.isTrusted(uuid) ? Role.TRUSTED : Role.OUTSIDER);
    }

    /**
     * Helper method to get the setting via the input permission.
     *
     * @param permission the input permission
     * @return reversed setting
     */
    public boolean getSetting(final Permission permission) {
        return this.getPermission(Role.OUTSIDER, permission);
    }

    /**
     * Transfers the Mayor of the town from one person to another.
     *
     * @param uuid the new mayor of the town
     */
    public void transferMayor(final UUID uuid) {
        this.members.remove(uuid);
        this.members.put(this.owner, Role.CO_MAYOR);
        this.owner = uuid;
    }

    /**
     * Helper method to demotes a player.
     *
     * @param uuid the uuid of the player to demote
     * @return the role of the user after being demoted
     */
    public Role demotePlayer(final UUID uuid) {
        final Role currentRole = this.getRole(uuid);
        final Role newRole = Role.getRoleByPriority(currentRole.getPriority() - 1);

        if (newRole == null || newRole == Role.OUTSIDER || newRole == Role.TRUSTED) {
            return null;
        }

        this.members.put(uuid, newRole); // Demotion;
        return newRole;
    }

    /**
     * Helper method to promote a player.
     *
     * @param uuid the uuid of the player to promote
     * @return the role of the user after being promoted
     */
    public Role promotePlayer(final UUID uuid) {
        final Role currentRole = this.getRole(uuid);
        final Role newRole = Role.getRoleByPriority(currentRole.getPriority() + 1);

        if (newRole == null || newRole == Role.MAYOR || newRole == Role.TRUSTED) {
            return null;
        }

        this.members.put(uuid, newRole); // Promotion;
        return newRole;
    }

    /**
     * Helper method to check if a player is banned in a town.
     *
     * @param player the player to check
     * @return if they have a ban entry or not
     */
    public Optional<BanEntry> checkBan(final Player player) {
        return this.checkBan(player.getUniqueId());
    }

    /**
     * Helper method to check if a uuid is banned from a town.
     *
     * @param uuid the uuid to check
     * @return if the uuid has a ban entry or not
     */
    public Optional<BanEntry> checkBan(final UUID uuid) {
        return this.bans.stream().filter(ban -> ban.getUser().equals(uuid)).findAny();
    }

    /**
     * Helper method to add a new ban to a town.
     *
     * @param player the player executing the ban
     * @param target the player being banned
     * @param reason the reason for being banned
     */
    public void addBan(final Player player, final OfflinePlayer target, final String reason) {
        this.bans.add(new BanEntry(player.getUniqueId(), target.getUniqueId(), new Date(), reason));
    }

    /**
     * Helper method to remove a ban from the list if it's present.
     *
     * @param target the uuid to check for
     */
    public void removeBan(final OfflinePlayer target) {
        this.checkBan(target.getUniqueId()).ifPresent(this.bans::remove);
    }

    /**
     * Helper method to set the next spawn setting in the toggles.
     */
    public void setNextSpawnSetting() {
        this.spawnSetting = this.spawnSetting.getNextSetting(this.spawnSetting);
    }

    public boolean isTrusted(final UUID uuid) {
        return this.trusted.stream().anyMatch(it -> it.getUser().equals(uuid));
    }

    public Set<TrustedEntry> getTrusted() {
        return this.trusted;
    }

    @Override
    public boolean isMember(final UUID uuid) {
        return this.members.containsKey(uuid);
    }

    public Location getHome() {
        return this.spawnPosition;
    }

    public void setHome(final Location location) {
        this.spawnPosition = location;
    }

    public boolean hasWorth(final Double toCheck) {
        return this.bankValue >= toCheck;
    }

    public Double getWorth() {
        return this.bankValue;
    }

    public void setWorth(final Double newWorth) {
        this.bankValue = newWorth;
    }

    public void addWorth(final Double toAdd) {
        this.bankValue += toAdd;
    }

    public void addWorth(final Integer toAdd) {
        this.bankValue += toAdd;
    }

    public void removeWorth(final Double toRemove) {
        this.bankValue -= toRemove;
    }

    public void removeWorth(final Integer toRemove) {
        this.bankValue -= toRemove;
    }

    @Override
    public @NotNull Iterable<? extends Audience> audiences() {
        return getOwnerAndMembers().stream().map(Bukkit::getPlayer).filter(Objects::nonNull).toList();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Town other)) return false;
        return this.townId.equals(other.getTownId());
    }

    @Override
    public TagResolver[] getPlaceholders() {
        return new TagResolver[]{
            Formatter.number("worth", bankValue),
            Placeholder.unparsed("name", name),
            Placeholder.unparsed("town", name),
            Placeholder.unparsed("spawn-setting", spawnSetting.getName()),
            Placeholder.unparsed("owner", Objects.requireNonNullElse(Bukkit.getOfflinePlayer(owner).getName(), "null")),
            Placeholder.parsed("owner-online", Optional.ofNullable(Bukkit.getPlayer(owner)).map(it -> "<green>" + it.getName()).orElse("<owner>")),
            Formatter.number("members", members.size()),
            Formatter.number("members-and-owner", members.size() + 1),
            Formatter.number("claims", playerPlots.keySet().size()),
            Formatter.number("maxclaims", getUpgradeValue(Upgrade.CLAIMS)),
            Formatter.number("outposts", outpostChunks.size()),
            Formatter.number("maxoutposts", getUpgradeValue(Upgrade.OUTPOSTS)),
            Formatter.date("age", creationDate.toInstant().atZone(ZoneId.systemDefault())),
            Formatter.number("upkeep", Math.ceil(getOutpostAndClaimedChunks().size() * TaxSchedule.getTownTaxValue())),
            Placeholder.parsed("member-names", members.keySet().stream().map(Bukkit::getOfflinePlayer).map(it -> (it.isOnline() ? "<green>" : "<gray>") + it.getName()).collect(Collectors.joining("<gray>, ")))
        };
    }
}
