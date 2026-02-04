package com.example.vetclinic.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.vetclinic.R
import com.example.vetclinic.presentation.viewmodel.VisitEditViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun VisitEditScreen(
    patientId: Long,
    visitId: Long,
    onSaveComplete: () -> Unit
) {
    val viewModel = hiltViewModel<VisitEditViewModel>()

    LaunchedEffect(patientId, visitId) {
        viewModel.loadVisit(patientId, if (visitId == 0L) null else visitId)
    }

    VisitEditScreenContent(
        viewModel = viewModel,
        visitId = visitId,
        onSaveComplete = onSaveComplete
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun VisitEditScreenContent(
    viewModel: VisitEditViewModel,
    visitId: Long,
    onSaveComplete: () -> Unit
) {
    val date by viewModel.date.collectAsState()
    val doctorName by viewModel.doctorName.collectAsState()
    val diagnosis by viewModel.diagnosis.collectAsState()
    val saveEnabled by viewModel.saveEnabled.collectAsState()
    val savingState by viewModel.savingState.collectAsState()

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (visitId == 0L) stringResource(R.string.new_visit) else stringResource(
                            R.string.edit_visit
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onSaveComplete) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                actions = {
                    if (savingState) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        TextButton(
                            onClick = {
                                coroutineScope.launch {
                                    val success = viewModel.saveVisit()
                                    if (success) {
                                        onSaveComplete()
                                    } else {
                                        Toast.makeText(
                                            context,
                                            context.getString(R.string.failed_to_save_visit),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            },
                            enabled = saveEnabled
                        ) {
                            Text(stringResource(R.string.save))
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            var showDatePicker by remember { mutableStateOf(false) }

            OutlinedTextField(
                value = SimpleDateFormat(
                    "dd MMM yyyy, HH:mm",
                    Locale.getDefault()
                ).format(Date(date)),
                onValueChange = {},
                label = { Text(stringResource(R.string.date)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker = true },
                readOnly = true,
                trailingIcon = {
                    Icon(
                        Icons.Filled.CalendarToday,
                        contentDescription = stringResource(R.string.pick_date),
                        modifier = Modifier.clickable {
                            showDatePicker = true

                        })
                }
            )

            if (showDatePicker) {
                val datePickerState = rememberDatePickerState(
                    initialSelectedDateMillis = date
                )
                val timePickerState = rememberTimePickerState(
                    initialHour = Calendar.getInstance().apply { time = Date(date) }
                        .get(Calendar.HOUR_OF_DAY),
                    initialMinute = Calendar.getInstance().apply { time = Date(date) }
                        .get(Calendar.MINUTE)
                )

                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        Button(onClick = {
                            val calendar = Calendar.getInstance()
                            calendar.timeInMillis = datePickerState.selectedDateMillis ?: date
                            calendar.set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                            calendar.set(Calendar.MINUTE, timePickerState.minute)
                            viewModel.date.value = calendar.timeInMillis
                            showDatePicker = false
                        }) {
                            Text(stringResource(R.string.ok))
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDatePicker = false }) {
                            Text(stringResource(R.string.cancel))
                        }
                    }
                ) {
                    Column {
                        DatePicker(state = datePickerState)
                        TimePicker(state = timePickerState)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = doctorName,
                onValueChange = { viewModel.doctorName.value = it },
                label = { Text(stringResource(R.string.doctor_name)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = diagnosis,
                onValueChange = { viewModel.diagnosis.value = it },
                label = { Text(stringResource(R.string.diagnosis)) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )
        }
    }
}