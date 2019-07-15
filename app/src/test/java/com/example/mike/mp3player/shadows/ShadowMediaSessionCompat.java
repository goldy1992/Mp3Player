package com.example.mike.mp3player.shadows;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.media.session.IMediaSession;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.versionedparcelable.VersionedParcelable;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.annotation.RealObject;

@Implements(MediaSessionCompat.class)
public class ShadowMediaSessionCompat {

    private @RealObject MediaSessionCompat mediaSessionCompat;

    @Implementation
    public void __constructor__(Context context, String tag) {}


    @Implementation
    public void __constructor__(Context context, String tag, ComponentName mbrComponent,
    PendingIntent mbrIntent) { }

    @Implementation
    public void __constructor__(Context context, String tag, ComponentName mbrComponent,
                              PendingIntent mbrIntent, VersionedParcelable session2Token) {
    }

    @Implements(MediaSessionCompat.Token.class)
    public static final class Token implements Parcelable {


        @Implementation
        public void __constructor__(Object inner) {

        }

        @Implementation
        public void __constructor__(Object inner, IMediaSession extraBinder) {

        }

        @Implementation
        public void __constructor__(Object inner, IMediaSession extraBinder, VersionedParcelable session2Token) {

        }


        @Override
        @Implementation
        public int describeContents() {
            return 0;
        }

        @Override
        @Implementation
        public void writeToParcel(Parcel dest, int flags) {

        }

        @Implementation
        public static MediaSessionCompat.Token fromToken(Object inner) {
            return null;
        }

    }
}
