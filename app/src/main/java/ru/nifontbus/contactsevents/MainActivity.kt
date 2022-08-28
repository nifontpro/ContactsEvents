package ru.nifontbus.contactsevents

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.res.stringResource
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.work.WorkManager
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import ru.nifontbus.contactsevents.ui.navigation.BottomBar
import ru.nifontbus.contactsevents.ui.theme.ContactsEventsTheme
import ru.nifontbus.core_ui.Argument
import ru.nifontbus.core_ui.R
import ru.nifontbus.core_ui.Screen
import ru.nifontbus.core_ui.permission.GetPermission
import ru.nifontbus.events_presenter.EventsScreen
import ru.nifontbus.events_presenter.update.EventUpdateScreen
import ru.nifontbus.events_presenter.update.NewEventScreen
import ru.nifontbus.groups_presenter.GroupScreen
import ru.nifontbus.persons_presenter.PersonsScreen
import ru.nifontbus.persons_presenter.info.PersonInfoScreen
import ru.nifontbus.settings_presenter.SettingScreen
import ru.nifontbus.templates_domain.model.Template
import ru.nifontbus.templates_presenter.TemplatesScreen
import ru.nifontbus.worker_domain.util.WORK_TAG
import javax.inject.Inject

@ExperimentalAnimationApi
@ExperimentalPermissionsApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

	@Inject
	lateinit var gson: Gson

	private val viewModel: MainViewModel by viewModels()

	@RequiresApi(33)
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		installSplashScreen().apply {
			setKeepOnScreenCondition {
				viewModel.isLoading.value
			}
		}

		setContent {

			ContactsEventsTheme {
				Surface(color = MaterialTheme.colors.background) {
					GetPermission(
						permission = Manifest.permission.READ_CONTACTS,
						text = stringResource(R.string.sReadDenied)
					) {
						ConfigureExtNavigate()
						InitWorker()

					}
				}
			}
		}
	}

	@Composable
	private fun InitWorker() {

		val mainActivity = this
		LaunchedEffect(key1 = true) {
			val workManager = WorkManager.getInstance(applicationContext)

/*
//            val myWorkRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            val workRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
                Duration.ofMinutes(15), // Периодичность
                Duration.ofMinutes(0) // Смещение внутри периода
            )
                .addTag(WORK_TAG)
//            .setInitialDelay(1, TimeUnit.MINUTES) // Начальная задержка
                .build()

                workManager.enqueueUniquePeriodicWork(
                    "worker",
                    ExistingPeriodicWorkPolicy.KEEP,
                    workRequest
                )

//            workManager.enqueueUniqueWork("worker", ExistingWorkPolicy.KEEP, myWorkRequest)
*/

			if (BuildConfig.DEBUG) {
				workManager.getWorkInfosByTagLiveData(WORK_TAG)
					.observe(mainActivity) { workInfo ->
						workInfo?.let {
							workInfo.forEach {
								Log.e("my", "Worker state: ${it.state}")
							}
						}
					}
			}
		} // Launch
	}

	@RequiresApi(33)
	@Composable
	private fun ConfigureExtNavigate() {
		val extNavController = rememberNavController()
		NavHost(
			navController = extNavController,
			startDestination = Screen.MainScreen.route
		) {
			composable(Screen.MainScreen.route) {
				ConfigureBottomNavigate(extNavController)
			}

			composable(
				Screen.ExtPersonInfoScreen.route,
				arguments = listOf(navArgument(Argument.personId)),
			) {
				PersonInfoScreen(extNavController)
			}

			composable(
				Screen.ExtNewEventScreen.route,
				arguments = listOf(navArgument(Argument.personId)),
			) { entry ->
				val returnTemplate = getTemplateArg(entry)
				NewEventScreen(extNavController, returnTemplate)
			}

			composable(
				Screen.ExtEventUpdateScreen.route,
				arguments = listOf(
					navArgument(Argument.personId), navArgument(Argument.eventId)
				),
			) { entry ->
				val returnTemplate = getTemplateArg(entry)
				EventUpdateScreen(extNavController, returnTemplate)
			}

			composable(
				Screen.ExtTemplatesScreen.route,
			) {
				TemplatesScreen(extNavController, gson)
			}
		}
	}

	private fun getTemplateArg(entry: NavBackStackEntry): Template? =
		try {
			gson.fromJson(entry.arguments?.getString(Argument.template), Template::class.java)
		} catch (e: Exception) {
			null
		}

	private fun navArgument(arg: String) = navArgument(arg) {
		type = NavType.LongType
		defaultValue = -1
		nullable = false
	}

	@Composable
	private fun ConfigureBottomNavigate(extNavController: NavHostController) {
		val navController = rememberNavController()
		Scaffold(
			bottomBar = { BottomBar(navController) }
		) { paddingValues ->
			val bottomPadding = paddingValues.calculateBottomPadding()
			NavHost(
				navController = navController,
				startDestination = Screen.NavEventScreen.route
			) {

				composable(Screen.NavEventScreen.route) {
					EventsScreen(extNavController, bottomPadding)
				}
				composable(Screen.NavPersonScreen.route) {
					PersonsScreen(extNavController, bottomPadding)
				}
				composable(Screen.NavGroupScreen.route) {
					GroupScreen(bottomPadding)
				}
				composable(Screen.NavSettingScreen.route) {
					SettingScreen(paddingValues)
				}
			}
		}
	}
}