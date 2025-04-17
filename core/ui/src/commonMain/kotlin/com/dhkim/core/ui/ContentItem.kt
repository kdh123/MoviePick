package com.dhkim.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.dhkim.common.Series
import com.skydoves.landscapist.coil3.CoilImage
import org.jetbrains.compose.resources.painterResource

@Composable
fun ContentItem(
    series: Series,
    onClick: () -> Unit
) {
    CoilImage(
        modifier = Modifier
            .clip(RoundedCornerShape(12f))
            .width(108.dp)
            .aspectRatio(7f / 10f)
            .noRippleClick(onClick),
        loading = {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12f))
                    .width(108.dp)
                    .aspectRatio(7f / 10f)
                    .background(brush = ShimmerBrush(targetValue = 1_300f))
            )
        },
        imageModel = { series.imageUrl },
        failure = {},
        previewPlaceholder = painterResource(Resources.Icon.MoviePosterSample)
    )
}