package com.example.budgettracker.ui_Activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import com.example.budgettracker.SheetfunctionsListView
import com.example.budgettracker.Values
import com.example.budgettracker.ui.theme.BudgetTrackerTheme
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class NewBudget : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BudgetTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    GetBudgetName()
                }
            }
        }
    }


    @OptIn(ExperimentalMaterial3Api::class, DelicateCoroutinesApi::class)
    @Composable
    fun GetBudgetName() {
        val creatingWorkbook = remember {
            mutableStateOf(false)
        }
        val context = LocalContext.current
        var workBookName by remember { mutableStateOf("") }
        if (creatingWorkbook.value) {
            Box {
                Column {
                    TextField(value = workBookName,
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = MaterialTheme.colorScheme.onSurface, // Text color when typing
                            disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.20f), // Text color when disabled
                            placeholderColor = Color.Red, // Hint text color
                            focusedIndicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.40f), // Color of the underline when focused
                            unfocusedIndicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.20f), // Color of the underline when not focused
                            disabledIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.40f) // Color of the underline when disabled
                        ),
                        onValueChange = { workBookName = it },
                        label = { Text("Budget name") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp, 8.dp)
                    )
                    Button(onClick = {},
                        modifier = Modifier
                            .padding(16.dp, 4.dp)
                            .align(Alignment.Start),
                        content = {
                            Text(text = "Create")
                        })
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary, strokeWidth = 4.dp
                        )
                        Text(
                            text = "Creating new Budget...",
                            color = Color.Gray,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }
        } else {
            Box {
                Column {
                    TextField(value = workBookName,
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = MaterialTheme.colorScheme.onSurface, // Text color when typing
                            disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.20f), // Text color when disabled
                            placeholderColor = Color.Red, // Hint text color
                            focusedIndicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.40f), // Color of the underline when focused
                            unfocusedIndicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.20f), // Color of the underline when not focused
                            disabledIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.40f) // Color of the underline when disabled
                        ),
                        singleLine = true,
                        onValueChange = { workBookName = it },
                        label = { Text("Budget name") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp, 8.dp)
                    )
                    Button(onClick = {
                        if (workBookName.isNotEmpty()) {
                            GlobalScope.launch {
                                createWorkBook(workBookName, context)
                            }
                            Values.budgetName = workBookName
                            Log.d("tension", "creating workbook")
                            creatingWorkbook.value = true
                        }
                    }, modifier = Modifier
                        .padding(16.dp, 4.dp)
                        .align(Alignment.Start),
                        content = {
                            Text(text = "Create")
                        })
                }
            }
        }
    }


    fun createWorkBook(name: String, context: Context) {
        Values.budgetName = name
        val url = "${Values.API_BaseUrl}create"
        val requestBody =
            "method=workbook.create&workbook_name=$name".toRequestBody("application/x-www-form-urlencoded".toMediaType())
        SheetfunctionsListView().MakeRequests(url, requestBody) { it ->
            GlobalScope.launch {
                finish()
                val intent = Intent(context, ListViewScreen::class.java)
                context.startActivity(intent)
            }
            val resourceId = JSONObject(it).getString("resource_id")
            Values.resource_Id = resourceId
            SheetfunctionsListView().createWorkSheets(Values.summarySheet)
            SheetfunctionsListView().createWorkSheets(Values.inputSheet)
        }
    }
}