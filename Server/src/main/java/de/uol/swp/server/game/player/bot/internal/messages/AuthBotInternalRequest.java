package de.uol.swp.server.game.player.bot.internal.messages;

import de.uol.swp.common.message.Message;
import de.uol.swp.common.message.MessageContext;
import de.uol.swp.common.user.Session;
import de.uol.swp.server.game.player.bot.BotPlayer;
import de.uol.swp.server.message.ServerInternalMessage;

import java.util.Optional;

/**
 * Serverseite Request einen Bot zu erstellen.
 *
 * @author Fenja, Ferit
 * @since Sprint7
 */
public class AuthBotInternalRequest implements ServerInternalMessage {

    private BotPlayer botPlayer;

    public AuthBotInternalRequest(BotPlayer theCreatedBot) {
        this.botPlayer = theCreatedBot;
    }

    public BotPlayer getBotPlayer() {
        return botPlayer;
    }

    @Override
    public Optional<MessageContext> getMessageContext() {
        return Optional.empty();
    }

    @Override
    public void setMessageContext(MessageContext messageContext) {

    }

    @Override
    public Optional<Session> getSession() {
        return Optional.empty();
    }

    @Override
    public void setSession(Session session) {

    }

    @Override
    public void initWithMessage(Message otherMessage) {

    }
}
