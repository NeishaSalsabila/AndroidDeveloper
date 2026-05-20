package com.neisha.technicaltest_androiddeveloper.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.neisha.technicaltest_androiddeveloper.domain.model.User
import com.neisha.technicaltest_androiddeveloper.ui.adaptive.LayoutType
import com.neisha.technicaltest_androiddeveloper.ui.adaptive.toLayoutType

/**
 * Adaptive user list:
 * - Phone  (Compact)  → single column LazyColumn
 * - Tablet (Expanded) → 2-column LazyVerticalGrid
 * - Medium            → 2-column grid as well
 */
@Composable
fun AdaptiveUserList(
    users: List<User>,
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier
) {
    val layoutType = windowSizeClass.toLayoutType()

    when (layoutType) {
        LayoutType.COMPACT -> {
            LazyColumn(
                modifier = modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 16.dp, end = 16.dp,
                    top = 8.dp, bottom = 100.dp
                ),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(users, key = { it.id }) { user ->
                    UserCard(user = user)
                }
            }
        }
        LayoutType.MEDIUM, LayoutType.EXPANDED -> {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 16.dp, end = 16.dp,
                    top = 8.dp, bottom = 100.dp
                ),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(users, key = { it.id }) { user ->
                    UserCard(user = user)
                }
            }
        }
    }
}