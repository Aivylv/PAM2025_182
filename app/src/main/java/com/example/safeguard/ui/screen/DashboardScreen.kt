package com.example.safeguard.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.safeguard.modeldata.Item
import com.example.safeguard.ui.viewmodel.DashboardUiState
import com.example.safeguard.ui.viewmodel.DashboardViewModel
import com.example.safeguard.ui.viewmodel.PenyediaViewModel
import androidx.compose.material.icons.filled.Info
import com.example.safeguard.ui.navigation.DestinasiHelp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navigateToItemEntry: () -> Unit,
    navigateToPatientEntry: () -> Unit,
    navigateToHelp: () -> Unit,
    onItemClick: (Int) -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    LaunchedEffect(Unit) {
        viewModel.getDashboardUiState()
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("Dashboard SafeGuard") },
                    actions = {
                        IconButton(onClick = navigateToHelp) {
                            Icon(Icons.Default.Info, contentDescription = "Bantuan")
                        }
                        IconButton(onClick = navigateToPatientEntry) {
                            Icon(Icons.Default.PersonAdd, contentDescription = "Tambah Pasien")
                        }
                        IconButton(onClick = onLogout) {
                            Icon(Icons.Default.ExitToApp, tint = MaterialTheme.colorScheme.error, contentDescription = "Logout")
                        }
                    }
                )
                OutlinedTextField(
                    value = viewModel.searchQuery,
                    onValueChange = { viewModel.updateSearchQuery(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    placeholder = { Text("Cari barang atau nama pasien...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = {
                        if (viewModel.searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.updateSearchQuery("") }) {
                                Icon(Icons.Default.Close, contentDescription = "Hapus")
                            }
                        }
                    },
                    singleLine = true
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = navigateToItemEntry) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Barang")
            }
        }
    ) { innerPadding ->
        DashboardBody(
            uiState = viewModel.dashboardUiState,
            onItemClick = onItemClick,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun DashboardBody(
    uiState: DashboardUiState,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        is DashboardUiState.Loading -> Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        is DashboardUiState.Success -> {
            if (uiState.items.isEmpty()) {
                Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Belum ada barang titipan") }
            } else {
                ListItemList(items = uiState.items, onItemClick = onItemClick, modifier = modifier)
            }
        }
        is DashboardUiState.Error -> Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Gagal memuat data") }
    }
}

@Composable
fun ListItemList(
    items: List<Item>,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp)
    ) {
        items(items) { item ->
            ItemCard(
                item = item,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .clickable { onItemClick(item.item_id ?: 0) }
            )
        }
    }
}

@Composable
fun ItemCard(
    item: Item,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = item.item_name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Milik: ${item.patient_name ?: "Pasien Umum"}",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.entry_date ?: "-",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )

                Spacer(Modifier.weight(1f))

                Text(
                    text = item.status,
                    style = MaterialTheme.typography.labelMedium,
                    color = if(item.status == "Dikembalikan") Color(0xFF4CAF50) else MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}