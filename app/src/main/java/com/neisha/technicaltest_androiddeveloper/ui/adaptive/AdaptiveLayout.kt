package com.neisha.technicaltest_androiddeveloper.ui.adaptive

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass

/**
 * Wrapper to determine current screen layout type.
 * Supports phone (compact), foldable (medium), and tablet (expanded).
 */
enum class LayoutType {
    COMPACT,
    MEDIUM,
    EXPANDED
}

fun WindowSizeClass.toLayoutType(): LayoutType = when (widthSizeClass) {
    WindowWidthSizeClass.Compact -> LayoutType.COMPACT
    WindowWidthSizeClass.Medium -> LayoutType.MEDIUM
    WindowWidthSizeClass.Expanded -> LayoutType.EXPANDED
    else -> LayoutType.COMPACT
}
