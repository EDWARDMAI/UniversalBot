package bot.discord;

import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManager;
import com.sedmelluq.discord.lavaplayer.track.AudioItem;
import com.sedmelluq.discord.lavaplayer.track.AudioReference;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class SourceManager implements AudioSourceManager{
    public SourceManager(){

    }

    public AudioItem loadItem(DefaultAudioPlayerManager defaultAudioPlayerManager, AudioReference audioReference) {
        return null;
    }

    public AudioTrack decodeTrack(AudioTrackInfo audioTrackInfo, DataInput dataInput) {
        return null;
    }

    public void encodeTrack(AudioTrack audioTrack, DataOutput dataOutput) {

    }

    public boolean isTrackEncodable(AudioTrack audioTrack) {
        return false;
    }

    public String getSourceName() {
        return null;
    }

    public void shutdown() {

    }
}
