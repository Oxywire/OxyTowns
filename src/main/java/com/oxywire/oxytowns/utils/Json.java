package com.oxywire.oxytowns.utils;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializer;
import com.oxywire.oxytowns.menu.town.VaultMenu;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

@UtilityClass
public class Json {

    public final Gson GSON = new GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapter(
            Location.class,
            (JsonSerializer<Location>) (src, typeOfSrc, context) -> {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("world", src.getWorld().getName());
                jsonObject.addProperty("x", src.getX());
                jsonObject.addProperty("y", src.getY());
                jsonObject.addProperty("z", src.getZ());
                jsonObject.addProperty("yaw", src.getYaw());
                jsonObject.addProperty("pitch", src.getPitch());
                return jsonObject;
            }
        )
        .registerTypeAdapter(
            Location.class,
            (JsonDeserializer<Location>) (json, typeOfT, context) -> {
                JsonObject jsonObject = json.getAsJsonObject();
                return new Location(
                    Bukkit.getWorld(jsonObject.get("world").getAsString()),
                    jsonObject.get("x").getAsDouble(),
                    jsonObject.get("y").getAsDouble(),
                    jsonObject.get("z").getAsDouble(),
                    jsonObject.get("yaw").getAsFloat(),
                    jsonObject.get("pitch").getAsFloat()
                );

            }
        )
        .registerTypeAdapter(
            ChunkPosition.class,
            (JsonSerializer<ChunkPosition>) (src, typeOfSrc, context) -> {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("world", src.getWorld());
                jsonObject.addProperty("x", src.getX());
                jsonObject.addProperty("z", src.getZ());
                return jsonObject;
            }
        )
        .registerTypeAdapter(
            ChunkPosition.class,
            (JsonDeserializer<Object>) (json, typeOfT, context) -> {
                JsonObject jsonObject = json.getAsJsonObject();
                return new ChunkPosition(
                    jsonObject.get("x").getAsInt(),
                    jsonObject.get("z").getAsInt(),
                    jsonObject.get("world").getAsString()
                );
            }
        )
        .registerTypeAdapter(
            VaultMenu.class,
            (JsonDeserializer<VaultMenu>) (json, typeOfT, context) -> {
                JsonObject object = json.getAsJsonObject();
                Preconditions.checkArgument(object.has("contents"));
                final VaultMenu menu = new VaultMenu(6, "Town Vault");
                final ItemStack[] items = VaultMenu.deserializeItemStacks(object.get("contents").getAsString());
                for (int i = 0; i < items.length; i++) {
                    menu.getInventory().setItem(i, items[i]);
                }
                return menu;
            }
        )
        .registerTypeAdapter(
            VaultMenu.class,
            (JsonSerializer<VaultMenu>) (src, typeOfSrc, context) -> {
                JsonObject object = new JsonObject();
                object.addProperty("contents", VaultMenu.serializeItemStacks(src.getInventory().getContents()));

                return object;
            }
        )
        .enableComplexMapKeySerialization()
        .create();
}
