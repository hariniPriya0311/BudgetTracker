package com.example.budgettracker

import org.json.JSONObject

object Values {
    val oauthtoken = "1000.60074ccfa24650628fd30e4b007832c9.df3450bdffe101ebee38173fdb2d9450"

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