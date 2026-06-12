package cz.cvut.fit.vinyltracker

import android.app.Application
import androidx.room.Room
import cz.cvut.fit.vinyltracker.data.local.AppDatabase
import cz.cvut.fit.vinyltracker.data.remote.ItunesApi
import cz.cvut.fit.vinyltracker.data.repository.VinylRepository
import cz.cvut.fit.vinyltracker.domain.usecase.AddToCollectionUseCase
import cz.cvut.fit.vinyltracker.domain.usecase.AddToWishlistUseCase
import cz.cvut.fit.vinyltracker.ui.feature.add.AddViewModel
import cz.cvut.fit.vinyltracker.ui.feature.collection.CollectionViewModel
import cz.cvut.fit.vinyltracker.ui.feature.detail.DetailViewModel
import cz.cvut.fit.vinyltracker.ui.feature.wishlist.WishlistViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.URLProtocol
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    // Room DB
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "vinyl_db").build()
    }

    single { get<AppDatabase>().vinylDao() }
    single { get<AppDatabase>().trackDao() }

    single { VinylRepository(get(), get()) }

    // itunes.apple.com HttpClient
    single {
        HttpClient(Android) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true }, contentType = ContentType.Any)
            }
            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS
                    host = "itunes.apple.com"
                }
            }
        }
    }

    single { ItunesApi(get()) }

    single { AddToCollectionUseCase(get(), get()) }
    single { AddToWishlistUseCase(get(), get()) }

    viewModel { CollectionViewModel(get()) }
    viewModel { WishlistViewModel(get()) }
    viewModel { params -> DetailViewModel(vinylId = params.get(), repository = get()) }
    viewModel { AddViewModel(get(), get(), get()) }
}

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(appModule)
        }
    }
}
