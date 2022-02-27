package com.example.testjetpackmultiapp

class Greeting {
    fun greeting(): String {
        return "Hello, ${Platform().platform}!"
    }
}