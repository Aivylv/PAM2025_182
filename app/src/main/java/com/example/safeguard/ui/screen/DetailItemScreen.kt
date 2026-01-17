package com.example.safeguard.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.safeguard.ui.viewmodel.EditItemViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailItemScreen(
    viewModel: EditItemViewModel,
    navigateBack: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showReturnDialog by remember { mutableStateOf(false) }
    var receiverName by remember { mutableStateOf("") }

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
                Button(
                    onClick = {
                        if (receiverName.isNotBlank()) {
                            viewModel.updateStatus("Dikembalikan", receiverName, navigateBack)
                            showReturnDialog = false
                        }
                    }
                ) { Text("Simpan") }
            },
            dismissButton = {
                TextButton(onClick = { showReturnDialog = false }) { Text("Batal") }
            }
        )
    }

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
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Text(text = "Barang: ${viewModel.itemState.item_name}", style = MaterialTheme.typography.headlineMedium)
            Text(text = "Status: ${viewModel.itemState.status}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Kondisi: ${viewModel.itemState.condition}", style = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { showReturnDialog = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Kembalikan Barang")
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = { showDeleteDialog = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Hapus Data")
            }
        }
    }
}