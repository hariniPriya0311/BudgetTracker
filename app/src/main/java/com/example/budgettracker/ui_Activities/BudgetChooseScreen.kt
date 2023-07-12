package com.example.budgettracker.ui_Activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.budgettracker.SheetfunctionsListView
import com.example.budgettracker.Values.budgetName
import com.example.budgettracker.Values.budgetsAndResourseId
import com.example.budgettracker.Values.resource_Id
import com.example.budgettracker.ui.theme.BudgetTrackerTheme
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject

class BudgetChooseScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BudgetTrackerTheme {
                val receivedArray = budgetsAndResourseId
                if(budgetsAndResourseId.length() == 0){
                    Log.d("finalTest","resource empty")
//                    SheetfunctionsListView().listAllWorkbooks(LocalContext.current){}
                }
                ShowBudgets(receivedArray)
            }
        }
    }

    @Composable
    fun ShowBudgets(budgetNames: JSONObject) {
        Surface(
            Modifier
                .fillMaxSize()
        ) {
            val context = LocalContext.current
            LazyColumn {
                items(budgetNames.length()) { index ->
                    val key = budgetNames.keys().asSequence().elementAt(index)
                    Card(
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxWidth()
                            .clickable { getResourceId(key, context) },
                    ) {
                        Text(
                            text = key, modifier = Modifier.padding(12.dp), fontSize = 16.sp, style = TextStyle(color = Color.Black)
                        )
                    }
                }
            }
        }
    }

    fun getResourceId(workBookName: String, context: Context) {
        budgetName = workBookName
        resource_Id = budgetsAndResourseId.getString(workBookName)
        GlobalScope.launch {
            val intent = Intent(context, ListViewScreen::class.java)
            context.startActivity(intent)
            finish()
        }
    }
}