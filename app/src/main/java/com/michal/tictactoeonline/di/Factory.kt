package com.michal.tictactoeonline.di

interface Factory<T> {
    fun create(): T
}