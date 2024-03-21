package com.oxywire.oxytowns.entities.impl.plot;

import com.oxywire.oxytowns.config.Messages;
import com.oxywire.oxytowns.config.messaging.Message;
import com.oxywire.oxytowns.entities.model.Named;
import com.oxywire.oxytowns.entities.types.PlotType;
import com.oxywire.oxytowns.entities.model.CreatedDateHolder;
import com.oxywire.oxytowns.entities.types.Role;
import com.oxywire.oxytowns.entities.types.perms.Permission;
import com.oxywire.oxytowns.utils.ChunkPosition;
import com.oxywire.oxytowns.utils.Placeholdered;
import lombok.Data;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Bukkit;

import java.time.ZoneId;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Data
public final class Plot implements CreatedDateHolder, Placeholdered, Named {

    private Set<UUID> assignedMembers;
    private Map<Role, Set<Permission>> permissions;

    private UUID uuid;
    private PlotType type;
    private ChunkPosition chunkPosition;
    private String name;
    private Date creationDate;

    public Plot(final UUID uuid, final PlotType type, final ChunkPosition chunkPosition, final String name) {
        this.uuid = uuid;
        this.type = type;
        this.chunkPosition = chunkPosition;
        this.name = name;
        this.assignedMembers = new HashSet<>();
        this.permissions = new HashMap<>();
        this.creationDate = new Date();
    }

    /**
     * Helper method to add a member to the plot.
     *
     * @param uuid the uuid to add
     */
    public void addMember(final UUID uuid) {
        this.assignedMembers.add(uuid);
    }

    /**
     * Helper method to get a list of the member names for the plot.
     *
     * @return the members of the plot via their name
     */
    public List<String> getMemberNames() {
        return this.assignedMembers.stream().map(member -> Bukkit.getOfflinePlayer(member).getName()).toList();
    }

    public boolean getPermission(final Role role, final Permission permission) {
        return this.permissions.getOrDefault(role, EnumSet.noneOf(Permission.class)).contains(permission);
    }

    public void setPermission(final Role role, final Permission permission, final boolean value) {
        final Set<Permission> permissions = this.permissions.computeIfAbsent(role, k -> EnumSet.noneOf(Permission.class));
        if (value) {
            permissions.add(permission);
        } else {
            permissions.remove(permission);
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }

        final Plot plot = (Plot) o;
        return Objects.equals(this.chunkPosition, plot.chunkPosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.chunkPosition);
    }

    @Override
    public TagResolver[] getPlaceholders() {
        return new TagResolver[]{
            Placeholder.unparsed("name", name),
            Placeholder.unparsed("type", Message.formatEnum(type)),
            Formatter.date("age", creationDate.toInstant().atZone(ZoneId.systemDefault())),
            Placeholder.parsed("members", String.join(Messages.get().getTown().getPlot().getHereDelimiter(), getMemberNames()))
        };
    }
}
