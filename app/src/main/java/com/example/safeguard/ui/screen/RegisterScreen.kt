package com.example.safeguard.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.example.safeguard.R
import com.example.safeguard.ui.viewmodel.RegisterViewModel

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    navigateBack: () -> Unit
) {
    val state = viewModel.uiState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.padding_large)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.register_title),
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacer_large)))

        // Input Nama
        OutlinedTextField(
            value = state.name,
            onValueChange = { viewModel.updateUiState(name = it) },
            label = { Text(stringResource(id = R.string.name_label)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacer_medium)))

        // Input Email
        OutlinedTextField(
            value = state.email,
            onValueChange = { viewModel.updateUiState(email = it) },
            label = { Text(stringResource(id = R.string.email_label)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacer_medium)))

        // Input Password
        OutlinedTextField(
            value = state.password,
            onValueChange = { viewModel.updateUiState(pass = it) },
            label = { Text(stringResource(id = R.string.password_label)) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        // Error Message
        state.errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = dimensionResource(id = R.dimen.padding_small))
            )
        }

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacer_large)))

        if (state.isLoading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = { viewModel.registerUser(navigateBack) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(id = R.string.btn_register))
            }

            TextButton(onClick = navigateBack) {
                Text(stringResource(id = R.string.already_have_account))
            }
        }
    }
}