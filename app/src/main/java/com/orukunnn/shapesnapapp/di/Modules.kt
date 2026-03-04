package com.orukunnn.shapesnapapp.di

import com.google.firebase.firestore.FirebaseFirestore
import com.orukunnn.shapesnapapp.MainScreenViewModel
import com.orukunnn.shapesnapapp.data.datasource.FirebaseAuthDatasource
import com.orukunnn.shapesnapapp.data.datasource.FirestoreDatasource
import com.orukunnn.shapesnapapp.data.repository.auth.AuthRepository
import com.orukunnn.shapesnapapp.data.repository.auth.AuthRepositoryImpl
import com.orukunnn.shapesnapapp.data.repository.preset.PresetsRepository
import com.orukunnn.shapesnapapp.data.repository.preset.PresetsRepositoryImpl
import com.orukunnn.shapesnapapp.data.repository.user.UserRepository
import com.orukunnn.shapesnapapp.data.repository.user.UserRepositoryImpl
import com.orukunnn.shapesnapapp.ui.home.HomeScreenViewModel
import com.orukunnn.shapesnapapp.ui.posts.PostManageViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val modules = module {
    single { FirebaseFirestore.getInstance("shape-snap") }
    single<PresetsRepository> { PresetsRepositoryImpl(get()) }
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single<UserRepository> { UserRepositoryImpl(get()) }
    single { FirestoreDatasource(get()) }
    single { FirebaseAuthDatasource() }
    viewModel { MainScreenViewModel(get(), get()) }
    viewModel { HomeScreenViewModel(get()) }
    viewModel { PostManageViewModel(get(), get()) }
}
