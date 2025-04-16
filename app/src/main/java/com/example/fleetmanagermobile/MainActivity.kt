package com.example.fleetmanagermobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fleetmanagermobile.ui.theme.FleetManagerMobileTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FleetManagerMobileTheme {
                App()
            }
        }
    }
}

@Composable
fun App() {
    val navController = rememberNavController()
    var logado by remember { mutableStateOf(false) }

    if (logado) {
        AppNavegacao(navController)
    } else {
        TelaLogin(onLoginSucesso = { logado = true })
    }
}

@Composable
fun TelaLogin(onLoginSucesso: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var erroLogin by remember { mutableStateOf(false) }

    val emailCorreto = "teste@teste.com"
    val senhaCorreta = "1234"

    Column(
        modifier = Modifier
            .background(color = Color(android.graphics.Color.parseColor("#BACCDE")))
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Text("Fleet Manager", style = MaterialTheme.typography.headlineLarge)

        Spacer(modifier = Modifier.height(40.dp))

        Text("Login", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                erroLogin = false
            },
            label = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = senha,
            onValueChange = {
                senha = it
                erroLogin = false
            },
            label = { Text("Senha") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White)
        )

        if (erroLogin) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Email ou senha inválidos",
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (email == emailCorreto && senha == senhaCorreta) {
                    onLoginSucesso()
                } else {
                    erroLogin = true
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Entrar")
        }
    }
}

@Composable
fun TelaPrincipal(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFB3D9FF))
    ) {
        MenuTopo(titulo = "Resumo") {
            navController.navigate("lista")
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(android.graphics.Color.parseColor("#B3D9FF")))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Text("Resumo do Usuário", style = MaterialTheme.typography.titleMedium)

            Spacer(modifier = Modifier.height(8.dp))

            TabelaResumo()

            Spacer(modifier = Modifier.height(32.dp))

            Text("Próximas reservas", style = MaterialTheme.typography.titleMedium)

            Spacer(modifier = Modifier.height(8.dp))

            TabelaReservas()
        }
    }
}

@Composable
fun TelaLista(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFB3D9FF))
    ) {
        MenuTopo(titulo = "Lista Geral") {
            navController.popBackStack()
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Lista de Veículos",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        TabelaLista()
    }
}

@Composable
fun AppNavegacao(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "principal") {
        composable("principal") { TelaPrincipal(navController) }
        composable("lista") { TelaLista(navController) }
    }
}

@Composable
fun TabelaResumo() {
    Row(
        modifier = Modifier
            .border(1.dp, Color.Black)
            .background(Color.LightGray)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Usos", style = MaterialTheme.typography.bodyMedium)
            Text("3")
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Pontualidade", style = MaterialTheme.typography.bodyMedium)
            Text("66%")
        }
    }
}

@Composable
fun TabelaReservas() {
    val dados = listOf(
        listOf("Veículo", "R", "D"),
        listOf("CHASSI1", "02/02", "03/02"),
        listOf("CHASSI2", "14/07", "18/07"),
        listOf("CHASSI3", "03/08", "03/08"),
        listOf("CHASSI4", "16/08", "18/08"),
    )

    Column(
        modifier = Modifier
            .border(1.dp, Color.Black)
            .background(Color.White)
    ) {
        dados.forEachIndexed { index, linha ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(if (index == 0) Color.LightGray else Color.Transparent)
                    .border(1.dp, Color.Black)
            ) {
                linha.forEach { celula ->
                    Text(
                        text = celula,
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
fun TabelaLista() {
    val dados = listOf(
        listOf("Veículo", "Modelo", "Disponibilidade"),
        listOf("CHASSI0", "Pick-Up", "D"),
        listOf("CHASSI1", "Pick-Up", "D"),
        listOf("CHASSI2", "Esportivo", "D"),
        listOf("CHASSI3", "SUV", "D"),
        listOf("CHASSI4", "SUV", "D"),
        listOf("CHASSI5", "Pick-Up", "R"),
        listOf("CHASSI6", "14/07", "R"),
        listOf("CHASSI7", "Hatch", "D"),
        listOf("CHASSI8", "Sedan", "D"),
        listOf("CHASSI9", "Sedan", "D"),
    )

    Column(
        modifier = Modifier
            .border(1.dp, Color.Black)
            .background(Color.White)
    ) {
        dados.forEachIndexed { index, linha ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(if (index == 0) Color.LightGray else Color.Transparent)
                    .border(1.dp, Color.Black)
            ) {
                linha.forEach { celula ->
                    Text(
                        text = celula,
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuTopo(
    titulo: String,
    onMenuClick: () -> Unit
) {
    TopAppBar(
        title = { Text(titulo) },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(Icons.Default.Menu, contentDescription = "Menu")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF336699),
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White
        )
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TelaLoginPreview() {
    FleetManagerMobileTheme {
        TelaLogin {}
    }
}

@Preview(showBackground = true)
@Composable
fun TelaPrincipalPreview() {
    FleetManagerMobileTheme {
        val navController = rememberNavController()
        TelaPrincipal(navController)
    }
}
