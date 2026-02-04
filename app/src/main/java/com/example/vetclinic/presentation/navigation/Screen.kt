package com.example.vetclinic.presentation.navigation

sealed class Screen(val route: String) {
    object MedicalRecord : Screen("medical_record") {
        fun createRoute(patientId: Long = 0) =
            if (patientId == 0L) "medical_record"
            else "medical_record/$patientId"
    }

    object PatientList : Screen("patient_list")

    object PatientEdit : Screen("patient_edit") {
        const val ARG_PATIENT_ID = "patient_id"
        const val ROUTE_WITH_ARGS = "patient_edit/{$ARG_PATIENT_ID}"

        fun createRoute(patientId: Long = 0) = "patient_edit/$patientId"
    }

    object VisitEdit : Screen("visit_edit") {
        const val ARG_PATIENT_ID = "patient_id"
        const val ARG_VISIT_ID = "visit_id"
        const val ROUTE_WITH_ARGS = "visit_edit/{$ARG_PATIENT_ID}/{$ARG_VISIT_ID}"

        fun createRoute(patientId: Long = 0, visitId: Long = 0) = "visit_edit/$patientId/$visitId"
    }
}