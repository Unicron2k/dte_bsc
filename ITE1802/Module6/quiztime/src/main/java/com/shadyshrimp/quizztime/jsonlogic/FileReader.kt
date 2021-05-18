package com.shadyshrimp.quizztime.jsonlogic

import android.content.Context
import java.io.*
import java.nio.charset.StandardCharsets

class FileReader {
    fun readFile(context: Context, fileNameInternal: String): String {
        val stringBuilder = StringBuilder()
        try {
            val fileInputStream = context.openFileInput(fileNameInternal)
            val inputStreamReader = InputStreamReader(fileInputStream!!, StandardCharsets.UTF_8)
            val reader = BufferedReader(inputStreamReader)
            var line = reader.readLine()
            while (line != null) {
                stringBuilder.append(line).append('\n')
                line = reader.readLine()
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } finally {
            return stringBuilder.toString()
        }
    }
}