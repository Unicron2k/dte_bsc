package com.shadyshrimp.quizztime.jsonlogic

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import java.lang.reflect.Type

class QuestionData {
    var allQuestions : ArrayList<Question>? = null
    constructor()
    constructor(allQuestions : ArrayList<Question>){
        this.allQuestions = allQuestions
    }
    constructor(allQuestionsAsJSON : String){
        val gson = Gson()
        val type : Type = object: TypeToken<ArrayList<Question>>(){}.type
        try{
            this.allQuestions = gson.fromJson<ArrayList<Question>>(allQuestionsAsJSON, type)
        }
        catch(e : Exception){
            e.printStackTrace()
        }
    }

     constructor(jsonObject : JSONObject){
         val gsonBuilder = GsonBuilder()
         gsonBuilder.setDateFormat("M.d.yy h:mm a")
         val gson : Gson = gsonBuilder.create()
         val type = object : TypeToken<ArrayList<Question>>(){}.type
         this.allQuestions = gson.fromJson(jsonObject.toString(), type)
     }
}
