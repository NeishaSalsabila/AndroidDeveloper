package com.neisha.technicaltest_androiddeveloper.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Warning
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

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(addUserState) {
        if (addUserState is AddUserUiState.Success) {
            snackbarHostState.showSnackbar("Pengguna berhasil ditambahkan")
            viewModel.resetAddUserState()
            onNavigateBack()
        }
    }

    val isLoading = addUserState is AddUserUiState.Loading
    val headerGradient = Brush.linearGradient(listOf(Color(0xFF5B7FFF), Color(0xFF8B5CF6)))

    Scaffold(
        containerColor = AppBackground,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = Color(0xFF065F46),
                    contentColor = Color.White,
                    shape = RoundedCornerShape(10.dp)
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

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

                val isAddError = addUserState is AddUserUiState.Error
                if (isAddError) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFE5E5)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Warning,
                                contentDescription = null,
                                tint = Color(0xFFDC2626),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = (addUserState as AddUserUiState.Error).message,
                                modifier = Modifier.weight(1f),
                                color = Color(0xFFDC2626),
                                fontSize = 12.sp
                            )
                            IconButton(
                                onClick = { viewModel.resetAddUserState() },
                                modifier = Modifier.size(20.dp)
                            ) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = null,
                                    tint = Color(0xFFDC2626),
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }

                Card(
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(1.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    border = androidx.compose.foundation.BorderStroke(0.5.dp, BorderLight)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Text("Data User", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = PrimaryBlue)

                        FormField(
                            label = "Nama Lengkap",
                            value = name,
                            onValueChange = { name = it; nameError = false; viewModel.resetAddUserState() },
                            placeholder = "Masukkan nama lengkap",
                            isError = nameError,
                            errorMessage = "Nama tidak boleh kosong",
                            required = true,
                            singleLine = true
                        )
                        FormField(
                            label = "Email",
                            value = email,
                            onValueChange = { email = it; emailError = false; viewModel.resetAddUserState() },
                            placeholder = "contoh@email.com",
                            isError = emailError,
                            errorMessage = "Email tidak valid",
                            keyboardType = KeyboardType.Email,
                            required = true,
                            singleLine = true
                        )
                        FormField(
                            label = "Nomor HP",
                            value = phoneNumber,
                            onValueChange = { phoneNumber = it; phoneError = false; viewModel.resetAddUserState() },
                            placeholder = "08xxxxxxxxxx",
                            isError = phoneError,
                            errorMessage = "Nomor HP tidak boleh kosong",
                            keyboardType = KeyboardType.Phone,
                            required = true,
                            singleLine = true
                        )
                    }
                }

                Card(
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(1.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    border = androidx.compose.foundation.BorderStroke(0.5.dp, BorderLight)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Text("Lokasi & Identitas", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = PrimaryBlue)

                        FormField(
                            label = "Alamat",
                            value = address,
                            onValueChange = { address = it; viewModel.resetAddUserState() },
                            placeholder = "Jl. Contoh No. 1...",
                            singleLine = false,
                            required = false
                        )

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("Kota", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF3A3A4E))
                                Text(" *", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFFE53935))
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            ExposedDropdownMenuBox(
                                expanded = expandedCityDropdown,
                                onExpandedChange = { expandedCityDropdown = !expandedCityDropdown }
                            ) {
                                OutlinedTextField(
                                    value = selectedCity,
                                    onValueChange = {},
                                    readOnly = true,
                                    placeholder = { Text("Pilih kota", fontSize = 16.sp, color = TextSecondary) },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCityDropdown) },
                                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                                    isError = cityError,
                                    supportingText = if (cityError) { { Text("Pilih kota terlebih dahulu", fontSize = 12.sp) } } else null,
                                    shape = RoundedCornerShape(10.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = PrimaryBlue,
                                        unfocusedBorderColor = BorderLight,
                                        focusedContainerColor = Color(0xFFF8F8FC),
                                        unfocusedContainerColor = Color(0xFFF8F8FC),
                                        focusedTextColor = TextPrimary,
                                        unfocusedTextColor = TextPrimary
                                    ),
                                    textStyle = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, color = TextPrimary)
                                )
                                ExposedDropdownMenu(
                                    expanded = expandedCityDropdown,
                                    onDismissRequest = { expandedCityDropdown = false },
                                    modifier = Modifier.background(Color.White)
                                ) {
                                    uiState.cities.forEach { city ->
                                    DropdownMenuItem(
                                        text = { Text(city.name, fontSize = 14.sp, color = TextPrimary) },
                                        onClick = {
                                            selectedCity = city.name
                                            cityError = false
                                            expandedCityDropdown = false
                                            viewModel.resetAddUserState()
                                        }
                                        )
                                    }
                                }
                            }
                        }

                        Column {
                            Text("Jenis Kelamin", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF3A3A4E), modifier = Modifier.padding(bottom = 8.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                GenderOption(
                                    label = "Laki-laki",
                                    selected = selectedGender == 0,
                                    onClick = { selectedGender = 0; viewModel.resetAddUserState() },
                                    modifier = Modifier.weight(1f)
                                )
                                GenderOption(
                                    label = "Perempuan",
                                    selected = selectedGender == 1,
                                    onClick = { selectedGender = 1; viewModel.resetAddUserState() },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

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
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    enabled = !isLoading,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.5.dp)
                        Spacer(modifier = Modifier.width(10.dp))
                    }
                    Text("Simpan Pengguna", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
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
    singleLine: Boolean = true,
    required: Boolean = false
) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(label, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF3A3A4E))
            if (required) {
                Text(" *", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFFE53935))
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, fontSize = 16.sp, color = TextSecondary) },
            modifier = Modifier.fillMaxWidth(),
            isError = isError,
            supportingText = if (isError) { { Text(errorMessage, fontSize = 12.sp) } } else null,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            singleLine = singleLine,
            maxLines = if (singleLine) 1 else 3,
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryBlue,
                unfocusedBorderColor = BorderLight,
                focusedContainerColor = Color(0xFFF8F8FC),
                unfocusedContainerColor = Color(0xFFF8F8FC),
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
            ),
            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, color = TextPrimary)
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
    val textColor = if (selected) PrimaryPurple else TextPrimary

    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.outlinedButtonColors(containerColor = bgColor),
        border = androidx.compose.foundation.BorderStroke(if (selected) 1.5.dp else 0.5.dp, borderColor)
    ) {
        RadioButton(
            selected = selected,
            onClick = null,
            colors = RadioButtonDefaults.colors(selectedColor = PrimaryPurple),
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(label, fontSize = 14.sp, color = textColor, fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal)
    }
}