package com.example.hw

object SongRepository {
    fun getSongs() = arrayListOf<Song>(
        Song("Demons", "Imagine Dragons", R.drawable.demons, R.raw.demons, "2:55"),
        Song( "Now or never", "halsey", R.drawable.halsey, R.raw.halsey, "3:34"),
        Song("Numb", "Neffex", R.drawable.numb, R.raw.numb, "2:24"),
        Song("Radioactive", "Imagine Dragons", R.drawable.radioactive, R.raw.radioactive, "3:08"),
        Song("Pon de Replay", "rihanna", R.drawable.rihanna, R.raw.rihanna, "3:50"),
        Song("Summertime Sadness", "Lana Del Rey", R.drawable.summertimesadness, R.raw.summertimesadness, "4:25"),
        Song("I knew you were trouble", "Taylor Swift", R.drawable.taylorswift, R.raw.taylorswift, "3:20")
    )
}