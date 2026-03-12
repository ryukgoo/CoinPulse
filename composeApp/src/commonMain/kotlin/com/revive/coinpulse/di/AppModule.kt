package com.revive.coinpulse.di

import com.revive.coinpulse.data.AppSettings
import com.revive.coinpulse.data.CoinCacheStorage
import com.revive.coinpulse.data.CoinRepositoryImpl
import com.revive.coinpulse.data.FavoriteStorage
import com.revive.coinpulse.data.createSettings
import com.revive.coinpulse.data.remote.CoinRemoteDataSource
import com.revive.coinpulse.data.remote.createHttpClient
import com.revive.coinpulse.domain.repository.CoinRepository
import com.revive.coinpulse.domain.usecase.GetCachedCoinsUseCase
import com.revive.coinpulse.domain.usecase.GetCoinsUseCase
import com.revive.coinpulse.domain.usecase.GetMarketChartUseCase
import com.revive.coinpulse.domain.usecase.ObserveFavoritesUseCase
import com.revive.coinpulse.domain.usecase.ToggleFavoriteUseCase
import com.revive.coinpulse.presentation.viewmodel.CoinViewModel
import org.koin.dsl.module

val appModule = module {
    // Network
    single { createHttpClient() }
    single { CoinRemoteDataSource(get()) }

    // Storage
    single { createSettings() }
    single { FavoriteStorage(get()) }
    single { CoinCacheStorage(get()) }

    // Repository
    single<CoinRepository> { CoinRepositoryImpl(get(), get(), get()) }


    // Use Cases
    single { GetCoinsUseCase(get()) }
    single { GetCachedCoinsUseCase(get()) }
    single { ToggleFavoriteUseCase(get()) }
    single { ObserveFavoritesUseCase(get()) }
    single { GetMarketChartUseCase(get()) }

    // Storage
    single { createSettings() }
    single { FavoriteStorage(get()) }
    single { CoinCacheStorage(get()) }
    single { AppSettings(get()) }

    // ViewModel
    single { CoinViewModel(get(), get(), get(), get(), get(), get()) }
}

