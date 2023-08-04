package xyz.nejcrozman.progress.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import xyz.nejcrozman.progress.ProgressApplication
import xyz.nejcrozman.progress.ui.types.TypeAddViewModel
import xyz.nejcrozman.progress.ui.types.TypeDetailsViewModel
import xyz.nejcrozman.progress.ui.types.TypeListViewModel

/**
 * Provides Factory to create instance of ViewModel for the entire Inventory app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for TypeEditViewModel
        /*initializer {
            TypeEditViewModel(
                this.createSavedStateHandle()
            )
        }*/
        // Initializer for TypeAddViewModel
        initializer {
            TypeAddViewModel(progressApplication().container.typeRepository)
        }

        // Initializer for TypeDetailsViewModel
        initializer {
            TypeDetailsViewModel(
                this.createSavedStateHandle(),
                progressApplication().container.typeRepository
            )
        }

        // Initializer for TypeListViewModel
        initializer {
            TypeListViewModel(progressApplication().container.typeRepository)
        }
    }
}

/**
 * Extension function to queries for Application object and returns an instance of
 * [ProgressApplication].
 */
fun CreationExtras.progressApplication(): ProgressApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ProgressApplication)