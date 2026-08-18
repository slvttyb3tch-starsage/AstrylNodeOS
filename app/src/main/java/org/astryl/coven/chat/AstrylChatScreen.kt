package org.astryl.coven.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AstrylChatScreen() {
    var messageInput by remember { mutableStateOf("") }
    var messages by remember { mutableStateOf(listOf("astryl mesh initialized. e2ee active.")) }

    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFF0D0D0D)).padding(16.dp)
    ) {
        LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(messages) { msg ->
                Box(modifier = Modifier.clip(RoundedCornerShape(18.dp)).background(Color(0xFF1A1A1A)).padding(12.dp)) {
                    Text(text = msg, color = Color.White)
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = messageInput,
                onValueChange = { messageInput = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("encrypted message...", color = Color.Gray) },
                colors = TextFieldDefaults.colors(focusedContainerColor = Color(0xFF1A1A1A), unfocusedContainerColor = Color(0xFF1A1A1A), focusedTextColor = Color.White),
                shape = RoundedCornerShape(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = { if (messageInput.isNotBlank()) { messages = messages + messageInput; messageInput = "" } },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB19CD9)),
                shape = RoundedCornerShape(24.dp)
            ) { Text("send", color = Color.Black) }
        }
    }
}