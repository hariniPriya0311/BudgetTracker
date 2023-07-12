package com.example.budgettracker.ui_Activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.budgettracker.SheetfunctionsListView
import com.example.budgettracker.Values
import com.example.budgettracker.ui.theme.BudgetTrackerTheme
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class ListViewScreen : AppCompatActivity() {


    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BudgetTrackerTheme {
                Checking()
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    @Composable
    fun Checking() {
        val url = "${Values.API_BaseUrl}workbooks"
        val requestBody =
            "method=workbook.list".toRequestBody("application/x-www-form-urlencoded".toMediaType())
        val workbookMap = JSONObject()
        SheetfunctionsListView().MakeRequests(url, requestBody) { it ->
            val jsonObject = JSONObject(it)
            val workbooks = jsonObject.getJSONArray("workbooks")
            for (i in 0 until workbooks.length()) {
                val workbook = workbooks.getJSONObject(i)
                val workbookName = workbook.getString("workbook_name")
                val workbookId = workbook.getString("resource_id")
                workbookMap.put(workbookName, workbookId)
            }
            Values.budgetsAndResourseId = workbookMap
        }
        val contentList = remember { mutableStateListOf<String>() }
        val amountList = remember { mutableStateListOf<String>() }
        val isLoading = remember { mutableStateOf(true) }
        val sizeOfExpenseList = remember {
            mutableStateOf("0")
        }
        val sizeOfIncomeList = remember {
            mutableStateOf("0")
        }
        val budget = remember {
            mutableStateOf("")
        }
        val totalIncome = remember {
            mutableStateOf("None")
        }
        val totalExpense = remember {
            mutableStateOf("None")
        }

        LaunchedEffect(key1 = Unit, block = {
            getContent(
                contentList,
                amountList,
                isLoading,
                budget,
                totalIncome,
                totalExpense,
                sizeOfExpenseList,
                sizeOfIncomeList
            )

        })

        val context = LocalContext.current
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.background(Color.White)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Text(
                            text = "Title",
                            modifier = Modifier.padding(4.dp, 0.dp),
                            style = TextStyle(fontSize = 16.sp),
                        )
                        Text(
                            text = Values.budgetName,
                            modifier = Modifier.padding(16.dp, 0.dp),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    Column(
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Text(
                            text = "Total Budget",
                            modifier = Modifier.padding(4.dp, 0.dp),
                            style = TextStyle(fontSize = 16.sp),
                        )
                        Text(
                            text = budget.value,
                            modifier = Modifier.padding(16.dp, 0.dp),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    if (isLoading.value) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.primary, strokeWidth = 4.dp
                            )
                            Text(
                                text = "Loading...",
                                color = Color.Gray,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    } else {
                        if (amountList.size == 0) {
                            Column(
                                modifier = Modifier
                                    .padding(4.dp)
                                    .fillMaxHeight()
                                    .align(Alignment.Center),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "No expenses or income",
                                    modifier = Modifier.padding(12.dp),
                                    style = TextStyle(fontSize = 16.sp)
                                )
                                Text(
                                    text = "Click the Add button to create new expenses and incomes",
                                    modifier = Modifier
                                        .padding(12.dp)
                                        .align(Alignment.CenterHorizontally),
                                    style = TextStyle(fontSize = 12.sp)
                                )
                            }
                        } else {
                            if (sizeOfExpenseList.value.toInt() < 1 && sizeOfIncomeList.value.toInt() >= 1) {
                                Column(
                                    Modifier.padding(8.dp),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Row(
                                        modifier = Modifier.padding(4.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(text = "Total Income : ")
                                        Text(text = totalIncome.value)
                                    }
                                    Row(
                                        modifier = Modifier.padding(4.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(text = "Total Expense : ")
                                        Text(text = totalExpense.value)
                                    }
                                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                                        items(count = amountList.size) { i ->
                                            if (i == 0) {
                                                Text(
                                                    text = "Income",
                                                    modifier = Modifier.padding(8.dp),
                                                    fontWeight = FontWeight.Bold,
                                                    style = TextStyle(fontSize = 18.sp)
                                                )
                                            } else {
                                                Card(
                                                    modifier = Modifier.padding(
                                                        8.dp, 4.dp, 8.dp, 0.dp
                                                    )
                                                ) {
                                                    Column {
                                                        Row(
                                                            modifier = Modifier
                                                                .padding(8.dp)
                                                                .fillMaxWidth(),
                                                            horizontalArrangement = Arrangement.SpaceBetween
                                                        ) {
                                                            Text(
                                                                text = contentList[i],
                                                                modifier = Modifier.padding(4.dp),
                                                                style = TextStyle(fontSize = 16.sp)
                                                            )
                                                            Text(
                                                                text = amountList[i],
                                                                modifier = Modifier.padding(4.dp),
                                                                style = TextStyle(fontSize = 16.sp)
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                            } else if (sizeOfExpenseList.value.toInt() >= 1 && sizeOfIncomeList.value.toInt() < 1) {
                                Column(
                                    Modifier.padding(8.dp),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Row(
                                        modifier = Modifier.padding(4.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(text = "Total Income : ")
                                        Text(text = totalIncome.value)
                                    }
                                    Row(
                                        modifier = Modifier.padding(4.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(text = "Total Expense : ")
                                        Text(text = totalExpense.value)
                                    }
                                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                                        items(count = amountList.size) { i ->
                                            if (i == 0) {
                                                Text(
                                                    text = "Expenses",
                                                    modifier = Modifier.padding(8.dp),
                                                    fontWeight = FontWeight.Bold,
                                                    style = TextStyle(fontSize = 18.sp)
                                                )
                                            } else {
                                                Card(
                                                    modifier = Modifier.padding(
                                                        8.dp, 4.dp, 8.dp, 0.dp
                                                    )
                                                ) {
                                                    Column {
                                                        Row(
                                                            modifier = Modifier
                                                                .padding(8.dp)
                                                                .fillMaxWidth(),
                                                            horizontalArrangement = Arrangement.SpaceBetween
                                                        ) {
                                                            Text(
                                                                text = contentList[i],
                                                                modifier = Modifier.padding(4.dp),
                                                                style = TextStyle(fontSize = 16.sp)
                                                            )
                                                            Text(
                                                                text = amountList[i],
                                                                modifier = Modifier.padding(4.dp),
                                                                style = TextStyle(fontSize = 16.sp)
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            if ((sizeOfExpenseList.value.toInt() >= 1 && sizeOfIncomeList.value.toInt() >= 1)) {
                                Column(
                                    Modifier.padding(8.dp),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Row(
                                        modifier = Modifier.padding(4.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(text = "Total Income : ")
                                        Text(text = totalIncome.value)
                                    }
                                    Row(
                                        modifier = Modifier.padding(4.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(text = "Total Expense : ")
                                        Text(text = totalExpense.value)
                                    }
                                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                                        items(count = amountList.size) { i ->
                                            if (i == 0) {
                                                Text(
                                                    text = "Expenses",
                                                    modifier = Modifier.padding(8.dp),
                                                    fontWeight = FontWeight.Bold,
                                                    style = TextStyle(fontSize = 18.sp)
                                                )
                                            } else if (i == sizeOfExpenseList.value.toInt() + 1) {
                                                Text(
                                                    text = "Incomes",
                                                    modifier = Modifier.padding(8.dp),
                                                    fontWeight = FontWeight.Bold,
                                                    style = TextStyle(fontSize = 18.sp)
                                                )
                                            } else {
                                                Card(
                                                    modifier = Modifier.padding(
                                                        8.dp, 4.dp, 8.dp, 0.dp
                                                    )
                                                ) {
                                                    Column {
                                                        Row(
                                                            modifier = Modifier
                                                                .padding(8.dp)
                                                                .fillMaxWidth(),
                                                            horizontalArrangement = Arrangement.SpaceBetween
                                                        ) {
                                                            Text(
                                                                text = contentList[i],
                                                                modifier = Modifier.padding(4.dp),
                                                                style = TextStyle(fontSize = 16.sp)
                                                            )
                                                            Text(
                                                                text = amountList[i],
                                                                modifier = Modifier.padding(4.dp),
                                                                style = TextStyle(fontSize = 16.sp)
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    FloatingActionButton(
                        onClick = { changeScreen(context) },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(top = 16.dp, bottom = 16.dp, end = 16.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add")
                    }
                }
            }
        }
    }


    private fun changeScreen(context: Context) {
        val intent = Intent(context, EditScreen::class.java)
        context.startActivity(intent)
        finish()
    }

    private fun getContent(
        contentList: MutableList<String>,
        amountList: MutableList<String>,
        isLoading: MutableState<Boolean>,
        budget: MutableState<String>,
        totalIncome: MutableState<String>,
        totalExpense: MutableState<String>,
        sizeOfExpenseList: MutableState<String>,
        sizeOfIncomeList: MutableState<String>,

        ) {
        GlobalScope.launch {
            delay(3000)
            SheetfunctionsListView().getRow("1", "1") { it ->
                Values.lastExpense = it
                val url = "${Values.API_BaseUrl}${Values.resource_Id}"
                val postData =
                    "method=range.content.get&worksheet_name=${Values.inputSheet}&start_row=7&start_column=2&end_row=${Values.lastExpense}&end_column=3".toRequestBody(
                        "application/x-www-form-urlencoded".toMediaType()
                    )
                SheetfunctionsListView().MakeRequests(url, postData) {
                    getContentValues(it, contentList, amountList, sizeOfExpenseList)
                }
                SheetfunctionsListView().getRow("1", "2") { it ->
                    Values.lastIncome = it
                    val url = "${Values.API_BaseUrl}${Values.resource_Id}"
                    val postData =
                        "method=range.content.get&worksheet_name=${Values.inputSheet}&start_row=7&start_column=8&end_row=${Values.lastIncome}&end_column=9".toRequestBody(
                            "application/x-www-form-urlencoded".toMediaType()
                        )
                    SheetfunctionsListView().MakeRequests(url, postData) {
                        getContentValues(it, contentList, amountList, sizeOfIncomeList)
                        SheetfunctionsListView().getRow("1", "7") {
                            budget.value = it
                            Values.budget = it
                            SheetfunctionsListView().getRow("1", "5") {
                                if (it.isNotEmpty()) {
                                    totalIncome.value = it
                                } else {
                                    totalIncome.value = "None"
                                }
                                SheetfunctionsListView().getRow("1", "6") {
                                    if (it.isNotEmpty()) {
                                        totalExpense.value = it
                                    } else {
                                        totalExpense.value = "None"
                                    }
                                    isLoading.value = false
                                }
                            }
                        }
                    }
                }
            }


        }
    }


    private fun getContentValues(
        jsonString: String,
        contentList: MutableList<String>,
        amountList: MutableList<String>,
        sizeVariable: MutableState<String>
    ) {
        try {
            val jsonObject = JSONObject(jsonString)
            val rangeDetails = jsonObject.getJSONArray("range_details")
            if (rangeDetails.length() != 0) {
                contentList.add("")
                amountList.add("")
                for (i in 0 until rangeDetails.length()) {
                    val rowDetails = rangeDetails.getJSONObject(i).getJSONArray("row_details")
                    contentList.add(rowDetails.getJSONObject(0).getString("content"))
                    Log.d("finalTest",rowDetails.getJSONObject(0).getString("content")).toString()
                    amountList.add(rowDetails.getJSONObject(1).getString("content"))
                }
            }
            sizeVariable.value = rangeDetails.length().toString()
        } catch (e: Exception) {
            Log.e("functionCall", "Error parsing JSON: ${e.message}")
        }
    }

    @Preview
    @Composable
    fun Previewing() {
        Checking()
    }

    override fun onBackPressed() {
        Log.d("finalTest", "Back pressed --------")

        val intent = Intent(this, BudgetChooseScreen::class.java)
        startActivity(intent)
        finish()
    }
}


