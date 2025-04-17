package com.example.fleetmanagermobile

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
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
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fleetmanagermobile.ui.theme.FleetManagerMobileTheme
import androidx.constraintlayout.compose.ConstraintLayout


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        criarCanalDeNotificacao(this)
        enableEdgeToEdge()
        setContent {
            FleetManagerMobileTheme {
                App()
            }
        }
    }
}

private fun criarCanalDeNotificacao(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val nome = "Canal de Reserva"
        val descricao = "Notificações de reservas de veículos"
        val importancia = NotificationManager.IMPORTANCE_DEFAULT
        val canal = NotificationChannel("reserva_channel_id", nome, importancia).apply {
            description = descricao
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(canal)
    }
}

@SuppressLint("MissingPermission")
fun enviarNotificacao(context: Context) {
    val notificationManager = NotificationManagerCompat.from(context)

    // Criar canal de notificação (se ainda não existir)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            "reserva_channel_id",
            "Canal de Reserva",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)
    }

    val builder = NotificationCompat.Builder(context, "reserva_channel_id")
        .setSmallIcon(android.R.drawable.ic_dialog_info)
        .setContentTitle("Reserva concluída")
        .setContentText("O veículo foi reservado com sucesso.")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    notificationManager.notify(1, builder.build())
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
            navController.navigate(it)
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
            navController.navigate(it)
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
fun TelaReserva(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFB3D9FF))
            .padding(0.dp,0.dp,0.dp,24.dp)
    ) {
        MenuTopo(titulo = "Reservar Veículo") {
            navController.navigate(it)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Reserva de Veículo",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        SistemaReserva()
    }
}


@Composable
fun AppNavegacao(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "principal") {
        composable("principal") { TelaPrincipal(navController) }
        composable("lista") { TelaLista(navController) }
        composable("reserva") { TelaReserva(navController)}
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

data class Veiculo(
    val chassi: String,
    val modelo: String,
    val km: Int,
    val ultimaRevisao: String
)
@SuppressLint("MissingPermission")
@Composable
fun SistemaReserva() {

    val context = LocalContext.current
    val veiculos = listOf(
        Veiculo("CHASSI0", "Pickup 1", 18000, "01/01/2025"),
        Veiculo("CHASSI1", "SUV X", 22000, "15/02/2025"),
        Veiculo("CHASSI2", "Hatch Z", 12500, "10/03/2025")
    )
    val veiculoSelecionado = remember { mutableStateOf(veiculos[0]) }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFB3D9FF))
            .padding(bottom = 24.dp)
    ) {
        val (
            tituloVeiculo, dropdownVeiculo, tituloModelo, textoModelo,
            spacer1,
            tituloKm, textoKm, tituloRevisao, textoRevisao,
            spacer2,
            tituloDisponibilidade, tabela,
            textoOvernight, radioOvernight
        ) = createRefs()

        val (
            textoDia, radioDia,
            textoTeste, checkboxTeste,
            botaoReservar
        ) = createRefs()


        // Veículo
        Text("Veículo", style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.constrainAs(tituloVeiculo) {
                top.linkTo(parent.top, margin = 16.dp)
                start.linkTo(parent.start, margin = 16.dp)
            })

        Box(modifier = Modifier.constrainAs(dropdownVeiculo) {
            top.linkTo(tituloVeiculo.bottom, margin = 4.dp)
            start.linkTo(tituloVeiculo.start)
        }) {
            DropdownVeiculos(
                veiculos = veiculos.map { it.chassi },
                onSelecionado = { chassiSelecionado ->
                    veiculoSelecionado.value = veiculos.first { it.chassi == chassiSelecionado }
                }
            )
        }

        // Spacer entre linhas
        Spacer(modifier = Modifier
            .height(16.dp)
            .constrainAs(spacer1) {
                top.linkTo(dropdownVeiculo.bottom)
            })
        // Títulos
        Text("Modelo", style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.constrainAs(tituloModelo) {
                top.linkTo(spacer1.bottom)
                start.linkTo(parent.start, margin = 16.dp)
            })

        Text("Km", style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.constrainAs(tituloKm) {
                top.linkTo(spacer1.bottom)
                start.linkTo(tituloModelo.end, margin = 16.dp)
            })

        Text("Manutenção", style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.constrainAs(tituloRevisao) {
                top.linkTo(tituloModelo.top)
                start.linkTo(tituloKm.end, margin = 16.dp)
            })

// Textos de valor
        Text(veiculoSelecionado.value.modelo,
            modifier = Modifier.constrainAs(textoModelo) {
                top.linkTo(tituloModelo.bottom, margin = 4.dp)
                start.linkTo(tituloModelo.start)
            })

        Text("${veiculoSelecionado.value.km}",
            modifier = Modifier.constrainAs(textoKm) {
                top.linkTo(tituloKm.bottom, margin = 4.dp)
                start.linkTo(tituloKm.start)
            })

        Text(veiculoSelecionado.value.ultimaRevisao,
            modifier = Modifier.constrainAs(textoRevisao) {
                top.linkTo(tituloRevisao.bottom, margin = 4.dp)
                start.linkTo(tituloRevisao.start)
            })

        // Spacer
        Spacer(modifier = Modifier
            .height(16.dp)
            .constrainAs(spacer2) {
                top.linkTo(textoKm.bottom)
            })

        // Disponibilidade
        Text(
            "Disponibilidade",
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.constrainAs(tituloDisponibilidade) {
                top.linkTo(spacer2.bottom, margin = 16.dp)
                centerHorizontallyTo(parent)
            })

        Box(modifier = Modifier.constrainAs(tabela) {
            top.linkTo(tituloDisponibilidade.bottom, margin = 8.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }) {
            TabelaDiasSelecionaveis()
        }

        // Overnight
        Text("Overnight", modifier = Modifier.constrainAs(textoOvernight) {
            top.linkTo(tabela.bottom, margin = 16.dp)
            start.linkTo(parent.start, margin = 16.dp)
        })
        RadioButton(selected = true, onClick = {}, modifier = Modifier.constrainAs(radioOvernight) {
            top.linkTo(textoOvernight.top)
            start.linkTo(textoOvernight.end, margin = 8.dp)
        })

        // Dia
        Text("Dia", modifier = Modifier.constrainAs(textoDia) {
            top.linkTo(textoOvernight.bottom, margin = 8.dp)
            start.linkTo(parent.start, margin = 16.dp)
        })
        RadioButton(selected = false, onClick = {}, modifier = Modifier.constrainAs(radioDia) {
            top.linkTo(textoDia.top)
            start.linkTo(textoDia.end, margin = 8.dp)
        })

        // Teste
        Text("Teste", modifier = Modifier.constrainAs(textoTeste) {
            top.linkTo(textoDia.bottom, margin = 8.dp)
            start.linkTo(parent.start, margin = 16.dp)
        })
        Checkbox(checked = false, onCheckedChange = {}, modifier = Modifier.constrainAs(checkboxTeste) {
            top.linkTo(textoTeste.top)
            start.linkTo(textoTeste.end, margin = 8.dp)
        })

        // Botão
        Button(
            onClick = { enviarNotificacao(context) },
            modifier = Modifier.constrainAs(botaoReservar) {
                top.linkTo(checkboxTeste.bottom, margin = 24.dp)
                centerHorizontallyTo(parent)
            }
        ) {
            Text("Reservar")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuTopo(titulo: String, onNavigateClick: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    TopAppBar(
        title = { Text(titulo) },
        navigationIcon = {
            Box {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.Menu, contentDescription = "Menu")
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Resumo") },
                        onClick = {
                            expanded = false
                            onNavigateClick("principal")
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Lista") },
                        onClick = {
                            expanded = false
                            onNavigateClick("lista")
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Reservar") },
                        onClick = {
                            expanded = false
                            onNavigateClick("reserva")
                        }
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF336699),
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White
        )
    )
}

@Composable
fun TabelaDiasSelecionaveis() {
    val dias = listOf("Seg", "Ter", "Qua", "Qui", "Sex")
    val diasSelecionados = remember { mutableStateListOf<String>() }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            dias.forEach { dia ->
                val selecionado = dia in diasSelecionados
                Button(
                    onClick = {
                        if (selecionado) diasSelecionados.remove(dia)
                        else diasSelecionados.add(dia)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selecionado) Color.Blue else Color.LightGray
                    )
                ) {
                    Text(dia, color = Color.White)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownVeiculos(veiculos: List<String>, onSelecionado: (String) -> Unit) {
    var expandido by remember { mutableStateOf(false) }
    var selecionado by remember { mutableStateOf("") }

    ExposedDropdownMenuBox(
        expanded = expandido,
        onExpandedChange = { expandido = !expandido }
    ) {
        TextField(
            value = selecionado,
            onValueChange = {},
            readOnly = true,
            label = { Text("Selecione o veículo") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandido) },
            modifier = Modifier.menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = expandido,
            onDismissRequest = { expandido = false }
        ) {
            veiculos.forEach { veiculo ->
                DropdownMenuItem(
                    text = { Text(veiculo) },
                    onClick = {
                        selecionado = veiculo
                        expandido = false
                        onSelecionado(veiculo)
                    }
                )
            }
        }
    }
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
