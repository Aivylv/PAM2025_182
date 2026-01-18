package com.example.safeguard.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.safeguard.ui.viewmodel.EditItemViewModel
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import kotlinx.coroutines.launch
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailItemScreen(
    viewModel: EditItemViewModel,
    navigateBack: () -> Unit
) {
    val uiState = viewModel.uiState
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var showDeleteDialog by remember { mutableStateOf(false) }
    var showReturnDialog by remember { mutableStateOf(false) }
    var isEditMode by remember { mutableStateOf(false) }

    var editName by remember { mutableStateOf("") }
    var editCondition by remember { mutableStateOf("") }
    var receiverName by remember { mutableStateOf("") }

    LaunchedEffect(uiState.item) {
        editName = uiState.item.item_name
        editCondition = uiState.item.condition
    }

    if (showReturnDialog) {
        AlertDialog(
            onDismissRequest = { showReturnDialog = false },
            title = { Text("Konfirmasi Pengembalian") },
            text = {
                Column {
                    Text("Barang akan dikembalikan ke keluarga/pasien.")
                    OutlinedTextField(
                        value = receiverName,
                        onValueChange = { receiverName = it },
                        label = { Text("Nama Penerima (Wajib)") },
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (receiverName.isNotBlank()) {
                        viewModel.returnItem(
                            receiverName = receiverName,
                            onSuccess = {
                                showReturnDialog = false
                                scope.launch {
                                    snackbarHostState.showSnackbar("Status Berhasil Diperbarui: Dikembalikan")
                                }
                            },
                            onError = { pesanError ->
                                scope.launch {
                                    snackbarHostState.showSnackbar(pesanError)
                                }
                            }
                        )
                    } else {
                        scope.launch { snackbarHostState.showSnackbar("Nama penerima wajib diisi!") }
                    }
                }) { Text("Simpan Status") }
            },
            dismissButton = { TextButton(onClick = { showReturnDialog = false }) { Text("Batal") } }
        )
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Apakah Anda yakin?") },
            text = { Text("Data yang dihapus tidak dapat dikembalikan. Apakah Anda yakin ingin menghapus data ini secara permanen?") },
            confirmButton = {
                Button(
                    onClick = { viewModel.deleteItem { navigateBack() } },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) { Text("Ya") }
            },
            dismissButton = { TextButton(onClick = { showDeleteDialog = false }) { Text("Batal") } }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(if (isEditMode) "Edit Barang" else "Detail Barang") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                },
                actions = {
                    if (uiState.item.status == "Disimpan" && !isEditMode) {
                        IconButton(onClick = { isEditMode = true }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit Data")
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        } else {
            val item = uiState.item

            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                if (isEditMode) {
                    OutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it },
                        label = { Text("Nama Barang") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = editCondition,
                        onValueChange = { editCondition = it },
                        label = { Text("Kondisi Fisik") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(16.dp))
                    Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                        TextButton(onClick = { isEditMode = false }) { Text("Batal") }
                        Spacer(Modifier.width(8.dp))
                        Button(onClick = {
                            if (editName.isBlank()) {
                                scope.launch { snackbarHostState.showSnackbar("Nama barang tidak boleh kosong!") }
                            } else {
                                viewModel.updateItemData(
                                    newName = editName,
                                    newCondition = editCondition,
                                    onSuccess = {
                                        isEditMode = false
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Data Berhasil Disimpan")
                                        }
                                    },
                                    onError = { pesanError ->
                                        scope.launch {
                                            snackbarHostState.showSnackbar(pesanError)
                                        }
                                    }
                                )
                            }
                        }) { Text("Simpan Perubahan") }
                    }

                } else {
                    if (!item.photo_path.isNullOrEmpty()) {
                        AsyncImage(
                            model = "http://10.0.2.2:3000/uploads/${item.photo_path}", // Sesuaikan IP Backend Anda
                            contentDescription = "Foto Bukti Barang",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp) // Sedikit diperbesar agar jelas
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .padding(bottom = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Tidak ada foto bukti", color = Color.Gray)
                        }
                    }
                    DetailRow("Nama Barang", item.item_name)
                    DetailRow("Pemilik", "${item.patient_name ?: "-"} (${item.rm_number ?: "-"})")
                    DetailRow("Kondisi", item.condition)
                    DetailRow("Waktu Masuk", item.entry_date ?: "-")

                    Text("Status Barang", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                    Text(
                        text = item.status,
                        style = MaterialTheme.typography.headlineSmall,
                        color = if(item.status == "Dikembalikan") Color(0xFF4CAF50) else MaterialTheme.colorScheme.primary
                    )
                    Divider(Modifier.padding(vertical = 8.dp))

                    if (item.status == "Dikembalikan") {
                        DetailRow("Diterima Oleh", item.receiver ?: "-")
                        DetailRow("Tanggal Dikembalikan", item.return_date ?: "Baru saja")
                    }

                    Spacer(Modifier.height(24.dp))

                    if (item.status == "Disimpan") {
                        Button(
                            onClick = { showReturnDialog = true },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) { Text("Kembalikan Barang") }
                    } else {
                        OutlinedCard(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                "Barang sudah dikembalikan.",
                                modifier = Modifier.padding(16.dp),
                                color = Color(0xFF2E7D32)
                            )
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    OutlinedButton(
                        onClick = { showDeleteDialog = true },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Hapus Data")
                    }
                }
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