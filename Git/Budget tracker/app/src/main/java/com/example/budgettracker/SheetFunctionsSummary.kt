package com.example.budgettracker

import android.util.Log
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

val listObject = SheetfunctionsListView()

class SheetFunctionsSummary {
    fun formatting() {
        val url = "${Values.API_BaseUrl}${Values.resource_Id}"
        val formatJsonArray =
            """[{"worksheet_name":"${Values.summarySheet}","range":"A1:Q100","horizontal_alignment":"center","vertical_alignment":"middle","column_width":200},{"worksheet_name":"${Values.summarySheet}","range":"L3:L5","fill_color":"#e9f4f8"},{"worksheet_name":"${Values.summarySheet}","range":"K3:K5","bold":"true","italic":"true","font_size":24,"row_height":35,"fill_color":"#2c778f","font_color":"#ffffff"},{"worksheet_name":"${Values.summarySheet}","range":"K3:K5","font_size":18,"row_height":35,"fill_color":"#2c778f"},{"worksheet_name":"${Values.summarySheet}","range":"A1:AMJ1","bold":"true","italic":"true","font_size":30,"font_color":"#1c4d61","row_height":45,"column_width":200},{"worksheet_name":"${Values.summarySheet}","range":"A3:D3","fill_color":"#2c778f","bold":"true","font_size":18,"row_height":40,"font_color":"#ffffff"},{"worksheet_name":"${Values.summarySheet}","range":"F3:I3","fill_color":"#2c778f","bold":"true","font_color":"#ffffff","font_size":18,"row_height":40}]""".trimMargin()
        val requestBody =
            "method=ranges.format.set&format_json=$formatJsonArray".toRequestBody("application/x-www-form-urlencoded".toMediaType())
        listObject.MakeRequests(url, requestBody) {
        }
    }

    fun expenseOrIncomeFormat(row: String, range: String) {
        val url = "${Values.API_BaseUrl}${Values.resource_Id}"
        val formatJsonArray =
            """[{"worksheet_name":"${Values.summarySheet}","range":"$range","font_size":14,"row_height":45,"fill_color":"#e9f4f8"}]""".trimMargin()
        val requestBody =
            "method=ranges.format.set&format_json=$formatJsonArray".toRequestBody("application/x-www-form-urlencoded".toMediaType())
        listObject.MakeRequests(url, requestBody) {
        }
    }
}