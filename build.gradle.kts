import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.minecrell.pluginyml.bukkit.BukkitPluginDescription
import xyz.jpenilla.runpaper.task.RunServer

plugins {
    java
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.freefair.lombok") version "8.2.2"

    // Paper environment
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
    id("xyz.jpenilla.run-paper") version "2.2.0"

    // Version checking
    id("com.github.ben-manes.versions") version "0.47.0"
}

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://maven.enginehub.org/repo/")
    maven("https://jitpack.io")
    maven("https://repo.dmulloy2.net/nexus/repository/public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
    compileOnly("me.clip:placeholderapi:2.11.3")
    compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.0.9")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7.1")
    compileOnly("xyz.jpenilla:squaremap-api:1.1.15")

    implementation("org.bstats:bstats-bukkit:3.0.2")
    implementation("org.spongepowered:configurate-yaml:4.1.2")
    implementation("cloud.commandframework:cloud-paper:1.8.3")
    implementation("cloud.commandframework:cloud-annotations:1.8.3")
    implementation("dev.triumphteam:triumph-gui:3.1.5") {
        isTransitive = false // Paper provides Adventure
    }
    implementation("fr.minuskube.inv:smart-invs:1.2.7") {
        isTransitive = false // Don't want spigot 1.8.8
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks {
    build {
        dependsOn(shadowJar)
    }

    withType<JavaCompile> {
        options.release.set(17)
        options.encoding = Charsets.UTF_8.name()
        options.compilerArgs = listOf("-parameters")
    }

    withType<ShadowJar> {
        fun reloc(vararg dependencies: String) {
            dependencies.forEach {
                relocate(it, "${project.group}.${rootProject.name}.libs.$it")
            }
        }

        reloc(
            "org.bstats",
            "dev.triumphteam.gui",
            "org.spongepowered",
            "cloud.commandframework",
            "io.leangen.geantyref",
            "fr.minuskube.inv"
        )

        archiveFileName.set("OxyTowns-${project.version}.jar")
    }

    withType<RunServer> {
        minecraftVersion("1.20.2")

        downloadPlugins {
            github("MilkBowl", "Vault", "1.7.3", "Vault.jar")
            hangar("squaremap", "1.2.1")
            url("https://github.com/EssentialsX/Essentials/releases/download/2.20.1/EssentialsX-2.20.1.jar")
            url("https://download.luckperms.net/1534/bukkit/loader/LuckPerms-Bukkit-5.4.121.jar")
            url("https://ci.enginehub.org/repository/download/bt11/22585:id/worldguard-bukkit-7.0.10-SNAPSHOT-dist.jar?branch=version/7.0.x&guest=1")
            url("https://ci.enginehub.org/repository/download/bt10/22825:id/worldedit-bukkit-7.2.18-SNAPSHOT-dist.jar?branch=version/7.2.x&guest=1")
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("plugin") {
            from(components["java"])
        }
    }
}

configure<BukkitPluginDescription> {
    name = "OxyTowns"
    description = "A land claiming plugin."

    apiVersion = "1.13"
    version = "${project.version}"

    main = "com.oxywire.oxytowns.OxyTownsPlugin"
    authors = listOf("SirKillian", "Glare", "SirSalad")
    depend = listOf("Vault")
    softDepend = listOf("WorldGuard", "PlaceholderAPI", "squaremap")
}
