package com.example.safeguard.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpScreen(
    navigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pusat Bantuan") },
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
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Info, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("SafeGuard RSJ", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    }
                    Text("Versi Aplikasi: 1.0.0 (Rilis Stabil)", style = MaterialTheme.typography.bodyMedium)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Kamus Arti Tombol (Ikon)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text("Berikut adalah arti dari simbol-simbol yang ada di layar Anda:", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

            Spacer(modifier = Modifier.height(12.dp))

            IconDefinitionItem(
                icon = Icons.Default.PersonAdd,
                label = "Tambah Pasien",
                desc = "Letak: Pojok Kanan Atas.\nFungsi: Mendaftarkan nama & No.RM pasien baru. Wajib diisi pertama kali."
            )
            IconDefinitionItem(
                icon = Icons.Default.Add,
                label = "Tambah Barang",
                desc = "Letak: Tombol Bulat Besar di Bawah (+).\nFungsi: Membuka formulir penitipan barang."
            )
            IconDefinitionItem(
                icon = Icons.Default.Search,
                label = "Cari Data",
                desc = "Letak: Kotak di bagian atas.\nFungsi: Ketik nama pasien untuk menemukan barangnya dengan cepat."
            )
            IconDefinitionItem(
                icon = Icons.Default.ExitToApp,
                label = "Keluar (Logout)",
                desc = "Letak: Pojok Kanan Atas.\nFungsi: Keluar dari akun saat pergantian shift jaga."
            )

            Spacer(modifier = Modifier.height(24.dp))
            Divider()
            Spacer(modifier = Modifier.height(16.dp))

            Text("Masalah & Solusi", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            HelpAccordionItem(
                question = "Kenapa tombol 'Simpan' tidak bisa ditekan (warna abu-abu)?",
                answer = "Aplikasi mengunci tombol jika data belum lengkap. Pastikan:\n" +
                        "1. Anda sudah memilih 'Nama Pasien'.\n" +
                        "2. Anda sudah mengetik 'Nama Barang'.\n" +
                        "Jika keduanya terisi, tombol akan otomatis menyala biru."
            )

            HelpAccordionItem(
                question = "Saya mau titip barang, tapi Nama Pasien tidak ada di daftar.",
                answer = "Itu artinya pasien belum didaftarkan. Silakan kembali ke menu utama, lalu klik ikon 'Orang+' (Pojok Kanan Atas) untuk mendaftar pasien tersebut."
            )

            HelpAccordionItem(
                question = "Bagaimana cara mengembalikan barang ke keluarga?",
                answer = "1. Klik barang di daftar utama.\n" +
                        "2. Klik tombol 'Detail'.\n" +
                        "3. Klik tombol 'Edit/Kembalikan'.\n" +
                        "4. PENTING: Isi kolom 'Nama Penerima' (Wajib) agar status bisa diubah menjadi 'Dikembalikan'."
            )

            Spacer(modifier = Modifier.height(24.dp))
            Text("Dukungan Teknis", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text("Jika aplikasi error atau tidak bisa dibuka, hubungi:", style = MaterialTheme.typography.bodyMedium)
            Text("IT Support RSJ (Ext. 102)", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun IconDefinitionItem(icon: ImageVector, label: String, desc: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(text = label, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = desc, style = MaterialTheme.typography.bodyMedium, lineHeight = 20.sp)
            }
        }
    }
}

@Composable
fun HelpAccordionItem(question: String, answer: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = question, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            }
            Divider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f)
            )
            Text(text = answer, style = MaterialTheme.typography.bodyMedium)
        }
    }
}