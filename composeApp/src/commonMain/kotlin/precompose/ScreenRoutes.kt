package precompose

sealed class ScreenRoutes(val route:String) {
    data object Home:ScreenRoutes("/home")
    data object AddUser:ScreenRoutes("/adduser")
}