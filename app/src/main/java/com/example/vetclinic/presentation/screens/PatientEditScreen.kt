package com.example.vetclinic.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.vetclinic.R
import com.example.vetclinic.presentation.viewmodel.PatientEditViewModel
import kotlinx.coroutines.launch

@Composable
fun PatientEditScreen(
    patientId: Long,
    onSaveComplete: () -> Unit
) {
    val viewModel = hiltViewModel<PatientEditViewModel>()

    LaunchedEffect(patientId) {
        viewModel.loadPatient(if (patientId == 0L) null else patientId)
    }

    PatientEditScreenContent(
        viewModel = viewModel,
        patientId = patientId,
        onSaveComplete = onSaveComplete
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PatientEditScreenContent(
    viewModel: PatientEditViewModel,
    patientId: Long,
    onSaveComplete: () -> Unit
) {
    val name by viewModel.name.collectAsState()
    val species by viewModel.species.collectAsState()
    val breed by viewModel.breed.collectAsState()
    val age by viewModel.age.collectAsState()
    val weight by viewModel.weight.collectAsState()
    val saveEnabled by viewModel.saveEnabled.collectAsState()
    val savingState by viewModel.savingState.collectAsState()

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (patientId == 0L) stringResource(R.string.new_patient) else stringResource(
                            R.string.edit_patient
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
                                    val success = viewModel.savePatient()
                                    if (success) {
                                        onSaveComplete()
                                    } else {
                                        Toast.makeText(
                                            context,
                                            context.getString(R.string.failed_to_save_patient),
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
            OutlinedTextField(
                value = name,
                onValueChange = { viewModel.name.value = it },
                label = { Text(stringResource(R.string.name_req)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = species,
                onValueChange = { viewModel.species.value = it },
                label = { Text(stringResource(R.string.species_req)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = breed,
                onValueChange = { viewModel.breed.value = it },
                label = { Text(stringResource(R.string.breed)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = age,
                onValueChange = {
                    if (it.isEmpty() || it.toIntOrNull() != null) {
                        viewModel.age.value = it
                    }
                },
                label = { Text(stringResource(R.string.age_years)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = weight,
                onValueChange = {
                    if (it.isEmpty() || it.toDoubleOrNull() != null) {
                        viewModel.weight.value = it
                    }
                },
                label = { Text(stringResource(R.string.weight_kg)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
    }
}