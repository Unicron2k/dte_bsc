package com.shadyshrimp.quizztime.jsonlogic

//classes with getters and setters etc. already built in because kotlin is amazing
//removed incorrect_answers class/model by simplifying it to a single ArrayList within the question model with data from the JSONArray (see repository class)
class Question(var category : String, var type : String, var difficulty : String, var question :String, var correct_answer : String, var incorrect_answer : ArrayList<String>) {
    constructor() : this("","","", "", "", arrayListOf())
}
