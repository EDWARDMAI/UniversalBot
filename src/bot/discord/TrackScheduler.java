package bot.discord;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.core.entities.Guild;

import java.util.LinkedList;

public class TrackScheduler extends AudioEventAdapter{
    private LinkedList<AudioTrack> songQueue;
    private boolean isPlaying;
    private Guild guild;
    public TrackScheduler(Guild guild){
        songQueue = new LinkedList<>();
        isPlaying = false;
        this.guild = guild;
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

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        isPlaying = true;

        for (AudioTrack tracks: songQueue) {
            System.out.println(tracks.getInfo());

        }
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {

        if(endReason.mayStartNext){
            player.startTrack(songQueue.poll(), false);
        }
    }

    @Override
    public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
        super.onTrackException(player, track, exception);
    }

    @Override
    public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
        super.onTrackStuck(player, track, thresholdMs);
    }


}
