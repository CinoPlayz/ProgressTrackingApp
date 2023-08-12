package xyz.nejcrozman.progress.ui.progression

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.kizitonwose.calendar.compose.CalendarLayoutInfo
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import xyz.nejcrozman.progress.shared.IconResource
import xyz.nejcrozman.progress.shared.toProgression
import xyz.nejcrozman.progress.ui.AppViewModelProvider
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressionAddScreen(navController: NavHostController, viewModel: ProgressionAddViewModel = viewModel(factory = AppViewModelProvider.Factory)) {

    val coroutineScope = rememberCoroutineScope()
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(100) } // Adjust as needed
    val endMonth = remember { currentMonth.plusMonths(100) } // Adjust as needed
    val daysOfWeek = daysOfWeek(firstDayOfWeek = DayOfWeek.MONDAY)



    val stateCalendar = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = daysOfWeek.first()
    )

    val visibleMonth = rememberFirstMostVisibleMonth(stateCalendar, viewportPercent = 90f)

    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(text = "Add Progression")
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.background
            ),
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.background
                    )
                }
            }
        )

    },
        content = { paddingScaffold ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(paddingScaffold),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp),

                ) {
                OutlinedTextField(
                    value = viewModel.progressionUiState.valueString,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    label = { Text(text = "Value") },
                    placeholder = { Text(text = "Value to be added to previous") },
                    onValueChange = {
                            viewModel.updateUiStateOnlyValueString(it)
                    },
                )


                Button(modifier = Modifier.padding(paddingValues = PaddingValues(top = 20.dp)),
                    onClick = {
                        viewModel.updateUiStateOnlyDialog(true)
                    }) {
                    Text(text = "DATE")
                }

                if(viewModel.progressionUiState.openDateDialog){
                    AlertDialog(onDismissRequest = {  viewModel.updateUiStateOnlyDialog(false)},
                        title = {
                            Text(text = "Attention")
                        },
                        text = {
                            Column {
                                val stateDateTime = viewModel.progressionUiState.progressionDetails.dateOfProgress
                                var pickedDate = LocalDate.of(stateDateTime.year, stateDateTime.monthValue, stateDateTime.dayOfMonth)
                                SimpleCalendarTitle(
                                    modifier = Modifier.padding(vertical = 10.dp, horizontal = 8.dp),
                                    currentMonth = visibleMonth.yearMonth,
                                    goToPrevious = {
                                        coroutineScope.launch {
                                            stateCalendar.animateScrollToMonth(stateCalendar.firstVisibleMonth.yearMonth.previousMonth)
                                        }
                                    },
                                    goToNext = {
                                        coroutineScope.launch {
                                            stateCalendar.animateScrollToMonth(stateCalendar.firstVisibleMonth.yearMonth.nextMonth)
                                        }
                                    },
                                )
                                DaysOfWeekTitle(daysOfWeek = daysOfWeek)
                                HorizontalCalendar(
                                    state = stateCalendar,
                                    dayContent = { day ->
                                        Day(day, isSelected = pickedDate == day.date) { day ->
                                            pickedDate = if (pickedDate == day.date) null else day.date
                                            val pickedDateTime = LocalDateTime.of(pickedDate.year, pickedDate.monthValue, pickedDate.dayOfMonth, 1, 1, 0)
                                            viewModel.updateUiState(viewModel.progressionUiState.progressionDetails.copy(dateOfProgress = pickedDateTime))
                                        }
                                    }
                                )
                            }
                        },


                        confirmButton = {
                            Button(
                                onClick = {




                                    viewModel.updateUiStateOnlyDialog(false)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)) {
                                Text("Yes")
                            }
                        },
                        dismissButton = {
                            Button(

                                onClick = {
                                    viewModel.updateUiStateOnlyDialog(false)
                                }) {
                                Text("No")
                            }
                        }

                    )

                }
                
                Text(text = viewModel.progressionUiState.progressionDetails.toProgression().getDOPFormatted)
                
                

                Button(modifier = Modifier.padding(paddingValues = PaddingValues(top = 20.dp)),
                    enabled = viewModel.progressionUiState.isEntryValid,
                    onClick = {
                        viewModel.updateUiState(viewModel.progressionUiState.progressionDetails.copy(value = viewModel.progressionUiState.valueString.toIntOrNull() ?: 1))
                        coroutineScope.launch {
                            viewModel.saveType()
                            navController.popBackStack()
                        }
                    }) {
                    Text(text = "ADD")
                }
            }
        })
}

@Composable
fun DaysOfWeekTitle(daysOfWeek: List<DayOfWeek>) {
    Row(modifier = Modifier.fillMaxWidth()) {
        for (dayOfWeek in daysOfWeek) {
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
            )
        }
    }
}

@Composable
fun Day(day: CalendarDay, isSelected: Boolean, onClick: (CalendarDay) -> Unit) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(color = if (isSelected) Color.Green else Color.Transparent)
            .clickable(
                enabled = day.position == DayPosition.MonthDate,
                onClick = { onClick(day) }
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(text = day.date.dayOfMonth.toString())
    }
}

@Composable
fun SimpleCalendarTitle(
    modifier: Modifier,
    currentMonth: YearMonth,
    goToPrevious: () -> Unit,
    goToNext: () -> Unit,
) {
    Row(
        modifier = modifier.height(40.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CalendarNavigationIcon(
            icon = IconResource.fromImageVector(Icons.Filled.KeyboardArrowLeft).asPainterResource(),
            contentDescription = "Previous",
            onClick = goToPrevious,
        )
        Text(
            modifier = Modifier
                .weight(1f)
                .testTag("MonthTitle"),
            text = currentMonth.displayText(),
            fontSize = 22.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
        )
        CalendarNavigationIcon(
            icon = IconResource.fromImageVector(Icons.Filled.KeyboardArrowRight).asPainterResource(),
            contentDescription = "Next",
            onClick = goToNext,
        )
    }
}

@Composable
private fun CalendarNavigationIcon(
    icon: Painter,
    contentDescription: String,
    onClick: () -> Unit,
) = Box(
    modifier = Modifier
        .fillMaxHeight()
        .aspectRatio(1f)
        .clip(shape = CircleShape)
        .clickable(role = Role.Button, onClick = onClick),
) {
    Icon(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
            .align(Alignment.Center),
        painter = icon,
        contentDescription = contentDescription,
    )
}


fun YearMonth.displayText(short: Boolean = false): String {
    return "${this.month.displayText(short = short)} ${this.year}"
}

fun Month.displayText(short: Boolean = true): String {
    val style = if (short) TextStyle.SHORT else TextStyle.FULL
    return getDisplayName(style, Locale.ENGLISH)
}

/**
 * Find the first month on the calendar visible up to the given [viewportPercent] size.
 *
 *  */
@Composable
fun rememberFirstMostVisibleMonth(
    state: CalendarState,
    viewportPercent: Float = 50f,
): CalendarMonth {
    val visibleMonth = remember(state) { mutableStateOf(state.firstVisibleMonth) }
    LaunchedEffect(state) {
        snapshotFlow { state.layoutInfo.firstMostVisibleMonth(viewportPercent) }
            .filterNotNull()
            .collect { month -> visibleMonth.value = month }
    }
    return visibleMonth.value
}

private fun CalendarLayoutInfo.firstMostVisibleMonth(viewportPercent: Float = 50f): CalendarMonth? {
    return if (visibleMonthsInfo.isEmpty()) {
        null
    } else {
        val viewportSize = (viewportEndOffset + viewportStartOffset) * viewportPercent / 100f
        visibleMonthsInfo.firstOrNull { itemInfo ->
            if (itemInfo.offset < 0) {
                itemInfo.offset + itemInfo.size >= viewportSize
            } else {
                itemInfo.size - itemInfo.offset >= viewportSize
            }
        }?.month
    }
}
