package com.example.newsapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.newsapp.domain.entity.ArticleItem
import com.example.newsapp.domain.useCase.GetNewsUseCase
import com.example.newsapp.presentation.model.UserAction
import com.example.newsapp.presentation.model.UserState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * NewsViewModel class is responsible for handling business logic and communication between the UI and the repository.
 * @param useCase The GetNewsUseCase instance for data operations.
 */

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val useCase: GetNewsUseCase
): ViewModel() {

    val newsPagingDataFlow: Flow<PagingData<ArticleItem>> = useCase.invoke()
        .cachedIn(viewModelScope)

    private val _userState= MutableStateFlow(UserState())
    val userState = _userState.asStateFlow()

    fun onAction(action: UserAction) {
        when (action) {
            is UserAction.OnNewsCardClick -> {
                _userState.update {
                    userState.value.copy(
                        openUrl = action.openUrl
                    )
                }
            }
        }
    }
}