package com.example.budgettracker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.budgettracker.ui.theme.BudgetTrackerTheme
import com.example.budgettracker.ui_Activities.NewBudget
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BudgetTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    CheckUser()
                }
            }
        }
    }
}


@Composable
fun CheckUser() {
    val context = LocalContext.current
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.Center)
        ) {
            Image(
                painter = painterResource(id = R.drawable.main_page_image),
                contentDescription = "Welcome Image",
                modifier = Modifier
                    .size(300.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
                    .clip(MaterialTheme.shapes.medium)
            ) {
                Column(Modifier.padding(15.dp)) {
                    Text(
                        text = stringResource(R.string.intro_heading),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                    Text(
                        text = stringResource(R.string.intro_content)
                    )
                    if (false) {
                        Button(onClick = {
                            moveToCreateBudgetScreen(context)
                        },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp),
                            content = {
                                Text(
                                    text = "Create Budget",
                                    )
                            })
                    } else {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Button(onClick = {
                                moveToCreateBudgetScreen(context)
                            },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 2.dp),
                                content = {
                                    Text(
                                        text = " Create Budget",
                                        )
                                })
                            Button(onClick = {
                                listingWorkbooks(context)
                            },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 2.dp),
                                content = {
                                    Text(
                                        text = "Go to an existing budget",
                                        )
                                })
                        }
                    }
                }
            }
        }
    }
}


@OptIn(DelicateCoroutinesApi::class)
fun listingWorkbooks(context: Context) {
    GlobalScope.launch() {
        val obj = SheetfunctionsListView()
        obj.workBookShelfCheck(context)
    }
}


fun moveToCreateBudgetScreen(context: Context) {
    Log.d("functionCall", "moveToCreateBudgetScreen")
    val intent = Intent(context, NewBudget::class.java)
    context.startActivity(intent)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BudgetTrackerTheme {
        CheckUser()
    }
}