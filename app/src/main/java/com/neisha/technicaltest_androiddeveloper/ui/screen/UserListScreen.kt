package com.neisha.technicaltest_androiddeveloper.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import com.neisha.technicaltest_androiddeveloper.domain.model.City
import com.neisha.technicaltest_androiddeveloper.domain.model.SortOption
import com.neisha.technicaltest_androiddeveloper.ui.components.EmptyStateView
import com.neisha.technicaltest_androiddeveloper.ui.components.ShimmerLoadingList
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
    val searchQuery by viewModel.searchQuery.collectAsState()
    var showFilterSheet by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()

    LaunchedEffect(uiState.sortOption) {
        listState.scrollToItem(0)
    }

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
                                text = "User App",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-14).dp)
                    .padding(horizontal = 12.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    Icon(
                        Icons.Default.Search,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(20.dp)
                    )

                    TextField(
                        value = searchQuery,
                        onValueChange = { viewModel.onSearchQueryChange(it) },

                        placeholder = {
                            Text(
                                text = "Cari nama, email, atau no HP",
                                fontSize = 16.sp,
                                color = TextSecondary
                            )
                        },

                        singleLine = true,

                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,

                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,

                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,

                            cursorColor = PrimaryBlue
                        ),

                        modifier = Modifier.weight(1f),

                        textStyle = LocalTextStyle.current.copy(
                            fontSize = 16.sp,
                            color = TextPrimary
                        ),

                        trailingIcon = {
                            if (searchQuery.isNotBlank()) {
                                IconButton(
                                    onClick = { viewModel.onSearchQueryChange("") }
                                ) {
                                    Icon(
                                        Icons.Default.Clear,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    )
                }
            }

            AnimatedVisibility(visible = uiState.error != null, enter = fadeIn(), exit = fadeOut()) {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp).offset(y = (-8).dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.WifiOff, contentDescription = null, tint = MaterialTheme.colorScheme.onErrorContainer, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = uiState.error ?: "", modifier = Modifier
                            .weight(1f)
                            .heightIn(min = 48.dp), color = MaterialTheme.colorScheme.onErrorContainer, fontSize = 12.sp)
                        IconButton(onClick = { viewModel.clearError() }, modifier = Modifier.size(20.dp)) {
                            Icon(Icons.Default.Close, contentDescription = null, tint = MaterialTheme.colorScheme.onErrorContainer, modifier = Modifier.size(14.dp))
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-6).dp)
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatCard(number = "${uiState.totalUsers}", label = "Total User", modifier = Modifier.weight(1f))
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    IconButton(onClick = { viewModel.onSortToggle() }) {
                        Icon(
                            Icons.Default.SwapVert,
                            contentDescription = "Urutkan",
                            tint = PrimaryBlue,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Text(
                        text = if (uiState.sortOption == SortOption.NAME_ASC) "A-Z" else "Z-A",
                        fontSize = 11.sp,
                        color = PrimaryBlue,
                        fontWeight = FontWeight.Medium
                    )

                    BadgedBox(badge = {
                        if (uiState.selectedCities.isNotEmpty()) Badge()
                    }) {
                        IconButton(onClick = { showFilterSheet = true }) {
                            Icon(
                                Icons.Default.FilterList,
                                contentDescription = "Filter",
                                tint = if (uiState.selectedCities.isNotEmpty()) PrimaryBlue else TextSecondary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            if (uiState.selectedCities.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    uiState.selectedCities.forEach { city ->
                        FilterChip(
                            selected = true,
                            onClick = { viewModel.onCityFilterChange(city) },
                            label = { Text(city, fontSize = 11.sp) },
                            trailingIcon = { Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(14.dp)) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = PrimaryBlue,
                                selectedLabelColor = Color.White,
                                selectedTrailingIconColor = Color.White
                            )
                        )
                    }
                }
            }

            when {
                uiState.isLoading -> ShimmerLoadingList()
                uiState.users.isEmpty() -> EmptyStateView(
                    message = if (uiState.searchQuery.isNotBlank() || uiState.selectedCities.isNotEmpty())
                        "Tidak ada pengguna yang sesuai" else "Belum ada pengguna"
                )
                else -> {
                    PullToRefreshBox(
                        isRefreshing = uiState.isRefreshing,
                        onRefresh = { viewModel.refresh() }
                    ) {
                        AdaptiveUserList(
                            users = uiState.users,
                            windowSizeClass = windowSizeClass,
                            listState = listState
                        )
                    }
                }
            }
        }
    }

    if (showFilterSheet) {
        FilterBottomSheet(
            cities = uiState.cities,
            selectedCities = uiState.selectedCities,
            onCityToggled = { city -> viewModel.onCityFilterChange(city) },
            onDismiss = { showFilterSheet = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    cities: List<City>,
    selectedCities: Set<String>,
    onCityToggled: (String) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        contentColor = TextPrimary,
        shape = RoundedCornerShape(
            topStart = 20.dp,
            topEnd = 20.dp
        ),
        dragHandle = { BottomSheetDefaults.DragHandle(color = BorderLight) }
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(horizontal = 16.dp)
                .fillMaxHeight(0.55f)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = PrimaryBlue,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Pilih Kota", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                }
                IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Close, contentDescription = "Tutup", tint = TextSecondary, modifier = Modifier.size(18.dp))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                ListItem(
                    headlineContent = { Text("Semua Kota", fontSize = 14.sp, color = if (selectedCities.isEmpty()) PrimaryBlue else TextPrimary, fontWeight = if (selectedCities.isEmpty()) FontWeight.Medium else FontWeight.Normal) },
                    leadingContent = {
                        Checkbox(
                            checked = selectedCities.isEmpty(),
                            onCheckedChange = {
                                if (selectedCities.isNotEmpty()) onCityToggled("")
                            },
                            colors = CheckboxDefaults.colors(checkedColor = PrimaryBlue)
                        )
                    },
                    colors = ListItemDefaults.colors(containerColor = Color.White)
                )

                HorizontalDivider(color = BorderLight, thickness = 0.5.dp)

                cities.forEach { city ->
                    val isSelected = city.name in selectedCities
                    ListItem(
                        headlineContent = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.Place,
                                    contentDescription = null,
                                    tint = if (isSelected) PrimaryBlue else TextSecondary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    city.name,
                                    fontSize = 14.sp,
                                    color = if (isSelected) PrimaryBlue else TextPrimary,
                                fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
                            )
                        }
                    },
                    leadingContent = {
                        Checkbox(
                            checked = isSelected,
                            onCheckedChange = { onCityToggled(city.name) },
                            colors = CheckboxDefaults.colors(checkedColor = PrimaryBlue)
                        )
                    },
                    colors = ListItemDefaults.colors(containerColor = Color.White)
                )
            }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
