package io.github.dexrnzacattack.rrdiscordbridge.discord;

import static io.github.dexrnzacattack.rrdiscordbridge.RRDiscordBridge.instance;
import static io.github.dexrnzacattack.rrdiscordbridge.RRDiscordBridge.logger;
import static io.github.dexrnzacattack.rrdiscordbridge.discord.DiscordBot.getName;
import static io.github.dexrnzacattack.rrdiscordbridge.discord.DiscordBot.trimDiscordMessage;

import io.github.dexrnzacattack.rrdiscordbridge.config.Settings;
import io.github.dexrnzacattack.rrdiscordbridge.extension.result.ModifiableExtensionChatResult;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReference;
import net.dv8tion.jda.api.entities.MessageType;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.entities.messages.MessagePoll;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.*;

/** Handles various Discord events from channels the bot listens to */
public class DiscordEventHandler extends ListenerAdapter {
    /** List of events and the methods they invoke */
    EnumMap<MessageType, DiscordMessageEvent<MessageReceivedEvent, Message, String, String>>
            events = new EnumMap<>(MessageType.class);

    public DiscordEventHandler() {
        events.put(MessageType.DEFAULT, this::onMessage);
        events.put(MessageType.INLINE_REPLY, this::onMessage);
        events.put(MessageType.GUILD_MEMBER_JOIN, this::onMemberJoin);
        events.put(MessageType.GUILD_MEMBER_BOOST, this::onServerBoost);
        events.put(MessageType.THREAD_CREATED, this::onThreadCreated);
        events.put(MessageType.CHANNEL_PINNED_ADD, this::onMessagePinned);
        events.put(MessageType.POLL_RESULT, this::onPollEnded);
        events.put(MessageType.CONTEXT_COMMAND, this::onUserAppActivity);
    }

    /** Runs when the bot is ready for use */
    @Override
    public void onReady(ReadyEvent event) {
        instance.getBot().channel =
                event.getJDA().getTextChannelById(instance.getSettings().relayChannelId);
    }

    /**
     * Runs when someone sends a message in the console channel
     *
     * @param event The JDA message event
     * @param message The JDA Message
     * @param author The author of the message
     * @param text The content of the message
     */
    public void onConsoleChannelMessageReceived(
            MessageReceivedEvent event, Message message, String author, String text) {
        Member member = event.getMember();

        if (member != null
                && instance.getSettings().discordOperators.contains(member.getId())
                && instance.getSupportedFeatures().canSendConsoleCommands()) {
            logger.info(String.format("[Discord Console] %s ran command '%s'", author, text));

            instance.getServer().runCommand(text);
        }
    }

    /**
     * Runs when someone runs a command in the channel the bot is watching
     *
     * @param event The interaction event
     */
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getInteraction().getMember() == null) return;

        String channel = event.getChannel().getId();
        String sender = event.getInteraction().getMember().getId();

        if (channel.equals(instance.getSettings().relayChannelId)
                && !sender.equals(instance.getBot().self.getId())
                && !sender.equals(instance.getBot().webhook.getId())) {

            String author =
                    event.getMember() != null
                            ? getName(event.getMember())
                            : getName(event.getUser());
            instance.getServer()
                    .broadcastMessage(
                            String.format(
                                    "§d[Discord] §e%s ran Discord command \"/%s\".",
                                    author, event.getFullCommandName()));
        }
    }

    /**
     * Runs when a join message is sent to the relay channel the bot is watching
     *
     * @param event The JDA message event
     * @param message The JDA Message
     * @param author The author of the message
     * @param text The content of the message
     */
    public void onMemberJoin(
            MessageReceivedEvent event, Message message, String author, String text) {
        if (instance.getSettings().enabledDiscordEvents.contains(Settings.DiscordEvents.USER_JOIN))
            instance.getServer()
                    .broadcastMessage(
                            String.format(
                                    "§d[Discord] §e%s has joined the Discord server.", author));
    }

    /**
     * Runs when a server boost message is sent to the relay channel the bot is watching
     *
     * @param event The JDA message event
     * @param message The JDA Message
     * @param author The author of the message
     * @param text The content of the message
     */
    public void onServerBoost(
            MessageReceivedEvent event, Message message, String author, String text) {
        if (instance.getSettings().enabledDiscordEvents.contains(Settings.DiscordEvents.USER_BOOST))
            instance.getServer()
                    .broadcastMessage(
                            String.format(
                                    "§d[Discord] §e%s has boosted the Discord server.", author));
    }

    /**
     * Runs when a thread creation message is sent to the relay channel the bot is watching
     *
     * @param event The JDA message event
     * @param message The JDA Message
     * @param author The author of the message
     * @param text The content of the message
     */
    public void onThreadCreated(
            MessageReceivedEvent event, Message message, String author, String text) {
        if (instance.getSettings()
                .enabledDiscordEvents
                .contains(Settings.DiscordEvents.THREAD_CREATION))
            instance.getServer()
                    .broadcastMessage(
                            String.format(
                                    "§d[Discord] §e%s has created the thread \"%s\".",
                                    author, message));
    }

    /**
     * Runs when a message is pinned in the relay channel the bot is watching
     *
     * @param event The JDA message event
     * @param message The JDA Message
     * @param author The author of the message
     * @param text The content of the message
     */
    public void onMessagePinned(
            MessageReceivedEvent event, Message message, String author, String text) {
        if (instance.getSettings()
                .enabledDiscordEvents
                .contains(Settings.DiscordEvents.MESSAGE_PIN))
            instance.getServer()
                    .broadcastMessage(
                            String.format(
                                    "§d[Discord] §e%s has pinned a message to the channel.",
                                    author));
    }

    /**
     * Runs when a poll has ended in the relay channel the bot is watching
     *
     * @param event The JDA message event
     * @param message The JDA Message
     * @param author The author of the message
     * @param text The content of the message
     */
    public void onPollEnded(
            MessageReceivedEvent event, Message message, String author, String text) {
        if (!instance.getSettings()
                .enabledDiscordEvents
                .contains(Settings.DiscordEvents.POLL_ENDED)) return;

        // for some reason we can't get the reference directly but it does give us the
        // ids so we have to use that... bruh
        MessageReference ref = message.getMessageReference();
        if (ref == null) return;

        String channelId = ref.getChannelId();
        TextChannel pollChannel = message.getGuild().getTextChannelById(channelId);

        if (pollChannel == null) return;

        // the actual poll message
        Message poll =
                pollChannel
                        .retrieveMessageById(message.getMessageReference().getMessageId())
                        .complete();

        MessagePoll mPoll = poll.getPoll();

        if (mPoll == null) return;

        instance.getServer()
                .broadcastMessage(
                        String.format(
                                "§d[Discord] §e%s's poll \"%s\" has ended.\nResults:",
                                author, mPoll.getQuestion().getText()));
        poll.getPoll()
                .getAnswers()
                .forEach(
                        answer -> {
                            instance.getServer()
                                    .broadcastMessage(
                                            String.format(
                                                    "§3%s: §b%s",
                                                    answer.getText(), answer.getVotes()));
                        });
    }

    /**
     * Runs when a user app/activity is run in the relay channel the bot is watching
     *
     * @param event The JDA message event
     * @param message The JDA Message
     * @param author The author of the message
     * @param text The content of the message
     */
    public void onUserAppActivity(
            MessageReceivedEvent event, Message message, String author, String text) {
        if (!instance.getSettings().enabledDiscordEvents.contains(Settings.DiscordEvents.USER_APP))
            return;

        if (message.getInteractionMetadata() == null) return;

        // those weird activity messages and user crapps
        instance.getServer()
                .broadcastMessage(
                        String.format(
                                "§d[Discord] §e%s used app \"%s\".",
                                author, message.getAuthor().getName()));
    }

    /**
     * Runs when a message is forwarded to the relay channel the bot is watching
     *
     * @param event The JDA message event
     * @param message The JDA Message
     * @param author The author of the message
     * @param text The content of the message
     */
    public void onForwardedMessage(
            MessageReceivedEvent event, Message message, String author, String text) {
        if (!instance.getSettings()
                .enabledDiscordEvents
                .contains(Settings.DiscordEvents.FORWARDED_MESSAGE)) return;

        instance.getServer()
                .broadcastMessage(String.format("§d[Discord] §e%s forwarded a message.", author));
    }

    /**
     * Runs when a poll is created in the relay channel the bot is watching
     *
     * @param event The JDA message event
     * @param message The JDA Message
     * @param author The author of the message
     * @param text The content of the message
     */
    public void onPollCreated(
            MessageReceivedEvent event, Message message, String author, String text) {
        if (!instance.getSettings()
                .enabledDiscordEvents
                .contains(Settings.DiscordEvents.POLL_CREATION)) return;

        MessagePoll poll = message.getPoll();
        if (poll == null) return;

        instance.getServer()
                .broadcastMessage(
                        String.format(
                                "§d[Discord] §e%s has created a poll \"%s\".",
                                author, poll.getQuestion().getText()));
    }

    /**
     * Runs when a normal message is sent to the relay channel the bot is watching
     *
     * @param event The JDA message event
     * @param message The JDA Message
     * @param author The author of the message
     * @param text The content of the message
     */
    public void onMessage(MessageReceivedEvent event, Message message, String author, String text) {
        // message forwarding
        if (message.getMessageReference() != null
                && message.getMessageReference().getType()
                        == MessageReference.MessageReferenceType.FORWARD) {
            onForwardedMessage(event, message, author, text);
            return;
        }

        // polls
        if (message.getPoll() != null) {
            onPollCreated(event, message, author, text);
            return;
        }

        if (!instance.getSettings()
                .enabledDiscordEvents
                .contains(Settings.DiscordEvents.USER_MESSAGE)) return;

        // so basically traits are things about the message, for example, if a message got trimmed
        // for being too long, it will have (T) added to the message inside MC.
        // if a message is both trimmed and has attachments as well as is replying to something, it
        // will look like (2, T, R)
        List<String> traits = new java.util.ArrayList<>(Collections.emptyList());

        boolean hasAttachment = false;
        int attachmentCount = 0;

        Message replyingTo = null;
        if (message.getMessageReference() != null) replyingTo = message.getReferencedMessage();

        String replyAuthor = "";
        if (replyingTo != null) {
            if (replyingTo.getMember() != null) {
                replyAuthor = getName(replyingTo.getMember());
            } else {
                replyAuthor = getName(replyingTo.getAuthor());
            }
        }

        // check for attachments
        if (!event.getMessage().getAttachments().isEmpty()
                || !event.getMessage().getStickers().isEmpty()) {
            hasAttachment = true;
            attachmentCount =
                    event.getMessage().getAttachments().size()
                            + event.getMessage().getStickers().size();
            traits.add(Integer.toString(attachmentCount));
        }

        // add trait if message is too long
        if (event.getMessage().getContentDisplay().length()
                > instance.getSettings().maxMessageSize) {
            traits.add("T");
            event.getMessage().addReaction(Emoji.fromUnicode("\uD83D\uDCCF")).queue();
        }

        instance.getServer()
                .broadcastMessage(
                        String.format(
                                "§d[Discord]%s%s §e%s§f: %s%s",
                                ((replyingTo != null && !replyAuthor.isEmpty())
                                        ? String.format(" §b(RE: §e%s§b)", replyAuthor)
                                        : ""),
                                (!traits.isEmpty()
                                        ? String.format(" §6(%s)", String.join(", ", traits))
                                        : ""),
                                author,
                                text,
                                (text.isEmpty() && hasAttachment
                                        ? String.format("§6%s attachment(s)", attachmentCount)
                                        : "")));
    }

    /**
     * Runs when a message is received in the channel the bot is watching
     *
     * @param event The JDA message event
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String channel = event.getChannel().getId();
        String author = event.getAuthor().getName();
        String authorId = event.getAuthor().getId();

        Message message = event.getMessage();

        boolean sendMc = true;

        if (instance.getBridgeExtensions() != null) {
            ModifiableExtensionChatResult<Message> ext =
                    instance.getBridgeExtensions().tryParseDiscord(message);
            message = ext.message;
            sendMc = ext.getShouldSendToMinecraft();
        }

        if (message.getMember() != null) author = getName(message.getMember());
        else author = getName(message.getAuthor());

        String messageTrimmed = trimDiscordMessage(message);

        if (channel.equals(instance.getSettings().consoleChannelId)) {
            onConsoleChannelMessageReceived(event, message, author, messageTrimmed);
            return;
        }

        if (!sendMc) return;

        if (channel.equals(instance.getSettings().relayChannelId)
                && !authorId.equals(instance.getBot().self.getId())
                && !authorId.equals(instance.getBot().webhook.getId())) {

            DiscordMessageEvent<MessageReceivedEvent, Message, String, String> run =
                    events.get(message.getType());

            if (run != null) {
                run.accept(event, message, author, messageTrimmed);
            }
        }
    }
}
