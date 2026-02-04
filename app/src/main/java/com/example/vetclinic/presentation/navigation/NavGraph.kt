package com.example.vetclinic.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.vetclinic.presentation.screens.MedicalRecordScreen
import com.example.vetclinic.presentation.screens.PatientEditScreen
import com.example.vetclinic.presentation.screens.PatientListScreen
import com.example.vetclinic.presentation.screens.VisitEditScreen
import com.example.vetclinic.presentation.viewmodel.MedicalRecordViewModel
import com.example.vetclinic.presentation.viewmodel.PatientListViewModel

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.PatientList.route
    ) {
        composable(Screen.PatientList.route) {
            val viewModel = hiltViewModel<PatientListViewModel>()
            PatientListScreen(
                viewModel = viewModel,
                onPatientClick = { patientId ->
                    navController.navigate(
                        Screen.MedicalRecord.createRoute(patientId = patientId)
                    )
                },
                onAddPatientClick = {
                    navController.navigate(
                        Screen.PatientEdit.createRoute()
                    )
                },
                onEditPatientClick = { patientId ->
                    navController.navigate(
                        Screen.PatientEdit.createRoute(patientId = patientId)
                    )
                }
            )
        }

        composable(
            route = "medical_record/{patientId}",
            arguments = listOf(
                navArgument("patientId") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            val patientId = backStackEntry.arguments?.getLong("patientId") ?: 0L
            val viewModel = hiltViewModel<MedicalRecordViewModel>()

            viewModel.setPatientId(patientId)

            MedicalRecordScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
                onChangePatientClick = {
                    navController.navigate(Screen.PatientList.route) {
                        popUpTo(Screen.MedicalRecord.route) { inclusive = true }
                    }
                },
                onAddVisitClick = { patientId ->
                    navController.navigate(Screen.VisitEdit.createRoute(patientId = patientId))
                },
                onEditVisitClick = { patientId, visitId ->
                    navController.navigate(
                        Screen.VisitEdit.createRoute(
                            patientId = patientId,
                            visitId = visitId
                        )
                    )
                }
            )
        }

        composable(
            route = Screen.PatientEdit.ROUTE_WITH_ARGS,
            arguments = listOf(
                navArgument(Screen.PatientEdit.ARG_PATIENT_ID) {
                    type = NavType.LongType
                    defaultValue = 0L
                }
            )
        ) { backStackEntry ->
            val patientId =
                backStackEntry.arguments?.getLong(Screen.PatientEdit.ARG_PATIENT_ID) ?: 0L
            PatientEditScreen(
                patientId = patientId,
                onSaveComplete = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.VisitEdit.ROUTE_WITH_ARGS,
            arguments = listOf(
                navArgument(Screen.VisitEdit.ARG_PATIENT_ID) {
                    type = NavType.LongType
                },
                navArgument(Screen.VisitEdit.ARG_VISIT_ID) {
                    type = NavType.LongType
                    defaultValue = 0L
                }
            )
        ) { backStackEntry ->
            val patientId = backStackEntry.arguments?.getLong(Screen.VisitEdit.ARG_PATIENT_ID) ?: 0L
            val visitId = backStackEntry.arguments?.getLong(Screen.VisitEdit.ARG_VISIT_ID) ?: 0L
            VisitEditScreen(
                patientId = patientId,
                visitId = visitId,
                onSaveComplete = { navController.popBackStack() }
            )
        }
    }
}