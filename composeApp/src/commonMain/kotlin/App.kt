
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import cafe.adriel.voyager.navigator.Navigator
import datastore.screen.DataStoreExampleScreen
import event.CounterAction
import event.CountriesAction
import event.UsersAction
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
import model.User
import model.UsersScreenState
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import sqlDelight.events.AddUserEvents
import sqlDelight.events.UsersScreenActions
import sqlDelight.model.AddUserScreenState
import sqlDelight.model.SqlUsersScreenState
import theme.AppTheme

@OptIn(ExperimentalResourceApi::class)
@Composable
@Preview
fun App() {
    AppTheme {
        Navigator(DataStoreExampleScreen())
//        Navigator(HomeScreen()){
//            SlideTransition(it)
//        }
    }

//    PreComposeApp {
//        val navigator= rememberNavigator()
//        AppTheme {
//            NavHost(
//                navigator = navigator,
//                initialRoute = ScreenRoutes.Home.route,
//                navTransition = NavTransition()
//            ){
//                scene(route = ScreenRoutes.Home.route){
//                    val viewModel= koinViewModel(SqlUsersViewModel::class)
//                    val state by viewModel.sqlUsersScreenState.collectAsState()
//
//                    SqlUsersScreen(
//                        sqlUsersScreenState = state,
//                        onEvent = viewModel::onEvent,
//                        onNavigateToAdd = {
//                            navigator.navigate(ScreenRoutes.AddUser.route)
//                        }
//                    )
//                }
//                scene(route = ScreenRoutes.AddUser.route){
//                    val viewModel= koinViewModel(AddUserViewModel::class)
//                    val state by viewModel.addUserScreenState.collectAsState()
//
//                    AddSqlUserScreen(
//                        addUserScreenState = state,
//                        onEvent = viewModel::onEvent,
//                        onNavigateUp = {
//                            navigator.goBack()
//                        }
//                    )
//                }
//            }
//        }
//    }
//    AppTheme {
//        Children(
//            stack = childStack,
//            animation = stackAnimation(slide())
//        ){child->
//            when(val instance=child.instance){
//                is HomeComponent.ChildScreens.Users->{
//                    val screenComponent=instance.usersComponent
//                    val sqlUsersScreenState by screenComponent.state.collectAsState()
//                    SqlUsersScreen(
//                        sqlUsersScreenState = sqlUsersScreenState,
//                        onEvent = screenComponent::onEvent
//                    )
//                }
//                is HomeComponent.ChildScreens.AddUser->{
//                    val screenComponent=instance.addUserComponent
//                    val addUserScreenState by screenComponent.state.collectAsState()
//                    AddSqlUserScreen(
//                        addUserScreenState = addUserScreenState,
//                        onEvent = screenComponent::onEvent
//                    )
//                }
//            }
//        }
//    }
}

@Composable
fun SqlUsersScreen(
    sqlUsersScreenState: SqlUsersScreenState,
    onEvent:(UsersScreenActions)->Unit={},
    onNavigateToAdd:()->Unit
) {
    val lazyListState= rememberLazyListState()
    Scaffold(modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {onNavigateToAdd()},
                backgroundColor = AppTheme.colorScheme.primary,
                contentColor = AppTheme.colorScheme.onPrimary
            ){
                Icon(imageVector = Icons.Default.Add,contentDescription = null)
            }
        },
        contentColor = AppTheme.colorScheme.onBackground,
        backgroundColor = AppTheme.colorScheme.background){
        Box(modifier = Modifier.fillMaxSize()
            .padding(it)){

            LazyColumn(modifier = Modifier.fillMaxSize(), state = lazyListState){
                items(items = sqlUsersScreenState.users, key = {user->
                    user.id
                }){user->
                    SqlUserItem(
                        user = user,
                        showDelete = true,
                        onEvent = onEvent
                    )
                }
            }
        }
    }
}


@Composable
fun SqlUserItem(
    modifier: Modifier=Modifier,
    user: User,
    onEvent: (UsersScreenActions) -> Unit={},
    showDelete: Boolean=false
) {
    Row(modifier = modifier
        .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier
            .padding(vertical = 10.dp, horizontal = 8.dp)
            .weight(1f),
            verticalArrangement = Arrangement.Center){

            Text(text = "Id is ${user.id}", fontSize = 20.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Name is ${user.name}", fontSize = 20.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Address is "+user.address, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Age is "+user.age.toString(), fontSize = 20.sp)
            Spacer(modifier = Modifier.height(8.dp))
        }

        AnimatedVisibility(visible = showDelete){
            IconButton(onClick = {
                onEvent(UsersScreenActions.DeleteUser(user.id))
            }){
                Icon(imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = AppTheme.colorScheme.primary)
            }
        }
    }
}

@Composable
fun AddSqlUserScreen(
    addUserScreenState: AddUserScreenState,
    onEvent: (AddUserEvents) -> Unit,
    onNavigateUp:()->Unit
) {
    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title={
                    Text(text = "Add User",
                        color = AppTheme.colorScheme.onPrimary)
                },
                navigationIcon = {
                    IconButton(onClick = { onNavigateUp() }){
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack ,
                            contentDescription = null,
                            tint = AppTheme.colorScheme.onPrimary)
                    }
                },
                backgroundColor = AppTheme.colorScheme.primary,
                contentColor = AppTheme.colorScheme.onPrimary
            )
        },
        backgroundColor = AppTheme.colorScheme.background,
        contentColor = AppTheme.colorScheme.onBackground) {paddingValues->
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(paddingValues)
                .background(AppTheme.colorScheme.background),
            verticalArrangement = Arrangement.Center ,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            OutlinedTextField(
                value = addUserScreenState.name ,
                onValueChange = {
                    onEvent(AddUserEvents.UpdateName(it))
                } ,
                label = {
                    Text(text = "Enter name")
                } ,
                colors = TextFieldDefaults.textFieldColors(
                    textColor = AppTheme.colorScheme.onBackground ,
                    backgroundColor = AppTheme.colorScheme.background ,
                    cursorColor = AppTheme.colorScheme.primary ,
                    focusedLabelColor = AppTheme.colorScheme.primary ,
                    unfocusedLabelColor = AppTheme.colorScheme.onBackground.copy(alpha = 0.6f) ,
                    focusedIndicatorColor = AppTheme.colorScheme.primary ,
                    unfocusedIndicatorColor = AppTheme.colorScheme.onBackground
                )
            )
            OutlinedTextField(
                value = addUserScreenState.address ,
                onValueChange = {
                    onEvent(AddUserEvents.UpdateAddress(it))
                } ,
                label = {
                    Text(text = "Enter address")
                } ,
                colors = TextFieldDefaults.textFieldColors(
                    textColor = AppTheme.colorScheme.onBackground ,
                    backgroundColor = AppTheme.colorScheme.background ,
                    cursorColor = AppTheme.colorScheme.primary ,
                    focusedLabelColor = AppTheme.colorScheme.primary ,
                    unfocusedLabelColor = AppTheme.colorScheme.onBackground.copy(alpha = 0.6f) ,
                    focusedIndicatorColor = AppTheme.colorScheme.primary ,
                    unfocusedIndicatorColor = AppTheme.colorScheme.onBackground
                )
            )
            OutlinedTextField(
                value = addUserScreenState.age ,
                onValueChange = {
                    onEvent(AddUserEvents.UpdateAge(it))
                } ,
                label = {
                    Text(text = "Enter age")
                } ,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ) ,
                colors = TextFieldDefaults.textFieldColors(
                    textColor = AppTheme.colorScheme.onBackground ,
                    backgroundColor = AppTheme.colorScheme.background ,
                    cursorColor = AppTheme.colorScheme.primary ,
                    focusedLabelColor = AppTheme.colorScheme.primary ,
                    unfocusedLabelColor = AppTheme.colorScheme.onBackground.copy(alpha = 0.6f) ,
                    focusedIndicatorColor = AppTheme.colorScheme.primary ,
                    unfocusedIndicatorColor = AppTheme.colorScheme.onBackground
                )
            )
            Button(
                onClick = {
                    onEvent(AddUserEvents.SaveUserToDb)
                    onEvent(AddUserEvents.ResetValues)
                } , colors = ButtonDefaults.buttonColors(
                    backgroundColor = AppTheme.colorScheme.primary ,
                    contentColor = AppTheme.colorScheme.onPrimary
                )
            ) {
                Text(text = "Add User")
            }
        }
    }
}

@Composable
fun UsersScreen(
    usersScreenState: UsersScreenState=UsersScreenState(),
    onEvent:(UsersAction)->Unit={},
    showScrollBar:Boolean=false
) {
    var showAddUserDialog by remember { mutableStateOf(false) }
    val lazyListState= rememberLazyListState()

    Scaffold(modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                          showAddUserDialog=!showAddUserDialog
                },
                backgroundColor = AppTheme.colorScheme.primary,
                contentColor = AppTheme.colorScheme.onPrimary
            ){
                Icon(imageVector = Icons.Default.Add,contentDescription = null)
            }
        },
        contentColor = AppTheme.colorScheme.onBackground,
        backgroundColor = AppTheme.colorScheme.background){
        Box(modifier = Modifier.fillMaxSize()
            .padding(it)){

            LazyColumn(modifier = Modifier.fillMaxSize(), state = lazyListState){
                items(items = usersScreenState.users, key = {user->
                    user.id
                }){user->
                    UserItem(
                        user = user,
                        modifier = Modifier.clickable {
                            onEvent(UsersAction.SelectUser(user.id))
                        },
                        showDelete = true,
                        onEvent = onEvent
                    )
                }
            }
            if (showAddUserDialog){
                AddUserDialog(
                    usersScreenState = usersScreenState,
                    onEvent = onEvent,
                    onDismiss = {
                        showAddUserDialog=false
                    }
                )
            }

            if (usersScreenState.selectedUser != null){
                UserDetailsDialog(
                    user = usersScreenState.selectedUser,
                    onDismiss = {onEvent(UsersAction.DismissDialog)}
                )
            }

//            AnimatedVisibility(showScrollBar){
//                VerticalScrollbar(
//                    adapter = rememberScrollbarAdapter(lazyListState),
//                    modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight()
//                )
//            }
        }
    }
}

@Composable
fun UserDetailsDialog(
    user: User,
    modifier: Modifier=Modifier,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss){
        UserItem(user, modifier.clip(RoundedCornerShape(8.dp))
            .background(AppTheme.colorScheme.background))
    }
}

@Composable
fun AddUserDialog(
    usersScreenState: UsersScreenState,
    onEvent: (UsersAction) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss){
        Column(modifier = Modifier.fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(AppTheme.colorScheme.background),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally){

            OutlinedTextField(
                value = usersScreenState.name,
                onValueChange = {
                    onEvent(UsersAction.UpdateName(it))
                },
                label = {
                    Text(text = "Enter name")
                },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = AppTheme.colorScheme.onBackground,
                    backgroundColor = AppTheme.colorScheme.background,
                    cursorColor = AppTheme.colorScheme.primary,
                    focusedLabelColor = AppTheme.colorScheme.primary,
                    unfocusedLabelColor = AppTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    focusedIndicatorColor = AppTheme.colorScheme.primary,
                    unfocusedIndicatorColor = AppTheme.colorScheme.onBackground
                )
            )
            OutlinedTextField(
                value = usersScreenState.address,
                onValueChange = {
                    onEvent(UsersAction.UpdateAddress(it))
                },
                label = {
                    Text(text = "Enter address")
                },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = AppTheme.colorScheme.onBackground,
                    backgroundColor = AppTheme.colorScheme.background,
                    cursorColor = AppTheme.colorScheme.primary,
                    focusedLabelColor = AppTheme.colorScheme.primary,
                    unfocusedLabelColor = AppTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    focusedIndicatorColor = AppTheme.colorScheme.primary,
                    unfocusedIndicatorColor = AppTheme.colorScheme.onBackground
                )
            )
            OutlinedTextField(
                value = usersScreenState.age,
                onValueChange = {
                    onEvent(UsersAction.UpdateAge(it))
                },
                label = {
                    Text(text = "Enter age")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                colors = TextFieldDefaults.textFieldColors(
                    textColor = AppTheme.colorScheme.onBackground,
                    backgroundColor = AppTheme.colorScheme.background,
                    cursorColor = AppTheme.colorScheme.primary,
                    focusedLabelColor = AppTheme.colorScheme.primary,
                    unfocusedLabelColor = AppTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    focusedIndicatorColor = AppTheme.colorScheme.primary,
                    unfocusedIndicatorColor = AppTheme.colorScheme.onBackground
                )
            )
            Button(onClick = {
                onEvent(UsersAction.AddUser)
                onDismiss()
                onEvent(UsersAction.ResetValues)
            }, colors = ButtonDefaults.buttonColors(
                backgroundColor = AppTheme.colorScheme.primary,
                contentColor = AppTheme.colorScheme.onPrimary
            )){
                Text(text = "Add User")
            }
        }
    }
}

@Composable
fun UserItem(
    user: User,
    modifier: Modifier=Modifier,
    showDelete:Boolean=false,
    onEvent: (UsersAction) -> Unit={}) {
    Row(modifier = modifier
        .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier
            .padding(vertical = 10.dp, horizontal = 8.dp)
            .weight(1f),
            verticalArrangement = Arrangement.Center){

            Text(text = "Id is ${user.id}", fontSize = 20.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Name is ${user.name}", fontSize = 20.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Address is "+user.address, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Age is "+user.age.toString(), fontSize = 20.sp)
            Spacer(modifier = Modifier.height(8.dp))
        }

        AnimatedVisibility(visible = showDelete){
            IconButton(onClick = {
                onEvent(UsersAction.DeleteUser(user.id))
            }){
                Icon(imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = AppTheme.colorScheme.primary)
            }
        }
    }
}

@Composable
fun CountryScreen(
    countryScreenState: CountryScreenState= CountryScreenState() ,
    onEvent:(CountriesAction)->Unit={},
    gridCount:Int=1,
    scrollBar:@Composable (state:LazyGridState)->Unit={},
) {
    val lazyListState= rememberLazyGridState()

    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = AppTheme.colorScheme.background)){
        if (countryScreenState.isLoading){
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }else{
            Row(modifier = Modifier.fillMaxSize()){
                LazyVerticalGrid(
                    columns = GridCells.Fixed(gridCount),
                    modifier = Modifier.fillMaxHeight()
                        .weight(1f),
                    state = lazyListState
                ){
                    items(items = countryScreenState.countries){country->
                        SingleCountry(simpleCountry = country,
                            modifier = Modifier
                                .padding(15.dp)
                                .clickable { onEvent(CountriesAction.SelectCountry(country.code))})
                    }
                }
                scrollBar(lazyListState)
            }
        }

        if (countryScreenState.selectedCountry != null){
            DetailedCountry(
                detailedCountry = countryScreenState.selectedCountry,
                onDismiss = {onEvent(CountriesAction.DismissDialog)},
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(AppTheme.colorScheme.background)
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
                Text(text = detailedCountry.emoji,
                    fontSize = 30.sp,
                    color = AppTheme.colorScheme.onBackground)
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = detailedCountry.name, fontSize = 24.sp,
                    color = AppTheme.colorScheme.onBackground)
            }
            Text(text = "Continent: ${detailedCountry.continent}",
                color = AppTheme.colorScheme.onBackground)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = "Capital: ${detailedCountry.capital}",
                color = AppTheme.colorScheme.onBackground)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = "Currency: ${detailedCountry.currency}",
                color = AppTheme.colorScheme.onBackground)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = "Language(s): ${detailedCountry.languages}",
                color = AppTheme.colorScheme.onBackground)
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
        Text(text = simpleCountry.emoji, fontSize = 30.sp,
            color = AppTheme.colorScheme.onBackground)
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center) {
            Text(text = simpleCountry.name, fontSize = 22.sp,
                color = AppTheme.colorScheme.onBackground)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = simpleCountry.capital,
                color = AppTheme.colorScheme.onBackground)
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