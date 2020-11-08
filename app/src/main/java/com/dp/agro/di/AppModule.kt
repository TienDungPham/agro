package com.dp.agro.di

import com.dp.agro.data.source.AppRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
abstract class AppModule {
    companion object {
        @Singleton
        @Provides
        fun provideDispatcher(): CoroutineDispatcher {
            return Dispatchers.IO
        }

        @Singleton
        @Provides
        fun provideFirestore(): FirebaseFirestore {
            val db = Firebase.firestore
            val settings = firestoreSettings {
                isPersistenceEnabled = true
            }
            db.firestoreSettings = settings
            return db
        }

        @Singleton
        @Provides
        fun provideAppRepository(
            firestore: FirebaseFirestore,
            dispatcher: CoroutineDispatcher
        ): AppRepository {
            return AppRepository(firestore, dispatcher)
        }
    }
}