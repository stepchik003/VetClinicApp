package com.example.vetclinic.presentation.screens

import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.vetclinic.R
import com.example.vetclinic.domain.model.Patient
import com.example.vetclinic.domain.model.UiState
import com.example.vetclinic.domain.model.Visit
import com.example.vetclinic.presentation.utils.StringUtils
import com.example.vetclinic.presentation.viewmodel.MedicalRecordViewModel
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicalRecordScreen(
    viewModel: MedicalRecordViewModel,
    onBackClick: () -> Unit,
    onChangePatientClick: () -> Unit,
    onAddVisitClick: (Long) -> Unit,
    onEditVisitClick: (Long, Long) -> Unit
) {
    val patientState by viewModel.patientState.collectAsState()
    val visitsState by viewModel.visitsState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.medical_record)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(
                                R.string.back
                            )
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            when (patientState) {
                is UiState.Success -> {
                    val patient = (patientState as UiState.Success<Patient>).data
                    FloatingActionButton(
                        onClick = { onAddVisitClick(patient.id) }
                    ) {
                        Icon(
                            Icons.Filled.Add,
                            contentDescription = stringResource(R.string.add_visit)
                        )
                    }
                }

                else -> {}
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            PatientInfoSection(
                patientState = patientState,
                onChangePatientClick = onChangePatientClick
            )

            HorizontalDivider()

            VisitsListSection(
                visitsState = visitsState,
                onEditVisitClick = onEditVisitClick,
                onDeleteVisitClick = { viewModel.deleteVisit(it) }
            )
        }
    }
}

@Composable
private fun PatientInfoSection(
    patientState: UiState<Patient>,
    onChangePatientClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.selected_patient),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Button(onClick = onChangePatientClick) {
                    Text(text = stringResource(R.string.change))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (patientState) {
                is UiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is UiState.Success -> {
                    val patient = patientState.data
                    PatientInfoContent(patient = patient)
                }

                is UiState.Error -> {
                    Text(
                        text = patientState.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun PatientInfoContent(patient: Patient) {
    Column {
        InfoRow(label = stringResource(R.string.name), value = patient.name)
        InfoRow(label = stringResource(R.string.species), value = patient.species)
        patient.breed?.let { InfoRow(label = stringResource(R.string.breed), value = it) }
        patient.age?.let {
            InfoRow(
                label = stringResource(R.string.age),
                value = StringUtils.getAgeString(it)
            )
        }
        patient.weight?.let {
            InfoRow(
                label = stringResource(R.string.weight),
                value = StringUtils.getWeightString(it)
            )
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = "$label:",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.width(100.dp)
        )
        Text(text = value)
    }
}

@Composable
private fun VisitsListSection(
    visitsState: UiState<List<Visit>>,
    onEditVisitClick: (Long, Long) -> Unit,
    onDeleteVisitClick: (Visit) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = stringResource(R.string.visits),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        when (visitsState) {
            is UiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is UiState.Success -> {
                val visits = visitsState.data
                if (visits.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.no_visits_yet),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    LazyColumn {
                        items(visits) { visit ->
                            VisitCard(
                                visit = visit,
                                onEditClick = { onEditVisitClick(visit.patientId, visit.id) },
                                onDeleteClick = { onDeleteVisitClick(visit) }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }

            is UiState.Error -> {
                Text(
                    text = visitsState.message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun VisitCard(
    visit: Visit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(
                        Date(
                            visit.date
                        )
                    ),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Row {
                    IconButton(onClick = onEditClick) {
                        Icon(Icons.Filled.Edit, contentDescription = stringResource(R.string.edit))
                    }
                    IconButton(onClick = onDeleteClick) {
                        Icon(
                            Icons.Filled.Delete,
                            contentDescription = stringResource(R.string.delete)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            visit.doctorName?.let {
                InfoRow(label = stringResource(R.string.doctor), value = it)
            }

            visit.diagnosis?.let {
                InfoRow(label = stringResource(R.string.diagnosis), value = it)
            }
        }
    }
}