package com.dhkim.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import com.dhkim.common.Series
import com.dhkim.core.designsystem.Black00
import com.dhkim.core.designsystem.Black10
import com.dhkim.core.designsystem.Black30
import com.dhkim.core.designsystem.Black50
import com.dhkim.core.designsystem.Black70
import com.dhkim.core.designsystem.Black80
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil3.CoilImage
import org.jetbrains.compose.resources.painterResource

@Composable
fun RecommendationSeries(
    series: Series,
    onClick: () -> Unit = {},
    onPosterImageLoadSuccess: ((Painter) -> Unit)? = null,
    onUpdateRecommendationSeriesHeight: ((Int) -> Unit)? = null,
    content: @Composable RecommendationSeriesScope.() -> Unit,
) {
    val bottomBackgroundColors = listOf(Black00, Black10, Black30, Black50, Black70, Black80)
    val scope = remember(series) { DefaultRecommendationSeriesScope(series) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(7f / 9f)
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(12.dp))
            .noRippleClick(onClick)
            .onGloballyPositioned {
                onUpdateRecommendationSeriesHeight?.invoke(it.size.height)
            }
    ) {
        CoilImage(
            modifier = Modifier.fillMaxSize(),
            imageModel = { series.imageUrl },
            failure = {},
            previewPlaceholder = painterResource(Resources.Icon.MoviePosterSample),
            imageOptions = ImageOptions(contentScale = ContentScale.FillBounds),
            success = { _, painter ->
                onPosterImageLoadSuccess?.invoke(painter)
                Image(
                    painter = painter,
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.fillMaxWidth()
                )
            },
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(Brush.verticalGradient(colors = bottomBackgroundColors))
                .align(Alignment.BottomCenter)
        )

        Column(
            modifier = Modifier.align(Alignment.BottomStart)
        ) {
            scope.content()
        }
    }
}