package com.example.safeguard.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.safeguard.ui.screen.DashboardScreen
import com.example.safeguard.ui.screen.DetailItemScreen
import com.example.safeguard.ui.screen.LoginScreen
import com.example.safeguard.ui.screen.RegisterScreen
import com.example.safeguard.ui.screen.TambahBarangScreen
import com.example.safeguard.ui.viewmodel.DashboardViewModel
import com.example.safeguard.ui.viewmodel.EditItemViewModel
import com.example.safeguard.ui.viewmodel.LoginViewModel
import com.example.safeguard.ui.viewmodel.PenyediaViewModel
import com.example.safeguard.ui.viewmodel.RegisterViewModel
import com.example.safeguard.ui.viewmodel.TambahBarangViewModel
import com.example.safeguard.ui.screen.TambahPasienScreen
import com.example.safeguard.ui.viewmodel.PasienViewModel

@Composable
fun PetaNavigasi(
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            val viewModel: LoginViewModel = viewModel(factory = PenyediaViewModel.Factory)
            LoginScreen(
                viewModel = viewModel,
                onLoginSuccess = {
                    // Hapus history login agar user tidak bisa back ke login (Security Requirement)
                    navController.navigate("dashboard") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onRegisterNav = { navController.navigate("register") }
            )
        }

        composable("register") {
            val viewModel: RegisterViewModel = viewModel(factory = PenyediaViewModel.Factory)
            RegisterScreen(
                viewModel = viewModel,
                navigateBack = { navController.popBackStack() }
            )
        }

        composable("dashboard") {
            val viewModel: DashboardViewModel = viewModel(factory = PenyediaViewModel.Factory)
            DashboardScreen(
                viewModel = viewModel,
                onFabClick = { navController.navigate("tambah_barang") },
                onItemClick = { itemId -> navController.navigate("detail_barang/$itemId") }
            )
        }

        composable("tambah_barang") {
            val viewModel: TambahBarangViewModel = viewModel(factory = PenyediaViewModel.Factory)
            TambahBarangScreen(
                viewModel = viewModel,
                navigateBack = { navController.popBackStack() }
            )
        }

        //rute detail untuk fitur Update/Delete
        composable(
            route = "detail_barang/{itemId}",
            arguments = listOf(navArgument("itemId") { type = NavType.IntType })
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getInt("itemId")
            val viewModel: EditItemViewModel = viewModel(factory = PenyediaViewModel.Factory)

            DetailItemScreen(
                viewModel = viewModel,
                navigateBack = { navController.popBackStack() }
            )
        }
    }
}