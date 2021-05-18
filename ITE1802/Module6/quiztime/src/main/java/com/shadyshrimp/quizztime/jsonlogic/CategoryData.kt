package com.shadyshrimp.quizztime.jsonlogic

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import java.lang.reflect.Type

class CategoryData {
    var allCategories : ArrayList<Category>? = null
    constructor()
    constructor(allCategories : ArrayList<Category>){
        this.allCategories = allCategories
    }
    constructor(allCategoriesAsJSON : String){
        val gson = Gson()
        val type : Type = object: TypeToken<ArrayList<Category>>(){}.type
        try{
            this.allCategories = gson.fromJson<ArrayList<Category>>(allCategoriesAsJSON, type)
        }
        catch(e: Exception){
            e.printStackTrace()
        }
    }
    constructor(jsonObject : JSONObject){
        val gsonBuilder = GsonBuilder()
        gsonBuilder.setDateFormat("M.d.yy h:mm a")
        val gson : Gson = gsonBuilder.create()
        val type = object : TypeToken<ArrayList<Category>>(){}.type
        this.allCategories = gson.fromJson(jsonObject.toString(), type)
    }

}