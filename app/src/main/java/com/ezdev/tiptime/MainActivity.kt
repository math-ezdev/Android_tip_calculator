package com.ezdev.tiptime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.ezdev.tiptime.ui.theme.TipTimeTheme
import java.text.DecimalFormat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TipTimeTheme {
                TipTimeApp()
            }
        }
    }
}

val decimalFormat = DecimalFormat("#.##")

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TipTimeApp() {
    //  data
    var isCustomUi by remember { mutableStateOf(true) }

    //  ui
    TipTimeTheme {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            //  switch to change ui
            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(32.dp),
                horizontalArrangement = Arrangement.spacedBy(32.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "TIP CALCULATOR: " + if (isCustomUi) "Custom UI" else "Basic UI",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Switch(
                    checked = isCustomUi,
                    onCheckedChange = {
                        isCustomUi = !isCustomUi
                    })
            }
            //  tip calculator
            if (isCustomUi) {
                TipCalculator()
            } else {
                TipCalculatorBasic()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TipCalculator(modifier: Modifier = Modifier) {
    //  data
    var bill by remember { mutableStateOf("") }
    var tipPercent by remember { mutableStateOf(0) }
    var numberOfPeople by remember { mutableStateOf(1) }

    val tipMoney: Float = if (bill != "")
        bill.toFloat() * tipPercent / 100
    else 0f
    val totalAmount: Float = if (bill != "")
        bill.toFloat() + tipMoney.toInt()
    else 0f
    val totalPerPerson: Float = if (bill != "")
        totalAmount / numberOfPeople
    else 0f
    val tipPerPerson: Float = if (bill != "")
        tipMoney / numberOfPeople
    else 0f

    //  ui
    Card(
        modifier = modifier
            .fillMaxSize()
            .wrapContentSize()
            .padding(32.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onPrimary)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            //  bill
            Row(
                modifier = Modifier
                    .wrapContentSize()
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Bill")
                TextField(
                    value = bill,
                    label = {
                        Text(text = "$")
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = MaterialTheme.colorScheme.onPrimary,
                        textColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier
                        .widthIn(max = 120.dp),
                    onValueChange = {
                        bill = if (it != "")
                            it
                        else ""
                    },
                )
            }
            //  tip
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Tip")
                Text(text = "$${decimalFormat.format(tipMoney)}")
            }
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(0.1f),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.primary
            )
            //  total amount
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Total Amount")
                Text(text = "$${decimalFormat.format(totalAmount)}")
            }
            //  tip slider
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Tip %")
                    Text(text = "$tipPercent%")
                }
                Slider(
                    value = tipPercent.toFloat(),
                    valueRange = 0f..100f,
                    steps = 19,
                    onValueChange = {
                        tipPercent = it.toInt()
                    })
            }
            //  number of people slider
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "No. Of People")
                    Text(text = "$numberOfPeople")
                }
                Slider(value = numberOfPeople.toFloat(),
                    valueRange = 0f..10f,
                    steps = 9,
                    onValueChange = {
                        println(it)
                        numberOfPeople = it.toInt()
                    })
            }
            //  share bill
            if (
                numberOfPeople > 1 && bill != ""
            ) {
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(vertical = 32.dp, horizontal = 16.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Tip Per Person")
                        Text(
                            text = "$${decimalFormat.format(tipPerPerson)}",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Total Per Person")
                        Text(
                            text = "$${decimalFormat.format(totalPerPerson)}",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TipCalculatorBasic(modifier: Modifier = Modifier) {
    //  data
    var bill by remember { mutableStateOf("") }
    var tipPercent by remember { mutableStateOf("") }
    var numberOfPeople by remember { mutableStateOf("1") }

    val tipMoney: Float = if (bill != "" && tipPercent != "")
        bill.toFloat() * tipPercent.toFloat() / 100
    else 0f
    val totalAmount: Float = if (bill != "" && tipPercent != "")
        bill.toFloat() + tipMoney.toInt()
    else 0f
    val totalPerPerson: Float = if (bill != "" && tipPercent != "" && numberOfPeople != "")
        totalAmount / numberOfPeople.toInt()
    else 0f
    val tipPerPerson: Float = if (bill != "" && tipPercent != "" && numberOfPeople != "")
        tipMoney / numberOfPeople.toInt()
    else 0f


    //  ui
    Column(
        modifier = modifier
            .fillMaxSize()
            .wrapContentSize()
            .padding(horizontal = 16.dp, vertical = 80.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = bill,
            label = {
                Text(text = "Bill")
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            onValueChange = {
                bill = if (it != "")
                    it
                else ""
            },
        )
        OutlinedTextField(
            value = tipPercent,
            label = {
                Text(text = "Tip %")
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            onValueChange = {
                tipPercent = if (it != "" && it.toFloat() in 1f..100f)
                    it
                else ""
            },
        )
        OutlinedTextField(
            value = numberOfPeople,
            label = {
                Text(text = "Number of people")
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            onValueChange = {
                numberOfPeople = if (it != "" && it.isDigitsOnly() && it.toInt() > 0)
                    it
                else ""
            },
        )
        //  tip and total
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Tip = ${decimalFormat.format(tipMoney)}",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Total amount = ${decimalFormat.format(totalAmount)}",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            //  share bill
            if (numberOfPeople != "" && numberOfPeople.toInt() > 1) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Per Person = ${decimalFormat.format(tipPerPerson)}",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "Per Person = ${decimalFormat.format(totalPerPerson)}",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}