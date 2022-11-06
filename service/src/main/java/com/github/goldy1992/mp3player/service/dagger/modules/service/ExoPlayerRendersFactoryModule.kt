package com.github.goldy1992.mp3player.service.dagger.modules.service

import android.content.Context
import android.os.Handler
import android.support.v4.media.session.MediaSessionCompat
import com.github.goldy1992.mp3player.service.player.equalizer.FFTAudioProcessor
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.Renderer
import com.google.android.exoplayer2.RenderersFactory
import com.google.android.exoplayer2.audio.AudioCapabilities
import com.google.android.exoplayer2.audio.AudioRendererEventListener
import com.google.android.exoplayer2.audio.AudioSink
import com.google.android.exoplayer2.audio.DefaultAudioSink
import com.google.android.exoplayer2.audio.MediaCodecAudioRenderer
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@InstallIn(ServiceComponent::class)
@Module
class ExoPlayerRendersFactoryModule {

    @Provides
    fun providesCustomRenderersFactory(@ApplicationContext context: Context,
                                       mediaSessionCompat: MediaSessionCompat) : RenderersFactory {
        val renderersFactory = object : DefaultRenderersFactory(context) {

            override fun buildAudioRenderers(
                context: Context,
                extensionRendererMode: Int,
                mediaCodecSelector: MediaCodecSelector,
                enableDecoderFallback: Boolean,
                audioSink: AudioSink,
                eventHandler: Handler,
                eventListener: AudioRendererEventListener,
                out: ArrayList<Renderer>
            ) {
                out.add(
                    MediaCodecAudioRenderer(
                        context,
                        mediaCodecSelector,
                        enableDecoderFallback,
                        eventHandler,
                        eventListener,
                        DefaultAudioSink.Builder().setAudioCapabilities(AudioCapabilities.getCapabilities(context))
                            .setAudioProcessors(arrayOf(FFTAudioProcessor(mediaSessionCompat)))
                            .build()
                    )
                )

                super.buildAudioRenderers(
                    context,
                    extensionRendererMode,
                    mediaCodecSelector,
                    enableDecoderFallback,
                    audioSink,
                    eventHandler,
                    eventListener,
                    out
                )
            }
        }
        return renderersFactory
    }
}