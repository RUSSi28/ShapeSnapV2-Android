package com.orukunnn.shapesnapapp.di

import com.google.firebase.firestore.FirebaseFirestore
import com.orukunnn.shapesnapapp.MainScreenViewModel
import com.orukunnn.shapesnapapp.data.datasource.FirebaseAuthDatasource
import com.orukunnn.shapesnapapp.data.datasource.FirestoreDatasource
import com.orukunnn.shapesnapapp.data.repository.PresetsRepository
import com.orukunnn.shapesnapapp.data.repository.PresetsRepositoryImpl
import com.orukunnn.shapesnapapp.ui.home.HomeScreenViewModel
import com.orukunnn.shapesnapapp.ui.login.LoginViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val modules = module {
    single { FirebaseFirestore.getInstance("shape-snap") }
    single<PresetsRepository> { PresetsRepositoryImpl(get()) }
    single { FirestoreDatasource(get()) }
    single { FirebaseAuthDatasource() }
    viewModel { MainScreenViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { HomeScreenViewModel(get(), get()) }
}