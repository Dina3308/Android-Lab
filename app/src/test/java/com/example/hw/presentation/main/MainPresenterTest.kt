package com.example.hw.presentation.main

import android.location.Location
import com.example.hw.data.LocationRepositoryImpl
import com.example.hw.data.db.entity.ListWeather
import com.example.hw.data.db.entity.Weather
import com.example.hw.domain.GetWeatherUseCase
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import retrofit2.HttpException
import java.net.UnknownHostException
import kotlin.math.exp

@ExtendWith(MockKExtension::class)
class MainPresenterTest {

    @MockK
    lateinit var locationRepository: LocationRepositoryImpl

    @MockK
    lateinit var getWeatherUseCase: GetWeatherUseCase

    @MockK(relaxed = true)
    lateinit var viewState: `MainView$$State`

    private lateinit var presenter: MainPresenter

    @ObsoleteCoroutinesApi
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @ExperimentalCoroutinesApi
    @ObsoleteCoroutinesApi
    @BeforeEach
    fun setUp() {
        presenter = spyk(MainPresenter(locationRepository, getWeatherUseCase))
        presenter.setViewState(viewState)
        Dispatchers.setMain(mainThreadSurrogate)
    }
    
    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun onCityClick() {
        val expectedId = 2;

        presenter.onCityClick(expectedId)

        verify {
            viewState.navigateToDescriptionWeather(expectedId)
        }
    }

    @Test
    fun searchCity() {
        val expectedCity = "Kazan"
        val expectedId = -1
        val expectedWeather = Weather(
            1,
            expectedCity,
            22.0,
            2,
            3,
            24.0,
            26.0,
            19.0,
            3,
            "cloudy",
            "1",
            2.5,
            2
        )
        coEvery { getWeatherUseCase.getWeatherByName(expectedCity)} returns expectedWeather

        presenter.searchCity(expectedCity)

        verify {
            viewState.showLoading()
            viewState.navigateToDescriptionWeather(expectedId)
            viewState.hideLoading()
        }
        verify(inverse = true) {
            viewState.consumerError(any())
        }
    }

    @Test
    fun searchCityExpectedError() {
        val expectedCity = "Kazan"
        val expectedId = -1
        val expectedError = mockk<Throwable>()
        coEvery { getWeatherUseCase.getWeatherByName(expectedCity)} throws  expectedError
        presenter.searchCity(expectedCity)

        verify {
            viewState.showLoading()
            viewState.consumerError(expectedError)
            viewState.hideLoading()
        }
        verify(inverse = true) {
            viewState.navigateToDescriptionWeather(expectedId)
        }
    }

    @Test
    fun onLocationAccess() {

        val expectedWeather = listOf<ListWeather>(
            ListWeather(
                1,
                "Kazan",
                22.0,
                2,
                3,
                24.0,
                26.0,
                19.0,
                3,
                "cloudy",
                "1",
                2.5,
                2
            ),
            ListWeather(
                2,
                "Moscow",
                22.0,
                2,
                3,
                24.0,
                26.0,
                19.0,
                3,
                "cloudy",
                "1",
                2.5,
                2
            )
        )
        val expectedLocation = mockk<Location>()

        coEvery { locationRepository.getUserLocation()} returns expectedLocation

        coEvery { getWeatherUseCase.getWeathersByCoord(
            55.7887,
            49.1221
        )} returns expectedWeather

        presenter.onLocationAccess()

        verify {
            viewState.showLoading()
            viewState.showCities(expectedWeather)
            viewState.hideLoading()
        }

        verify(inverse = true) {
            viewState.consumerError(any())
        }
    }

    @Test
    fun onLocationAccessExpectedError() {

        val expectedWeather = listOf<ListWeather>(
            ListWeather(
                1,
                "Kazan",
                22.0,
                2,
                3,
                24.0,
                26.0,
                19.0,
                3,
                "cloudy",
                "1",
                2.5,
                2
            ),
            ListWeather(
                2,
                "Moscow",
                22.0,
                2,
                3,
                24.0,
                26.0,
                19.0,
                3,
                "cloudy",
                "1",
                2.5,
                2
            )
        )
        val expectedLocation = mockk<Location>()
        val expectedError = mockk<Throwable>()

        coEvery { locationRepository.getUserLocation()} returns expectedLocation

        coEvery { getWeatherUseCase.getWeathersByCoord(
            55.7887,
            49.1221
        )} throws  expectedError

        coEvery { getWeatherUseCase.getAllWeathersFromDb()} returns expectedWeather

        presenter.onLocationAccess()

        verify {
            viewState.showLoading()
            viewState.showCities(expectedWeather)
            viewState.consumerError(expectedError)
            viewState.hideLoading()
        }

    }

    @Test
    fun onLocationDenied() {
        val expectedWeather = listOf<ListWeather>(
            ListWeather(
                1,
                "Kazan",
                22.0,
                2,
                3,
                24.0,
                26.0,
                19.0,
                3,
                "cloudy",
                "1",
                2.5,
                2
            ),
            ListWeather(
                2,
                "Moscow",
                22.0,
                2,
                3,
                24.0,
                26.0,
                19.0,
                3,
                "cloudy",
                "1",
                2.5,
                2
            )
        )

        coEvery { getWeatherUseCase.getWeathersByCoord(
            55.7887,
            49.1221
        )} returns expectedWeather

        presenter.onLocationDenied()

        verify {
            viewState.showLoading()
            viewState.showCities(expectedWeather)
            viewState.hideLoading()
        }

        verify(inverse = true) {
            viewState.consumerError(any())
        }
    }

    @Test
    fun onLocationDeniedExpectedError() {
        val expectedError = mockk<Throwable>()
        val expectedWeather = listOf<ListWeather>(
            ListWeather(
                1,
                "Kazan",
                22.0,
                2,
                3,
                24.0,
                26.0,
                19.0,
                3,
                "cloudy",
                "1",
                2.5,
                2
            ),
            ListWeather(
                2,
                "Moscow",
                22.0,
                2,
                3,
                24.0,
                26.0,
                19.0,
                3,
                "cloudy",
                "1",
                2.5,
                2
            )
        )

        coEvery { getWeatherUseCase.getWeathersByCoord(
            55.7887,
            49.1221
        )} throws  expectedError

        coEvery { getWeatherUseCase.getAllWeathersFromDb()} returns expectedWeather

        presenter.onLocationDenied()

        verify {
            viewState.showLoading()
            viewState.showCities(expectedWeather)
            viewState.consumerError(expectedError)
            viewState.hideLoading()
        }

    }
}
