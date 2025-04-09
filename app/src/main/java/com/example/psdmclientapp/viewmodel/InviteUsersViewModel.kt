package com.example.psdmclientapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.psdmclientapp.model.InviteUserRequest
import com.example.psdmclientapp.model.User
import com.example.psdmclientapp.network.ApiClient.userApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InviteUsersViewModel @Inject constructor() : ViewModel() {

    var availableUsers by mutableStateOf<List<User>>(emptyList())
        private set

    var selectedUserIds by mutableStateOf<Set<Long>>(emptySet())
        private set

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    fun loadUsers() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                // TODO: Replace with real API call
                delay(500)
                availableUsers = listOf(
                    User(1, "Ivan"),
                    User(2, "Ana"),
                    User(3, "Petra"),
                    User(4, "Marko")
                )
            } catch (e: Exception) {
                errorMessage = "Greška pri dohvaćanju korisnika"
            } finally {
                isLoading = false
            }
        }
    }

    fun toggleUserSelection(userId: Long) {
        selectedUserIds = if (selectedUserIds.contains(userId)) {
            selectedUserIds - userId
        } else {
            selectedUserIds + userId
        }
    }

    fun sendInvites(sessionId: Long) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
               // val request = InviteUserRequest(sessionId, selectedUserIds.toList())
                // userApi.inviteUsers(request) // pretend this is implemented
            } catch (e: Exception) {
                errorMessage = "Neuspjelo slanje pozivnica"
            } finally {
                isLoading = false
            }
        }
    }
}
