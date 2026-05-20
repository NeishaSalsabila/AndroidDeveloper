package com.neisha.technicaltest_androiddeveloper.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.neisha.technicaltest_androiddeveloper.domain.model.User
import com.neisha.technicaltest_androiddeveloper.ui.theme.AppBackground
import com.neisha.technicaltest_androiddeveloper.ui.theme.BorderLight
import com.neisha.technicaltest_androiddeveloper.ui.theme.CardSurface
import com.neisha.technicaltest_androiddeveloper.ui.theme.PrimaryBlue
import com.neisha.technicaltest_androiddeveloper.ui.theme.PrimaryPurple
import com.neisha.technicaltest_androiddeveloper.ui.theme.PurpleLight
import com.neisha.technicaltest_androiddeveloper.ui.theme.TextHint
import com.neisha.technicaltest_androiddeveloper.ui.theme.TextPrimary
import com.neisha.technicaltest_androiddeveloper.ui.theme.TextSecondary
import com.neisha.technicaltest_androiddeveloper.viewmodel.AddUserUiState
import com.neisha.technicaltest_androiddeveloper.viewmodel.UserListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddUserScreen(
    onNavigateBack: () -> Unit,
    viewModel: UserListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val addUserState by viewModel.addUserState.collectAsState()

    var name by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var selectedCity by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf(0) }
    var expandedCityDropdown by remember { mutableStateOf(false) }

    var nameError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var phoneError by remember { mutableStateOf(false) }
    var cityError by remember { mutableStateOf(false) }

    LaunchedEffect(addUserState) {
        if (addUserState is AddUserUiState.Success) {
            viewModel.resetAddUserState()
            onNavigateBack()
        }
    }

    val isLoading = addUserState is AddUserUiState.Loading
    val headerGradient = Brush.linearGradient(listOf(Color(0xFF5B7FFF), Color(0xFF8B5CF6)))

    Scaffold(containerColor = AppBackground) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Gradient header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(headerGradient)
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali", tint = Color.White)
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Tambah Pengguna", fontSize = 15.sp, fontWeight = FontWeight.Medium, color = Color.White)
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 12.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Error banner
                if (addUserState is AddUserUiState.Error) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = (addUserState as AddUserUiState.Error).message,
                            modifier = Modifier.padding(12.dp),
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            fontSize = 12.sp
                        )
                    }
                }

                // Card 1 — Informasi Dasar
                Card(
                    shape = RoundedCornerShape(14.dp),
                    elevation = CardDefaults.cardElevation(0.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    border = androidx.compose.foundation.BorderStroke(0.5.dp, BorderLight)
                ) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Informasi Dasar", fontSize = 11.sp, fontWeight = FontWeight.Medium, color = PrimaryBlue)

                        FormField(
                            label = "Nama Lengkap *",
                            value = name,
                            onValueChange = { name = it; nameError = false },
                            placeholder = "Masukkan nama lengkap",
                            isError = nameError,
                            errorMessage = "Nama tidak boleh kosong",
                            singleLine = true
                        )
                        FormField(
                            label = "Email *",
                            value = email,
                            onValueChange = { email = it; emailError = false },
                            placeholder = "contoh@email.com",
                            isError = emailError,
                            errorMessage = "Email tidak valid",
                            keyboardType = KeyboardType.Email,
                            singleLine = true
                        )
                        FormField(
                            label = "Nomor HP *",
                            value = phoneNumber,
                            onValueChange = { phoneNumber = it; phoneError = false },
                            placeholder = "08xxxxxxxxxx",
                            isError = phoneError,
                            errorMessage = "Nomor HP tidak boleh kosong",
                            keyboardType = KeyboardType.Phone,
                            singleLine = true
                        )
                    }
                }

                // Card 2 — Lokasi & Identitas
                Card(
                    shape = RoundedCornerShape(14.dp),
                    elevation = CardDefaults.cardElevation(0.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    border = androidx.compose.foundation.BorderStroke(0.5.dp, BorderLight)
                ) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Lokasi & Identitas", fontSize = 11.sp, fontWeight = FontWeight.Medium, color = PrimaryBlue)

                        FormField(
                            label = "Alamat",
                            value = address,
                            onValueChange = { address = it },
                            placeholder = "Jl. Contoh No. 1...",
                            singleLine = false
                        )

                        // Kota dropdown
                        Column {
                            Text("Kota *", fontSize = 10.sp, color = TextSecondary, modifier = Modifier.padding(bottom = 3.dp))
                            ExposedDropdownMenuBox(
                                expanded = expandedCityDropdown,
                                onExpandedChange = { expandedCityDropdown = !expandedCityDropdown }
                            ) {
                                OutlinedTextField(
                                    value = selectedCity,
                                    onValueChange = {},
                                    readOnly = true,
                                    placeholder = { Text("Pilih kota", fontSize = 13.sp, color = TextHint) },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCityDropdown) },
                                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                                    isError = cityError,
                                    supportingText = if (cityError) { { Text("Pilih kota terlebih dahulu", fontSize = 11.sp) } } else null,
                                    shape = RoundedCornerShape(9.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = PrimaryBlue,
                                        unfocusedBorderColor = BorderLight,
                                        focusedContainerColor = Color(0xFFF8F8FC),
                                        unfocusedContainerColor = Color(0xFFF8F8FC)
                                    ),
                                    textStyle = androidx.compose.ui.text.TextStyle(fontSize = 13.sp, color = TextPrimary)
                                )
                                ExposedDropdownMenu(
                                    expanded = expandedCityDropdown,
                                    onDismissRequest = { expandedCityDropdown = false },
                                    modifier = Modifier.background(Color.White)
                                ) {
                                    uiState.cities.forEach { city ->
                                        DropdownMenuItem(
                                            text = { Text(city.name, fontSize = 13.sp) },
                                            onClick = {
                                                selectedCity = city.name
                                                cityError = false
                                                expandedCityDropdown = false
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        // Gender
                        Column {
                            Text("Jenis Kelamin", fontSize = 10.sp, color = TextSecondary, modifier = Modifier.padding(bottom = 6.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                GenderOption(
                                    label = "Laki-laki",
                                    selected = selectedGender == 0,
                                    onClick = { selectedGender = 0 },
                                    modifier = Modifier.weight(1f)
                                )
                                GenderOption(
                                    label = "Perempuan",
                                    selected = selectedGender == 1,
                                    onClick = { selectedGender = 1 },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Submit button
                Button(
                    onClick = {
                        nameError = name.isBlank()
                        emailError = !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
                        phoneError = phoneNumber.isBlank()
                        cityError = selectedCity.isBlank()
                        if (!nameError && !emailError && !phoneError && !cityError) {
                            viewModel.addUser(
                                User(
                                    id = "",
                                    name = name.trim(),
                                    address = address.trim(),
                                    email = email.trim(),
                                    phoneNumber = phoneNumber.trim(),
                                    city = selectedCity,
                                    gender = selectedGender
                                )
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    enabled = !isLoading,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                    } else {
                        Text("Simpan Pengguna", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun FormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isError: Boolean = false,
    errorMessage: String = "",
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true
) {
    Column {
        Text(label, fontSize = 10.sp, color = TextSecondary, modifier = Modifier.padding(bottom = 3.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, fontSize = 13.sp, color = TextHint) },
            modifier = Modifier.fillMaxWidth(),
            isError = isError,
            supportingText = if (isError) { { Text(errorMessage, fontSize = 11.sp) } } else null,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            singleLine = singleLine,
            maxLines = if (singleLine) 1 else 3,
            shape = RoundedCornerShape(9.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryBlue,
                unfocusedBorderColor = BorderLight,
                focusedContainerColor = Color(0xFFF8F8FC),
                unfocusedContainerColor = Color(0xFFF8F8FC)
            ),
            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 13.sp, color = TextPrimary)
        )
    }
}

@Composable
private fun GenderOption(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = if (selected) PrimaryPurple else BorderLight
    val bgColor = if (selected) PurpleLight else Color(0xFFF8F8FC)
    val textColor = if (selected) PrimaryPurple else TextSecondary

    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(40.dp),
        shape = RoundedCornerShape(9.dp),
        colors = ButtonDefaults.outlinedButtonColors(containerColor = bgColor),
        border = androidx.compose.foundation.BorderStroke(if (selected) 1.5.dp else 0.5.dp, borderColor)
    ) {
        RadioButton(
            selected = selected,
            onClick = null,
            colors = RadioButtonDefaults.colors(selectedColor = PrimaryPurple),
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(label, fontSize = 12.sp, color = textColor, fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal)
    }
}