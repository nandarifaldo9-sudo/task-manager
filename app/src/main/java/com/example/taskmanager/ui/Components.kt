@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.taskmanager.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.dp
import com.example.taskmanager.data.TaskStatus

/** Warna status: Belum = merah, Proses = oranye, Selesai = hijau */
fun statusColor(s: TaskStatus): Color = when (s) {
    TaskStatus.BELUM -> Color(0xFFE5484D)
    TaskStatus.PROSES -> Color(0xFFD98200)
    TaskStatus.SELESAI -> Color(0xFF2E9E6B)
}

/** Lencana kecil: titik berwarna + label status */
@Composable
fun StatusBadge(status: TaskStatus, modifier: Modifier = Modifier) {
    val c = statusColor(status)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(c.copy(alpha = 0.12f), CircleShape)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Box(Modifier.size(6.dp).background(c, CircleShape))
        Spacer(Modifier.width(6.dp))
        Text(
            status.label,
            color = c,
            style = MaterialTheme.typography.labelMedium,
            maxLines = 1,
            softWrap = false
        )
    }
}

/** Pengganti FilterChip: pill minimalis. Parameter sengaja dibuat sama (selected, onClick, label). */
@Composable
fun ChoicePill(
    selected: Boolean,
    onClick: () -> Unit,
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    selectedColor: Color = MaterialTheme.colorScheme.primary
) {
    val contentColor = when {
        !selected -> MaterialTheme.colorScheme.onSurfaceVariant
        selectedColor.luminance() > 0.5f -> Color.Black
        else -> Color.White
    }
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = CircleShape,
        color = if (selected) selectedColor else Color.Transparent,
        contentColor = contentColor,
        border = BorderStroke(
            1.dp,
            if (selected) selectedColor else MaterialTheme.colorScheme.outlineVariant
        )
    ) {
        Box(
            Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            ProvideTextStyle(MaterialTheme.typography.labelLarge) { label() }
        }
    }
}

/** Pengganti OutlinedTextField: sudut membulat, border tipis. Parameter sama dengan yang dipakai di kode. */
@Composable
fun MinimalField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    singleLine: Boolean = false,
    minLines: Int = 1
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = label,
        leadingIcon = leadingIcon,
        isError = isError,
        singleLine = singleLine,
        minLines = minLines,
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
            focusedBorderColor = MaterialTheme.colorScheme.onSurface,
            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
        )
    )
}