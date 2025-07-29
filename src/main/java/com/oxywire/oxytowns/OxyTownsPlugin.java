package com.oxywire.oxytowns;

import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.services.types.ConsumerService;
import com.oxywire.oxytowns.addons.BStats;
import com.oxywire.oxytowns.addons.OxyTownsExpansion;
import com.oxywire.oxytowns.addons.SquareMapAddon;
import com.oxywire.oxytowns.api.OxyTownsApi;
import com.oxywire.oxytowns.cache.TownCache;
import com.oxywire.oxytowns.command.CommandManager;
import com.oxywire.oxytowns.command.annotation.AcceptConfirmation;
import com.oxywire.oxytowns.command.annotation.CreateConfirmation;
import com.oxywire.oxytowns.command.annotation.MustBeInTown;
import com.oxywire.oxytowns.command.annotation.SendersTown;
import com.oxywire.oxytowns.command.argument.ArgumentType;
import com.oxywire.oxytowns.command.commands.plot.PlotCommand;
import com.oxywire.oxytowns.command.commands.town.TownCommand;
import com.oxywire.oxytowns.command.confirmation.CommandConfirmationManager;
import com.oxywire.oxytowns.config.Config;
import com.oxywire.oxytowns.config.Menus;
import com.oxywire.oxytowns.config.Messages;
import com.oxywire.oxytowns.config.UpkeepTimes;
import com.oxywire.oxytowns.config.internal.ConfigManager;
import com.oxywire.oxytowns.entities.impl.town.Town;
import com.oxywire.oxytowns.listeners.NewEventsHandler;
import com.oxywire.oxytowns.menu.Menu;
import com.oxywire.oxytowns.runnable.MobsRunnable;
import com.oxywire.oxytowns.runnable.TaxSchedule;
import lombok.Getter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.Optional;

@Getter
public class OxyTownsPlugin extends JavaPlugin {

    private static OxyTownsPlugin instance;
    public static ConfigManager configManager;

    private TownCache townCache;
    private Economy economy;
    private OxyTownsApi oxyTownsApi;
    private TaxSchedule taxSchedule;

    @Override
    public void onEnable() {
        final long start = System.currentTimeMillis();

        instance = this;
        Menu.INVENTORY_MANAGER.init();

        try {
            configManager = new ConfigManager(getDataFolder().toPath());
            configManager.get(Messages.class);
            configManager.get(Config.class);
            configManager.get(Menus.class);
            configManager.get(UpkeepTimes.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.townCache = new TownCache(this);

        final RegisteredServiceProvider<Economy> rsp = this.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            this.getLogger().info("This plugin requires an economy implementation alongside vault.");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }
        this.economy = rsp.getProvider();


        this.oxyTownsApi = new OxyTownsApi();
        this.getServer().getServicesManager().register(OxyTownsApi.class, this.oxyTownsApi, this, ServicePriority.High);

        final CommandManager commandManager = CommandManager.install(this)
            .withArgumentType(
                Town.class,
                new ArgumentType<>(
                    input -> this.townCache.getTownByName(input).orElse(null),
                    input -> this.townCache.getTowns().stream().map(Town::getName).toList()
                )
            )
            .withPostProcessor(context -> {
                if (context.getCommand().getCommandMeta().getOrDefault(CommandMeta.Key.of(Boolean.class, "oxytowns:must_be_in_town"), false) && context.getCommandContext().getSender() instanceof Player player) {
                    Optional<Town> town = this.townCache.getTownByPlayer(player);
                    if (town.isEmpty()) {
                        Messages.get().getTown().getNoTown().send(player);
                        ConsumerService.interrupt();
                    }
                }
            })
            .withPostProcessor(new CommandConfirmationManager())
            .withBuilderModifier(
                MustBeInTown.class,
                (annotation, builder) -> builder.meta(CommandMeta.Key.of(Boolean.class, "oxytowns:must_be_in_town"), true)
            )
            .withBuilderModifier(
                CreateConfirmation.class,
                (annotation, builder) -> builder.meta(CommandMeta.Key.of(String.class, "oxytowns:prompt_confirmation"), annotation.value())
            )
            .withBuilderModifier(
                AcceptConfirmation.class,
                (annotation, builder) -> builder.meta(CommandMeta.Key.of(String.class, "oxytowns:accept_confirmation"), annotation.value())
            )
            .withInjector(
                Town.class,
                (context, annotations) -> annotations.annotation(SendersTown.class) == null
                    ? null
                    : context.getSender() instanceof Player player
                    ? this.townCache.getTownByPlayer(player).orElse(null)
                    : null
            )
            .withCommands(
                new PlotCommand(this.townCache),
                new TownCommand(this.townCache)
            )
            .withCommands("com.oxywire.oxytowns.command.commands.admin", this, this.townCache)
            .withCommands("com.oxywire.oxytowns.command.commands.plot.sub", this, this.townCache)
            .withCommands("com.oxywire.oxytowns.command.commands.town.sub", this, this.townCache);

        if (Config.get().getTownChat().isEnabled()) {
            commandManager.command(
                commandManager.commandBuilder("townchat", "tc")
                    .senderType(Player.class)
                    .meta(CommandMeta.Key.of(Boolean.class, "oxytowns:must_be_in_town"), true)
                    .argument(StringArgument.greedy("message"))
                    .handler(context -> {
                        Player sender = (Player) context.getSender();
                        Town town = this.townCache.getTownByPlayer(sender).orElse(null);
                        if (town == null) {
                            Messages.get().getTown().getNoTown().send(sender);
                            return;
                        }
                        Config.get().getTownChat().getFormat().send(town, Placeholder.unparsed("sender", sender.getName()), Placeholder.unparsed("message", context.get("message")));
                    })
            );
        }

        this.getServer().getPluginManager().registerEvents(new NewEventsHandler(), this);
        this.registerAddons();

        this.taxSchedule = new TaxSchedule(this);
        new MobsRunnable().runTaskTimer(this, 0, 20 * 8);
    }

    private void registerAddons() {
        new BStats(this);
        if (this.getServer().getPluginManager().isPluginEnabled("squaremap")) new SquareMapAddon();
        if (this.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) new OxyTownsExpansion();
    }

    @Override
    public void onDisable() {
        this.townCache.unloadTowns();
    }

    public static OxyTownsPlugin get() {
        return instance;
    }

}
