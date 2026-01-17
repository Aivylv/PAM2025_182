package com.example.safeguard.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.safeguard.ui.viewmodel.EditItemViewModel

@Composable
fun DetailItemScreen(
    viewModel: EditItemViewModel,
    navigateBack: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showReturnDialog by remember { mutableStateOf(false) } // Dialog Nama Penerima
    var receiverName by remember { mutableStateOf("") }

    // Dialog Input Penerima (Wajib sesuai SRS Safety Requirements)
    if (showReturnDialog) {
        AlertDialog(
            onDismissRequest = { showReturnDialog = false },
            title = { Text("Konfirmasi Pengembalian") },
            text = {
                Column {
                    Text("Barang akan dikembalikan. Masukkan nama penerima (Keluarga/Pasien):")
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
                        // REQ-330: Validasi input tidak boleh kosong
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

    // Dialog Konfirmasi Hapus (Sama seperti sebelumnya)
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Konfirmasi Hapus") },
            text = { Text("Apakah Anda yakin ingin menghapus data ini secara permanen?") },
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

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Barang: ${viewModel.itemState.item_name}", style = MaterialTheme.typography.headlineMedium)
        // ... (Tampilkan detail lainnya seperti kondisi, nama pasien) ...

        Spacer(modifier = Modifier.height(24.dp))

        // Tombol Update Status
        Button(
            onClick = { showReturnDialog = true }, // Munculkan dialog, jangan langsung update!
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