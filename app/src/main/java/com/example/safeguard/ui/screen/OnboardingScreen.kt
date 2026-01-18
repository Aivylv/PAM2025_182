package com.example.safeguard.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.safeguard.repository.UserPreferences
import kotlinx.coroutines.launch

data class OnboardingPage(
    val title: String,
    val description: String,
    val icon: ImageVector
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    onFinish: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val userPreferences = UserPreferences(context)

    val pages = listOf(
        OnboardingPage(
            title = "Selamat Datang di SafeGuard",
            description = "Aplikasi SafeGuard RSJ hadir untuk menggantikan buku catatan manual.\n\nKini mencatat barang titipan pasien jadi lebih rapi, aman, dan data tidak akan hilang.",
            icon = Icons.Default.Security
        ),
        OnboardingPage(
            title = "Kenali Tombol Aplikasi",
            description = "Agar tidak salah pencet, hafalkan ikon ini:\n\n" +
                    "• Ikon Orang+ (Pojok Kanan Atas): Untuk Mendaftarkan Pasien Baru.\n\n" +
                    "• Ikon + Besar (Pojok Bawah): Untuk Menitipkan Barang.\n\n" +
                    "• Ikon Tanda Tanya (?): Untuk Bantuan.",
            icon = Icons.Default.Info
        ),
        OnboardingPage(
            title = "Alur Kerja Wajib",
            description = "Sistem ini memiliki aturan ketat demi keamanan:\n\n" +
                    "1. WAJIB tekan 'Ikon Orang+' dulu untuk mendaftarkan nama pasien.\n" +
                    "2. BARU tekan tombol '+ Besar' untuk menitipkan barang.\n\n" +
                    "Anda tidak bisa titip barang jika pasien belum didaftarkan!",
            icon = Icons.Default.PersonAdd
        ),
        OnboardingPage(
            title = "Siap Bertugas!",
            description = "Jangan lupa gunakan kamera HP Anda lewat aplikasi ini untuk memfoto barang sebagai bukti.\n\nTekan tombol di bawah untuk mulai bekerja.",
            icon = Icons.Default.AssignmentTurnedIn
        )
    )

    val pagerState = rememberPagerState(pageCount = { pages.size })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            if (pagerState.currentPage < pages.size - 1) {
                TextButton(onClick = {
                    scope.launch {
                        userPreferences.setFirstRunCompleted()
                        onFinish()
                    }
                }) {
                    Text("Lewati Tutorial", color = Color.Gray)
                }
            } else {
                Spacer(modifier = Modifier.height(48.dp))
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
        ) { position ->
            PagerScreen(page = pages[position])
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier
                .height(60.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(pages.size) { iteration ->
                    val color = if (pagerState.currentPage == iteration)
                        MaterialTheme.colorScheme.primary else Color.LightGray
                    val width = if (pagerState.currentPage == iteration) 24.dp else 10.dp

                    Box(
                        modifier = Modifier
                            .padding(2.dp)
                            .clip(CircleShape)
                            .background(color)
                            .height(10.dp)
                            .width(width)
                    )
                }
            }

            Button(
                onClick = {
                    scope.launch {
                        if (pagerState.currentPage < pages.size - 1) {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        } else {
                            userPreferences.setFirstRunCompleted()
                            onFinish()
                        }
                    }
                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.height(48.dp)
            ) {
                Text(
                    text = if (pagerState.currentPage == pages.size - 1) "Mulai Bertugas" else "Lanjut",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun PagerScreen(page: OnboardingPage) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = page.icon,
            contentDescription = null,
            modifier = Modifier
                .size(140.dp)
                .padding(bottom = 32.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Text(
            text = page.title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = page.description,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 24.sp
        )
    }
}