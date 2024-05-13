package com.example.jetspend3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jetspend3.ui.theme.JetSpend3Theme
import androidx.compose.ui.Alignment
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import android.content.pm.ActivityInfo

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        enableEdgeToEdge()
        setContent {
            JetSpend3Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ExpenseTracker(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}
data class Expense(val description: String, val amount: Double, val category: String)
@Composable
fun ExpenseTracker(modifier: Modifier = Modifier) {
    var expenses by remember { mutableStateOf(listOf<Expense>()) }

    Column(modifier = modifier.padding(16.dp)) {
        ExpenseInput(onAddExpense = { expense -> expenses = expenses + expense })
        Spacer(modifier = Modifier.height(16.dp))
        ExpenseList(expenses = expenses, onRemoveExpense = { expense -> expenses = expenses - expense })
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Suma:${expenses.sumByDouble { it.amount }} zł", style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun ExpenseInput(onAddExpense: (Expense) -> Unit) {
    var description by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }

    Column {
        OutlinedTextField(value = description, onValueChange = { description = it },
            label = { Text("Opis") })
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Cena w zł") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = category, onValueChange = { category = it },
            label = { Text("Kategoria") })
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            if (description.isNotEmpty() && amount.isNotEmpty() && category.isNotEmpty()) {
                onAddExpense(Expense(description, amount.toDouble(), category))
                description = ""
                amount = ""
                category = ""
            }
        }) {
            Text("Dodaj wydatek")
        }
    }
}
@Composable
fun ExpenseList(expenses: List<Expense>, onRemoveExpense: (Expense) -> Unit) {
    LazyColumn {
        items(expenses) { expense ->
            ExpenseItem(expense = expense, onRemoveExpense = onRemoveExpense)
            Text(text = "Kategoria: ${expense.category}", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JetSpend3Theme {
        ExpenseTracker()
    }
}