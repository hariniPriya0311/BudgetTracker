package com.example.budgettracker

import android.content.Context
import android.content.Intent
import com.example.budgettracker.Values.API_BaseUrl
import com.example.budgettracker.Values.budgetsAndResourseId
import com.example.budgettracker.Values.inputSheet
import com.example.budgettracker.Values.lastExpense
import com.example.budgettracker.Values.lastIncome
import com.example.budgettracker.Values.oauthtoken
import com.example.budgettracker.Values.resource_Id
import com.example.budgettracker.Values.summarySheet
import com.example.budgettracker.ui_Activities.BudgetChooseScreen
import com.example.budgettracker.ui_Activities.ListViewScreen
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

val summaryObject = SheetFunctionsSummary()

class SheetfunctionsListView {

    fun MakeRequests(
        url: String, requestBody: RequestBody, onResponseReceived: (String) -> Unit
    ) {
        val client = OkHttpClient()
        val request =
            Request.Builder().url(url).header("Authorization", "Zoho-oauthtoken $oauthtoken")
                .post(requestBody).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.let {
                    val temp = it.string()
                    onResponseReceived(temp)
                }
            }
        })
    }


    fun createWorkSheets(name: String) {
        val url = "$API_BaseUrl$resource_Id"
        val requestBody =
            "method=worksheet.insert&worksheet_name=$name".toRequestBody("application/x-www-form-urlencoded".toMediaType())
        MakeRequests(url, requestBody) {
            if (name.equals(inputSheet)) {
                setDefaultContents() {}
                setDefaultContents() {
                    summaryObject.formatting()
                }
                setContents("=(L3-L4)", "5", "12", summarySheet) {}
            }
        }
    }

    fun workBookShelfCheck(context: Context) {
        listAllWorkbooks(context) {
            budgetsAndResourseId = it
            if (budgetsAndResourseId.length() == 1) {
                val intent = Intent(context, ListViewScreen::class.java)
                context.startActivity(intent)
            }
        }
    }


     fun listAllWorkbooks(context: Context, mapReturn: (JSONObject) -> Unit) {
        val url = "${API_BaseUrl}workbooks"
        val requestBody =
            "method=workbook.list".toRequestBody("application/x-www-form-urlencoded".toMediaType())
        val workbookMap = JSONObject()
        MakeRequests(url, requestBody) { it ->
            val jsonObject = JSONObject(it)
            val workbooks = jsonObject.getJSONArray("workbooks")
            for (i in 0 until workbooks.length()) {
                val workbook = workbooks.getJSONObject(i)
                val workbookName = workbook.getString("workbook_name")
                val workbookId = workbook.getString("resource_id")
                workbookMap.put(workbookName, workbookId)
            }
            budgetsAndResourseId = workbookMap
            val intent = Intent(context, BudgetChooseScreen::class.java)
            context.startActivity(intent)
        }
    }


    fun addIncome(date: String, content: String, amount: String, category: String) {
        getRow("1", "2") {
            lastIncome = it
            val url = "${API_BaseUrl}${resource_Id}"
            val requestString =
                "method=row.content.set&worksheet_name=$inputSheet&row=$lastIncome&column_array=[7,8,9,10]&data_array=[\"${date}\",\"${content}\",\"${amount}\",\"${category}\"]".toRequestBody(
                    "application/x-www-form-urlencoded".toMediaType()
                )
            MakeRequests(url, requestString) {


            }
            setRow(lastIncome, "2") {}
            var row = lastIncome.toInt() - 3
            val requestStringSummary =
                "method=row.content.set&worksheet_name=$summarySheet&row=$row&column_array=[6,7,8,9]&data_array=[\"${date}\",\"${content}\",\"${amount}\",\"${category}\"]".toRequestBody(
                    "application/x-www-form-urlencoded".toMediaType()
                )
            MakeRequests(url, requestStringSummary) {}
            val range = "F$row:I$row"
            summaryObject.expenseOrIncomeFormat(lastIncome, range)
            val totalIncome = "=SUM(I7:I$lastIncome)"
            setContents(totalIncome, "1", "5", inputSheet) {}
            val totalSummarySheet = "=SUM(List.I7:List.I$lastIncome)"
            setContents(totalSummarySheet, "3", "12", summarySheet) {}
        }
    }

    private fun setDefaultContents(
        onResponseReceived: () -> Unit
    ) {
        val url = "${API_BaseUrl}${resource_Id}"
        val requestStringOne =
            "method=row.content.set&worksheet_name=$inputSheet&row=6&column_array=[1,2,3,4,7,8,9,10]&data_array=[\"Date\",\"Description\",\"Amount\",\"Category\",\"Date\",\"Description\",\"Amount\",\"Category\"]".toRequestBody(
                "application/x-www-form-urlencoded".toMediaType()
            )
        MakeRequests(url, requestStringOne) {}

        val requestStringTwo =
            "method=row.content.set&worksheet_name=$inputSheet&row=1&column_array=[1,2,7]&data_array=[\"7\",\"7\",\"=(E1-F1)\"]".toRequestBody(
                "application/x-www-form-urlencoded".toMediaType()
            )
        MakeRequests(url, requestStringTwo) {
            onResponseReceived()
        }
        val requestStringThree =
            "method=row.content.set&worksheet_name=$summarySheet&row=3&column_array=[1,2,3,4,6,7,8,9,11]&data_array=[\"Date\",\"Description\",\"Amount\",\"Category\",\"Date\",\"Description\",\"Amount\",\"Category\",\"Total Income\"]".toRequestBody(
                "application/x-www-form-urlencoded".toMediaType()
            )
        MakeRequests(url, requestStringThree) {}
        val requestStringFour =
            "method=row.content.set&worksheet_name=$summarySheet&row=1&column_array=[1,6]&data_array=[\"Expenses\",\"Income\"]".toRequestBody(
                "application/x-www-form-urlencoded".toMediaType()
            )
        MakeRequests(url, requestStringFour) {
            setContents("Expenses", "1", "1", summarySheet) {}
            setContents("Income", "1", "6", summarySheet) {}
            setContents("Total Expenses", "4", "11", summarySheet) {}
            setContents("Budget", "5", "11", summarySheet) {}
        }
    }

    fun addExpense(date: String, content: String, amount: String, category: String) {
        getRow("1", "1") {
            lastExpense = it
        }
        val url = "${API_BaseUrl}${resource_Id}"
        val requestString =
            "method=row.content.set&worksheet_name=$inputSheet&row=$lastExpense&column_array=[1,2,3,4]&data_array=[\"${date}\",\"${content}\",\"${amount}\",\"${category}\",]".toRequestBody(
                "application/x-www-form-urlencoded".toMediaType()
            )
        MakeRequests(url, requestString) {}
        setRow(lastExpense, "1") {}
        val row = lastExpense.toInt() - 3
        val requestStringSummary =
            "method=row.content.set&worksheet_name=$summarySheet&row=$row&column_array=[1,2,3,4]&data_array=[\"${date}\",\"${content}\",\"${amount}\",\"${category}\"]".toRequestBody(
                "application/x-www-form-urlencoded".toMediaType()
            )
        MakeRequests(url, requestStringSummary) {}
        val range = "A$row:D$row"
        summaryObject.expenseOrIncomeFormat(lastExpense, range)
        val totalExpense = "=SUM(C7:C$lastExpense)"
        setContents(totalExpense, "1", "6", inputSheet) {}
        val totalSummarySheet = "=SUM(List.C7:List.C$lastExpense)"
        setContents(totalSummarySheet, "4", "12", summarySheet) {}
    }


    private fun setRow(row: String, col: String, onResponseReceived: () -> Unit) {
        var rowVal = row.toInt()
        rowVal += 1
        val url = "$API_BaseUrl$resource_Id"
        val requestBody =
            "method=cell.content.set&worksheet_name=$inputSheet&row=1&column=$col&content=$rowVal".toRequestBody(
                "application/x-www-form-urlencoded".toMediaType()
            )
        MakeRequests(url, requestBody) {
            onResponseReceived()
        }
    }

    private fun setContents(
        content: String,
        row: String,
        col: String,
        workSheetName: String,
        onResponeReceived: () -> Unit
    ) {
        val url = "$API_BaseUrl$resource_Id"
        val requestBody =
            "method=cell.content.set&worksheet_name=$workSheetName&row=$row&column=$col&content=$content".toRequestBody(
                "application/x-www-form-urlencoded".toMediaType()
            )
        MakeRequests(url, requestBody) {}
    }

    fun getRow(row: String, col: String, onResponseReceived: (String) -> Unit) {
        val url = "$API_BaseUrl$resource_Id"
        val requestBody =
            "method=cell.content.get&worksheet_name=$inputSheet&row=$row&column=$col".toRequestBody(
                "application/x-www-form-urlencoded".toMediaType()
            )
        MakeRequests(url, requestBody) {
            val jsonObj = JSONObject(it)
            if (col.equals("1")) {
                lastExpense = jsonObj.getString("content").toString()
            } else {
                if (jsonObj.has("content")) {
                    lastIncome = jsonObj.getString("content").toString()
                }
            }
            onResponseReceived(jsonObj.getString("content").toString())
        }
    }


}