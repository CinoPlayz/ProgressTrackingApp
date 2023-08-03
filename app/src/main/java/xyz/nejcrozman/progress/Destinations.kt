package xyz.nejcrozman.progress

sealed class Destinations(val route: String){
    object Types: Destinations("types")
    object TypesAdd: Destinations("types/add")
    object TypesDetail: Destinations("types/detail/{elementId}"){
        fun createRoute(elementId: Int) = "types/detail/$elementId"
    }
}
