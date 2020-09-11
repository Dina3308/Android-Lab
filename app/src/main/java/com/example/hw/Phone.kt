package com.example.hw

class Phone (private val processor: String, private val RAM: Int, name: String, price: Int, producer: String) :
    Device(name, price, producer), DeviceInterface {
    override fun on() {
        println("phone is on")
    }

    override fun off() {
        println("phone is off")
    }

    override fun getInfo() {
        println("name: $name, price: $price, processor: $processor, RAM: $RAM")
    }
}
