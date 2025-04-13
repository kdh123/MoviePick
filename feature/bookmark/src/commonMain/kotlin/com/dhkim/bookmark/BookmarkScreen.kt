package com.dhkim.bookmark

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dhkim.common.SeriesBookmark
import com.dhkim.core.designsystem.MoviePickTheme
import com.dhkim.core.ui.Resources
import com.skydoves.landscapist.coil3.CoilImage
import kotlinx.collections.immutable.ImmutableList
import org.jetbrains.compose.resources.painterResource

@Composable
fun BookmarkScreen(
    uiState: BookmarkUiState
) {
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "내가 찜한 리스트",
                    style = MoviePickTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .padding(10.dp)
                )
            }
        }
    ) { paddingValues ->
        when (uiState.displayState) {
            BookmarkDisplayState.Loading -> {

            }

            is BookmarkDisplayState.Contents -> {
                Contents(
                    uiState.displayState.series,
                    modifier = Modifier
                        .padding(
                            top = paddingValues.calculateTopPadding(),
                            bottom = paddingValues.calculateBottomPadding()
                        )
                )
            }

            is BookmarkDisplayState.Error -> {

            }
        }
    }
}

@Composable
private fun Contents(
    bookmarks: ImmutableList<SeriesBookmark>,
    modifier: Modifier = Modifier
) {
    if (bookmarks.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxSize()
        ) {
            Text(
                text = "내가 찜한 리스트가 존재하지 않습니다.",
                style = MoviePickTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            )
        }

        return
    }

    LazyColumn(
        contentPadding = PaddingValues(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
            .fillMaxWidth()
    ) {
        items(items = bookmarks, key = { it.id }) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .border(
                        width = 1.dp,
                        color = Color.LightGray,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .fillMaxWidth()
            ) {
                CoilImage(
                    imageModel = { it.imageUrl },
                    previewPlaceholder = painterResource(Resources.Icon.MoviePosterSample),
                    modifier = Modifier
                        .height(100.dp)
                        .aspectRatio(0.8f)
                )
                Text(
                    text = it.title,
                    style = MoviePickTheme.typography.bodyLargeBold,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                )
            }
        }
    }
}