package com.michal.tictactoeonline

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.michal.tictactoeonline.data.model.Player
import com.michal.tictactoeonline.data.model.Session
import com.michal.tictactoeonline.di.MyApplication
import com.michal.tictactoeonline.ui.theme.TicTacToeOnlineTheme
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import com.michal.tictactoeonline.util.Resource

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TicTacToeOnlineTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding),
                        (application as MyApplication).appContainer.ticTacToeRepository
                    )

                }
            }
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun Greeting(
    name: String,
    modifier: Modifier = Modifier,
    ticTacToeRepository: TicTacToeRepository
) {
    val coroutineScope = rememberCoroutineScope()
    var idSesji by remember {
        mutableStateOf("")
    }
    var sesja by remember {
        mutableStateOf<Session?>(Session())
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row{
            Button(onClick = {
                coroutineScope.launch {
                    val wynik = ticTacToeRepository.createSession(
                        player1 = Player(
                            "Michal11",
                            "32422S55E235"
                        )
                    )
                        .filter { it is Resource.Success }  // Filtrujemy tylko wynik sukcesu
                        .first()
                    Log.i("test", "${wynik.data}")
                }
            }) {
                Text(text = "Dodaj")
            }

            Button(onClick = {
                coroutineScope.launch {
                    sesja = ticTacToeRepository.getSessionById(idSesji.toLong()).first()
                    val wynik = ticTacToeRepository.removeSession(sesja)
                        .filter { it is Resource.Success }
                        .first()

                    Log.i("test", "${wynik.data}")
                    Log.i("test", "$sesja")
                }
            }) {
                Text(text = "Usuń")
            }


        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Wpisz id sesji")
            TextField(value = idSesji, onValueChange = { idSesji = it })
            Button(onClick = {
                coroutineScope.launch {
                    sesja = ticTacToeRepository.getSessionById(idSesji.toLong()).first()
                }
            }) {
                Text(text = "Wyswietl dane")
            }
                Text(text = "Dane sesji:")
                sesja?.toMap()?.forEach {
                    Text(text = "${it.key} : ${it.value}")
                }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TicTacToeOnlineTheme {

    }
}