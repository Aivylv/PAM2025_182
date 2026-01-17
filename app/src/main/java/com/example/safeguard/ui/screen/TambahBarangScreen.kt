package com.example.safeguard.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.safeguard.ui.viewmodel.TambahBarangViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TambahBarangScreen(
    viewModel: TambahBarangViewModel,
    navigateBack: () -> Unit
) {
    val uiState = viewModel.uiState

    Scaffold(
        topBar = { TopAppBar(title = { Text("Input Barang Baru") }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // REQ-14: Interface formulir input
            OutlinedTextField(
                value = uiState.patient_id,
                onValueChange = { viewModel.updateUiState(uiState.copy(patient_id = it)) },
                label = { Text("ID Pasien (Wajib)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.item_name,
                onValueChange = { viewModel.updateUiState(uiState.copy(item_name = it)) },
                label = { Text("Nama Barang (Wajib)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.condition,
                onValueChange = { viewModel.updateUiState(uiState.copy(condition = it)) },
                label = { Text("Kondisi Fisik Barang") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            if (uiState.error != null) {
                Text(text = uiState.error!!, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { viewModel.saveItem(navigateBack) },
                enabled = uiState.isEntryValid && !uiState.isSaving,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (uiState.isSaving) CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                else Text("Simpan Data (REQ-253)")
            }
        }
    }
}