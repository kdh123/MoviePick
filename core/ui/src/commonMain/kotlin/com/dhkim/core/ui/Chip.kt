package com.dhkim.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Chip(onClick: () -> Unit, content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .background(color = Color.Transparent)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onBackground,
                shape = RoundedCornerShape(32.dp)
            )
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .noRippleClick(onClick)
    ) {
        content()
    }
}