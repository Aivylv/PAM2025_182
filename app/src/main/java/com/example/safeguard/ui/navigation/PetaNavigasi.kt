package com.example.safeguard.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.safeguard.repository.UserPreferences
import com.example.safeguard.ui.screen.DashboardScreen
import com.example.safeguard.ui.screen.DetailItemScreen
import com.example.safeguard.ui.screen.LoginScreen
import com.example.safeguard.ui.screen.RegisterScreen
import com.example.safeguard.ui.screen.TambahBarangScreen
import com.example.safeguard.ui.screen.TambahPasienScreen
import com.example.safeguard.ui.viewmodel.EditItemViewModel
import com.example.safeguard.ui.viewmodel.LoginViewModel
import com.example.safeguard.ui.viewmodel.PasienViewModel
import com.example.safeguard.ui.viewmodel.PenyediaViewModel
import com.example.safeguard.ui.viewmodel.RegisterViewModel
import com.example.safeguard.ui.viewmodel.TambahBarangViewModel
import kotlinx.coroutines.launch
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.safeguard.ui.screen.HelpScreen
import com.example.safeguard.ui.screen.OnboardingScreen

@Composable
fun PetaNavigasi(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val context = LocalContext.current
    val userPreferences = UserPreferences(context)
    val scope = rememberCoroutineScope()
    val isFirstRun by userPreferences.isFirstRun.collectAsState(initial = true)
    val startDest = if (isFirstRun) DestinasiOnboarding.route else DestinasiLogin.route
    NavHost(
        navController = navController,
        startDestination = startDest,
        modifier = modifier
    ) {
        //SCREEN ONBOARDING
        composable(route = DestinasiOnboarding.route) {
            OnboardingScreen(
                onFinish = {
                    navController.navigate(DestinasiLogin.route) {
                        popUpTo(DestinasiOnboarding.route) { inclusive = true }
                    }
                }
            )
        }

        //SCREEN BANTUAN
        composable(route = DestinasiHelp.route) {
            HelpScreen(
                navigateBack = { navController.popBackStack() }
            )
        }

        //LOGIN SCREEN
        composable(route = DestinasiLogin.route) {
            val viewModel: LoginViewModel = viewModel(factory = PenyediaViewModel.Factory)
            LoginScreen(
                viewModel = viewModel,
                onLoginSuccess = {
                    navController.navigate(DestinasiHome.route) {
                        popUpTo(DestinasiLogin.route) { inclusive = true }
                    }
                },
                onRegisterNav = {
                    navController.navigate(DestinasiRegister.route)
                }
            )
        }

        //REGISTER SCREEN
        composable(route = DestinasiRegister.route) {
            val viewModel: RegisterViewModel = viewModel(factory = PenyediaViewModel.Factory)
            RegisterScreen(
                viewModel = viewModel,
                navigateBack = { navController.popBackStack() }
            )
        }

        //DASHBOARD (HOME)
        composable(route = DestinasiHome.route) {
            DashboardScreen(
                navigateToItemEntry = { navController.navigate(DestinasiEntry.route) },
                navigateToPatientEntry = { navController.navigate("tambah_pasien") },
                navigateToHelp = { navController.navigate(DestinasiHelp.route) },
                onItemClick = { itemId ->
                    navController.navigate("${DestinasiDetail.route}/$itemId")
                },
                //LOGOUT
                onLogout = {
                    scope.launch {
                        userPreferences.clearSession()
                        navController.navigate(DestinasiLogin.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }
            )
        }

        //TAMBAH PASIEN
        composable(route = "tambah_pasien") {
            val viewModel: PasienViewModel = viewModel(factory = PenyediaViewModel.Factory)
            TambahPasienScreen(
                viewModel = viewModel,
                navigateBack = { navController.popBackStack() }
            )
        }

        //TAMBAH BARANG (ENTRY)
        composable(route = DestinasiEntry.route) {
            val viewModel: TambahBarangViewModel = viewModel(factory = PenyediaViewModel.Factory)
            TambahBarangScreen(
                viewModel = viewModel,
                navigateBack = { navController.popBackStack() }
            )
        }

        //DETAIL BARANG
        composable(
            route = "${DestinasiDetail.route}/{itemId}",
            arguments = listOf(navArgument("itemId") { type = NavType.IntType })
        ) {
            val viewModel: EditItemViewModel = viewModel(factory = PenyediaViewModel.Factory)
            DetailItemScreen(
                viewModel = viewModel,
                navigateBack = { navController.popBackStack() }
            )
        }
    }
}