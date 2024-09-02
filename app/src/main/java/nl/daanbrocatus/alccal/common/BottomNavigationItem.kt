package nl.daanbrocatus.alccal.common

import androidx.annotation.DrawableRes
import nl.daanbrocatus.alccal.R

data class BottomNavigationItem(
    val title: String,
    @DrawableRes
    val selectedIcon: Int,
    @DrawableRes
    val unselectedIcon: Int
) {
    companion object {
        val items = listOf(
            BottomNavigationItem(
                title = "Stats",
                selectedIcon = R.drawable.baseline_insert_chart_24,
                unselectedIcon = R.drawable.outline_insert_chart_24
            ),
            BottomNavigationItem(
                title = "Home",
                selectedIcon = R.drawable.baseline_sports_bar_24,
                unselectedIcon = R.drawable.outline_sports_bar_24
            ),
            BottomNavigationItem(
                title = "Settings",
                selectedIcon = R.drawable.baseline_settings_24,
                unselectedIcon = R.drawable.outline_settings_24
            )
        )
    }
}
