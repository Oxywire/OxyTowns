package com.oxywire.oxytowns.storage;

import com.oxywire.oxytowns.OxyTownsPlugin;
import com.oxywire.oxytowns.entities.impl.town.Town;
import com.oxywire.oxytowns.utils.Json;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class TownStorageManager {

    private final File dataFile;

    public TownStorageManager(final OxyTownsPlugin plugin) {
        this.dataFile = new File(plugin.getDataFolder(), "towns");
        if (!this.dataFile.exists()) {
            this.dataFile.mkdirs();
        }
    }

    /**
     * Handles getting all the towns from the file system
     *
     * @return the list of towns from the file system
     */
    public List<Town> getAll() {
        final List<Town> towns = new ArrayList<>();
        for (final File file : this.dataFile.listFiles()) {
            try {
                final Town town = Json.GSON.fromJson(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8), Town.class);
                towns.add(town);
            } catch (final FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return towns;
    }

    /**
     * Handles saving a town to the file system
     *
     * @param entity the town to save to the file system
     */
    public void save(final Town entity) {
        CompletableFuture.runAsync(() -> this.unload(entity));
    }

    /**
     * Called when the server is shutting down. Can't schedule new task
     *
     * @param entity the town to save
     */
    public void unload(final Town entity) {
        final File file = new File(this.dataFile, entity.getTownId().toString() + ".json");
        try {
            Files.writeString(file.toPath(), Json.GSON.toJson(entity, Town.class));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Handles deleting a town from the file system
     *
     * @param entity the town to save from the file system
     */
    public void delete(final Town entity) {
        CompletableFuture.runAsync(() -> new File(this.dataFile, entity.getTownId().toString() + ".json").delete());
    }
}
