package com.revive.coinpulse.presentation.ui.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.revive.coinpulse.data.model.PricePoint

@Composable
fun PriceChart(
    pricePoints: List<PricePoint>,
    modifier: Modifier = Modifier,
    lineColor: Color = Color(0xFF4A90D9),
    gradientStartColor: Color = Color(0x664A90D9),
    gradientEndColor: Color = Color(0x004A90D9),
) {
    if (pricePoints.isEmpty()) return

    val prices = pricePoints.map { it.price }
    val minPrice = prices.min()
    val maxPrice = prices.max()
    val priceRange = maxPrice - minPrice

    Canvas(
        modifier =
            modifier
                .fillMaxWidth()
                .height(200.dp),
    ) {
        val width = size.width
        val height = size.height
        val stepX = width / (pricePoints.size - 1).coerceAtLeast(1)

        fun xAt(index: Int) = index * stepX

        fun yAt(price: Double): Float {
            if (priceRange == 0.0) return height / 2
            return height - ((price - minPrice) / priceRange * height).toFloat()
        }

        // 그라데이션 채우기
        val fillPath =
            Path().apply {
                moveTo(xAt(0), yAt(prices[0]))
                for (i in 1 until prices.size) {
                    val x0 = xAt(i - 1)
                    val x1 = xAt(i)
                    val y0 = yAt(prices[i - 1])
                    val y1 = yAt(prices[i])
                    val cx = (x0 + x1) / 2
                    cubicTo(cx, y0, cx, y1, x1, y1)
                }
                lineTo(xAt(prices.size - 1), height)
                lineTo(xAt(0), height)
                close()
            }
        drawPath(
            path = fillPath,
            brush =
                Brush.verticalGradient(
                    colors = listOf(gradientStartColor, gradientEndColor),
                    startY = 0f,
                    endY = height,
                ),
        )

        // 라인
        val linePath =
            Path().apply {
                moveTo(xAt(0), yAt(prices[0]))
                for (i in 1 until prices.size) {
                    val x0 = xAt(i - 1)
                    val x1 = xAt(i)
                    val y0 = yAt(prices[i - 1])
                    val y1 = yAt(prices[i])
                    val cx = (x0 + x1) / 2
                    cubicTo(cx, y0, cx, y1, x1, y1)
                }
            }
        drawPath(
            path = linePath,
            color = lineColor,
            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round),
        )

        // 시작점 & 끝점 도트
        drawCircle(
            color = lineColor,
            radius = 4.dp.toPx(),
            center = Offset(xAt(0), yAt(prices.first())),
        )
        drawCircle(
            color = lineColor,
            radius = 4.dp.toPx(),
            center = Offset(xAt(prices.size - 1), yAt(prices.last())),
        )
    }
}
