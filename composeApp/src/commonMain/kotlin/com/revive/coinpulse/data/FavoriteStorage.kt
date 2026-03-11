package com.revive.coinpulse.data

import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FavoriteStorage(private val settings: Settings) {

    companion object {
        private const val KEY_FAVORITES = "favorites"
    }

    private val _favoritesFlow = MutableStateFlow(getFavoriteIds())
    val favoritesFlow: StateFlow<Set<String>> = _favoritesFlow.asStateFlow()

    fun isFavorite(coinId: String): Boolean {
        return getFavoriteIds().contains(coinId)
    }

    fun toggleFavorite(coinId: String) {
        val current = getFavoriteIds().toMutableSet()
        if (current.contains(coinId)) current.remove(coinId)
        else current.add(coinId)
        settings.putString(KEY_FAVORITES, current.joinToString(","))
        _favoritesFlow.value = current
    }

    private fun getFavoriteIds(): Set<String> {
        val raw = settings.getString(KEY_FAVORITES, "")
        return if (raw.isEmpty()) emptySet() else raw.split(",").toSet()
    }
}