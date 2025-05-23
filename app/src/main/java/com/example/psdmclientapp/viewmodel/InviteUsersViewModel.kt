package com.example.psdmclientapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.psdmclientapp.model.request.InviteUserRequest
import com.example.psdmclientapp.model.UserResponse
import com.example.psdmclientapp.network.SessionApiService
import com.example.psdmclientapp.network.UserApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InviteUsersViewModel @Inject constructor(
    private val userApi: UserApiService,
    private val sessionApi: SessionApiService
) : ViewModel() {

    // 1) keep a reference to your own UserResponse
    var currentUser by mutableStateOf<UserResponse?>(null)
        private set

    var availableUsers by mutableStateOf<List<UserResponse>>(emptyList())
        private set

    // 2) include your own ID in the selected set by default
    var selectedUserIds by mutableStateOf<Set<Long>>(emptySet())
        private set

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    init {
        loadAllData()
    }

    private fun loadAllData() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                // fetch current user…
                val me = userApi.getCurrentUser()
                currentUser = me

                // mark yourself as “already invited”
                selectedUserIds = setOf(me.id)

                // fetch everybody else…
                val users = userApi.getUsers()

                // …but filter out yourself
                availableUsers = users.filter { it.id != me.id }
            } catch (e: Exception) {
                errorMessage = "Greška pri dohvaćanju korisnika"
            } finally {
                isLoading = false
            }
        }
    }

    fun toggleUserSelection(userId: Long) {
        // make sure you never un-select yourself
        val baseSet = currentUser?.id?.let { selectedUserIds + it } ?: selectedUserIds

        selectedUserIds = if (baseSet.contains(userId)) {
            baseSet - userId
        } else {
            baseSet + userId
        }
    }

    fun sendInvites(sessionId: Long) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val request = InviteUserRequest(
                    sessionId,
                    selectedUserIds.toList()
                )
                sessionApi.inviteUsers(request)
            } catch (e: Exception) {
                errorMessage = "Neuspjelo slanje pozivnica"
            } finally {
                isLoading = false
            }
        }
    }
}

