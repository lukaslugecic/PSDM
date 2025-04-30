package com.example.psdmclientapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.psdmclientapp.model.request.InviteUserRequest
import com.example.psdmclientapp.model.User
import com.example.psdmclientapp.network.ApiClient.userApi
import com.example.psdmclientapp.network.ApiClient.sessionApi
import dagger.hilt.android.lifecycle.HiltViewModel
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

    init {
        loadUsers()
    }

    fun loadUsers() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                availableUsers = userApi.getUsers()
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
                println(selectedUserIds)
                val request = InviteUserRequest(sessionId, selectedUserIds.toList())
                sessionApi.inviteUsers(request)
            } catch (e: Exception) {
                errorMessage = "Neuspjelo slanje pozivnica"
            } finally {
                isLoading = false
            }
        }
    }
}
