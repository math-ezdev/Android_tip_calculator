package com.ezdev.tiptime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.TextFieldColors
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.ezdev.tiptime.ui.theme.TipTimeTheme
import java.text.NumberFormat
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TipTimeTheme {
                TipCalculatorApp()
            }
        }
    }
}

//  _app
@Preview(
    showBackground = true, showSystemUi = true, device = "spec:width=411dp,height=891dp",

    )
@Composable
fun TipCalculatorApp() {
    //  _data
    var isCustomUi by remember { mutableStateOf(false) }
    var billInput by remember { mutableStateOf("") }
    var tipPercentInput by remember { mutableStateOf("") }
    var numberOfPeopleInput by remember { mutableStateOf("1") }

    val bill = billInput.toFloatOrNull() ?: 0f
    val tipPercent = tipPercentInput.toFloatOrNull() ?: 0f
    val numberOfPeople = numberOfPeopleInput.toIntOrNull() ?: 0

    val tip: Float = bill * tipPercent / 100
    val total: Float = bill + tip
    val tipPerPerson: Float = if (numberOfPeople > 0) tip / numberOfPeople else 0f
    val totalPerPerson: Float = if (numberOfPeople > 0) total / numberOfPeople else 0f
    val resultCalculator = ResultCalculator(
        tip = NumberFormat.getCurrencyInstance().format(tip),
        total = NumberFormat.getCurrencyInstance().format(total),
        tipPerPerson = NumberFormat.getCurrencyInstance().format(tipPerPerson),
        totalPerPerson = NumberFormat.getCurrencyInstance().format(totalPerPerson)
    )
    println("tip percent input = $tipPercentInput")
    println("tip percent = $tipPercent")


    //  _ui
    TipTimeTheme(darkTheme = false) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            //  _switch
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
            //  _app
            if (isCustomUi) {
                TipCalculatorCustomUi(
                    billInput = billInput,
                    tipPercentInput = tipPercent,
                    numberOfPeopleInput = numberOfPeople.toFloat(),
                    onBillChange = {
                        billInput = it
                    },
                    onTipPercentChange = {
                        tipPercentInput = it.toString()
                    },
                    onNumberOfPeopleChange = {
                        numberOfPeopleInput = it.toInt().toString()
                    },
                    tipPercentRange = 0f..100f,
                    tipPercentSteps = 19,
                    numberOfPeopleRange = 0f..10f,
                    numberOfPeopleSteps = 9,
                    result = resultCalculator,
                    modifier = Modifier.widthIn(max = 560.dp)
                )
            } else {
                TipCalculatorBasic(
                    billInput = billInput,
                    tipPercentInput = tipPercentInput,
                    numberOfPeopleInput = numberOfPeopleInput,
                    onBillChange = {
                        billInput = it
                    },
                    onTipPercentChange = {
                        tipPercentInput = if (
                            it.toFloatOrNull() != null && it.toFloat() in 0f..100f
                        ) {
                            it
                        } else {
                            ""
                        }
                    },
                    onNumberOfPeopleChange = {
                        numberOfPeopleInput = if (
                            it.toIntOrNull() != null && it.isDigitsOnly()
                        ) {
                            it
                        } else {
                            ""
                        }
                    },
                    result = resultCalculator,
                    modifier = Modifier.widthIn(max = 560.dp)
                )
            }


        }
    }
}

@Composable
fun TipCalculatorBasic(
    billInput: String,
    tipPercentInput: String,
    numberOfPeopleInput: String,
    onBillChange: (String) -> Unit,
    onTipPercentChange: (String) -> Unit,
    onNumberOfPeopleChange: (String) -> Unit,
    result: ResultCalculator,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .wrapContentSize()
            .padding(horizontal = 16.dp, vertical = 80.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //  _input: TextField
        NumericTextField(
            value = billInput, onValueChange = onBillChange, label = "Bill",
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Next,
            )
        )
        NumericTextField(
            value = tipPercentInput, onValueChange = onTipPercentChange, label = "Tip %",
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next,
            )
        )
        NumericTextField(
            value = numberOfPeopleInput,
            onValueChange = onNumberOfPeopleChange,
            label = "Number of people",
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done,
            )
        )

        //  _result: Text
        if (billInput != "" && tipPercentInput != "" && numberOfPeopleInput != "" &&
            numberOfPeopleInput.toIntOrNull() != null && numberOfPeopleInput.toInt() > 0
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ResultText(tip = result.tip, total = result.total, modifier = Modifier.weight(1f))
                if (numberOfPeopleInput.toIntOrNull() != null && numberOfPeopleInput.toInt() > 1) {
                    Divider(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxHeight()
                            .width(1.dp)
                            .alpha(0.1f),
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    ResultText(
                        tip = result.tipPerPerson,
                        total = result.totalPerPerson,
                        tipText = "Per Person",
                        totalText = "Per Person",
                        maxLines = 2,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TipCalculatorCustomUi(
    billInput: String,
    tipPercentInput: Float,
    numberOfPeopleInput: Float,
    onBillChange: (String) -> Unit,
    onTipPercentChange: (Float) -> Unit,
    onNumberOfPeopleChange: (Float) -> Unit,
    tipPercentRange: ClosedFloatingPointRange<Float>,
    tipPercentSteps: Int,
    numberOfPeopleRange: ClosedFloatingPointRange<Float>,
    numberOfPeopleSteps: Int,
    result: ResultCalculator,
    modifier: Modifier = Modifier
) {
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
            //  _input: TextField + Slider
            Row(
                modifier = Modifier
                    .wrapContentSize()
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Bill")
                TextField(
                    value = billInput,
                    onValueChange = onBillChange,
                    label = {
                        Text(
                            text = NumberFormat.getCurrencyInstance(Locale.getDefault()).currency?.symbol
                                ?: "$"
                        )
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Done
                    ),
                    colors = TextFieldDefaults.outlinedTextFieldColors(),
                    modifier = Modifier
                        .widthIn(max = 120.dp)

                )
            }
            NumericSlider(
                value = tipPercentInput,
                onValueChange = onTipPercentChange,
                valueRange = tipPercentRange,
                steps = tipPercentSteps,
                title = "Tip %",
                valueText = "${tipPercentInput.toInt()} %"
            )
            NumericSlider(
                value = numberOfPeopleInput,
                onValueChange = onNumberOfPeopleChange,
                valueRange = numberOfPeopleRange,
                steps = numberOfPeopleSteps,
                title = "No. Of People",
                valueText = "${numberOfPeopleInput.toInt()}"
            )

            //  _result: Text
            if (billInput != "" && numberOfPeopleInput > 0) {
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .alpha(0.1f),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.primary
                )
                ResultText(tip = result.tip, total = result.total)
                if (numberOfPeopleInput > 1) {
                    Column(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(vertical = 32.dp, horizontal = 16.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ResultText(
                            tip = result.tipPerPerson,
                            total = result.totalPerPerson,
                            tipText = "Tip Per Person",
                            totalText = "Total Per Person"
                        )
                    }
                }
            }
        }
    }
}

//  _data
data class ResultCalculator(
    var tip: String,
    var total: String,
    var tipPerPerson: String,
    var totalPerPerson: String
)

//  _composable
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NumericTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardOptions: KeyboardOptions,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    colors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors()
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(text = label)
        },
        keyboardOptions = keyboardOptions,
        singleLine = singleLine,
        colors = colors,
        modifier = modifier,

        )
}

@Composable
fun ResultText(
    tip: String,
    total: String,
    modifier: Modifier = Modifier,
    tipText: String = "Tip",
    totalText: String = "Total",
    maxLines: Int = 1
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = tipText)
            Text(
                text = tip,
                maxLines = maxLines,
                overflow = TextOverflow.Ellipsis
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = totalText)
            Text(
                text = total,
                maxLines = maxLines,
                overflow = TextOverflow.Ellipsis
            )
        }

    }
}

@Composable
fun NumericSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int,
    title: String,
    valueText: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = title)
            Text(text = valueText)
        }
        Slider(
            value = value,
            valueRange = valueRange,
            steps = steps,
            onValueChange = onValueChange
        )
    }
}


