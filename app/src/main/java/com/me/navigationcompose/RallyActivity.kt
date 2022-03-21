package com.me.navigationcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.me.navigationcompose.data.UserData
import com.me.navigationcompose.ui.accounts.AccountsBody
import com.me.navigationcompose.ui.accounts.SingleAccountBody
import com.me.navigationcompose.ui.bills.BillsBody
import com.me.navigationcompose.ui.components.RallyTabRow
import com.me.navigationcompose.ui.overview.OverviewBody
import com.me.navigationcompose.ui.theme.RallyTheme

/**
 * This Activity recreates part of the Rally Material Study from
 * https://material.io/design/material-studies/rally.html
 */
class RallyActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RallyApp()
        }
    }
}

@Composable
fun RallyApp() {
    RallyTheme {
        val allScreens = RallyScreen.values().toList()
        val navController = rememberNavController()
        val backstackEntry = navController.currentBackStackEntryAsState()
        val currentScreen  = RallyScreen.fromRoute(
            backstackEntry.value?.destination?.route
        )
        val accountsName = RallyScreen.Accounts.name

        Scaffold(
            topBar = {
                RallyTabRow(
                    allScreens = allScreens,
                    onTabSelected = { screen ->
                        navController.navigate(screen.name)
                    },
                    currentScreen = currentScreen
                )
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = RallyScreen.Overview.name,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(RallyScreen.Overview.name) {
                    OverviewBody(
                        onClickSeeAllAccounts = { navController.navigate(RallyScreen.Accounts.name) },
                        onClickSeeAllBills = { navController.navigate(RallyScreen.Bills.name) },
                        onAccountClick = { name ->
                            navigateToSingleAccount(navController, name)
                        }
                    )
                }
                composable(RallyScreen.Accounts.name) {
                    AccountsBody(accounts = UserData.accounts) { name ->
                        navigateToSingleAccount(
                            navController = navController,
                            accountName = name
                        )
                    }
                }
                composable(RallyScreen.Bills.name) {
                    BillsBody(bills = UserData.bills)
                }
                composable(
                    route = "$accountsName/{name}",
                    arguments = listOf(
                        navArgument("name") {
                            // Make argument type safe
                            type = NavType.StringType
                        }
                    )
                ){ entry -> // Look up "name" in NavBackStackEntry's arguments
                    val accountName = entry.arguments?.getString("name")
                    // Find first name match in UserData
                    val account = UserData.getAccount(accountName)
                    // Pass account to SingleAccountBody
                    SingleAccountBody(account = account)
                }

            }
        }
    }
}

private fun navigateToSingleAccount(
    navController: NavHostController,
    accountName: String
) {
    navController.navigate("${RallyScreen.Accounts.name}/$accountName")
}