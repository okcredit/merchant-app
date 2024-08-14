package app.okcredit.merchant.ledger.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import app.okcredit.merchant.ledger.CategoryOption
import app.okcredit.merchant.ledger.HomeTab
import app.okcredit.merchant.ledger.ReminderFilterOption
import app.okcredit.merchant.ledger.SortOption
import app.okcredit.merchant.ledger.isCustomerTab
import app.okcredit.ui.theme.OkCreditTheme
import app.okcredit.ui.theme.green_lite
import app.okcredit.ui.theme.green_lite_1
import app.okcredit.ui.theme.green_primary
import app.okcredit.ui.theme.grey300
import app.okcredit.ui.theme.grey50
import app.okcredit.ui.theme.grey900
import app.okcredit.ui.theme.white
import merchant_app.shared.generated.resources.Res
import merchant_app.shared.generated.resources.amount_due
import merchant_app.shared.generated.resources.clear
import merchant_app.shared.generated.resources.cta_apply
import merchant_app.shared.generated.resources.filter
import merchant_app.shared.generated.resources.last_payment
import merchant_app.shared.generated.resources.latest
import merchant_app.shared.generated.resources.name
import merchant_app.shared.generated.resources.pending
import merchant_app.shared.generated.resources.reminder_date
import merchant_app.shared.generated.resources.sort_by
import merchant_app.shared.generated.resources.today
import merchant_app.shared.generated.resources.upcoming
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SortAndFilterBottomSheet(
    currentTab: HomeTab,
    selectedSortByOption: SortOption,
    selectedReminderFilters: Set<ReminderFilterOption>,
    onApplyClicked: (SortOption, Set<ReminderFilterOption>) -> Unit,
    onDismissClicked: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            )
    ) {
        SortAndFilterHeader(
            tab = currentTab,
            onDismissClicked = onDismissClicked
        )
        HorizontalDivider()

        val currentSortBy = remember { mutableStateOf(selectedSortByOption) }
        val currentReminderFilters = remember { mutableStateOf(selectedReminderFilters) }
        SortAndFilterContent(
            currentTab = currentTab,
            selectedSortByOption = currentSortBy.value,
            selectedReminderFilters = currentReminderFilters.value,
            modifier = Modifier.aspectRatio(1f),
            onSortBySelected = {
                currentSortBy.value = it
            },
            onReminderFilterSelected = {
                if (currentReminderFilters.value.contains(it)) {
                    currentReminderFilters.value = currentReminderFilters.value.minus(it)
                } else {
                    currentReminderFilters.value = currentReminderFilters.value.plus(it)
                }
            },
        )
        LaunchedEffect(selectedSortByOption) {
            currentSortBy.value = selectedSortByOption
        }
        LaunchedEffect(selectedReminderFilters) {
            currentReminderFilters.value = selectedReminderFilters
        }
        HorizontalDivider()
        Footer(
            onApplyClicked = {
                onApplyClicked(currentSortBy.value, currentReminderFilters.value)
            },
            onClearClicked = {
                onApplyClicked(SortOption.LAST_ACTIVITY, emptySet())
                currentSortBy.value = SortOption.LAST_ACTIVITY
                currentReminderFilters.value = emptySet()
            }
        )
    }
}

@Composable
fun SortAndFilterContent(
    currentTab: HomeTab,
    modifier: Modifier,
    selectedSortByOption: SortOption,
    selectedReminderFilters: Set<ReminderFilterOption>,
    onSortBySelected: (SortOption) -> Unit,
    onReminderFilterSelected: (ReminderFilterOption) -> Unit,
) {
    if (currentTab.isCustomerTab()) {
        SortByAndFilterUi(
            modifier = modifier,
            selectedSortByOption = selectedSortByOption,
            selectedReminderFilters = selectedReminderFilters,
            onSortBySelected = onSortBySelected,
            onReminderFilterSelected = onReminderFilterSelected,
        )
    } else {
        SortByUi(
            modifier = modifier,
            selectedSortByOption = selectedSortByOption,
            onSortBySelected = onSortBySelected,
        )
    }
}

@Composable
fun SortByUi(
    modifier: Modifier,
    selectedSortByOption: SortOption,
    onSortBySelected: (SortOption) -> Unit
) {
    FilterOptionsRadioButtonsUi(
        modifier = modifier.fillMaxWidth(),
        selectedOption = selectedSortByOption,
        onOptionSelected = {
            onSortBySelected(it)
        },
        optionsList = SortOption.entries - SortOption.LAST_PAYMENT,
    )
}

@Composable
fun SortByAndFilterUi(
    modifier: Modifier,
    selectedSortByOption: SortOption,
    selectedReminderFilters: Set<ReminderFilterOption>,
    onSortBySelected: (SortOption) -> Unit,
    onReminderFilterSelected: (ReminderFilterOption) -> Unit
) {
    Row(modifier = modifier) {
        val selectedCategory = remember { mutableStateOf(CategoryOption.SORT_BY) }

        FilterCategoryItemUi(
            categoryOptions = CategoryOption.entries,
            selectedCategory = selectedCategory.value,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(grey50),
            onCategorySelected = {
                selectedCategory.value = it
            },
        )

        when (selectedCategory.value) {
            CategoryOption.SORT_BY -> FilterOptionsRadioButtonsUi(
                modifier = Modifier.weight(2f),
                selectedOption = selectedSortByOption,
                onOptionSelected = onSortBySelected,
                optionsList = SortOption.entries,
            )

            CategoryOption.REMINDER_DATE -> FilterOptionsCheckboxUi(
                optionsList = ReminderFilterOption.entries,
                selectedOptions = selectedReminderFilters,
                modifier = Modifier.weight(2f),
                onOptionSelected = onReminderFilterSelected,
            )
        }
    }
}

@Composable
fun findLabelForSortOption(sortOption: SortOption): String {
    return when (sortOption) {
        SortOption.LAST_ACTIVITY -> stringResource(resource = Res.string.latest)
        SortOption.LAST_PAYMENT -> stringResource(resource = Res.string.last_payment)
        SortOption.AMOUNT_DUE -> stringResource(resource = Res.string.amount_due)
        SortOption.NAME -> stringResource(resource = Res.string.name)
    }
}

@Composable
fun FilterCategoryItemUi(
    categoryOptions: List<CategoryOption>,
    selectedCategory: CategoryOption,
    modifier: Modifier,
    onCategorySelected: (CategoryOption) -> Unit,
) {
    Column(modifier = modifier) {
        val color = MaterialTheme.colorScheme.primary
        categoryOptions.forEach { category ->
            val isSelected = selectedCategory == category
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = if (!isSelected) grey50 else white)
                    .drawBehind {
                        if (isSelected) {
                            drawRect(
                                color = color,
                                size = Size(width = 4.dp.toPx(), height = size.height)
                            )
                        }
                    }
                    .clickable { onCategorySelected(category) }
            ) {
                Text(
                    modifier = Modifier
                        .padding(
                            start = if (isSelected) 12.dp else 16.dp,
                            top = 14.dp,
                            bottom = 14.dp
                        ),
                    text = findLabelForCategory(category),
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isSelected) green_primary else grey900
                )
            }
            HorizontalDivider(
                modifier = Modifier
                    .align(Alignment.End)
                    .width(104.dp),
                color = grey300,
                thickness = 1.dp
            )
        }
    }
}

@Composable
fun findLabelForCategory(category: CategoryOption): String {
    return when (category) {
        CategoryOption.SORT_BY -> stringResource(resource = Res.string.sort_by)
        CategoryOption.REMINDER_DATE -> stringResource(resource = Res.string.reminder_date)
    }
}

@Composable
fun FilterOptionsRadioButtonsUi(
    modifier: Modifier = Modifier,
    selectedOption: SortOption,
    onOptionSelected: (SortOption) -> Unit,
    optionsList: List<SortOption>,
) {
    Column(modifier = modifier) {
        optionsList.forEach { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOptionSelected(option) },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.padding(
                        start = 16.dp,
                        top = 16.dp,
                        bottom = 18.dp
                    ),
                    text = findLabelForSortOption(option),
                    style = MaterialTheme.typography.bodyMedium,
                )

                RadioButton(
                    modifier = Modifier.padding(end = 4.dp),
                    selected = selectedOption == option,
                    colors = RadioButtonDefaults.colors(
                        selectedColor = green_primary,
                        unselectedColor = grey900
                    ),
                    onClick = { onOptionSelected(option) },
                )
            }
        }
    }
}

@Composable
fun FilterOptionsCheckboxUi(
    optionsList: List<ReminderFilterOption>,
    selectedOptions: Set<ReminderFilterOption>,
    modifier: Modifier = Modifier,
    onOptionSelected: (ReminderFilterOption) -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .background(white)
            .fillMaxHeight(),
        userScrollEnabled = true,
    ) {
        optionsList.forEach { item ->
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOptionSelected(item) },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.padding(
                            start = 16.dp,
                            top = 18.dp,
                            bottom = 18.dp
                        ),
                        text = findLabelForReminderOption(item),
                        style = MaterialTheme.typography.bodyMedium,
                    )

                    Checkbox(
                        checked = selectedOptions.contains(item),
                        onCheckedChange = { _ -> onOptionSelected(item) },
                        colors = CheckboxDefaults.colors(
                            checkedColor = green_primary,
                            uncheckedColor = MaterialTheme.colorScheme.onSurface.copy(
                                alpha = 0.6f
                            ),
                            checkmarkColor = MaterialTheme.colorScheme.surface
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun findLabelForReminderOption(option: ReminderFilterOption): String {
    return when (option) {
        ReminderFilterOption.TODAY -> stringResource(resource = Res.string.today)
        ReminderFilterOption.OVERDUE -> stringResource(resource = Res.string.pending)
        ReminderFilterOption.UPCOMING -> stringResource(resource = Res.string.upcoming)
    }
}

@Composable
fun Footer(onApplyClicked: () -> Unit, onClearClicked: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Button(
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
            border = BorderStroke(1.dp, green_lite_1),
            colors = ButtonDefaults.buttonColors(containerColor = green_lite),
            onClick = onClearClicked
        ) {
            Text(text = stringResource(Res.string.clear), style = MaterialTheme.typography.labelLarge)
        }

        Button(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = green_primary),
            onClick = onApplyClicked
        ) {
            Text(
                text = stringResource(resource = Res.string.cta_apply),
                style = MaterialTheme.typography.labelLarge,
                color = white
            )
        }
    }
}

@Composable
fun SortAndFilterHeader(tab: HomeTab, onDismissClicked: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = if (tab.isCustomerTab()) {
                stringResource(resource = Res.string.filter)
            } else {
                stringResource(resource = Res.string.sort_by)
            },
            modifier = Modifier
                .weight(1.0f)
                .padding(16.dp),
            style = MaterialTheme.typography.titleLarge
        )

        IconButton(onClick = onDismissClicked) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "")
        }
    }
}

@Composable
@Preview
fun SortAndFilterBottomSheetPreview() {
    OkCreditTheme {
        SortAndFilterBottomSheet(
            selectedSortByOption = SortOption.LAST_ACTIVITY,
            selectedReminderFilters = emptySet(),
            onApplyClicked = { _, _ -> },
            onDismissClicked = { },
            currentTab = HomeTab.SUPPLIER_TAB
        )
    }
}
