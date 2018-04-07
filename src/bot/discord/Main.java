package bot.discord;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioReference;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.managers.AudioManager;

import javax.security.auth.login.LoginException;


public class Main extends ListenerAdapter{
    private String prefix = ".";
    private int prefixLen = 1;
    public static JDA jda;
    public static void main(String[] args){ 
        JDABuilder builder = new JDABuilder(AccountType.BOT);
        builder.setToken("NDAwNDc3OTk3OTcyMzI0MzYy.DTkz7w.lB8cZBhmCzi05E9NxmeyEgUOqtM");
        builder.setAutoReconnect(true);
        builder.setStatus(OnlineStatus.ONLINE);
        try{
            jda = builder.buildBlocking();
        }
        catch(LoginException e){
            e.printStackTrace();
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        jda.addEventListener(new Main());
    }


    @Override
    public void onMessageReceived(MessageReceivedEvent e){
        //Data from message.
        User objUser = e.getAuthor();
        Message objMsg = e.getMessage();
        MessageChannel objChannel = e.getChannel();

        //Retrieved a command message
        String message = objMsg.getContentRaw();
        if(message.startsWith(prefix)){
            commandHelp(message.substring(1), objUser, objChannel, e);
        }
    }

    //Checks first word of the command string, does whatever the command is.
    private void commandHelp(String command, final User objUser, final MessageChannel objChannel, MessageReceivedEvent e){
        command = command.trim();
        String[] commands = command.split(" ");
        if(commands[0].equals("ping")){
            objChannel.sendMessage(objUser.getAsMention() + " pong.").queue();
        }
        else if(commands[0].equals("join")){
            Guild guild = e.getGuild();

            DefaultAudioPlayerManager playerManager = new DefaultAudioPlayerManager();
            final AudioPlayer player = playerManager.createPlayer();

            final TrackScheduler trackScheduler = new TrackScheduler(player);
            player.addListener(trackScheduler);

            //Currently hardcoding to test.
            final String identifier = "https://youtu.be/-ikn-f5ZD4c";

            VoiceChannel channel = guild.getVoiceChannelsByName("music", true).get(0);
            AudioManager manager = guild.getAudioManager();

            //Youtube video so we need a YoutubeAudioSourceManager
            YoutubeAudioSourceManager youtubeManager = new YoutubeAudioSourceManager();

            //Loading in a track and then playing it
            AudioTrack track = (AudioTrack)youtubeManager.loadItem(playerManager, new AudioReference(identifier, ""));
            player.playTrack(track);

            //Connecting into the chat
            manager.setSendingHandler(new AudioPlayerSendHandler(player));
            manager.openAudioConnection(channel);
        }
        else if(commands[0].equals("leave")){
            Guild guild = e.getGuild();
            VoiceChannel channel = guild.getVoiceChannelsByName("music", true).get(0);
            AudioManager manager = guild.getAudioManager();
            manager.closeAudioConnection();
        }
        else{
            objChannel.sendMessage(objUser.getAsMention() + " command not recognized.").queue();
        }
    }


}
