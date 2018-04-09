package bot.discord;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Guild;

import javax.security.auth.login.LoginException;

public class Application {

    public static JDA jda;
    private String prefix = ".";

    public static void main(String[] args) {
        JDABuilder builder = new JDABuilder(AccountType.BOT);
        builder.setToken("NDAwNDc3OTk3OTcyMzI0MzYy.DTkz7w.lB8cZBhmCzi05E9NxmeyEgUOqtM");
        builder.setAutoReconnect(true);
        builder.setStatus(OnlineStatus.ONLINE);
        try {
            jda = builder.buildBlocking();
        } catch (LoginException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        UniversalBot bot = new UniversalBot();
        jda.addEventListener(bot);

        for (Guild guild : jda.getGuilds()) {
            bot.addGuild(guild);
            guild.getDefaultChannel().sendMessage("Reconnected to " + guild.getName()).queue();
        }
    }
}
