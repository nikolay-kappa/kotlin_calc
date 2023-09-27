package com.example.newcalc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.databinding.DataBindingUtil
import com.example.newcalc.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
//        var binding: ActivityMainBinding? = null
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)


       // binding = ActivityMainBinding.inflate(layoutInflater)
//        val view = binding.root
//        setContentView(view)

     //   setContentView(R.layout.activity_main)
      //  binding.textbox.append("111")
    }
    var canAddDecimal = true
    var canAddOperation = false
    fun operationAction(view: View)
    {
        if(view is Button && canAddOperation)
        {
            binding.workingsTV.append(view.text)
            canAddOperation = false
            canAddDecimal = true
        }
    }
    fun numberAction(view: View)
    {

        if(view is Button)
        {
            if(view.text == ".")
            {
                if(canAddDecimal)
                    binding.workingsTV.append(view.text)

                canAddDecimal = false
            }
            else
                binding.workingsTV.append(view.text)

            canAddOperation = true
        }
    }
    private fun digitsOperators(): MutableList<Any>
    {
        val list = mutableListOf<Any>()
        var currentDigit = ""

        for(character in binding.workingsTV.text)
        {
            if(character.isDigit() || character == '.')
                currentDigit += character
            else
            {
                list.add(currentDigit.toFloat())
                currentDigit = ""
                list.add(character)
            }
        }

        if(currentDigit != "")
            list.add(currentDigit.toFloat())

        return list
    }
    fun allClearAction(view: View){
        binding.workingsTV.setText("")
        binding.resultsTV.setText("")
    }
    fun DeleteAction(view: View){
        val len = binding.workingsTV.length()
        if(len>0){
            binding.workingsTV.text = binding.workingsTV.text.subSequence(0,len- 1)
        }
    }
    fun equals(view: View)
    {
        binding.resultsTV.text = calculateResults()
    }
    fun calculateResults():String{
        val digitsOperators = digitsOperators()
        if(digitsOperators.isEmpty()) return ""

        val timesDivision = timesDivisionCalculate(digitsOperators)
        if(timesDivision.isEmpty()) return ""

        val result = addSubtractCalculate(timesDivision)
        return result.toString()
    }
    private fun addSubtractCalculate(passedList: MutableList<Any>): Float{
        var result = passedList[0] as Float
        for(i in passedList.indices){
            if(passedList[i] is Char && i!= passedList.lastIndex){
                val operator = passedList[i]
                val nextDigit = passedList[i+1] as Float
                if(operator=='+'){
                    result += nextDigit
                }
                if(operator=='-'){
                    result-=nextDigit
                }
            }
        }
        return result
    }
    private fun timesDivisionCalculate(passedList: MutableList<Any>): MutableList<Any>
    {
        var list = passedList
        while (list.contains('*') || list.contains('/'))
        {
            list = dividemultiply(list)
        }
        return list
    }
    private fun dividemultiply(passedList: MutableList<Any>):MutableList<Any>{
        //var result=passedList[0] as Float
        val newList = mutableListOf<Any>()
        var index = passedList.size
        for(i in passedList.indices){
            if(passedList[i] is Char &&i!= passedList.lastIndex &&i<index){
                val operator = passedList[i]
                val nextDigit = passedList[i+1] as Float
                val prevDigit = passedList[i-1] as Float
                when(operator){
                    '*'-> {newList.add(nextDigit * prevDigit)
                        index = i+1
                    }
                    "/"-> {newList.add(prevDigit / nextDigit)
                        index = i+1
                    }
                    else ->
                    {
                        newList.add(prevDigit)
                        newList.add(operator)
                    }
                }
            }
            if(i >passedList.size)
                newList.add(passedList[i])
        }
        return newList
    }
}


