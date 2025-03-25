package com.dhkim.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.dhkim.core.designsystem.Black
import com.dhkim.core.designsystem.MoviePickTheme

@Preview(showBackground = true)
@Composable
fun CategoryModalPreview() {
    MoviePickTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Black)
        ) {
            CategoryModal(
                onClose = {}
            )
        }
    }
}