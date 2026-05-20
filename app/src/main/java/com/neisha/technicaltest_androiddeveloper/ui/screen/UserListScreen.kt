package com.neisha.technicaltest_androiddeveloper.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import com.neisha.technicaltest_androiddeveloper.domain.model.City
import com.neisha.technicaltest_androiddeveloper.ui.components.EmptyStateView
import com.neisha.technicaltest_androiddeveloper.ui.components.LoadingView
import com.neisha.technicaltest_androiddeveloper.ui.components.StatCard
import com.neisha.technicaltest_androiddeveloper.ui.theme.AppBackground
import com.neisha.technicaltest_androiddeveloper.ui.theme.BorderLight
import com.neisha.technicaltest_androiddeveloper.ui.theme.PrimaryBlue
import com.neisha.technicaltest_androiddeveloper.ui.theme.TextPrimary
import com.neisha.technicaltest_androiddeveloper.ui.theme.TextSecondary
import com.neisha.technicaltest_androiddeveloper.viewmodel.UserListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListScreen(
    windowSizeClass: WindowSizeClass,
    onNavigateToAddUser: () -> Unit,
    viewModel: UserListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showFilterSheet by remember { mutableStateOf(false) }

    val headerGradient = Brush.linearGradient(
        listOf(Color(0xFF5B7FFF), Color(0xFF8B5CF6))
    )

    Scaffold(
        containerColor = AppBackground,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAddUser,
                containerColor = PrimaryBlue,
                contentColor = Color.White,
                shape = RoundedCornerShape(22.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                    Text("Tambah User", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Gradient header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(headerGradient)
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 28.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column {
                            Text(
                                text = "Selamat datang 👋",
                                fontSize = 11.sp,
                                color = Color.White.copy(alpha = 0.7f)
                            )
                            Text(
                                text = "Accurate Users",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White
                            )
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            // Sort button
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color.White.copy(alpha = 0.18f))
                                    .size(36.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                IconButton(onClick = { viewModel.onSortToggle() }) {
                                    Icon(
                                        imageVector = if (uiState.sortAscending) Icons.Default.ArrowUpward
                                        else Icons.Default.ArrowDownward,
                                        contentDescription = "Urutkan",
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                            // Filter button
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color.White.copy(alpha = 0.18f))
                                    .size(36.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                BadgedBox(badge = {
                                    if (uiState.selectedCity.isNotBlank()) Badge()
                                }) {
                                    IconButton(onClick = { showFilterSheet = true }) {
                                        Icon(
                                            Icons.Default.FilterList,
                                            contentDescription = "Filter",
                                            tint = Color.White,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Sort pill indicator
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.White.copy(alpha = 0.22f))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = if (uiState.sortAscending) "Urutan A → Z" else "Urutan Z → A",
                            fontSize = 11.sp,
                            color = Color.White
                        )
                    }
                }
            }

            // Search bar floating
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-14).dp)
                    .padding(horizontal = 12.dp),
                shape = RoundedCornerShape(14.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 11.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(9.dp)
                ) {
                    Icon(Icons.Default.Search, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(17.dp))
                    OutlinedTextField(
                        value = uiState.searchQuery,
                        onValueChange = { viewModel.onSearchQueryChange(it) },
                        placeholder = { Text("Cari nama, email, nomor HP...", fontSize = 13.sp, color = TextSecondary) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp),
                        textStyle = androidx.compose.ui.text.TextStyle(fontSize = 13.sp, color = TextPrimary),
                        trailingIcon = {
                            if (uiState.searchQuery.isNotBlank()) {
                                IconButton(onClick = { viewModel.onSearchQueryChange("") }) {
                                    Icon(Icons.Default.Clear, contentDescription = null, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    )
                }
            }

            // Offline error
            AnimatedVisibility(visible = uiState.error != null, enter = fadeIn(), exit = fadeOut()) {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp).offset(y = (-8).dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.WifiOff, contentDescription = null, tint = MaterialTheme.colorScheme.onErrorContainer, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = uiState.error ?: "", modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.onErrorContainer, fontSize = 12.sp)
                        IconButton(onClick = { viewModel.clearError() }, modifier = Modifier.size(20.dp)) {
                            Icon(Icons.Default.Close, contentDescription = null, tint = MaterialTheme.colorScheme.onErrorContainer, modifier = Modifier.size(14.dp))
                        }
                    }
                }
            }

            // Stats row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-6).dp)
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatCard(number = "${uiState.users.size + (if (uiState.selectedCity.isNotBlank() || uiState.searchQuery.isNotBlank()) 0 else 0)}", label = "Total User", modifier = Modifier.weight(1f))
                StatCard(number = "${uiState.cities.size}", label = "Kota", modifier = Modifier.weight(1f))
                StatCard(number = "${uiState.users.size}", label = "Hasil Filter", modifier = Modifier.weight(1f))
            }

            // Section header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Semua Pengguna", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = TextPrimary)
            }

            // Active filter chips
            if (uiState.selectedCity.isNotBlank()) {
                Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)) {
                    FilterChip(
                        selected = true,
                        onClick = { viewModel.onCityFilterChange("") },
                        label = { Text(uiState.selectedCity, fontSize = 11.sp) },
                        trailingIcon = { Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(14.dp)) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = PrimaryBlue,
                            selectedLabelColor = Color.White,
                            selectedTrailingIconColor = Color.White
                        )
                    )
                }
            }

            when {
                uiState.isLoading -> LoadingView()
                uiState.users.isEmpty() -> EmptyStateView(
                    message = if (uiState.searchQuery.isNotBlank() || uiState.selectedCity.isNotBlank())
                        "Tidak ada pengguna yang sesuai" else "Belum ada pengguna"
                )
                else -> {
                    PullToRefreshBox(
                        isRefreshing = uiState.isRefreshing,
                        onRefresh = { viewModel.refresh() }
                    ) {
                        AdaptiveUserList(
                            users = uiState.users,
                            windowSizeClass = windowSizeClass
                        )
                    }
                }
            }
        }
    }

    if (showFilterSheet) {
        FilterBottomSheet(
            cities = uiState.cities,
            selectedCity = uiState.selectedCity,
            onCitySelected = { city ->
                viewModel.onCityFilterChange(city)
                showFilterSheet = false
            },
            onDismiss = { showFilterSheet = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    cities: List<City>,
    selectedCity: String,
    onCitySelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {
        Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp)) {
            Text("Pilih Kota", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = TextPrimary)
            Spacer(modifier = Modifier.height(12.dp))
            ListItem(
                headlineContent = { Text("Semua Kota", fontSize = 13.sp, color = if (selectedCity.isBlank()) PrimaryBlue else TextPrimary, fontWeight = if (selectedCity.isBlank()) FontWeight.Medium else FontWeight.Normal) },
                leadingContent = { RadioButton(selected = selectedCity.isBlank(), onClick = { onCitySelected("") }, colors = RadioButtonDefaults.colors(selectedColor = PrimaryBlue)) }
            )
            HorizontalDivider(color = BorderLight, thickness = 0.5.dp)
            cities.forEach { city ->
                ListItem(
                    headlineContent = { Text(city.name, fontSize = 13.sp, color = if (selectedCity == city.name) PrimaryBlue else TextPrimary, fontWeight = if (selectedCity == city.name) FontWeight.Medium else FontWeight.Normal) },
                    leadingContent = { RadioButton(selected = selectedCity == city.name, onClick = { onCitySelected(city.name) }, colors = RadioButtonDefaults.colors(selectedColor = PrimaryBlue)) }
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
