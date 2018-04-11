package bot.discord;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.awt.*;
import java.util.LinkedList;

public class TrackScheduler extends AudioEventAdapter{
    private LinkedList<AudioTrack> songQueue;
    private boolean isPlaying;
    private Guild guild;
    private EmbedBuilder builder;
    public TrackScheduler(Guild guild){
        songQueue = new LinkedList<>();
        isPlaying = false;
        this.guild = guild;

        //Setting up message builder
        builder = new EmbedBuilder();
        builder.setColor(Color.GREEN);
    }

    public void queue(AudioPlayer player, AudioTrack track){
        songQueue.add(track);
        //Song is not playing
        if(!isPlaying) {
            songQueue.poll();
            player.startTrack(track, false);
            isPlaying = true;
        }
    }

    public void songList(AudioPlayer player){
        String message = "Displaying 10 Songs \n" ;
        int count = 0;
        System.out.println(songQueue.size());
        for(AudioTrack song : songQueue){
            count++;
            message = message.concat(song.getInfo().title + " \n");
            System.out.println(song.getInfo().title);
            if(count >= 10){
                break;
            }
        }
        System.out.println(songQueue.size());

        builder.clearFields();
        builder.addField("Queue", message, true);
        MessageEmbed message2 = builder.build();
        guild.getDefaultChannel().sendMessage(message2).queue();
    }

    public void forcePlay(AudioPlayer player, AudioTrack track){
        AudioTrack playing = player.getPlayingTrack();
        if(playing != null){
            //vid keeps getting stuck without cloning it
            songQueue.addFirst(playing.makeClone());
        }
        player.startTrack(track, false);
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        nowPlaying(player, false);
        isPlaying = true;
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {

        if(endReason.mayStartNext){
            AudioTrack next = songQueue.poll();
            if(next != null){
                player.startTrack(next, false);
            }
            else{
                isPlaying = false;
            }
        }
        else if(endReason == AudioTrackEndReason.STOPPED){
            guild.getDefaultChannel().sendMessage("Skipping: `" + track.getInfo().title + "`").queue();
            player.startTrack(songQueue.poll(), false);
        }
        else{
            System.out.println(endReason);
            guild.getDefaultChannel().sendMessage("Error `"+ endReason + " " + track.getInfo().title + "`").queue();
        }
    }

    @Override
    public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
        System.out.println(exception + " EXCEPTION " + track.getInfo().title);
    }

    @Override
    public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
        System.out.println(thresholdMs + " STUCK " + track.getInfo().title);
    }

    public void nowPlaying(AudioPlayer player, boolean moreInfo){
        AudioTrack track = player.getPlayingTrack();
        String message;
        if(!moreInfo){
            message = "Now playing: `" + track.getInfo().title + "`";
        }
        else{
            message = "Now playing: `" + track.getInfo().title + "`\n" +
                    track.getInfo().uri;
        }
        guild.getDefaultChannel().sendMessage(message).queue();

    }

    private void durationOfSong(AudioTrack track){
        long milli = track.getDuration();
        long hours = 0;
        long minutes = 0;
        long seconds = 0;
        seconds = milli % 1000;

    }
}
