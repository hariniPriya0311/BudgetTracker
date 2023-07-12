package com.example.budgettracker

import org.json.JSONObject

object Values {
    val oauthtoken = "1000.b754f833ea47d8d52f77fe55d0719cc3.2093655412e61254102a98293df45d8d"

    val API_BaseUrl = "https://sheet.zoho.com/api/v2/"
    var resource_Id = ""

    var budgetsAndResourseId = JSONObject()

    var budgetName = ""
    val inputSheet = "List"
    val summarySheet = "Summary"

    var lastExpense = ""
    var lastIncome = ""

    var expenseCategories = mutableListOf("Food", "Rent", "Fees", "EMI","Salary")
    var budget = "None"
}