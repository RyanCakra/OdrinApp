package com.example.odrinapp

class Model(val nDrinkName: String, val nDrinkDetail: String, val nDrinkPhoto: Int) {

    fun getnDrinkName(): String {
        return nDrinkName
    }

    fun getnDrinkDetail(): String {
        return nDrinkDetail
    }

    fun getnDrinkPhoto(): Int {
        return nDrinkPhoto
    }
}