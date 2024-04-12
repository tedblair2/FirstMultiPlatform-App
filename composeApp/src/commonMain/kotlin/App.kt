
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import event.CounterAction
import event.CountriesAction
import firstmultiplatform.composeapp.generated.resources.Res
import firstmultiplatform.composeapp.generated.resources.eg
import firstmultiplatform.composeapp.generated.resources.fr
import firstmultiplatform.composeapp.generated.resources.id
import firstmultiplatform.composeapp.generated.resources.jp
import firstmultiplatform.composeapp.generated.resources.ke
import firstmultiplatform.composeapp.generated.resources.mx
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import model.CounterState
import model.Country
import model.CountryScreenState
import model.DetailedCountry
import model.SimpleCountry
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalResourceApi::class)
@Composable
@Preview
fun App(
    countryScreenState: CountryScreenState= CountryScreenState() ,
    onEvent:(CountriesAction)->Unit={},
    gridCount: Int=1
) {
    MaterialTheme {
        CountryScreen(countryScreenState, onEvent,gridCount)
    }
}

@Composable
fun CountryScreen(
    countryScreenState: CountryScreenState= CountryScreenState() ,
    onEvent:(CountriesAction)->Unit={},
    gridCount:Int=1
) {
    Box(modifier = Modifier.fillMaxSize()){
        if (countryScreenState.isLoading){
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }else{
            LazyVerticalGrid(
                columns = GridCells.Fixed(gridCount),
                modifier = Modifier.fillMaxSize()
            ){
                items(items = countryScreenState.countries){country->
                    SingleCountry(simpleCountry = country,
                        modifier = Modifier
                            .padding(15.dp)
                            .clickable { onEvent(CountriesAction.SelectCountry(country.code))})
                }
            }
//            LazyColumn(modifier = Modifier.fillMaxSize()) {
//                items(items = countryScreenState.countries){country->
//                    SingleCountry(simpleCountry = country,
//                        modifier = Modifier
//                            .padding(15.dp)
//                            .clickable { onEvent(CountriesAction.SelectCountry(country.code))})
//                }
//            }
        }

        if (countryScreenState.selectedCountry != null){
            DetailedCountry(
                detailedCountry = countryScreenState.selectedCountry,
                onDismiss = {onEvent(CountriesAction.DismissDialog)},
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color.White)
                    .padding(16.dp))
        }
    }
}

@Composable
fun DetailedCountry(
    modifier: Modifier=Modifier,
    onDismiss:()->Unit,
    detailedCountry: DetailedCountry
) {
    Dialog(onDismissRequest = onDismiss) {
        Column (modifier = modifier.fillMaxWidth()) {
            Row (modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically){
                Text(text = detailedCountry.emoji, fontSize = 30.sp)
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = detailedCountry.name, fontSize = 24.sp)
            }
            Text(text = "Continent: ${detailedCountry.continent}")
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = "Capital: ${detailedCountry.capital}")
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = "Currency: ${detailedCountry.currency}")
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = "Language(s): ${detailedCountry.languages}")
            Spacer(modifier = Modifier.height(6.dp))
        }
    }
}

@Composable
fun SingleCountry(
    modifier: Modifier=Modifier,
    simpleCountry: SimpleCountry
) {
    Row(modifier = modifier.fillMaxWidth()) {
        Text(text = simpleCountry.emoji, fontSize = 30.sp)
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center) {
            Text(text = simpleCountry.name, fontSize = 22.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = simpleCountry.capital)
        }
    }
}

@Composable
fun CounterScreen(
    counterState: CounterState= CounterState() ,
    onEvent:(CounterAction)->Unit={}
) {
    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {

        Text(text = counterState.count.toString(), fontSize = 20.sp)
        Button(onClick = {
            onEvent(CounterAction.Increment)
        }, modifier = Modifier.padding(vertical = 8.dp)){
            Text("Increase by 1")
        }

        Button(onClick = {
            onEvent(CounterAction.Decrement)
        },modifier = Modifier.padding(vertical = 8.dp)){
            Text("Decrease by 1")
        }

        Button(onClick = {
            onEvent(CounterAction.LoadData)
        },modifier = Modifier.padding(vertical = 8.dp)){
            Text("Increase by 20")
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun SelectCountry(countries: List<Country> = defaultCounties) {
    var showCountries by remember { mutableStateOf(false) }
    var timeAtLocation by remember { mutableStateOf("No Location Selected") }

    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = timeAtLocation,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            textAlign = TextAlign.Center,
            fontSize = 20.sp)
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 15.dp, horizontal = 10.dp)) {
            DropdownMenu(
                expanded = showCountries,
                onDismissRequest = { showCountries = !showCountries}
            ){
                countries.forEach { country ->
                    DropdownMenuItem(
                        onClick = {
                            timeAtLocation=currentTimeAt(country)
                            showCountries=false
                        }
                    ){
                        Row(verticalAlignment = Alignment.CenterVertically){
                            Image(
                                painter = painterResource(country.flag),
                                modifier = Modifier.size(50.dp).padding(end = 10.dp),
                                contentDescription = null
                            )
                            Text(text = country.name, fontSize = 18.sp)
                        }
                    }
                }
            }
        }

        Button(onClick = {
            showCountries=!showCountries
        }, modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 15.dp)){
            Text("Select Country!")
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
val defaultCounties= listOf(
    Country("Japan",TimeZone.of("Asia/Tokyo"),Res.drawable.jp),
    Country("France", TimeZone.of("Europe/Paris"),Res.drawable.fr),
    Country("Mexico", TimeZone.of("America/Mexico_City"),Res.drawable.mx),
    Country("Indonesia", TimeZone.of("Asia/Jakarta"),Res.drawable.id),
    Country("Egypt", TimeZone.of("Africa/Cairo"),Res.drawable.eg),
    Country("Kenya", TimeZone.of("Africa/Nairobi"),Res.drawable.ke)
)

fun currentTimeAt(country: Country):String{
    val time=Clock.System.now().toLocalDateTime(country.timeZone).time
    return "Time at ${country.name} is ${time.formatted()}"
}

fun LocalTime.formatted():String="$hour:$minute:$second"