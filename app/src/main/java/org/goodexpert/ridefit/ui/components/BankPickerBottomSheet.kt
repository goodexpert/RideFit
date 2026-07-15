package org.goodexpert.ridefit.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.goodexpert.ridefit.R
import org.goodexpert.ridefit.model.KoreaBankIcons
import org.goodexpert.ridefit.ui.theme.rideFitColors

private val bankPriorityOrder = listOf(
    "090", "088", "004", "020", "081", "011", "003", "092", "089", "071",
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BankPickerBottomSheet(
    onDismiss: () -> Unit,
    onBankSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var query by remember { mutableStateOf("") }

    val allBanks = remember {
        buildList {
            bankPriorityOrder.forEach { code ->
                KoreaBankIcons.banks[code]?.let { add(code to it) }
            }
            KoreaBankIcons.banks.entries
                .filter { it.key !in bankPriorityOrder }
                .forEach { add(it.key to it.value) }
        }
    }
    val allSecurities = remember {
        KoreaBankIcons.securities.entries.map { it.key to it.value }
    }

    val filteredBanks = remember(query) {
        if (query.isBlank()) {
            allBanks
        } else {
            allBanks.filter { it.second.contains(query) }
        }
    }
    val filteredSecurities = remember(query) {
        if (query.isBlank()) {
            allSecurities
        } else {
            allSecurities.filter { it.second.contains(query) }
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = modifier,
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            BankPickerTopRow(onDismiss = onDismiss)
            BankSearchField(
                query = query,
                onQueryChange = { query = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 8.dp),
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                if (filteredBanks.isNotEmpty()) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        BankSectionHeader(title = stringResource(R.string.bank_picker_section_bank))
                    }
                    items(filteredBanks, key = { it.first }) { (code, name) ->
                        BankGridItem(
                            code = code,
                            name = name,
                            onClick = { onBankSelect(name) },
                        )
                    }
                }
                if (filteredSecurities.isNotEmpty()) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        BankSectionHeader(title = stringResource(R.string.bank_picker_section_securities))
                    }
                    items(filteredSecurities, key = { it.first }) { (code, name) ->
                        BankGridItem(
                            code = code,
                            name = name,
                            onClick = { onBankSelect(name) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BankPickerTopRow(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.bank_picker_title),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f),
        )
        TextButton(onClick = onDismiss) {
            Text(
                text = stringResource(R.string.bank_picker_cancel),
                color = MaterialTheme.rideFitColors.brand,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

@Composable
private fun BankSearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val appColors = MaterialTheme.rideFitColors

    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier,
        placeholder = {
            Text(
                text = stringResource(R.string.bank_picker_search_hint),
                style = MaterialTheme.typography.bodyLarge,
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = null,
                tint = appColors.onBackgroundSecondary,
            )
        },
        singleLine = true,
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = appColors.brand,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
        ),
    )
}

@Composable
private fun BankSectionHeader(
    title: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 8.dp),
    )
}

@Composable
private fun BankGridItem(
    code: String,
    name: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val resId = remember(code) { KoreaBankIcons.getIconResId(context, code) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Image(
            painter = painterResource(id = resId),
            contentDescription = name,
            modifier = Modifier.size(44.dp),
        )
        Text(
            text = name,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 2,
        )
    }
}
