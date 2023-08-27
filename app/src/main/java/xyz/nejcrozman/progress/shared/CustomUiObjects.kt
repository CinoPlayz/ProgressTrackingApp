package xyz.nejcrozman.progress.shared

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import co.yml.charts.axis.AxisData
import co.yml.charts.common.extensions.formatToSinglePrecision
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.LineType
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp

@Composable
fun StraightLineChart(pointsData: List<Point>, xAxisDataFun:(Int) -> String, xAxisSelectionFun:(Float) -> String) {
    val configuration = LocalConfiguration.current

    val screenWidth = configuration.screenWidthDp.dp
    val xAxisData = AxisData.Builder()
        //.axisStepSize(10.dp)
        .steps(3)
        .labelData {i -> xAxisDataFun(i)}
        .labelAndAxisLinePadding(15.dp)
        .startDrawPadding(40.dp)
        .axisLabelColor(MaterialTheme.colorScheme.tertiary)
        .axisLineColor(MaterialTheme.colorScheme.tertiary)
        .build()
    val yAxisData = AxisData.Builder()
        .steps(5)
        .labelData { i -> val yMin = pointsData.minOf { it.y }
            val yMax = pointsData.maxOf { it.y }
            val yScale = (yMax - yMin) / 5
            ((i * yScale) + yMin).formatToSinglePrecision() }
        .labelAndAxisLinePadding(30.dp)
        .axisLabelColor(MaterialTheme.colorScheme.tertiary)
        .axisLineColor(MaterialTheme.colorScheme.tertiary)
        .build()
    val data = LineChartData(
        gridLines = GridLines(MaterialTheme.colorScheme.outlineVariant),
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = pointsData,
                    lineStyle = LineStyle(lineType = LineType.Straight(), color = MaterialTheme.colorScheme.tertiary),
                    intersectionPoint = IntersectionPoint(color = MaterialTheme.colorScheme.tertiary),
                    selectionHighlightPopUp = SelectionHighlightPopUp(popUpLabel = { x, y ->
                        val xLabel = "x : ${xAxisSelectionFun(x)} "
                        val yLabel = "y : ${String.format("%.2f", y)}"
                        "$xLabel $yLabel"
                    })
                )
            )
        ),
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        bottomPadding = 20.dp,
        backgroundColor = MaterialTheme.colorScheme.background

    )
    LineChart(
        modifier = Modifier
            .width(screenWidth+50.dp)
            .height(300.dp),
        lineChartData = data,



    )
}
