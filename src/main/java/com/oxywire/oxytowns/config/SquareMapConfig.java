package com.oxywire.oxytowns.config;

import com.oxywire.oxytowns.OxyTownsPlugin;
import lombok.Getter;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;
import xyz.jpenilla.squaremap.api.marker.MarkerOptions;

import java.net.URI;

@Getter
@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
@ConfigSerializable
public class SquareMapConfig {

    @Setting
    private boolean stroke = true;
    @Setting
    private int strokeColor = 0x000000;
    @Setting
    private int strokeWeight = 3;
    @Setting
    private double strokeOpacity = 1.0;
    @Setting
    private boolean fill = true;
    @Setting
    private int fillColor = 0xffc800;
    @Setting
    private double fillOpacity = 0.5;
    @Setting
    private MarkerOptions.FillRule fillRule = MarkerOptions.FillRule.EVENODD;
    @Setting
    private String clickTooltip = """
            <bold><town></bold>
            Mayor: <bold><mayor></bold>
            Established on: <bold><age></bold>
            Land Mass: <bold><claims> claims and <outposts> outposts</bold>

            Residents: <bold><residents></bold>
            """;
    @Setting
    private String hoverTooltip = "<town>";
    @Setting
    private URI townSpawnIcon = URI.create("https://i.imgur.com/foGD6lb.png");
    @Setting
    private int townSpawnIconSize = 100;

    public static SquareMapConfig get() {
        return OxyTownsPlugin.configManager.get(SquareMapConfig.class);
    }
}
