package com.example.blocdenotas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.dataStore
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.blocdenotas.databinding.FragmentLoginBinding
import com.example.blocdenotas.viewmodels.LoginViewModel
import com.example.blocdenotas.viewmodels.NoteShareViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Login : Fragment(R.layout.fragment_login) {
    lateinit var binding: FragmentLoginBinding
    lateinit var shareViewModel: NoteShareViewModel
    lateinit var loginViewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginViewModel = (activity as MainActivity).loginViewModel
        shareViewModel = (activity as MainActivity).noteShareViewModel
        binding.loginViewModel = loginViewModel
        binding.lifecycleOwner = this
        setupLoginButton()
        lifecycleScope.launch {
            shareViewModel.getAccessToken().collect {
                if (it.token.isNotBlank() && it.token.isNotEmpty()) {
                    val direction = LoginDirections.actionLoginToNotesListPage()
                    binding.root.findNavController().navigate(direction)
                }
            }
        }
    }

    private fun setupLoginButton() {
        binding.loginButton.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                shareViewModel.saveAccessToken("hello")
            }
            val direction = LoginDirections.actionLoginToNotesListPage()
            binding.root.findNavController().navigate(direction)
        }
    }
}