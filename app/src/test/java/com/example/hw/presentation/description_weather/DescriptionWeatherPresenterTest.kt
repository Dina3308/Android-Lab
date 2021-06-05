package com.example.hw.presentation.description_weather

import com.example.hw.data.db.entity.ListWeather
import com.example.hw.data.db.entity.Weather
import com.example.hw.domain.GetWeatherUseCase
import com.example.hw.presentation.main.MainPresenter
import com.example.hw.presentation.main.`MainView$$State`
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class DescriptionWeatherPresenterTest {

    @MockK
    lateinit var getWeatherUseCase: GetWeatherUseCase

    @MockK(relaxed = true)
    lateinit var viewState: `DescriptionWeatherView$$State`

    private lateinit var presenter: DescriptionWeatherPresenter

    @ObsoleteCoroutinesApi
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @ExperimentalCoroutinesApi
    @ObsoleteCoroutinesApi
    @BeforeEach
    fun setUp() {
        presenter = spyk(DescriptionWeatherPresenter(getWeatherUseCase))
        presenter.setViewState(viewState)
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }
    
    @Test
    fun showWeatherById() {
        val expectedId = 1
        val expectedWeather = ListWeather(
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
        )
        val expectedWindDirection = "N"
        coEvery { getWeatherUseCase.getWeatherById(expectedId)} returns expectedWeather
        every { presenter.showWeather(
            expectedWeather.cityName,
            expectedWeather.feelsLike.toInt().toString(),
            expectedWeather.humidity.toString(),
            expectedWeather.pressure.toString(),
            expectedWeather.temp.toInt().toString(),
            expectedWeather.tempMax.toInt().toString(),
            expectedWeather.tempMin.toInt().toString(),
            expectedWeather.cloudiness.toString(),
            expectedWeather.description,
            expectedWeather.icon,
            expectedWeather.speed.toString(),
            expectedWindDirection
        ) } just Runs

        presenter.showWeatherById(expectedId)

        verify {
            viewState.showLoading()
            presenter.showWeather(
                expectedWeather.cityName,
                expectedWeather.feelsLike.toInt().toString(),
                expectedWeather.humidity.toString(),
                expectedWeather.pressure.toString(),
                expectedWeather.temp.toInt().toString(),
                expectedWeather.tempMax.toInt().toString(),
                expectedWeather.tempMin.toInt().toString(),
                expectedWeather.cloudiness.toString(),
                expectedWeather.description,
                expectedWeather.icon,
                expectedWeather.speed.toString(),
                expectedWindDirection
            )
            viewState.hideLoading()
        }
    }

    @Test
    fun showWeatherFromFromBd() {
        val expectedWeather = Weather(
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
        )
        val expectedWindDirection = "N"
        coEvery { getWeatherUseCase.getWeatherFromBd()} returns expectedWeather
        every { presenter.showWeather(
            expectedWeather.cityName,
            expectedWeather.feelsLike.toInt().toString(),
            expectedWeather.humidity.toString(),
            expectedWeather.pressure.toString(),
            expectedWeather.temp.toInt().toString(),
            expectedWeather.tempMax.toInt().toString(),
            expectedWeather.tempMin.toInt().toString(),
            expectedWeather.cloudiness.toString(),
            expectedWeather.description,
            expectedWeather.icon,
            expectedWeather.speed.toString(),
            expectedWindDirection
        ) } just Runs

        presenter.showWeatherFromFromBd()

        verify {
            viewState.showLoading()
            presenter.showWeather(
                expectedWeather.cityName,
                expectedWeather.feelsLike.toInt().toString(),
                expectedWeather.humidity.toString(),
                expectedWeather.pressure.toString(),
                expectedWeather.temp.toInt().toString(),
                expectedWeather.tempMax.toInt().toString(),
                expectedWeather.tempMin.toInt().toString(),
                expectedWeather.cloudiness.toString(),
                expectedWeather.description,
                expectedWeather.icon,
                expectedWeather.speed.toString(),
                expectedWindDirection
            )
            viewState.hideLoading()
        }
    }
}
