package com.mycardash

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Tile(val title: String, val pkg: String)

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val tiles = listOf(
      Tile("Ayarlar", "com.android.settings"),
      Tile("Harita", "com.google.android.apps.maps"),
      Tile("Müzik", "com.spotify.music"),
      Tile("YouTube", "com.google.android.youtube"),
      Tile("Tarayıcı", "com.android.chrome"),
      Tile("Telefon", "com.android.dialer"),
    )

    setContent {
      MaterialTheme {
        Dashboard(tiles) { launchApp(it) }
      }
    }
  }

  private fun launchApp(packageName: String) {
    val i: Intent? = packageManager.getLaunchIntentForPackage(packageName)
    if (i != null) startActivity(i)
  }
}

@Composable
fun Dashboard(tiles: List<Tile>, onLaunch: (String) -> Unit) {
  val timeText by produceState(initialValue = nowStr()) {
    while (true) {
      value = nowStr()
      delay(1000)
    }
  }

  Column(
    modifier = Modifier.fillMaxSize().padding(18.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    Card {
      Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(timeText, style = MaterialTheme.typography.headlineMedium)
        Text("MyCarDash", style = MaterialTheme.typography.titleMedium)
      }
    }

    LazyVerticalGrid(
      columns = GridCells.Fixed(3),
      verticalArrangement = Arrangement.spacedBy(12.dp),
      horizontalArrangement = Arrangement.spacedBy(12.dp),
      modifier = Modifier.fillMaxSize()
    ) {
      items(tiles) { t ->
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable { onLaunch(t.pkg) }
        ) {
          Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(t.title, style = MaterialTheme.typography.headlineSmall)
          }
        }
      }
    }
  }
}

private fun nowStr(): String {
  val dt = LocalDateTime.now()
  val fmt = DateTimeFormatter.ofPattern("HH:mm:ss  •  dd.MM.yyyy")
  return dt.format(fmt)
}
