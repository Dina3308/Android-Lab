package com.example.hw.domain

import com.example.hw.data.db.entity.ListWeather
import com.example.hw.data.db.entity.Weather
import com.example.hw.presentation.main.MainPresenter
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.*
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.coroutines.CoroutineContext

@ExtendWith(MockKExtension::class)
class GetWeatherUseCaseTest {

    @MockK
    lateinit var weatherRepository: WeatherRepository

    private lateinit var getWeatherUseCase: GetWeatherUseCase

    @ObsoleteCoroutinesApi
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @ExperimentalCoroutinesApi
    @ObsoleteCoroutinesApi
    @BeforeEach
    fun setUp() {
        getWeatherUseCase = spyk(GetWeatherUseCase(weatherRepository,  mainThreadSurrogate))
    }

    @Test
    fun getWeatherByName() = runBlocking{
        val expectedCity = "Kazan"
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
        coEvery {getWeatherUseCase.getWeatherByName(expectedCity)} returns expectedWeather

        val weather = getWeatherUseCase.getWeatherByName(expectedCity)

        assertEquals(expectedWeather, weather)

    }

    @Test
    fun getWeathersByCoord() = runBlocking{
        val expectedLong = 55.7887
        val expectedLat = 49.1221
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
        coEvery {getWeatherUseCase.getWeathersByCoord(expectedLat, expectedLong)} returns expectedWeather

        val weathers = getWeatherUseCase.getWeathersByCoord(expectedLat, expectedLong)

        assertEquals(expectedWeather[0], weathers[0])
    }

    @Test
    fun getAllWeathersFromDb() = runBlocking{
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
        coEvery {getWeatherUseCase.getAllWeathersFromDb()} returns expectedWeather

        val weathers = getWeatherUseCase.getAllWeathersFromDb()

        assertEquals(expectedWeather[0], weathers?.get(0))
    }

    @Test
    fun getWeatherById() = runBlocking{
        val expectedId = 1
        val expectedWeather =
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
            )

        coEvery {getWeatherUseCase.getWeatherById(expectedId)} returns expectedWeather

        val weather = getWeatherUseCase.getWeatherById(expectedId)

        assertEquals(expectedWeather, weather)
    }

    @Test
    fun getWeatherFromBd() = runBlocking {
        val expectedWeather =
            Weather(
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

        coEvery {getWeatherUseCase.getWeatherFromBd()} returns expectedWeather

        val weather = getWeatherUseCase.getWeatherFromBd()

        assertEquals(expectedWeather, weather)
    }
}