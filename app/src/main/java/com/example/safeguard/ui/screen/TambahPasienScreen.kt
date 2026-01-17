package com.example.safeguard.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.safeguard.ui.viewmodel.PasienViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TambahPasienScreen(
    viewModel: PasienViewModel,
    navigateBack: () -> Unit
) {
    val uiState = viewModel.uiState

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Pendaftaran Pasien Baru") })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Deskripsi sesuai tanggung jawab petugas di SRS [cite: 149]
            Text(
                text = "Input data fisik pasien baru untuk keperluan administrasi penitipan barang.",
                style = MaterialTheme.typography.bodyMedium
            )

            // Input Nomor RM (REQ-197 & Tabel 2)
            OutlinedTextField(
                value = uiState.rm_number,
                onValueChange = { viewModel.updateUiState(uiState.copy(rm_number = it)) },
                label = { Text("Nomor Rekam Medis (Wajib)") },
                placeholder = { Text("Contoh: RM-12345") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Input Nama Pasien (Tabel 2) [cite: 400]
            OutlinedTextField(
                value = uiState.name,
                onValueChange = { viewModel.updateUiState(uiState.copy(name = it)) },
                label = { Text("Nama Lengkap Pasien (Wajib)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Input Informasi Ruangan (Tabel 2) [cite: 400]
            OutlinedTextField(
                value = uiState.room_info,
                onValueChange = { viewModel.updateUiState(uiState.copy(room_info = it)) },
                label = { Text("Informasi Ruang Rawat (Opsional)") },
                placeholder = { Text("Contoh: Bangsal Merpati") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Menampilkan pesan error jika ada [cite: 282]
            if (uiState.error != null) {
                Text(
                    text = uiState.error!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelMedium
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Tombol Simpan Data Pasien
            Button(
                onClick = { viewModel.savePatient(onSuccess = navigateBack) },
                modifier = Modifier.fillMaxWidth(),
                // Tombol aktif hanya jika kolom wajib diisi [cite: 257]
                enabled = uiState.rm_number.isNotBlank() && uiState.name.isNotBlank() && !uiState.isSaving
            ) {
                if (uiState.isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Simpan Data Pasien")
                }
            }
        }
    }
}