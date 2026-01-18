package com.example.safeguard.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.safeguard.ui.viewmodel.LoginViewModel
import com.example.safeguard.R
import androidx.compose.ui.res.painterResource

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginSuccess: () -> Unit,
    onRegisterNav: () -> Unit
) {
    val uiState = viewModel.uiState

    Column(
        modifier = Modifier.fillMaxSize().padding(dimensionResource(R.dimen.padding_large)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo SafeGuard",
            modifier = Modifier
                .size(150.dp)
                .padding(bottom = 24.dp)
        )
        Text(text = stringResource(R.string.app_name), style = MaterialTheme.typography.headlineLarge)

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacer_large)))

        OutlinedTextField(
            value = uiState.email,
            onValueChange = { viewModel.onEmailChange(it) },
            label = { Text(stringResource(R.string.email_label)) },
            modifier = Modifier.fillMaxWidth(),
            isError = uiState.errorMessage != null
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacer_medium)))

        OutlinedTextField(
            value = uiState.password,
            onValueChange = { viewModel.onPasswordChange(it) },
            label = { Text(stringResource(R.string.password_label)) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            isError = uiState.errorMessage != null
        )

        //MENAMPILKAN PESAN ERROR (REQ-282 & REQ-344)
        uiState.errorMessage?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacer_large)))

        Button(
            onClick = { viewModel.login(onLoginSuccess) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = { viewModel.login(onLoginSuccess) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Masuk")
                }
            }
        }

        TextButton(onClick = onRegisterNav, enabled = !uiState.isLoading) {
            Text(stringResource(R.string.no_account))
        }
    }
}