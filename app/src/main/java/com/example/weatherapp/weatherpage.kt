import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.weatherapp.api.networkresponce
import com.example.weatherapp.api.weathermodel
import com.example.weatherapp.weatherViewModel

@Composable
fun weatherpage(viewModel: weatherViewModel) {
    var city by remember { mutableStateOf("") }
    val weatherResult = viewModel.weatherresult.observeAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF084680), Color(0xFF3873A1))
                )
            )
            .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = " Weather Forecast",
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(top = 40.dp, bottom = 24.dp),
            color = Color(0xFFFFFFFF)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            OutlinedTextField(
                value = city,
                onValueChange = { city = it },
                label = { Text("Enter city name") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )

            Spacer(modifier = Modifier.width(12.dp))

            IconButton(
                onClick = {
                    if (city.isNotBlank()) viewModel.getData(city)
                },
                modifier = Modifier.size(56.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        when (val result = weatherResult.value) {
            is networkresponce.Loading -> CircularProgressIndicator()

            is networkresponce.Error -> Text(
                text = " ${result.message}",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyLarge
            )

            is networkresponce.Success<*> -> {
                val data = result.data as? weathermodel
                if (data != null) WeatherDetails(data) else Text(" No data found.")
            }

            null -> {}
        }
    }
}

@Composable
fun WeatherDetails(data: weathermodel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //  Weather Icon from API
            AsyncImage(
                model = "https:${data.current.condition.icon}",
                contentDescription = data.current.condition.text,
                modifier = Modifier.size(80.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = " ${data.location.name}, ${data.location.country}",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                color = Color(0xFF000000)
            )

            Text(
                text = "${(data.current.condition.text)} ${data.current.condition.text}",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Text(
                text = " ${data.current.temp_c}°C",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF000000)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(" Wind: ${data.current.wind_kph} km/h", fontSize = 16.sp)
            Text(" Humidity: ${data.current.humidity}%", fontSize = 16.sp)

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = " Updated: ${data.current.last_updated}",
                fontSize = 13.sp,
                color = Color.Gray
            )
        }
    }
}