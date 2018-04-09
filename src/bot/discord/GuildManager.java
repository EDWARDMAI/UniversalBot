package bot.discord;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.core.entities.Guild;

public class GuildManager {
    private AudioPlayer player;
    private TrackScheduler trackScheduler;

    public GuildManager(AudioPlayer player, TrackScheduler trackScheduler){
        this.player = player;
        this.trackScheduler = trackScheduler;
    }

    public AudioPlayer getPlayer() {
        return player;
    }

    public TrackScheduler getTrackScheduler() {
        return trackScheduler;
    }
}
