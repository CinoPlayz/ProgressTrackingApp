package xyz.nejcrozman.progress

sealed class Destinations(val route: String){
    object Types: Destinations("types")
    object TypesAdd: Destinations("types/add")
    object TypesDetail: Destinations("types/detail" ){
        const val itemIdArg = "elementId"
        val routeWithArgs = "$route/{$itemIdArg}"
    }

    object TypesEdit: Destinations("types/edit" ){
        const val itemIdArg = "elementId"
        val routeWithArgs = "$route/{$itemIdArg}"
    }


    object ProgressionList: Destinations("progressions" ){
        const val itemIdArg = "elementId"
        val routeWithArgs = "$route/{$itemIdArg}"
    }

    object ProgressionAdd: Destinations("progression/add" ){
        const val itemIdArg = "elementId"
        val routeWithArgs = "$route/{$itemIdArg}"
    }

}
