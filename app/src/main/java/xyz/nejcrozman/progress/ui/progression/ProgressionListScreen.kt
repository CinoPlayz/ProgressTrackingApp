package xyz.nejcrozman.progress.ui.progression

import android.graphics.Canvas
import android.graphics.drawable.GradientDrawable
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.renderer.XAxisRenderer
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.utils.Transformer
import com.github.mikephil.charting.utils.Utils
import com.github.mikephil.charting.utils.ViewPortHandler
import xyz.nejcrozman.progress.Destinations
import xyz.nejcrozman.progress.R
import xyz.nejcrozman.progress.shared.entities.Progression
import xyz.nejcrozman.progress.shared.recreateRoute
import xyz.nejcrozman.progress.ui.AppViewModelProvider
import java.time.Instant
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ProgressionListScreen(
    navController: NavHostController,
    viewModel: ProgressionListViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState = viewModel.progressionListUiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(text = "Progress")
            },
            colors = topAppBarColors(
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
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("${Destinations.ProgressionAdd.route}/${viewModel.progressionListUiState.value.typeId}") },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.typeAddTitle)
                )
            }
        },
        content = { paddingScaffold ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    //.verticalScroll(rememberScrollState())
                    .padding(paddingScaffold),

                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp),

                ) {


                val progressionListUiState = viewModel.progressionListUiState.collectAsState()

                Text(
                    text = stringResource(R.string.labelEntries),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 28.sp
                )



                if (progressionListUiState.value.progressionList.isEmpty()) {
                    Text(
                        text = stringResource(R.string.noEntriesDescription),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 24.sp
                    )
                } else {

                    //Display entries
                    LazyColumn(
                        modifier = Modifier
                    ) {
                        //Header
                        item {
                            //Chart

                            //Sorted by date of progress ASC
                            Crossfade(targetState = progressionListUiState.value.progressionList.sortedBy {
                                it.dateOfProgress.toEpochSecond(
                                    ZoneId.systemDefault().rules.getOffset(Instant.now())
                                )
                            }) { lineChartData ->

                                val colorScheme = MaterialTheme.colorScheme
                                AndroidView(
                                    factory = { context ->
                                        //Creates a LineChart with width and height parameters

                                        LineChart(context).apply {
                                            layoutParams = LinearLayout.LayoutParams(
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                700,
                                            )

                                            val dateFormattedWithID: List<String> =
                                                lineChartData.map {
                                                    it.getDOPFormatted + "\n(ID:" + it.progress_id + ")"
                                                }.toList()
                                            //Sets LineCharts properties
                                            with(this) {
                                                animateX(1200, Easing.EaseInSine)
                                                description.isEnabled = false

                                                xAxis.setDrawGridLines(false)
                                                xAxis.position = XAxis.XAxisPosition.BOTTOM
                                                xAxis.granularity = 1F
                                                xAxis.textColor = colorScheme.onBackground.toArgb()
                                                xAxis.valueFormatter =
                                                    MyAxisFormatter(dateFormattedWithID)

                                                this.setXAxisRenderer(
                                                    CustomXAxisRenderer(
                                                        this.viewPortHandler,
                                                        this.xAxis,
                                                        this.getTransformer(YAxis.AxisDependency.LEFT)
                                                    )
                                                )
                                                xAxis.labelRotationAngle = 5f


                                                axisRight.isEnabled = false
                                                //Presledki na straneh
                                                extraRightOffset = 10f
                                                extraLeftOffset = 10f
                                                extraTopOffset = 24f
                                                extraBottomOffset = 14f

                                                legend.orientation =
                                                    Legend.LegendOrientation.VERTICAL
                                                legend.verticalAlignment =
                                                    Legend.LegendVerticalAlignment.TOP
                                                legend.horizontalAlignment =
                                                    Legend.LegendHorizontalAlignment.CENTER
                                                legend.textSize = 15F
                                                legend.form = Legend.LegendForm.LINE
                                                legend.textColor = colorScheme.onBackground.toArgb()
                                            }

                                        }
                                    },
                                    //Modifier for AndroidView
                                    modifier = Modifier
                                        .wrapContentSize()
                                        .padding(5.dp), update = {
                                        //Updates LineChart
                                        updateLineChartWithData(
                                            it,
                                            lineChartData,
                                            "Your progress",
                                            colorScheme = colorScheme
                                        )
                                    })
                            }

                        }

                        //Body
                        items(
                            items = progressionListUiState.value.progressionList,
                            key = { it.progress_id }) { progression ->
                            ProgressionDisplay(
                                progression = progression,
                                modifier = Modifier
                                    .padding(10.dp)
                                    .combinedClickable(
                                        onClick = { navController.navigate("${Destinations.ProgressionEdit.route}/${progression.progress_id}") },
                                        onLongClick = {
                                            viewModel.updateUiStateDialog(true)
                                            viewModel.updateUiProgressionId(progression.progress_id)
                                        },
                                    )
                            )


                        }
                    }
                }


                if (uiState.value.openDeleteDialog) {
                    AlertDialog(onDismissRequest = { viewModel.updateUiStateDialog(false) },
                        title = {
                            Text(text = "Attention")
                        },
                        text = {
                            Text(text = "Are you sure you want to delete?")
                        },

                        confirmButton = {
                            Button(
                                onClick = {
                                    viewModel.updateUiStateDialog(false)
                                    viewModel.deleteType(coroutineScope)
                                    recreateRoute(
                                        navController,
                                        "${Destinations.ProgressionList.route}/${uiState.value.typeId}"
                                    )
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                            ) {
                                Text("Yes")
                            }
                        },
                        dismissButton = {
                            Button(

                                onClick = {
                                    viewModel.updateUiStateDialog(false)
                                }) {
                                Text("No")
                            }
                        }

                    )


                }
            }
        })
}


@Composable
private fun ProgressionDisplay(
    progression: Progression, modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier, elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(4.dp),
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "ID: ${progression.progress_id}, Date: ${progression.getDOPFormatted}, Value: ${progression.value}",
                    style = MaterialTheme.typography.labelLarge,
                    fontSize = 24.sp
                )
            }
        }
    }
}

//Updates LineChart with data
fun updateLineChartWithData(
    lineChart: LineChart,
    data: List<Progression>,
    label: String,
    colorScheme: ColorScheme,
) {
    val progressDataSet = LineDataSet(setDataEntries(data), label)
    progressDataSet.lineWidth = 3f
    progressDataSet.valueTextSize = 15f
    progressDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER


    //Sets colors and add a gradient under the line
    progressDataSet.enableDashedLine(20f, 10f, 0f)
    progressDataSet.setDrawFilled(true)
    progressDataSet.color = colorScheme.onBackground.toArgb()
    progressDataSet.valueTextColor = colorScheme.secondary.toArgb()

    val fillGradient = GradientDrawable(
        GradientDrawable.Orientation.TOP_BOTTOM,
        intArrayOf(colorScheme.primary.toArgb(), colorScheme.secondary.toArgb())
    )
    progressDataSet.fillDrawable = fillGradient

    val dataSet = ArrayList<ILineDataSet>()
    dataSet.add(progressDataSet)

    val lineData = LineData(dataSet)
    lineChart.data = lineData

    lineChart.invalidate()
}

private fun setDataEntries(listProgress: List<Progression>): ArrayList<Entry> {
    val entries = ArrayList<Entry>()
    listProgress.forEachIndexed { index, element ->
        entries.add(Entry(index.toFloat(), element.value.toFloat()))
    }
    return entries
}

//Custom xAxis Renderer with a custom Formatter
class CustomXAxisRenderer(
    viewPortHandler: ViewPortHandler?,
    xAxis: XAxis?,
    trans: Transformer?
) :
    XAxisRenderer(viewPortHandler, xAxis, trans) {
    override fun drawLabel(
        c: Canvas?,
        formattedLabel: String,
        x: Float,
        y: Float,
        anchor: MPPointF?,
        angleDegrees: Float
    ) {
        val lines = formattedLabel.split("\n".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        for (i in lines.indices) {
            val vOffset = i * mAxisLabelPaint.textSize
            Utils.drawXAxisValue(
                c,
                lines[i], x, y + vOffset, mAxisLabelPaint, anchor, angleDegrees
            )
        }
    }
}


class MyAxisFormatter(itemsPassed: List<String>) : IndexAxisValueFormatter() {
    private var items = itemsPassed
    override fun getAxisLabel(value: Float, axis: AxisBase?): String? {

        val index = value.toInt()
        return if (index < items.size) {
            items[index]
        } else {
            null
        }
    }
}

