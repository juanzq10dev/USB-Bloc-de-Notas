package com.example.blocdenotas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.blocdenotas.databinding.FragmentLoginBinding
import com.example.blocdenotas.observer.ConnectivityObserver
import com.example.blocdenotas.retrofit.entity.LoginPost
import com.example.blocdenotas.room.models.AccessToken
import com.example.blocdenotas.viewmodels.LoginViewModel
import com.example.blocdenotas.viewmodels.NoteShareViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.log

class Login : Fragment(R.layout.fragment_login) {
    lateinit var binding: FragmentLoginBinding
    lateinit var shareViewModel: NoteShareViewModel
    lateinit var loginViewModel: LoginViewModel
    lateinit var pref: DataStore<Preferences>
    lateinit var connectivityObserver: ConnectivityObserver
    var isLogged = false
    // val pref = requireContext().applicationContext.dataStore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        pref = (activity as MainActivity).dataStore
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginViewModel = (activity as MainActivity).loginViewModel
        shareViewModel = (activity as MainActivity).noteShareViewModel
        connectivityObserver = (activity as MainActivity).connectivityObserver
        binding.loginViewModel = loginViewModel
        binding.lifecycleOwner = this
        loginViewModel.isValid.value = false
        setupLoginButton()
    }

    override fun onStart() {
        lifecycleScope.launch(Dispatchers.IO) {
            shareViewModel.getAccessToken().collect {
                withContext(Dispatchers.Main) {
                    isLogged = it.token.isNotEmpty()
                }
            }
        }

        if (isLogged) {
            val direction = LoginDirections.actionLoginToNotesListPage()
            binding.root.findNavController().navigate(direction)
        }
        super.onStart()
    }

    override fun onResume() {
        lifecycleScope.launch(Dispatchers.IO) {
            shareViewModel.getAccessToken().collect {
                isLogged = it.token.isNotEmpty()
            }
        }
        super.onResume()
    }

    override fun onPause() {
        loginViewModel.userEmail.value = ""
        loginViewModel.userPassword.value = ""

        super.onPause()
    }

    private fun setupLoginButton() {
        lifecycleScope.launch {
            connectivityObserver.observe().collect {
                if (it != ConnectivityObserver.InternetStatus.Available ) {
                    withContext(Dispatchers.Main) {
                        binding.loginButton.isEnabled = false
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        binding.loginButton.isEnabled = true
                    }
                }
            }
        }

        binding.loginButton.setOnClickListener {
            binding.errorText.text = "Cargando"
            shareViewModel.login(
                LoginPost(loginViewModel.userEmail.value!!, loginViewModel.userPassword.value!!))

            lifecycleScope.launch(Dispatchers.IO) {
                shareViewModel.getAccessToken().collect {
                    withContext(Dispatchers.Main) {
                        isLogged = it.token.isNotEmpty()
                        if (!isLogged) {
                            binding.errorText.text = "Error de credenciales"
                        }
                    }
                }
            }

            if (isLogged) {
                binding.errorText.text = "Conectado!"
                val direction = LoginDirections.actionLoginToNotesListPage()
                binding.root.findNavController().navigate(direction)
            }
        }
    }
}
