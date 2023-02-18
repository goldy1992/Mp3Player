package com.github.goldy1992.mp3player.service.dagger.modules.service

import android.content.Context
import android.os.Handler
import androidx.media3.common.audio.AudioProcessor
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.Renderer
import androidx.media3.exoplayer.RenderersFactory
import androidx.media3.exoplayer.audio.*
import androidx.media3.exoplayer.mediacodec.MediaCodecSelector
import com.github.goldy1992.mp3player.service.player.equalizer.FFTAudioProcessor
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
                                       fftAudioProcessor: FFTAudioProcessor) : RenderersFactory {
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
                            .setAudioProcessors(arrayOf(fftAudioProcessor))
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