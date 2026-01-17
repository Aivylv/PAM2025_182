package com.example.safeguard.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.safeguard.ui.viewmodel.EditItemViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailItemScreen(
    viewModel: EditItemViewModel,
    navigateBack: () -> Unit
) {
    val uiState = viewModel.uiState
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showReturnDialog by remember { mutableStateOf(false) }
    var receiverName by remember { mutableStateOf("") }

    // Dialog Konfirmasi Pengembalian
    if (showReturnDialog) {
        AlertDialog(
            onDismissRequest = { showReturnDialog = false },
            title = { Text("Konfirmasi Pengembalian") },
            text = {
                Column {
                    Text("Barang akan dikembalikan. Masukkan nama penerima:")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = receiverName,
                        onValueChange = { receiverName = it },
                        label = { Text("Nama Penerima") }
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (receiverName.isNotBlank()) {
                        viewModel.updateStatus("Dikembalikan", receiverName, navigateBack)
                        showReturnDialog = false
                    }
                }) { Text("Simpan") }
            },
            dismissButton = {
                TextButton(onClick = { showReturnDialog = false }) { Text("Batal") }
            }
        )
    }

    // Dialog Konfirmasi Hapus
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Konfirmasi Hapus") },
            text = { Text("Apakah Anda yakin ingin menghapus data ini?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteItem {
                        showDeleteDialog = false
                        navigateBack()
                    }
                }) { Text("Ya, Hapus") }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("Batal") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Barang") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { innerPadding ->
        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (uiState.isError) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = uiState.errorMessage, color = MaterialTheme.colorScheme.error)
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = { viewModel.fetchItemDetails() }) { Text("Coba Lagi") }
                }
            }
        } else {
            val item = uiState.item

            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                DetailRow(label = "Nama Barang", value = item.item_name)
                DetailRow(label = "Pemilik (Pasien)", value = item.patient_name ?: "Tidak Diketahui")
                DetailRow(label = "Nomor RM", value = item.rm_number ?: "-")
                DetailRow(label = "Kondisi", value = item.condition)
                DetailRow(label = "Status Saat Ini", value = item.status)
                DetailRow(label = "Tanggal Masuk", value = item.entry_date ?: "-")

                if (item.status == "Dikembalikan") {
                    DetailRow(label = "Diterima Oleh", value = item.receiver ?: "-")
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (item.status == "Disimpan") {
                    Button(
                        onClick = { showReturnDialog = true },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("Kembalikan Barang") }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = { showDeleteDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) { Text("Hapus Data") }
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(text = label, style = MaterialTheme.typography.labelMedium, color = Color.Gray)
        Text(text = value, style = MaterialTheme.typography.bodyLarge)
        Divider(modifier = Modifier.padding(top = 8.dp))
    }
}