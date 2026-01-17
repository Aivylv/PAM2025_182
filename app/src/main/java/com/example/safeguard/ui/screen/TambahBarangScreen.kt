package com.example.safeguard.ui.screen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
    var expanded by remember { mutableStateOf(false) }

    //Launcher untuk mengambil foto (REQ-252)
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> viewModel.onImageSelected(uri) }

    Scaffold(
        topBar = { TopAppBar(
            title = {
                Text("Input Barang Baru") },
            navigationIcon = {
                IconButton(onClick = navigateBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                }
            })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Dropdown Nama Pasien (REQ-14) - Pengganti Input ID Manual
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = uiState.selectedPatientName,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Pilih Pemilik Barang (Wajib)") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    uiState.listPasien.forEach { pasien ->
                        DropdownMenuItem(
                            text = { Text("${pasien.name} (${pasien.rm_number})") },
                            onClick = {
                                viewModel.updateUiState(uiState.copy(
                                    patient_id = pasien.patient_id,
                                    selectedPatientName = pasien.name
                                ))
                                expanded = false
                            }
                        )
                    }
                }
            }

            // 2. Input Nama Barang (REQ-14)
            OutlinedTextField(
                value = uiState.item_name,
                onValueChange = { viewModel.updateUiState(uiState.copy(item_name = it)) },
                label = { Text("Nama Barang (Wajib)") },
                modifier = Modifier.fillMaxWidth()
            )

            // 3. Input Kondisi (REQ-14)
            OutlinedTextField(
                value = uiState.condition,
                onValueChange = { viewModel.updateUiState(uiState.copy(condition = it)) },
                label = { Text("Kondisi Fisik Barang") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            // 4. Fitur Foto Barang (REQ-252)
            OutlinedButton(
                onClick = { imagePickerLauncher.launch("image/*") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.CameraAlt, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(if (uiState.imageUri == null) "Ambil Foto Bukti (Opsional)" else "Foto Berhasil Dipilih")
            }

            if (uiState.error != null) {
                Text(text = uiState.error!!, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.weight(1f))

            // 5. Tombol Simpan (REQ-253)
            Button(
                onClick = { viewModel.saveItem(navigateBack) },
                enabled = uiState.isEntryValid && !uiState.isSaving,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (uiState.isSaving) CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                else Text("Simpan Data")
            }
        }
    }
}