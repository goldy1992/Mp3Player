@file:OptIn(ExperimentalTextApi::class)

package com.github.goldy1992.mp3player.client.ui.modifiers

import androidx.compose.animation.core.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import kotlin.math.sqrt


@OptIn(ExperimentalTextApi::class)
fun Modifier.drawDiagonalLabel(
    text: String,
    color: Color,
    style: TextStyle = TextStyle(
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color.White
    ),
    labelTextRatio: Float = 3f
) = composed(
    factory = {

        val textMeasurer = rememberTextMeasurer()
        val textLayoutResult: TextLayoutResult = remember {
            textMeasurer.measure(text = AnnotatedString(text), style = style)
        }


        Modifier
            .clipToBounds()
            .drawWithContent {
                val canvasWidth = size.width

                val textSize = textLayoutResult.size
                val textWidth = textSize.width
                val textHeight = textSize.height

                val rectWidth = textWidth * labelTextRatio
                val rectHeight = textHeight * 1.1f

                val rect = Rect(
                    offset = Offset(canvasWidth - rectWidth, 0f),
                    size = Size(rectWidth, rectHeight)
                )

                val sqrt = sqrt(rectWidth / 2f)
                val translatePos = sqrt * sqrt

                drawContent()
                withTransform(
                    {
                        rotate(
                            degrees = 45f,
                            pivot = Offset(
                                canvasWidth - rectWidth / 2,
                                translatePos
                            )
                        )
                    }
                ) {
                    drawRect(
                        color = color,
                        topLeft = rect.topLeft,
                        size = rect.size
                    )
                    drawText(
                        textMeasurer = textMeasurer,
                        text = text,
                        style = style,
                        topLeft = Offset(
                            rect.left + (rectWidth - textWidth) / 2f,
                            rect.top + (rect.bottom - textHeight) / 2f
                        )
                    )
                }

            }
    }
)


@ExperimentalTextApi
fun Modifier.drawDiagonalShimmerLabel(
    text: String,
    color: Color,
    style: TextStyle = TextStyle(
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color.White
    ),
    labelTextRatio: Float = 7f,
) = composed(
    factory = {

        val textMeasurer = rememberTextMeasurer()
        val textLayoutResult: TextLayoutResult = remember {
            textMeasurer.measure(text = AnnotatedString(text), style = style)
        }

        val transition = rememberInfiniteTransition()

        val progress by transition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(3000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            )
        )

        Modifier
            .clipToBounds()
            .drawWithContent {
                val canvasWidth = size.width
                val canvasHeight = size.height

                val textSize = textLayoutResult.size
                val textWidth = textSize.width
                val textHeight = textSize.height

                val rectWidth = textWidth * labelTextRatio
                val rectHeight = textHeight * 1.1f

                val rect = Rect(
                    offset = Offset(canvasWidth - rectWidth, 0f),
                    size = Size(rectWidth, rectHeight)
                )

                val sqrt = sqrt(rectWidth / 2f)
                val translatePos = sqrt * sqrt

                val brush = Brush.linearGradient(
                    colors = listOf(
                        color,
                        style.color,
                        color,
                    ),
                    start = Offset(progress * canvasWidth, progress * canvasHeight),
                    end = Offset(
                        x = progress * canvasWidth + rectHeight,
                        y = progress * canvasHeight + rectHeight
                    ),
                )

                drawContent()
                withTransform(
                    {
                        rotate(
                            degrees = 45f,
                            pivot = Offset(
                                canvasWidth - rectWidth / 2,
                                translatePos
                            )
                        )
                    }
                ) {
                    drawRect(
                        brush = brush,
                        topLeft = rect.topLeft,
                        size = rect.size
                    )
                    drawText(
                        textMeasurer = textMeasurer,
                        text = text,
                        style = style,
                        topLeft = Offset(
                            rect.left + (rectWidth - textWidth) / 2f,
                            rect.top + (rect.bottom - textHeight) / 2f
                        )
                    )
                }

            }
    }
)