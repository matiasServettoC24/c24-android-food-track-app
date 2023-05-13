package com.example.c24_android_food_track_app.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.c24_android_food_track_app.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _login = MutableSharedFlow<Resource<FirebaseUser>>()
    val login: Flow<Resource<FirebaseUser>> = _login.asSharedFlow()

    fun login(email: String, password: String) {
        val user = firebaseAuth.currentUser
        if (user != null) {
            viewModelScope.launch {_login.emit(Resource.Success(user)) }
        } else {
            viewModelScope.launch { _login.emit(Resource.Loading()) }
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    viewModelScope.launch {
                        it.user?.let {
                            _login.emit(Resource.Success(it))
                        }
                    }
                }
                .addOnFailureListener {
                    viewModelScope.launch {
                        _login.emit(Resource.Error(it.message.toString()))
                    }
                }
        }
    }
}