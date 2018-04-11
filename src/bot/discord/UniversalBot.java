package bot.discord;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.track.AudioItem;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioReference;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.managers.AudioManager;
import java.util.HashMap;


public class UniversalBot extends ListenerAdapter{
    private HashMap<Guild, GuildManager> guilds;
    private DefaultAudioPlayerManager playerManager;
    private String prefix = ".";
    private int prefixLen = 1;

    public UniversalBot(){
        guilds = new HashMap<>();
        playerManager = new DefaultAudioPlayerManager();
    }

    public void addGuild(Guild guild){
        TrackScheduler trackScheduler = new TrackScheduler(guild);


        AudioPlayer player = playerManager.createPlayer();
        player.addListener(trackScheduler);
        GuildManager guildManager = new GuildManager(player, trackScheduler);

        guilds.put(guild, guildManager);
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
    private void commandHelp(String commandLine, User objUser, MessageChannel objChannel, MessageReceivedEvent e){
        commandLine = commandLine.trim();
        String[] commands = commandLine.split(" ");
        String command = commands[0];

        //Storing information about the command call's origin
        String user = objUser.getAsMention();
        Guild guild = e.getGuild();
        GuildManager guildManager = guilds.get(guild);
        AudioManager manager = guild.getAudioManager();



        if(command.equals("ping")){
            objChannel.sendMessage(user + " pong!").queue();
        }

        else if(command.equals("join")){
            Member member = guild.getMember(objUser);
            //Connecting into the chat
            VoiceChannel channel = member.getVoiceState().getChannel();

            if(channel == null) {
                objChannel.sendMessage(user + " you must be in a channel to do that!").queue();
                return;
            }
            manager.openAudioConnection(channel);
        }

        else if(command.equals("leave")){
            //may need to pause
            manager.closeAudioConnection();
        }

        else if(command.equals("play")){
            TrackScheduler trackScheduler = guildManager.getTrackScheduler();
            AudioPlayer player = guildManager.getPlayer();
            if (commands.length <= 1) {
                objChannel.sendMessage(user + " please specify a song.").queue();
            }
            else {
                String identifier = commands[1];

                //Youtube video so we need a YoutubeAudioSourceManager
                YoutubeAudioSourceManager youtubeManager = new YoutubeAudioSourceManager();

                //Bot is not in a channel
                if (!manager.isConnected()) {
                    Member member = guild.getMember(objUser);
                    //Connecting into the chat
                    VoiceChannel channel = member.getVoiceState().getChannel();

                    if(channel == null) {
                        objChannel.sendMessage(user + " you must be in a channel to do that!").queue();
                        return;
                    }
                    manager.openAudioConnection(channel);

                    objChannel.sendMessage(user + " bot is not in a channel! Joining " + user + ".").queue();
                }

                AudioItem audioItem = youtubeManager.loadItem(playerManager, new AudioReference(identifier, ""));
                //Loading in a track and then playing it
                if(audioItem instanceof AudioTrack){
                    AudioTrack track = (AudioTrack) audioItem;
                    trackScheduler.queue(player, track);
                }
                else if(audioItem instanceof AudioPlaylist){
                    AudioPlaylist playlist = (AudioPlaylist) audioItem;
                    for(AudioTrack track : playlist.getTracks()){
                        trackScheduler.queue(player, track);
                    }
                }
                else{
                    objChannel.sendMessage("error").queue();
                }
                manager.setSendingHandler(new AudioPlayerSendHandler(player));
            }
        }

        else if(command.equals("forceplay")){
            TrackScheduler trackScheduler = guildManager.getTrackScheduler();
            AudioPlayer player = guildManager.getPlayer();

            String identifier = commands[1];

            //Youtube video so we need a YoutubeAudioSourceManager
            YoutubeAudioSourceManager youtubeManager = new YoutubeAudioSourceManager();
            AudioItem audioItem = youtubeManager.loadItem(playerManager, new AudioReference(identifier, ""));
            //Loading in a track and then playing it
            if(audioItem instanceof AudioTrack){
                AudioTrack track = (AudioTrack) audioItem;
                trackScheduler.forcePlay(player, track);
            }
            else if(audioItem instanceof AudioPlaylist){
                objChannel.sendMessage(user + " cannot forceplay a playlist.").queue();
            }
            else{
                objChannel.sendMessage("error").queue();
            }
        }

        else if(command.equals("pause")){
            AudioPlayer player = guildManager.getPlayer();
            if(player.isPaused()) {
                objChannel.sendMessage(user + " player is already paused.").queue();
            }
            else {
                objChannel.sendMessage("Pausing...").queue();
                player.setPaused(true);
            }
        }

        else if(command.equals("resume")){
            AudioPlayer player = guildManager.getPlayer();
            if(player.isPaused()) {
                objChannel.sendMessage("Resuming...").queue();
                player.setPaused(false);
            }
            else {
                objChannel.sendMessage(user + " player is not paused.").queue();
            }
        }

        else if(command.equals("np")){
            TrackScheduler trackScheduler = guildManager.getTrackScheduler();
            AudioPlayer player = guildManager.getPlayer();
            trackScheduler.nowPlaying(player, true);
        }

        else if(command.equals("skip")){
            AudioPlayer player = guildManager.getPlayer();
            player.stopTrack();
        }

        else if(command.equals("queue")){
            TrackScheduler trackScheduler = guildManager.getTrackScheduler();
            AudioPlayer player = guildManager.getPlayer();
            trackScheduler.songList(player);
        }

        else if(command.equals("seek")){

        }

        else{
            objChannel.sendMessage(user + " command not recognized. Try again.").queue();
        }
    }

}
