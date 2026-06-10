package com.guguchan.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.guguchan.app.data.local.GuguChanDatabase
import com.guguchan.app.data.repository.GenerateRecordRepository
import com.guguchan.app.data.repository.OrderRepository
import com.guguchan.app.data.repository.TemplateRepository
import com.guguchan.app.domain.usecase.BatchGenerateUseCase
import com.guguchan.app.domain.usecase.GenerateImageUseCase
import com.guguchan.app.domain.usecase.ImportOrderUseCase
import com.guguchan.app.domain.usecase.MapOrderFieldsUseCase
import com.guguchan.app.render.BitmapExporter
import com.guguchan.app.render.TemplateRenderer
import com.guguchan.app.render.TextFieldRenderer
import com.guguchan.app.ui.GuguChanApp
import com.guguchan.app.ui.GuguChanViewModel
import com.guguchan.app.ui.GuguChanViewModelFactory
import com.guguchan.app.ui.theme.GuguChanTheme
import com.guguchan.app.utils.CsvParser
import com.guguchan.app.utils.ExcelParser

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = Room.databaseBuilder(
            applicationContext,
            GuguChanDatabase::class.java,
            "guguchan.db"
        ).build()

        setContent {
            GuguChanTheme {
                val dependencies = remember {
                    AppDependencies(
                        templateRepository = TemplateRepository(applicationContext),
                        orderRepository = OrderRepository(database.orderDao()),
                        generateRecordRepository = GenerateRecordRepository(database.generateRecordDao()),
                        importOrderUseCase = ImportOrderUseCase(
                            csvParser = CsvParser(),
                            excelParser = ExcelParser()
                        ),
                        mapOrderFieldsUseCase = MapOrderFieldsUseCase(),
                        generateImageUseCase = GenerateImageUseCase(
                            templateRenderer = TemplateRenderer(TextFieldRenderer()),
                            bitmapExporter = BitmapExporter()
                        ),
                        batchGenerateUseCase = BatchGenerateUseCase(
                            generateImageUseCase = GenerateImageUseCase(
                                templateRenderer = TemplateRenderer(TextFieldRenderer()),
                                bitmapExporter = BitmapExporter()
                            )
                        )
                    )
                }

                val appViewModel: GuguChanViewModel = viewModel(
                    factory = GuguChanViewModelFactory(applicationContext, dependencies)
                )
                GuguChanApp(viewModel = appViewModel)
            }
        }
    }
}

data class AppDependencies(
    val templateRepository: TemplateRepository,
    val orderRepository: OrderRepository,
    val generateRecordRepository: GenerateRecordRepository,
    val importOrderUseCase: ImportOrderUseCase,
    val mapOrderFieldsUseCase: MapOrderFieldsUseCase,
    val generateImageUseCase: GenerateImageUseCase,
    val batchGenerateUseCase: BatchGenerateUseCase
)
