package com.demosportsapl.app.ui.navigation

object NavRoutes {
    const val HOME = "home"
    const val SPORTS = "sports"
    const val SPORT_DETAIL = "sport_detail/{sportId}"
    const val MY_TABLE = "mytable"
    const val ALERTS = "alerts"
    const val FIXTURES = "fixtures"
    const val RESULTS = "results"
    const val STANDINGS = "standings"
    const val SEARCH = "search"
    const val RULEBOOK = "rulebook"
    const val LOGIN = "login"

    fun sportDetail(sportId: Int) = "sport_detail/$sportId"
}
