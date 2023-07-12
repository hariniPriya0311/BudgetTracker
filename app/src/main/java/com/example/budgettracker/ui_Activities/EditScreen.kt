package com.example.budgettracker.ui_Activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.budgettracker.SheetfunctionsListView
import com.example.budgettracker.Values
import com.example.budgettracker.ui.theme.BudgetTrackerTheme
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class EditScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BudgetTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    CreateScreen()
                }
            }
        }
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun CreateScreen() {
        val buttonText = remember { mutableStateOf("Add Category") }
        val context = LocalContext.current
        var selectedOption by remember { mutableStateOf(true) }
        var description by remember { mutableStateOf("") }
        var date by remember { mutableStateOf("") }
        var amount by remember { mutableStateOf("") }
        var category by remember { mutableStateOf("") }

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopStart) {
            Column(
                modifier = Modifier
                    .padding(6.dp)
                    .align(Alignment.TopStart)

            ) {
                Text(
                    text = "Add new expense / income",
                    modifier = Modifier.padding(12.dp),
                    style = TextStyle(fontSize = 28.sp)
                )
                Row(
                    modifier = Modifier.padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Row {
                        RadioButton(selected = selectedOption, onClick = { selectedOption = true })
                        Text(
                            text = "Expense",
                            modifier = Modifier.padding(4.dp, 12.dp),
                            style = TextStyle(fontSize = 20.sp)
                        )
                    }
                    Row {
                        RadioButton(selected = !selectedOption,
                            onClick = { selectedOption = false })
                        Text(
                            text = "Income",
                            modifier = Modifier.padding(4.dp, 12.dp),
                            style = TextStyle(fontSize = 20.sp)
                        )
                    }
                }
                TextField(value = description,
                    modifier = Modifier
                        .padding(12.dp, 20.dp, 12.dp, 0.dp)
                        .fillMaxWidth(),
                    label = { Text("Description") },
                    singleLine = true,
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = MaterialTheme.colorScheme.onSurface, // Text color when typing
                        disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.20f), // Text color when disabled
                        placeholderColor = Color.Red, // Hint text color
                        focusedIndicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.40f), // Color of the underline when focused
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.20f), // Color of the underline when not focused
                        disabledIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.40f) // Color of the underline when disabled
                    ),
                    onValueChange = { description = it })
                TextField(value = date,
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = MaterialTheme.colorScheme.onSurface, // Text color when typing
                        disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.20f), // Text color when disabled
                        placeholderColor = Color.Red, // Hint text color
                        focusedIndicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.40f), // Color of the underline when focused
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.20f), // Color of the underline when not focused
                        disabledIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.40f) // Color of the underline when disabled
                    ),
                    label = { Text("DD/MM/YYYY") },
                    singleLine = true,
                    modifier = Modifier
                        .padding(12.dp, 20.dp, 12.dp, 0.dp)
                        .fillMaxWidth(),
                    onValueChange = { date = it })
                TextField(value = amount,
                    label = { Text("Amount") },
                    singleLine = true,
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = MaterialTheme.colorScheme.onSurface, // Text color when typing
                        disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.20f), // Text color when disabled
                        placeholderColor = Color.Red, // Hint text color
                        focusedIndicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.40f), // Color of the underline when focused
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.20f), // Color of the underline when not focused
                        disabledIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.40f) // Color of the underline when disabled
                    ),
                    modifier = Modifier
                        .padding(12.dp, 20.dp, 12.dp, 0.dp)
                        .fillMaxWidth(),
                    onValueChange = { amount = it })
                var isExpanded by remember { mutableStateOf(false) }
                val items = Values.expenseCategories.toList()
                TextField(value = category,
                    onValueChange = { category = it },
                    singleLine = true,
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = MaterialTheme.colorScheme.onSurface, // Text color when typing
                        disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.20f), // Text color when disabled
                        placeholderColor = Color.Red, // Hint text color
                        focusedIndicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.40f), // Color of the underline when focused
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.20f), // Color of the underline when not focused
                        disabledIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.40f) // Color of the underline when disabled
                    ),
                    label = { Text("Category") },
                    trailingIcon = {
                        Icon(imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Expand",
                            modifier = Modifier.clickable { isExpanded = true })
                    },
                    readOnly = true,
                    modifier = Modifier
                        .padding(12.dp, 20.dp, 12.dp, 0.dp)
                        .fillMaxWidth()
                )
                Box(modifier = Modifier.padding(start = 12.dp)) {
                    DropdownMenu(
                        expanded = isExpanded,
                        onDismissRequest = { isExpanded = false },
                        modifier = Modifier
                            .padding(horizontal = 20.dp, vertical = 10.dp)
                            .width(200.dp)
                    ) {
                        items.forEach { item ->
                            DropdownMenuItem(text = { Text(text = item) }, onClick = {
                                isExpanded = false
                                category = item
                                buttonText.value = item
                            })
                        }
                    }
                }
                IconButton(onClick = {
                    sendContents(
                        date, description, amount, category, selectedOption, context
                    )
                },
                    modifier = Modifier
                        .padding(0.dp, 20.dp, 24.dp)
                        .size(60.dp)
                        .align(Alignment.End)
                        .background(color = MaterialTheme.colorScheme.primary, shape = CircleShape),
                    content = {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Add",
                            tint = Color.White
                        )
                    })
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun sendContents(
        date: String,
        content: String,
        amount: String,
        category: String,
        selectedOption: Boolean,
        context: Context
    ) {
        GlobalScope.launch {
            val obj = SheetfunctionsListView()
            if (selectedOption) {
                obj.addExpense(date, content, amount, category)
            } else {
                obj.addIncome(date, content, amount, category)
            }
            changeScreen(context)
        }
    }

    fun changeScreen(context: Context) {
        finish()
        val intent = Intent(context, ListViewScreen::class.java)
        context.startActivity(intent)
    }

    override fun onBackPressed() {
        Log.d("finalTest", "Back pressed --------")
        val intent = Intent(this, ListViewScreen::class.java)
        startActivity(intent)
        finish()
    }

    @Preview(showBackground = true)
    @Composable
    fun ShowingPreview() {
        BudgetTrackerTheme {
            CreateScreen()
        }
    }
}