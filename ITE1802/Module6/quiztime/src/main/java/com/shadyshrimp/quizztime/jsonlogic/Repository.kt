package com.shadyshrimp.quizztime.jsonlogic

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.google.gson.Gson
import com.shadyshrimp.quizztime.QuizURLGenerator
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class Repository {

    var questionData = MutableLiveData<QuestionData>(QuestionData(""))
    var errorMessage = MutableLiveData<String>("")
    private var queue: RequestQueue? = null
    private val fileNameInternal = "questionsSaveFile.txt"
    private val fileNameCategoriesInternal = "categorySaveFile.txt"
    var categoryArray: ArrayList<Category> = arrayListOf()
    var mCategoryData = MutableLiveData<CategoryData>(CategoryData(""))

    // Downloads and writes questions to file
    fun download(context: Context) {
        val fileDir = context.filesDir.toString()
        queue = MySingletonQueue.getInstance(context).requestQueue
        if (fileDir.contains(fileNameInternal)) {
            context.deleteFile(fileNameInternal)
        }
        // Creates JSONObjectRequest and listener for response from REST API
        val jsonObjectRequest = object : JsonObjectRequest(
                Method.GET,
                QuizURLGenerator.generate(context),
                null,
                Response.Listener<JSONObject> { response ->
                    try {
                        Thread.sleep(2000)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                    try {
                        val array: JSONArray = response.getJSONArray("results")
                        try {
                            val fileOutputStream = context.openFileOutput(fileNameInternal, Context.MODE_PRIVATE)
                            fileOutputStream.write(array.toString().toByteArray())
                            fileOutputStream.close()
                            jsonQuestionsFromFile(context)
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { error -> errorMessage.postValue(error.message) }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                return mapOf("Content-type" to "application/json")
            }
        }
        queue?.add(jsonObjectRequest)
    }

    // Downloads categories
    fun downloadCategories(context: Context) {
        val fileDir = context.filesDir.toString()
        queue = MySingletonQueue.getInstance(context).requestQueue

        // Creates JSONObjectRequest and listener for response from REST API
        val jsonObjectRequest = object : JsonObjectRequest(
                Method.GET,
                "https://opentdb.com/api_category.php",
                null,
                Response.Listener<JSONObject> { response ->
                    try {
                        Thread.sleep(2000)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                    try {
                        val array: JSONArray = response.getJSONArray("trivia_categories")
                        try { //if the FileDirectory does not contain the file, then it creates it
                            if (!fileDir.contains(fileNameCategoriesInternal)) {
                                val fileOutputStream = context.openFileOutput(fileNameCategoriesInternal, Context.MODE_PRIVATE)
                                fileOutputStream.write(array.toString().toByteArray())
                                fileOutputStream.close()
                                jsonCategoriesFromFile(context)
                            } else {
                                val fileReader = FileReader()
                                //otherwise the file exists, and a check for differences between the downloaded string and file string happens
                                //if they are not equal, then the file is deleted and a new one with the new information is created. Otherwise, nothing happens.
                                if (array.toString() != fileReader.readFile(context, fileNameCategoriesInternal)) {
                                    context.deleteFile(fileNameCategoriesInternal)
                                    val fileOutputStream = context.openFileOutput(fileNameCategoriesInternal, Context.MODE_PRIVATE)
                                    fileOutputStream.write(array.toString().toByteArray())
                                    fileOutputStream.close()
                                    jsonCategoriesFromFile(context)
                                }
                            }
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { error -> errorMessage.postValue(error.message) }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                return mapOf("Content-type" to "application/json")
            }
        }
        queue?.add(jsonObjectRequest)

    }

    // Fetches categories from the internal file
    fun jsonCategoriesFromFile(context: Context) {
        val content = FileReader().readFile(context, fileNameCategoriesInternal)
        if(content.isNotEmpty()){
            val array = JSONArray(content)
            val gson = Gson()
            val tmpList: ArrayList<Category> = ArrayList()

            for (i in 0 until array.length()) {
                val categoryJSON = array.getJSONObject(i)
                val category = gson.fromJson(categoryJSON.toString(), Category::class.java)
                tmpList.add(category)
            }
            categoryArray = CategoryData(tmpList).allCategories!!
            mCategoryData.postValue(CategoryData(tmpList))
        }
    }

    // Reads the saved questions file and puts the data into Question objects
    fun jsonQuestionsFromFile(context: Context) {
        val content = FileReader().readFile(context, fileNameInternal)
        val gson = Gson()
        if(content.isNotEmpty()){
            val array = JSONArray(content)
            val tmpList: ArrayList<Question> = ArrayList()

            // Iterates for length of the results array and fetches the JSONObjects from it and puts them into their respective models
            for (i in 0 until array.length()) {
                val questionJSON = array.getJSONObject(i)
                val question = gson.fromJson(questionJSON.toString(), Question::class.java)

                // Fetches incorrect answers arrayList from the incorrect_answers JSONArray within the question JSONObject
                val incorrectArray = questionJSON.getJSONArray("incorrect_answers")

                // Creates a list of the incorrect answers *insert Xzibit inception meme here*
                val list: ArrayList<String> = ArrayList()
                for (j in 0 until incorrectArray.length()) {
                    list.add(incorrectArray.get(j).toString())
                }

                // Sets the incorrect answers list we just created in the Question object
                question.incorrect_answer = list
                tmpList.add(question)
            }
            val tmpQuestionData = QuestionData(tmpList)
            questionData.postValue(tmpQuestionData)
        }
    }
}