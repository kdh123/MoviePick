package com.dhkim.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dhkim.core.designsystem.Black
import com.dhkim.core.designsystem.DarkGray60
import com.dhkim.core.designsystem.MoviePickTheme
import com.dhkim.core.designsystem.White
import com.dhkim.core.ui.MoviePickButton
import com.dhkim.core.ui.RecommendationSeriesScope
import com.dhkim.core.ui.Resources
import com.dhkim.core.ui.noRippleClick
import com.dhkim.domain.movie.model.MovieVideoType
import org.jetbrains.compose.resources.painterResource

@Composable
fun RecommendationSeriesScope.RecommendationButtons(navigateToVideo: (String) -> Unit) {
    val hasVideo = series.video != null

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
    ) {
        if (hasVideo) {
            MoviePickButton(
                color = White,
                onClick = {},
                modifier = Modifier
                    .weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .noRippleClick {
                            navigateToVideo(series.video!!.key)
                        }
                ) {
                    Icon(
                        painter = painterResource(Resources.Icon.Play),
                        contentDescription = null,
                        tint = Black,
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .size(24.dp)
                    )
                    Text(
                        text = series.video?.type?.type ?: MovieVideoType.Teaser.type,
                        style = MoviePickTheme.typography.labelLarge,
                        color = Black,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }

        MoviePickButton(
            color = DarkGray60,
            onClick = {},
            modifier = Modifier
                .weight(1f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(Resources.Icon.Add),
                    tint = White,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .size(24.dp)
                )
                Text(
                    text = "찜",
                    style = MoviePickTheme.typography.labelLarge,
                    color = White,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}