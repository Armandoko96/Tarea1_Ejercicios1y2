package com.curso.tarea1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    PantallaPrincipal()
                }
            }
        }
    }
}

@Composable
fun PantallaPrincipal() {
    // Variable para saber en qué pestaña estamos
    var pestaña by remember { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = pestaña) {
            Tab(selected = pestaña == 0, onClick = { pestaña = 0 }, text = { Text("Ej 1: IVA/ISR") })
            Tab(selected = pestaña == 1, onClick = { pestaña = 1 }, text = { Text("Ej 2: Constraint") })
        }
        
        if (pestaña == 0) {
            Ejercicio1()
        } else {
            Ejercicio2()
        }
    }
}

@Composable
fun Ejercicio1() {
    var numeroIngresado by remember { mutableStateOf("") }
    var verResultados by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Impuestos", fontSize = 30.sp, fontWeight = FontWeight.Bold)
        
        Spacer(modifier = Modifier.height(10.dp))
        
        Text("Importe:")
        OutlinedTextField(
            value = numeroIngresado,
            onValueChange = { 
                numeroIngresado = it
                verResultados = false 
            },
            placeholder = { Text("000.00") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        val n = numeroIngresado.toDoubleOrNull() ?: 0.0
        val miIva = n * 0.16
        
        Text("IVA (16%) = ${String.format("%.2f", miIva)}$", color = Color.Gray)

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = { verResultados = true },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(5.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
            border = ButtonDefaults.outlinedButtonBorder
        ) {
            Text("CALCULAR")
        }

        if (verResultados) {
            val sub = n + miIva
            val rIva = n * 0.10666
            val rIsr = n * 0.10
            val tot = n + miIva - rIva - rIsr

            Spacer(modifier = Modifier.height(20.dp))
            
            FilaDato("Sub total :", String.format("%.2f", sub))
            FilaDato("Retencion IVA (10.66) :", String.format("%.2f", rIva))
            FilaDato("Retencion ISR (10) :", String.format("%.2f", rIsr))
            FilaDato("Total:", String.format("%.2f", tot), true)
        }
    }
}

@Composable
fun FilaDato(texto: String, valor: String, negrita: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = texto, fontSize = 18.sp, fontWeight = if (negrita) FontWeight.Bold else FontWeight.Normal)
        Text(text = valor, fontSize = 18.sp, fontWeight = if (negrita) FontWeight.Bold else FontWeight.Normal)
    }
}

@Composable
fun Ejercicio2() {
    var azulOk by remember { mutableStateOf(true) }
    var verdeOk by remember { mutableStateOf(true) }
    var rojoOk by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier.fillMaxSize().padding(15.dp)
    ) {
        Text("Ejercicio 2:", fontSize = 22.sp)
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = azulOk, onClick = { azulOk = !azulOk })
                Text("Azul")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = verdeOk, onClick = { verdeOk = !verdeOk })
                Text("Verde")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = rojoOk, onClick = { rojoOk = !rojoOk })
                Text("Rojo")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (cuadroRojo, cuadroVerde, cuadroAzul) = createRefs()

            if (azulOk) {
                Box(modifier = Modifier.size(110.dp).background(Color.Blue).constrainAs(cuadroAzul) {
                    top.linkTo(parent.top, margin = 50.dp)
                    start.linkTo(parent.start)
                })
            }

            if (rojoOk) {
                Box(modifier = Modifier.size(110.dp).background(Color.Red).constrainAs(cuadroRojo) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end, margin = 30.dp)
                })
            }

            if (verdeOk) {
                Box(modifier = Modifier.size(110.dp).background(Color.Green).constrainAs(cuadroVerde) {
                    if (azulOk) {
                        top.linkTo(cuadroAzul.bottom, margin = 15.dp)
                        start.linkTo(cuadroAzul.end, margin = 15.dp)
                    } else {
                        centerTo(parent)
                    }
                })
            }
        }
    }
}
