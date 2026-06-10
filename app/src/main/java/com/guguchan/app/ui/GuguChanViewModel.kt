package com.guguchan.app.ui

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import java.io.File
import java.util.UUID
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.guguchan.app.AppDependencies
import com.guguchan.app.data.model.FieldMapping
import com.guguchan.app.data.model.GenerateRecordModel
import com.guguchan.app.data.model.GenerateStatus
import com.guguchan.app.data.model.OrderModel
import com.guguchan.app.data.model.OrderSource
import com.guguchan.app.data.model.TemplateSummary

data class GuguChanUiState(
    val templates: List<TemplateSummary> = emptyList(),
    val selectedTemplateId: String? = null,
    val manualConsumerId: String = "",
    val importedHeaders: List<String> = emptyList(),
    val importedRows: List<Map<String, String>> = emptyList(),
    val importedSource: OrderSource? = null,
    val orders: List<OrderModel> = emptyList(),
    val records: List<GenerateRecordModel> = emptyList(),
    val previewPath: String? = null,
    val latestSavedPath: String? = null,
    val message: String? = null,
    val isGenerating: Boolean = false
)

class GuguChanViewModel(
    private val context: Context,
    private val dependencies: AppDependencies
) : ViewModel() {
    private val mutableState = MutableStateFlow(GuguChanUiState())

    val uiState: StateFlow<GuguChanUiState> = combine(
        mutableState,
        dependencies.orderRepository.observeOrders(),
        dependencies.generateRecordRepository.observeRecords()
    ) { state, orders, records ->
        state.copy(orders = orders, records = records)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = GuguChanUiState()
    )

    init {
        loadTemplates()
    }

    fun updateManualConsumerId(value: String) {
        mutableState.value = mutableState.value.copy(manualConsumerId = value)
    }

    fun selectTemplate(templateId: String) {
        mutableState.value = mutableState.value.copy(selectedTemplateId = templateId)
    }

    fun importOrders(uri: Uri) {
        viewModelScope.launch {
            runCatching {
                dependencies.importOrderUseCase.import(context, uri)
            }.onSuccess { (source, rows) ->
                mutableState.value = mutableState.value.copy(
                    importedSource = source,
                    importedRows = rows,
                    importedHeaders = rows.firstOrNull()?.keys?.toList().orEmpty(),
                    message = "已导入 ${rows.size} 条原始订单数据"
                )
            }.onFailure {
                mutableState.value = mutableState.value.copy(message = it.message ?: "导入失败")
            }
        }
    }

    fun mapImportedOrders(consumerIdColumn: String, orderIdColumn: String?) {
        viewModelScope.launch {
            val state = mutableState.value
            val source = state.importedSource ?: return@launch
            val orders = dependencies.mapOrderFieldsUseCase.mapFields(
                rawRows = state.importedRows,
                fieldMapping = FieldMapping(
                    consumerIdColumn = consumerIdColumn,
                    orderIdColumn = orderIdColumn
                ),
                source = source
            )
            dependencies.orderRepository.saveOrders(orders)
            mutableState.value = state.copy(message = "已映射并保存 ${orders.size} 条订单")
        }
    }

    fun generateFromManualInput(saveToGallery: Boolean) {
        viewModelScope.launch {
            val state = mutableState.value
            val template = state.templates.firstOrNull {
                it.template.templateId == state.selectedTemplateId
            }?.template ?: return@launch
            val consumerId = state.manualConsumerId.trim()
            if (consumerId.isBlank()) {
                mutableState.value = state.copy(message = "请先输入消费者 ID")
                return@launch
            }

            mutableState.value = state.copy(isGenerating = true)
            val order = OrderModel(
                orderId = "manual_${UUID.randomUUID()}",
                consumerId = consumerId,
                createdAt = System.currentTimeMillis(),
                source = OrderSource.MANUAL_INPUT
            )
            val result = dependencies.generateImageUseCase.generate(
                context = context,
                template = template,
                order = order,
                saveToGallery = saveToGallery
            )
            if (result.success) {
                val record = GenerateRecordModel(
                    recordId = UUID.randomUUID().toString(),
                    orderId = order.orderId,
                    consumerId = order.consumerId,
                    templateId = template.templateId,
                    imagePath = result.imagePath.orEmpty(),
                    source = order.source,
                    status = GenerateStatus.SUCCESS,
                    createdAt = System.currentTimeMillis()
                )
                dependencies.generateRecordRepository.saveRecord(record)
            }
            mutableState.value = state.copy(
                isGenerating = false,
                previewPath = result.previewPath,
                latestSavedPath = result.imagePath,
                message = result.errorMessage ?: "图片已生成"
            )
        }
    }

    fun batchGenerateSelectedOrders() {
        viewModelScope.launch {
            val state = mutableState.value
            val template = state.templates.firstOrNull {
                it.template.templateId == state.selectedTemplateId
            }?.template ?: return@launch

            mutableState.value = state.copy(isGenerating = true)
            val records = dependencies.batchGenerateUseCase.generateBatch(
                context = context,
                template = template,
                orders = state.orders
            )
            records.forEach { dependencies.generateRecordRepository.saveRecord(it) }
            mutableState.value = state.copy(
                isGenerating = false,
                latestSavedPath = records.lastOrNull()?.imagePath,
                message = "批量生成完成，共 ${records.size} 条"
            )
        }
    }

    fun clearMessage() {
        mutableState.value = mutableState.value.copy(message = null)
    }

    private fun loadTemplates() {
        val templates = dependencies.templateRepository.loadTemplates()
        mutableState.value = mutableState.value.copy(
            templates = templates,
            selectedTemplateId = templates.firstOrNull()?.template?.templateId
        )
    }
}

class GuguChanViewModelFactory(
    private val context: Context,
    private val dependencies: AppDependencies
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GuguChanViewModel(context, dependencies) as T
    }
}
