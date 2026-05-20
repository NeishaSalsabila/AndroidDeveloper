package com.neisha.technicaltest_androiddeveloper.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.neisha.technicaltest_androiddeveloper.domain.model.User
import com.neisha.technicaltest_androiddeveloper.ui.theme.*

private fun avatarGradient(name: String): Brush {
    val initial = name.firstOrNull()?.uppercaseChar() ?: 'A'

    return when (initial) {
        in 'A'..'E' -> Brush.linearGradient(
            listOf(Color(0xFF5B7FFF), Color(0xFF8B5CF6))
        )

        in 'F'..'J' -> Brush.linearGradient(
            listOf(Color(0xFFEC4899), Color(0xFF8B5CF6))
        )

        in 'K'..'O' -> Brush.linearGradient(
            listOf(Color(0xFF06B6D4), Color(0xFF5B7FFF))
        )

        in 'P'..'T' -> Brush.linearGradient(
            listOf(Color(0xFFFB923C), Color(0xFFF43F5E))
        )

        else -> Brush.linearGradient(
            listOf(Color(0xFF34D399), Color(0xFF3B82F6))
        )
    }

}

@Composable
fun UserCard(
    user: User,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(0.5.dp, BorderLight),
        colors = CardDefaults.cardColors(
            containerColor = CardSurface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 13.dp, vertical = 11.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            AvatarIcon(name = user.name)

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = user.name,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = user.email,
                    fontSize = 11.sp,
                    color = TextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(5.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    CityChip(city = user.city)

                    Spacer(modifier = Modifier.width(4.dp))

                    GenderChip(gender = user.gender)
                }

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = user.phoneNumber,
                    fontSize = 10.sp,
                    color = TextSecondary
                )
            }

            Spacer(modifier = Modifier.width(6.dp))

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = BorderLight,
                modifier = Modifier.size(18.dp)
            )
        }
    }

}

@Composable
fun AvatarIcon(name: String) {

    val initial = name.firstOrNull()?.uppercaseChar() ?: '?'

    Box(
        modifier = Modifier
            .size(42.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(avatarGradient(name)),
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = initial.toString(),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White
        )
    }
}

@Composable
fun CityChip(city: String) {

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(PrimaryBlueLight)
            .padding(horizontal = 7.dp, vertical = 2.dp)
    ) {

        Text(
            text = city,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            color = PrimaryBlue
        )
    }
}

@Composable
fun GenderChip(gender: Int) {

    val label =
        if (gender == 0) "Laki-laki"
        else "Perempuan"

    val bgColor =
        if (gender == 0) GreenLight
        else PurpleLight

    val textColor =
        if (gender == 0) GreenText
        else PurpleText

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bgColor)
            .padding(horizontal = 7.dp, vertical = 2.dp)
    ) {

        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            color = textColor
        )
    }
}

@Composable
fun EmptyStateView(message: String) {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = BorderLight
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = message,
            fontSize = 13.sp,
            color = TextSecondary
        )
    }
}

@Composable
fun LoadingView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        CircularProgressIndicator(
            color = PrimaryBlue
        )
    }

}
