// com.example.techinterviewdemo.di.AppContainer.kt
package com.example.techinterviewdemo.di

import android.content.Context
import androidx.room.Room
import com.example.techinterviewdemo.data.api.MovieApiService
import com.example.techinterviewdemo.data.db.MovieDao
import com.example.techinterviewdemo.data.db.MovieDatabase
import com.example.techinterviewdemo.data.repository.MovieRepositoryImpl
import com.example.techinterviewdemo.domain.repository.MovieRepository
import com.example.techinterviewdemo.domain.usecase.GetMoviesUseCase
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class AppContainer(private val context: Context) {

    // API
    private val baseUrl = "https://api.themoviedb.org/3/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val movieApiService: MovieApiService = retrofit.create(MovieApiService::class.java)

    // Database
    private val database = Room.databaseBuilder(
        context,
        MovieDatabase::class.java,
        "movies.db"
    ).build()

    private val movieDao: MovieDao = database.movieDao()

    // Repository
    private val movieRepository: MovieRepository = MovieRepositoryImpl(movieApiService, movieDao)

    // UseCase
    val getMoviesUseCase = GetMoviesUseCase(movieRepository)
}

@Composable
fun MovieScreen() {
    val context = LocalContext.current
    val application = context.applicationContext as TechInterviewDemoApplication
    val getMoviesUseCase = application.appContainer.getMoviesUseCase

    val viewModel = viewModel<MoviesViewModel>(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MoviesViewModel(getMoviesUseCase) as T
            }
        }
    )
// com.example.techinterviewdemo.TechInterviewDemoApplication.kt
package com.example.techinterviewdemo

import android.app.Application
import com.example.techinterviewdemo.di.AppContainer

class TechInterviewDemoApplication : Application() {

    lateinit var appContainer: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer(this)
    }
}