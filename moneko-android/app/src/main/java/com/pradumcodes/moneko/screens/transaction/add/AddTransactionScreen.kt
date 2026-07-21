package com.pradumcodes.moneko.screens.transaction.add

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pradumcodes.moneko.R
import com.pradumcodes.moneko.ui.theme.MonekoTheme
import com.pradumcodes.moneko.ui.theme.Spacing

enum class AddTransactionType(val title: String, val iconRes: Int?, val iconVector: androidx.compose.ui.graphics.vector.ImageVector?) {
    Expense("Expense", null, Icons.Default.KeyboardArrowDown),
    Income("Income", null, Icons.Default.KeyboardArrowUp),
    Loan("Loan", R.drawable.handshake_24px, null)
}

data class AddTransactionUiState(
    val amount: String = "",
    val type: AddTransactionType = AddTransactionType.Expense,
    val currentBalance: String = "10,000",
    val category: String = "Shopping",
    val date: String = "Today",
    val time: String = "08:15 PM",
    val note: String = "",
    val loanType: String = "Lent",
    val personName: String = "",
    val dueDate: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    onBackClick: () -> Unit = {}
) {
    var uiState by remember { mutableStateOf(AddTransactionUiState()) }
    val sheetState = rememberModalBottomSheetState()
    var showCategorySheet by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Spacing.md)
                    .statusBarsPadding(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back")
                }
                Spacer(modifier = Modifier.width(Spacing.sm))
                Text(
                    text = "Add Transaction",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        },
        bottomBar = {
            SaveButton(
                transactionType = uiState.type,
                onClick = { /* Save */ }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState)
                .padding(horizontal = Spacing.md)
        ) {
            // Transaction Type Selector
            TransactionTypeSelector(
                selectedType = uiState.type,
                onTypeSelected = { uiState = uiState.copy(type = it) }
            )

            Spacer(modifier = Modifier.height(Spacing.xl))

            // Amount Input
            AmountInput(
                amount = uiState.amount,
                onAmountChange = { uiState = uiState.copy(amount = it) }
            )

            Spacer(modifier = Modifier.height(Spacing.xl))

            // Main Form Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                ),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = SolidColor(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                )
            ) {
                Column {
                    FormListItem(
                        iconRes = R.drawable.home_24px,
                        label = "Current Balance",
                        value = "₹${uiState.currentBalance}",
                        onClick = { /* Select account */ }
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(start = 72.dp),
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                    )
                    FormListItem(
                        iconRes = R.drawable.cat_groceries, // Shopping placeholder
                        label = "Category",
                        value = uiState.category,
                        onClick = { showCategorySheet = true }
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(start = 72.dp),
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                    )
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Box(modifier = Modifier.weight(1f)) {
                            FormListItem(
                                iconVector = Icons.Outlined.DateRange,
                                label = "Date",
                                value = uiState.date,
                                onClick = { /* Date picker */ }
                            )
                        }
                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(56.dp)
                                .align(Alignment.CenterVertically)
                                .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                        )
                        Box(modifier = Modifier.weight(1f)) {
                            FormListItem(
                                iconVector = Icons.Outlined.DateRange, // No separate schedule icon easily found
                                label = "Time",
                                value = uiState.time,
                                onClick = { /* Time picker */ }
                            )
                        }
                    }
                    HorizontalDivider(
                        modifier = Modifier.padding(start = 72.dp),
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(Spacing.md),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Outlined.Edit,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(Spacing.md))
                        BasicTextField(
                            value = uiState.note,
                            onValueChange = { uiState = uiState.copy(note = it) },
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = MaterialTheme.typography.bodyLarge.copy(
                                color = MaterialTheme.colorScheme.onSurface
                            ),
                            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                            decorationBox = { innerTextField ->
                                if (uiState.note.isEmpty()) {
                                    Text(
                                        "Add a note",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.outline
                                    )
                                }
                                innerTextField()
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(Spacing.md))

            // Loan Layout (Dynamic)
            AnimatedVisibility(visible = uiState.type == AddTransactionType.Loan) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                    ),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = SolidColor(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                    )
                ) {
                    Column {
                        // Loan Type Toggle
                        Row(
                            modifier = Modifier
                                .padding(Spacing.sm)
                                .fillMaxWidth()
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                                .padding(4.dp)
                        ) {
                            listOf("You Lent", "You Borrowed").forEach { type ->
                                val isSelected = uiState.loanType == type
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(CircleShape)
                                        .background(if (isSelected) MaterialTheme.colorScheme.surfaceVariant else Color.Transparent)
                                        .clickable { uiState = uiState.copy(loanType = type) }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = type,
                                        style = MaterialTheme.typography.labelLarge,
                                        color = if (isSelected) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                                    )
                                }
                            }
                        }

                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))

                        FormInputItem(
                            iconVector = Icons.Outlined.Person,
                            placeholder = "Person Name",
                            value = uiState.personName,
                            onValueChange = { uiState = uiState.copy(personName = it) }
                        )

                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))

                        FormInputItem(
                            iconVector = Icons.Outlined.DateRange,
                            placeholder = "Due Date",
                            value = uiState.dueDate,
                            onValueChange = { uiState = uiState.copy(dueDate = it) }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(Spacing.xl * 2))
        }

        if (showCategorySheet) {
            CategoryBottomSheet(
                onDismiss = { showCategorySheet = false },
                onCategorySelected = {
                    uiState = uiState.copy(category = it)
                    showCategorySheet = false
                },
                sheetState = sheetState
            )
        }
    }
}

@Composable
fun TransactionTypeSelector(
    selectedType: AddTransactionType,
    onTypeSelected: (AddTransactionType) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
            .padding(4.dp)
    ) {
        AddTransactionType.entries.forEach { type ->
            val isSelected = selectedType == type
            val backgroundColor by animateColorAsState(
                if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
                label = "backgroundColor"
            )
            val contentColor by animateColorAsState(
                if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
                label = "contentColor"
            )

            Row(
                modifier = Modifier
                    .weight(1f)
                    .clip(CircleShape)
                    .background(backgroundColor)
                    .clickable { onTypeSelected(type) }
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (type.iconRes != null) {
                    Icon(
                        painter = painterResource(type.iconRes),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = contentColor
                    )
                } else if (type.iconVector != null) {
                    Icon(
                        type.iconVector,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = contentColor
                    )
                }
                Spacer(modifier = Modifier.width(Spacing.sm))
                Text(
                    text = type.title,
                    style = MaterialTheme.typography.labelLarge,
                    color = contentColor
                )
            }
        }
    }
}

@Composable
fun AmountInput(
    amount: String,
    onAmountChange: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            text = "₹",
            style = MaterialTheme.typography.displayLarge.copy(
                fontSize = 40.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Spacer(modifier = Modifier.width(Spacing.sm))
        BasicTextField(
            value = amount,
            onValueChange = {
                if (it.isEmpty() || it.all { char -> char.isDigit() || char == '.' }) {
                    onAmountChange(it)
                }
            },
            modifier = Modifier.width(IntrinsicSize.Min),
            textStyle = MaterialTheme.typography.displayLarge.copy(
                fontSize = 48.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            decorationBox = { innerTextField ->
                if (amount.isEmpty()) {
                    Text(
                        "0.00",
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontSize = 48.sp,
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                    )
                }
                innerTextField()
            }
        )
    }
}

@Composable
fun FormListItem(
    iconRes: Int? = null,
    iconVector: androidx.compose.ui.graphics.vector.ImageVector? = null,
    label: String,
    value: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(Spacing.md),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            if (iconRes != null) {
                Icon(
                    painter = painterResource(iconRes),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.size(24.dp)
                )
            } else if (iconVector != null) {
                Icon(
                    iconVector,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        Spacer(modifier = Modifier.width(Spacing.md))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun FormInputItem(
    iconVector: androidx.compose.ui.graphics.vector.ImageVector,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Spacing.md),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            iconVector,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(Spacing.md))
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onSurface
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            decorationBox = { innerTextField ->
                if (value.isEmpty()) {
                    Text(
                        placeholder,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
                innerTextField()
            }
        )
    }
}

@Composable
fun SaveButton(
    transactionType: AddTransactionType,
    onClick: () -> Unit
) {
    val containerColor = when (transactionType) {
        AddTransactionType.Expense -> MaterialTheme.colorScheme.error
        AddTransactionType.Income -> MaterialTheme.colorScheme.primaryContainer
        AddTransactionType.Loan -> MaterialTheme.colorScheme.tertiaryContainer
    }
    val contentColor = when (transactionType) {
        AddTransactionType.Expense -> MaterialTheme.colorScheme.onError
        AddTransactionType.Income -> MaterialTheme.colorScheme.onPrimaryContainer
        AddTransactionType.Loan -> MaterialTheme.colorScheme.onTertiaryContainer
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        MaterialTheme.colorScheme.background.copy(alpha = 0.8f),
                        MaterialTheme.colorScheme.background
                    )
                )
            )
            .padding(Spacing.md)
            .padding(bottom = Spacing.sm)
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = containerColor,
                contentColor = contentColor
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
        ) {
            Icon(Icons.Outlined.CheckCircle, contentDescription = null)
            Spacer(modifier = Modifier.width(Spacing.sm))
            Text(
                text = "Add ${transactionType.title}",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryBottomSheet(
    onDismiss: () -> Unit,
    onCategorySelected: (String) -> Unit,
    sheetState: SheetState
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .width(40.dp)
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.outlineVariant)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.lg)
                .padding(bottom = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Select Category",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(Spacing.lg))
            
            val categories = listOf(
                "Food" to R.drawable.cat_food,
                "Transport" to R.drawable.cat_transport,
                "Groceries" to R.drawable.cat_groceries,
                "Health" to R.drawable.cat_health,
                "Rent" to R.drawable.cat_rent,
                "Utilities" to R.drawable.cat_utilities,
                "Salary" to R.drawable.cat_salary,
                "Business" to R.drawable.cat_business,
                "Entertainment" to R.drawable.cat_entertainment
            )

            Column(verticalArrangement = Arrangement.spacedBy(Spacing.md)) {
                for (i in categories.indices step 4) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        for (j in 0 until 4) {
                            if (i + j < categories.size) {
                                val category = categories[i + j]
                                CategoryItem(
                                    name = category.first,
                                    iconRes = category.second,
                                    onClick = { onCategorySelected(category.first) }
                                )
                            } else {
                                Spacer(modifier = Modifier.size(72.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryItem(
    name: String,
    iconRes: Int,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(Spacing.xs),
        verticalArrangement = Arrangement.spacedBy(Spacing.sm)
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceContainerHigh),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = Color.Unspecified
            )
        }
        Text(
            text = name,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
@Preview(showBackground = true)
fun AddTransactionScreenPreview() {
    MonekoTheme {
        AddTransactionScreen()
    }
}
