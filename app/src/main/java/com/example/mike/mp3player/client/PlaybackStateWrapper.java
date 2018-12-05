package com.example.mike.mp3player.client;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.media.session.PlaybackStateCompat;

public final class PlaybackStateWrapper implements Parcelable {
    private final PlaybackStateCompat playbackState;
    private long timestanp;

    public PlaybackStateWrapper(PlaybackStateCompat playbackState) {
        this.playbackState = playbackState;
        this.timestanp = System.currentTimeMillis();
    }

    protected PlaybackStateWrapper(Parcel in) {
        playbackState = in.readParcelable(PlaybackStateCompat.class.getClassLoader());
        timestanp = in.readLong();
    }

    public static final Creator<PlaybackStateWrapper> CREATOR = new Creator<PlaybackStateWrapper>() {
        @Override
        public PlaybackStateWrapper createFromParcel(Parcel in) {
            return new PlaybackStateWrapper(in);
        }

        @Override
        public PlaybackStateWrapper[] newArray(int size) {
            return new PlaybackStateWrapper[size];
        }
    };

    public PlaybackStateCompat getPlaybackState() {
        return playbackState;
    }

    public long getTimestanp() {
        return timestanp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(playbackState, 0);
        parcel.writeLong(timestanp);
    }
}
