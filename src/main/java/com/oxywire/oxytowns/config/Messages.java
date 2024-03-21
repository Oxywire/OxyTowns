package com.oxywire.oxytowns.config;

import com.oxywire.oxytowns.OxyTownsPlugin;
import com.oxywire.oxytowns.config.messaging.Message;
import com.oxywire.oxytowns.config.messaging.Message.Sound;
import com.oxywire.oxytowns.config.messaging.Message.Title;
import lombok.Getter;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@Getter
@ConfigSerializable
@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
public final class Messages {

    @Setting
    private Message actionCancelled = new Message().setMessage("<gold><b>Info</b> <dark_gray>» <gray>You have cancelled this action.");

    @Setting
    private Message invalidName = new Message().setMessage(
        "<red><b>Error</b> <dark_gray>» <red>Your town or plot name may only contain A-Z and 0-9 characters.");

    @Setting
    private Message nowEnteringWilderness = new Message().setActionBar("<gold><bold>Now entering: <red>Wilderness");

    @Setting
    private Message nowEnteringTown = new Message().setActionBar("<gold><bold>Now entering: <yellow><town>");

    @Setting
    private Message bypassOn = new Message()
        .setMessage("<gold><b>Info</b> <dark_gray>» <gray>You have toggled <yellow>Bypass <green>on<gray>.");

    @Setting
    private Message bypassOff = new Message()
        .setMessage("<gold><b>Info</b> <dark_gray>» <gray>You have toggled <yellow>Bypass <red>off<gray>.");

    @Setting
    private Town town = new Town();

    @Setting
    private Player player = new Player();

    @Setting
    private Admin admin = new Admin();

    @Setting
    private Tax tax = new Tax();

    @Setting
    private CommandFeedback commandFeedback = new CommandFeedback();

    public static Messages get() {
        return OxyTownsPlugin.configManager.get(Messages.class);
    }

    @Getter
    @ConfigSerializable
    public static final class Town {

        @Setting
        private Message noTown = new Message().setMessage("<red><b>Error</b> <dark_gray>» <red>You're not in a town.");

        @Setting
        private Message alreadyMember = new Message().setMessage("<red><b>Error</b> <dark_gray>» <red>You're already in a town.");

        @Setting
        private Message notOwner = new Message().setMessage("<red><b>Error</b> <dark_gray>» <red>You must be the town mayor to do this.");

        @Setting
        private Message notFound = new Message().setMessage(
            "<red><b>Error</b> <dark_gray>» <red>That player is invalid. Have they joined this server before?");

        @Setting
        private Message invalidName = new Message().setMessage("<red><b>Error</b> <dark_gray>» <red>Please put a valid MC username.");

        @Setting
        private Message creationNoPermission = new Message().setMessage(
            "<red><b>Error</b> <dark_gray>» You do not have permission to create a town.");

        @Setting
        private Message creationNameAlreadyExists = new Message().setMessage(
            "<red><b>Error</b> <dark_gray>» <yellow><name> <red>already exists. Please pick another name.");

        @Setting
        private Message creationTownCreated = new Message()
            .setMessage("<gold><b>Info</b> <dark_gray>» <gray>Town <yellow><name> <gray>has been created by <yellow><player><gray>.")
            .setSound(new Sound()
                .setKey("entity.ui.toast.challenge_complete")
                .setVolume(1)
                .setPitch(1));

        @Setting
        private Message successTitle = new Message()
            .setTitle(new Title()
                .setTitle("<gold>Success!")
                .setSubTitle("<white>You established a town."));

        @Setting
        private Message townyMapHeader = new Message().setMessage(
            "<yellow><st>                                <gold></st><bold> Town Map <yellow><st>                                ");

        @Setting
        private Message townyMapFooter = new Message().setMessage(
            "<yellow><st>                                                                                ");

        @Setting
        private Message townDisbandConfirm = new Message().setMessage("""
            <gray>
            <gray>              <gold><st>               <gold></st><bold> Town Disband <gold><st>               <reset>
            <gray>
            <red>         You are about to disband your town. Are you sure?
            <gray>
            <gray><italic>                          Please make a choice.
            <gray>                        <click:run_command:/t cancel><red><bold>[Cancel]</click>     <click:run_command:/t disband confirm><green><bold>[Confirm]</click>
            <gray>                 <gold><st>                                             <reset>
            """);

        @Setting
        private Message townDisbandSuccess = new Message().setMessage(
            "<gold><b>Info</b> <dark_gray>» <red>The town of <yellow><town><red> fell into ruins.");

        @Setting
        private Message errorPlayerAlreadyInTown = new Message().setMessage(
            "<red><b>Error</b> <dark_gray>» <yellow><player> <red>is already in a town.");

        @Setting
        private Message errorPlayerAlreadyInvited = new Message().setMessage(
            "<red><b>Error</b> <dark_gray>» <yellow><player> <red>is already invited.");

        @Setting
        private Message broadcastPlayerInvited = new Message().setMessage(
            "<gold><b>Info</b> <dark_gray>» <yellow><player> <gray>has been invited to the town.");

        @Setting
        private Message errorMemberUpgradeNeeded = new Message().setMessage(
            "<red><b>Warning</b> <dark_gray>» <red>Maximum amount of residents reached. Buy town upgrades if you need more.");

        @Setting
        private Message errorNoPermissionInvite = new Message().setMessage(
            "<red><b>Error</b> <dark_gray>» <red>You are not allowed to invite members.");

        @Setting
        private Message broadcastMemberJoined = new Message().setMessage("<gold><b>Info</b> <dark_gray>» <yellow><player> <gray>joined the town.");

        @Setting
        private Message broadcastMemberLeave = new Message().setMessage("<gold><b>Info</b> <dark_gray>» <yellow><player> <gray>left the town.");

        @Setting
        private Message notMember = new Message().setMessage("<red><b>Error</b> <dark_gray>» <yellow><player> <red>is not a member of your town.");

        @Setting
        private Message cantKickMayor = new Message().setMessage("<red><b>Error</b> <dark_gray>» <red>You can't kick the town mayor.");

        @Setting
        private Message kickSuccess = new Message().setMessage(
            "<red><b>Warning</b> <dark_gray>» <yellow><player> <red>has been kicked from the town.");

        @Setting
        private Message noPermissionKick = new Message().setMessage("<red><b>Error</b> <dark_gray>» <red>You are not allowed to kick someone.");

        @Setting
        private Message renameBroadcast = new Message().setMessage(
            "<gold><b>Info</b> <dark_gray>» <gray>Town has been renamed to <yellow><new-name><gray>.");

        @Setting
        private Message renameNoPermission = new Message().setMessage("<red><b>Error</b> <dark_gray>» <red>You are not allowed to rename your town.");

        @Setting
        private Message settingsNoPermission = new Message().setMessage("<red><b>Error</b> <dark_gray>» <red>You are not allowed to manage town settings.");

        @Setting
        private Message chunkNotClaimed = new Message().setMessage("<red><b>Error</b> <dark_gray>» <red>You've not claimed this chunk.");

        @Setting
        private Message spawnSet = new Message().setMessage("<gold><b>Info</b> <dark_gray>» <green>Successfully set town spawn.");

        @Setting
        private Message noPermissionSpawnSet = new Message().setMessage(
            "<red><b>Error</b> <dark_gray>» <red>You are not allowed to set your town spawn.");

        @Setting
        private Message noPermissionClaim = new Message().setMessage("<red><b>Error</b> <dark_gray>» <red>You are not allowed to claim chunks.");

        @Setting
        private Message notValidClaim = new Message().setMessage("<red><b>Error</b> <dark_gray>» <red>This is an invalid town.");

        @Setting
        private Message viewTownChunk = new Message().setMessage("""
            <gold><st>               </st><gold><bold> <name> <gold><st>               <reset>
            <gray> <gray><italic>Established on <age>             <gray>
            <white> Population: <yellow><members> Players
            <white> Land Mass: <yellow><claims> Claims <white>and <yellow><outposts> Outposts<gray>.
            <gray>
            <white> The town has <yellow><worth><white> in the bank and pays <yellow><upkeep> <white>in taxes.
            <gray>
            """);

        @Setting
        private Message noPermissionTeleport = new Message().setMessage(
            "<red><b>Error</b> <dark_gray>» <red>You currently do not have permission to teleport to this town!");

        @Setting
        private Message noSpawnSet = new Message().setMessage("<red><b>Error</b> <dark_gray>» <red>No home has been set.");

        @Setting
        private Message broadcastPromotion = new Message().setMessage(
            "<gold><b>Info</b> <dark_gray>» <yellow><player> <gray>has been demoted to <yellow><role><gray>.");

        @Setting
        private Message broadcastDemotion = new Message().setMessage(
            "<gold><b>Info</b> <dark_gray>» <yellow><player> <gray>has been promoted to <yellow><role><gray>.");

        @Setting
        private Message notOnline = new Message().setMessage("<red><b>Error</b> <dark_gray>» <red>Player is not online.");

        @Setting
        private Message trustAdded = new Message().setMessage("<gold><b>Info</b> <dark_gray>» <yellow><player> <gray>has been added to the town trust list.");

        @Setting
        private Message trustRemoved = new Message().setMessage("<gold><b>Info</b> <dark_gray>» <yellow><player> <gray>has been removed from the town trust list.");

        @Setting
        private Message cantTrustThem = new Message().setMessage("<red><b>Error</b> <dark_gray>» <red>You can't trust members of your town.");

        @Setting
        private Message noPermissionTrust = new Message().setMessage("<red><b>Error</b> <dark_gray>» <red>You are not allowed to manage town trust.");

        @Setting
        private Message alreadyMayor = new Message().setMessage("<red><b>Error</b> <dark_gray>» <red>That player is already the town mayor.");

        @Setting
        private Message townTransferMayorConfirm = new Message().setMessage("""
            gray>
            <gray>              <gold><st>               </st><gold><bold> Transfer Mayorship <gold><st>               <reset>
            <gray>
            <yellow>               <gray>Are you sure you want to transfer mayorship to <yellow><player><gray>.
            <gray>
            <gray><italic>                          Please make a choice.
            <gray>                        <click:run_command:/t cancel><red><bold>[Cancel]</click>     <click:run_command:/t setmayor <player> confirm><green><bold>[Accept]</click>
            <gray>                 <gold><st>                                             <reset>
            """);

        @Setting
        private Message townTransferMayorSuccess = new Message().setMessage("<gold><b>Info</b> <dark_gray>» <yellow><player> <gray>is now the mayor of the town.");

        @Setting
        private Plot plot = new Plot();

        @Setting
        private Claim claim = new Claim();

        @Setting
        private Unclaim unclaim = new Unclaim();

        @Setting
        private Bank bank = new Bank();

        @Setting
        private Outpost outpost = new Outpost();

        @Setting
        private Upgrade upgrade = new Upgrade();

        @Setting
        private Vault vault = new Vault();

        @Setting
        private Ban ban = new Ban();

        @Setting
        private Unban unban = new Unban();

        @Getter
        @ConfigSerializable
        public static final class Unban {

            @Setting
            private Message notBanned = new Message().setMessage("<red><b>Error</b> <dark_gray>» <red>That player is not banned.");

            @Setting
            private Message noPermission = new Message().setMessage(
                "<red><b>Error</b> <dark_gray>» <red>You don't have permission to unban users from your town.");

            @Setting
            private Message broadcastUnban = new Message().setMessage(
                "<gold><b>Info</b> <dark_gray>» <yellow><player> <gray>is now unbanned from your town.");

        }

        @Getter
        @ConfigSerializable
        public static class Ban {

            @Setting
            private Message alreadyBanned = new Message().setMessage(
                "<red><b>Error</b> <dark_gray>» <red>That user is already banned from your town.");

            @Setting
            private Message noPermission = new Message().setMessage(
                "<red><b>Error</b> <dark_gray>» <red>You don't have permission to ban users from your town.");

            @Setting
            private Message targetInTown = new Message().setMessage("<red><b>Error</b> <dark_gray>» <red>You can't ban town members.");

            @Setting
            private Message broadcastBan = new Message().setMessage(
                "<red><b>Warning</b> <dark_gray>» <yellow><player> <gray>is now banned from your town.");

            @Setting
            private Message invalidReason = new Message().setMessage(
                "<red><b>Error</b> <dark_gray>» <red>Please keep your ban reason to under 20 characters.");

        }

        @Getter
        @ConfigSerializable
        public static final class Vault {

            @Setting
            private Message noOpenPermission = new Message().setMessage(
                "<red><b>Error</b> <dark_gray>» <red>You don't have permission to open the Town Vault.");

            @Setting
            private Message invalidNumber = new Message().setMessage(
                "<red><b>Error</b> <dark_gray>» <gray>That is not a valid Vault number. Please try again!");

        }

        @Getter
        @ConfigSerializable
        public static final class Upgrade {

            @Setting
            private Message unlockedAlready = new Message().setMessage("<gold><b>Info</b> <dark_gray>» <green>You've already unlocked this.");

            @Setting
            private Message cannotAfford = new Message().setMessage(
                "<red><b>Error</b> <dark_gray>» <red>Your town can't afford this unlock. <gray>(<price>)");

            @Setting
            private Message unlockFirst = new Message().setMessage("<red><b>Error</b> <dark_gray>» <red>Unlock <yellow><tier> <red>first.");

            @Setting
            private Message upgraded = new Message().setMessage(
                "<gold><b>Info</b> <dark_gray>» <green>Successfully upgraded <yellow><upgrade> <green>to <yellow><tier> <green>for <yellow><price><green>.");

            @Setting
            private Message noPermission = new Message()
                .setMessage("<red><b>Error</b> <dark_gray>» <red>You don't have permission to upgrade your town.");

        }

        @Getter
        @ConfigSerializable
        public static final class Outpost {

            @Setting
            private Message claimSuccessful = new Message().setMessage(
                "<gold><b>Info</b> <dark_gray>» <yellow><town> <gray>successfully claimed this outpost.");

            @Setting
            private Message upgradeRequired = new Message().setMessage(
                "<red><b>Warning</b> <dark_gray>» <red>Maximum amount of outposts reached. Buy town upgrades if you need more.");

            @Setting
            private Message noPermissionClaim = new Message().setMessage(
                "<red><b>Error</b> <dark_gray>» <red>You are not allowed to claim an outpost.");

        }

        @Getter
        @ConfigSerializable
        public static final class Bank {

            @Setting
            private Message errorCannotAfford = new Message().setMessage("<red><b>Error</b> <dark_gray>» <red>Your town can't afford this.");

            @Setting
            private Message errorWithdrawNotAllowed = new Message().setMessage(
                "<red><b>Error</b> <dark_gray>» <red>You are not allowed to withdraw money from your town bank.");

            @Setting
            private Message depositSuccessful = new Message().setMessage(
                "<gold><b>Info</b> <dark_gray>» <yellow><player> <gray>has deposited <yellow><amount><gray> into the town bank.");

            @Setting
            private Message withdrawSuccessful = new Message().setMessage(
                "<gold><b>Info</b> <dark_gray>» <yellow><player> <gray>has withdrawn <yellow><amount> <gray>from the town bank.");

        }

        @Getter
        @ConfigSerializable
        public static final class Claim {

            @Setting
            private Message claimSuccess = new Message().setMessage(
                "<gold><b>Info</b> <dark_gray>» <gray>You successfully claimed <yellow><claims> chunk(s) <gray>for <yellow>$<price><gray>.");

            @Setting
            private Message chunkAlreadyClaimed = new Message().setMessage("<red><b>Error</b> <dark_gray>» <red>This chunk is already claimed.");

            @Setting
            private Message townNear = new Message().setMessage(
                "<red><b>Error</b> <dark_gray>» <red>There's already another town within 5 chunks. Move a bit further away.");

            @Setting
            private Message errorConnectedClaims = new Message().setMessage("<red><b>Error</b> <dark_gray>» <red>Your claims must be connected.");

            @Setting
            private Message errorProtectedClaim = new Message().setMessage("<red><b>Error</b> <dark_gray>» <red>You can't claim this area.");

            @Setting
            private Message errorUpgradeRequired = new Message().setMessage(
                "<red><b>Error</b> <dark_gray>» <red>Maximum amount of claimed chunks reached. Buy town upgrades if you need more.");

            @Setting
            private Message errorBlacklistedWorld = new Message().setMessage("<red><b>Error</b> <dark_gray>» <red>You can't claim in this world.");

            @Setting
            private Message errorCannotAffordClaim = new Message().setMessage(
                "<red><b>Error</b> <dark_gray>» <red>Your town can't afford this. Deposit money using <yellow>/t deposit <amount><red>.");

            @Setting
            private Message errorOverMaxRadius = new Message().setMessage(
                "<red><b>Error</b> <dark_gray>» <red>The maximum radius allowed is <yellow><max><red>.");

            @Setting
            private Message errorZeroInput = new Message().setMessage(
                "<red><b>Error</b> <dark_gray>» <red>Please use <yellow>/t claim <red>to claim the current chunk you are standing in.");

            @Setting
            private Message errorNegativeInput = new Message().setMessage(
                "<red><b>Error</b> <dark_gray>» <red>Please specify a positive number for the radius to claim.");

        }

        @Getter
        @ConfigSerializable
        public static final class Unclaim {

            @Setting
            private Message notClaimed = new Message().setMessage("<red><b>Error</b> <dark_gray>» <red>This area isn't claimed.");

            @Setting
            private Message unclaimSuccess = new Message().setMessage("<red><b>Warning</b> <dark_gray>» <gray>Chunk unclaimed.");

            @Setting
            private Message confirmHomeBlockUnclaim = new Message().setMessage("""
                <gray>
                <gray>              <gold><st>               </st><gold><bold> Town Unclaim <gold><st>               <reset>
                <gray>
                <red>    This claim contains your town's home. Are you sure you want to unclaim this?
                <gray>
                <gray><italic>                          Please make a choice.
                <gray>                        <click:run_command:/t cancel><red><bold>[Cancel]</click>     <click:run_command:/t unclaim confirm><green><bold>[Confirm]</click>
                <gray>                 <gold><st>                                             <reset>
                """);

            @Setting
            private Message chunkLinkedOutpost = new Message().setMessage(
                "<red><b>Error</b> <dark_gray>» <red>You can not unclaim this chunk because it is linked to an outpost.");

            @Setting
            private Message unclaimConfirm = new Message().setMessage("""
                <gray>
                <gray>              <gold><st>               </st><gold><bold> Town Unclaim <gold><st>               <reset>
                <gray>
                <red>    You are about to unclaim a town outpost. Are you sure?
                <gray>
                <gray><italic>                          Please make a choice.
                <gray>                        <click:run_command:/t cancel><red><bold>[Cancel]</click>     <click:run_command:/t unclaim confirm><green><bold>[Confirm]</click>
                <gray>                 <gold><st>
                """);

        }

        @Getter
        @ConfigSerializable
        public static final class Plot {

            @Setting
            private Type type = new Type();

            @Setting
            private Member member = new Member();

            @Setting
            private Message notClaimed = new Message().setMessage("<red><b>Error</b> <dark_gray>» <red>No members have been assigned to this plot.");

            @Setting
            private Message enter = new Message().setActionBar("<gold>You've entered: <yellow><plot> <gray><italic>[<type>]");

            @Setting
            private Message noPermissionAssign = new Message().setMessage(
                "<red><b>Error</b> <dark_gray>» <red>You are not allowed to assign members to plots.");

            @Setting
            private Message evicted = new Message().setMessage("<red><b>Warning</b> <dark_gray>» <yellow><player> <red>evicted from the plot.");

            @Setting
            private Message evictedNoPermission = new Message().setMessage(
                "<red><b>Error</b> <dark_gray>» <red>You are not allowed to evict members from plots.");

            @Setting
            private Message renameNoPermission = new Message().setMessage(
                "<red><b>Error</b> <dark_gray>» <red>You are not allowed to rename the plot.");

            @Setting
            private Message manageNoPermission = new Message().setMessage(
                "<red><b>Error</b> <dark_gray>» <red>You are not allowed to manage plots.");

            @Setting
            private Message renameSuccess = new Message().setMessage("<gold><b>Info</b> <dark_gray>» <gray>Renamed plot to <yellow><name><gray>.");

            @Setting
            private Message isNotPlot = new Message().setMessage("<red><b>Error</b> <dark_gray>» <red>This is not a plot.");

            @Setting
            private Message plotInfo = new Message().setMessage("""
                <gold><st>               </st><gold><bold>Plot <name> <gold><st>               <reset>
                <gray>This <yellow><type> <gray>plot was created on <yellow><age><gray>.
                <gray>
                <gold>Members: <yellow><members><gray>.
                <gold><st>
                """);

            @Setting
            private String hereDelimiter = "<gray>, <yellow>";

            @Getter
            @ConfigSerializable
            public static final class Type {

                @Setting
                private Message setTypeNoPermission = new Message().setMessage(
                    "<red><b>Error</b> <dark_gray>» <red>You are not allowed to change this plot type.");

                @Setting
                private Message typeChanged = new Message().setMessage(
                    "<gold><b>Info</b> <dark_gray>» <gray>You changed this plot to <yellow><type><gray>.");

            }

            @Getter
            @ConfigSerializable
            public static final class Member {

                @Setting
                private Message notMember = new Message().setMessage(
                    "<red><b>Error</b> <dark_gray>» <yellow><player> <red>is not a member of the town.");

                @Setting
                private Message success = new Message().setMessage("<gold><b>Info</b> <dark_gray>» <yellow><player> <gray>was added to this plot.");

                @Setting
                private Message added = new Message().setMessage("<gold><b>Info</b> <dark_gray>» <yellow><player> <gray>added you to a plot.");

                @Setting
                private Message alreadyMember = new Message().setMessage(
                    "<red><b>Error</b> <dark_gray>» <yellow><player> <red>is already a member of this plot.");

            }
        }
    }

    @Getter
    @ConfigSerializable
    public static final class Tax {

        @Setting
        private Message collectionMessage = new Message().setMessage("""
            <gray>
            <dark_red>(<red>!<dark_red>) <red>A new day is here! Taxes and rent have been collected <dark_red>(<red>!<dark_red>)
            <gray>
            """);

        @Setting
        private Message townDisbanded = new Message().setMessage(
            "<dark_gray> » <gray>The town of <yellow><town> <gray>couldn''t afford to pay taxes.");

        @Setting
        private Message collectionWarning = new Message().setMessage("""
            <gray>
            <dark_red>(<red>!<dark_red>) <red>Taxes and rent will be collected in <yellow><time> <red>minutes <dark_red>(<red>!<dark_red>)
            <gray>
            """);
    }

    @Getter
    @ConfigSerializable
    public static final class Player {

        @Setting
        private Message errorCannotAffordDeposit = new Message().setMessage(
            "<red><b>Error</b> <dark_gray>» <red>You don't have enough money to deposit <yellow><amount><red>.");

        @Setting
        private Message townInvitedExpired = new Message().setMessage(
            "<gold><b>Info</b> <dark_gray>» Your invite from <yellow><town> <red>has expired.");

        @Setting
        private Message townJoinConfirmation = new Message().setMessage("""
            gray>
            <gray>              <gold><st>               </st><gold><bold> Town Invite <gold><st>               <reset>
            <gray>
            <yellow>               <player> <gray>invited you to join <yellow><town><gray>.
            <gray>
            <gray><italic>                          Please make a choice.
            <gray>                        <click:run_command:/t cancel><red><bold>[Cancel]</click>     <click:run_command:/t join <town>><green><bold>[Accept]</click>
            <gray>                 <gold><st>                                             <reset>
            """);

        @Setting
        private Message alreadyInTown = new Message().setMessage("<red><b>Error</b> <dark_gray>» <red>You're already in a town.");

        @Setting
        private Message invalidTown = new Message().setMessage("<red><b>Error</b> <dark_gray>» <red>That's not a valid town.");

        @Setting
        private Message notInvited = new Message().setMessage("<red><b>Error</b> <dark_gray>» <red>You're not invited to this town.");

        @Setting
        private Message fullTown = new Message().setMessage("<red><b>Error</b> <dark_gray>» <red>This town is full. Come back later.");

        @Setting
        private Message mayorCannotLeave = new Message().setMessage("<red><b>Error</b> <dark_gray>» <red>Town mayors can't leave their town.");

        @Setting
        private Message successfullyLeft = new Message().setMessage("<red><b>Warning</b> <dark_gray>» <red>You left your town.");

        @Setting
        private Message kickedFromTown = new Message().setMessage("<red><b>Warning</b> <dark_gray>» <gray>You have been kicked from the town.");

        @Setting
        private Message cantKickSelf = new Message().setMessage("<red><b>Error</b> <dark_gray>» <red>You can't kick yourself.");

        @Setting
        private Message bannedFromTown = new Message().setMessage(
            "<red><b>Warning</b> <dark_gray>» <yellow><player> <red>banned you from their town <yellow><town><red>.");

        @Setting
        private Message bannedWarningTitle = new Message().setTitle(
            new Title().setTitle("<dark_red>(<red>!<dark_red>) <red>Banned <dark_red>(<red>!<dark_red>)")
                .setSubTitle("<red>You're banned from that town."));

        @Setting
        private Message unbanMessage = new Message().setMessage(
            "<gold><b>Info</b> <dark_gray>» <gray>You've been unbanned from <yellow><town><gray>.");

        @Setting
        private Message cantDoThatCommandInThisPlotType = new Message().setMessage(
            "<red><b>Error</b> <dark_gray>» <red>You can't do that command in this <type> plots.");

        @Setting
        private Message youWereTrusted = new Message().setMessage(
            "<gold><b>Info</b> <dark_gray>» <gray>You were trusted in <yellow><town><gray>.");

        @Setting
        private Message youWereUntrusted = new Message().setMessage(
            "<gold><b>Info</b> <dark_gray>» <gray>You were untrusted in <yellow><town><gray>.");

        @Setting
        private Message youAreNowTheMayor = new Message().setMessage(
            "<gold><b>Info</b> <dark_gray>» <gray>You are now the mayor of <yellow><town><gray>.");
    }

    @Getter
    @ConfigSerializable
    public static final class Admin {

        @Setting
        private Town town = new Town();

        @Setting
        private Message stats = new Message().setMessage("""
            - Amount of towns: <towns>
            - Users in a town: <members>
            - Amount of outposts: <outposts>
            - Amount of chunks claimed: <chunks>
            - Amount of money in all banks: <money>
            - Amount of plots: <plots>
            """);

        @Getter
        @ConfigSerializable
        public static final class Town {

            @Setting
            private Message playerInTown = new Message().setMessage("<red><b>Error</b> <dark_gray>» <red>That player is already in a town.");

            @Setting
            private Message playerNotInTown = new Message().setMessage("<red><b>Error</b> <dark_gray>» <red>That player is not in a town.");

            @Setting
            private Message playerIsMayor = new Message().setMessage(
                "<red><b>Error</b> <dark_gray>» <red>This user is the mayor of the town. Try disbanding instead?");

            @Setting
            private Message playerForceAdded = new Message().setMessage(
                "<red><b>Warning</b> <dark_gray>» <gray>You've added <yellow><player> <gray>to <yellow><town><gray>.");

            @Setting
            private Message playerForceRemoved = new Message().setMessage(
                "<red><b>Warning</b> <dark_gray>» <gray>You've removed <yellow><player> <gray>from <yellow><town><gray>.");

            @Setting
            private Message playerForceMayor = new Message().setMessage(
                "<red><b>Warning</b> <dark_gray>» <yellow><player> <gray>is now the new mayor of <yellow><town><gray>.");

            @Setting
            private Message disbandWarning = new Message()
                .setMessage("""
                    <gray>
                    <gray>              <gold><st>               </st><gold><bold> Town Disband <gold><st>               <reset>
                    <gray>
                    <red>         You are about to disband this town. Are you sure?
                    <gray>
                    <gray><italic>                          Please make a choice.
                    <gray>                        <click:run_command:/t cancel><red><bold>[Cancel]</click>     <click:run_command:/ta town disband <town> confirm><green><bold>[Confirm]</click>
                    <gray>                 <gold><st>                                             <reset>
                    """);

            @Setting
            private Message disbandSuccess = new Message()
                .setMessage("<red><b>Warning</b> <dark_gray>» <gray>You've disbanded the town of <yellow><town><gray>.");

            @Setting
            private Message chunkAlreadyClaimed = new Message()
                .setMessage("<red><b>Error</b> <dark_gray>» <red>This chunk is already claimed by <town>.");

            @Setting
            private Message chunkClaimSuccess = new Message()
                .setMessage("<red><b>Warning</b> <dark_gray>» <gray>You've claimed this chunk for <yellow><town><gray>.");

            private Message chunkUnclaimSuccess = new Message()
                .setMessage("<red><b>Warning</b> <dark_gray>» <gray>You've unclaimed this chunk for <yellow><town><gray>.");

            @Setting
            private Bank bank = new Bank();

            @Getter
            @ConfigSerializable
            public static final class Bank {

                @Setting
                private Message added = new Message().setMessage(
                    "<red><b>Warning</b> <dark_gray>» <gray>You've added <yellow><amount> <gray>to <yellow><town>'s <gray>bank. Their new "
                        + "balance is <yellow><balance><gray>.");

                @Setting
                private Message balance = new Message().setMessage(
                    "<gold><b>Info</b> <dark_gray>» <yellow><town> <gray>has a balance of <yellow><balance><gray>.");

                @Setting
                private Message notEnough = new Message().setMessage(
                    "<red><b>Error</b> <dark_gray>» <red>This town does not have enough money to take from their bank.");

                @Setting
                private Message removed = new Message().setMessage(
                    "<red><b>Warning</b> <dark_gray>» <gray>You've removed <yellow><amount> <gray>from <yellow><town>'s <gray>bank. Their new "
                        + "balance is <yellow><balance> <gray>.");

            }
        }
    }

    @Getter
    @ConfigSerializable
    public static final class CommandFeedback {

        @Setting
        private Message argumentParse = new Message().setMessage("<red>Invalid command argument: <gray><value>");

        @Setting
        private Message commandExecution = new Message().setMessage("<red>An internal error occurred while attempting to perform this command.");

        @Setting
        private Message invalidCommandSender = new Message().setMessage("<red>Invalid command sender. You must be of type <gray><value>");

        @Setting
        private Message invalidSyntax = new Message().setMessage("<red>Invalid command syntax. Correct command syntax is: <gray>/<value>");

        @Setting
        private Message noPermission = new Message().setMessage("<red>I'm sorry, but you do not have permission to perform this command.<br>Please contact the server administrators if you believe that this is an error.");

    }
}
