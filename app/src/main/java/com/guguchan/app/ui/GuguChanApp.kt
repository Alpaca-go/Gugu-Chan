package com.guguchan.app.ui

import android.net.Uri
import java.io.File
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage

@Composable
fun GuguChanApp(viewModel: GuguChanViewModel) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val fileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.importOrders(uri)
        }
    }

    LaunchedEffect(state.message) {
        state.message?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessage()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFFFFF7EF), Color(0xFFF8E0D2), Color(0xFFF5C7AE))
                    )
                )
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                HeroHeader()
            }
            item {
                TemplateSection(
                    state = state,
                    onTemplateSelected = viewModel::selectTemplate
                )
            }
            item {
                ManualGenerateSection(
                    state = state,
                    onValueChange = viewModel::updateManualConsumerId,
                    onGeneratePreview = { viewModel.generateFromManualInput(saveToGallery = false) },
                    onGenerateAndSave = { viewModel.generateFromManualInput(saveToGallery = true) }
                )
            }
            item {
                ImportSection(
                    headers = state.importedHeaders,
                    onPickFile = {
                        fileLauncher.launch(
                            arrayOf(
                                "text/csv",
                                "application/vnd.ms-excel",
                                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                            )
                        )
                    },
                    onMap = viewModel::mapImportedOrders
                )
            }
            item {
                BatchSection(
                    orderCount = state.orders.size,
                    onBatchGenerate = viewModel::batchGenerateSelectedOrders
                )
            }
            if (state.previewPath != null) {
                item {
                    PreviewSection(state.previewPath)
                }
            }
            item {
                RecordsSection(state = state)
            }
        }
    }
}

@Composable
private fun HeroHeader() {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF372F2B)),
        shape = RoundedCornerShape(28.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("谷谷酱", style = MaterialTheme.typography.headlineMedium, color = Color.White)
            Text(
                "微店订单导入、消费者 ID 映射、模板填充、批量图片生成一体化 MVP。",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFFFFE2D1)
            )
        }
    }
}

@Composable
private fun TemplateSection(
    state: GuguChanUiState,
    onTemplateSelected: (String) -> Unit
) {
    SectionCard(title = "模板管理", subtitle = "本地 PNG + JSON 模板") {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            state.templates.forEach { summary ->
                val selected = summary.template.templateId == state.selectedTemplateId
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onTemplateSelected(summary.template.templateId) }
                        .background(
                            if (selected) Color(0xFFFFE0CC) else Color.White.copy(alpha = 0.72f),
                            RoundedCornerShape(18.dp)
                        )
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(summary.template.templateName, fontWeight = FontWeight.SemiBold)
                        Text(summary.template.templateType, style = MaterialTheme.typography.bodySmall)
                    }
                    Text(if (selected) "已选中" else "点击选择")
                }
            }
        }
    }
}

@Composable
private fun ManualGenerateSection(
    state: GuguChanUiState,
    onValueChange: (String) -> Unit,
    onGeneratePreview: () -> Unit,
    onGenerateAndSave: () -> Unit
) {
    SectionCard(title = "单张生成", subtitle = "手动输入消费者 ID") {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(
                value = state.manualConsumerId,
                onValueChange = onValueChange,
                label = { Text("消费者 ID") },
                modifier = Modifier.fillMaxWidth()
            )
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = onGeneratePreview, enabled = !state.isGenerating) {
                    Text("生成预览")
                }
                Button(onClick = onGenerateAndSave, enabled = !state.isGenerating) {
                    Text("生成并保存")
                }
            }
            state.latestSavedPath?.let {
                Text("最近输出: $it", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
private fun ImportSection(
    headers: List<String>,
    onPickFile: () -> Unit,
    onMap: (String, String?) -> Unit
) {
    SectionCard(title = "订单导入", subtitle = "支持 CSV / Excel") {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = onPickFile) {
                Text("选择订单文件")
            }
            if (headers.isNotEmpty()) {
                Text("识别到字段: ${headers.joinToString(" / ")}")
                val consumerIdSuggestion = headers.firstOrNull {
                    it.contains("消费者", ignoreCase = true) || it.contains("买家", ignoreCase = true) || it.contains("ID", ignoreCase = true)
                } ?: headers.first()
                val orderIdSuggestion = headers.firstOrNull { it.contains("订单", ignoreCase = true) }
                Button(onClick = { onMap(consumerIdSuggestion, orderIdSuggestion) }) {
                    Text("按推荐字段映射")
                }
            }
        }
    }
}

@Composable
private fun BatchSection(
    orderCount: Int,
    onBatchGenerate: () -> Unit
) {
    SectionCard(title = "批量生成", subtitle = "针对已导入订单批量出图") {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("当前订单数: $orderCount")
            Button(onClick = onBatchGenerate, enabled = orderCount > 0) {
                Text("批量生成并保存")
            }
        }
    }
}

@Composable
private fun PreviewSection(path: String) {
    SectionCard(title = "生成预览", subtitle = "当前缓存中的预览图") {
        AsyncImage(
            model = File(path),
            contentDescription = "preview",
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
private fun RecordsSection(state: GuguChanUiState) {
    SectionCard(title = "生成记录", subtitle = "可扩展为筛选、重导出和分享") {
        if (state.records.isEmpty()) {
            Text("暂无记录")
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                state.records.take(8).forEach { record ->
                    Card(colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.75f))) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(record.orderId, fontWeight = FontWeight.SemiBold)
                            Text("消费者 ID: ${record.consumerId}")
                            Text("模板: ${record.templateId}")
                            Text("状态: ${record.status.name}")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionCard(
    title: String,
    subtitle: String,
    content: @Composable () -> Unit
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.82f))
    ) {
        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(title, style = MaterialTheme.typography.titleLarge)
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = Color(0xFF725C4E))
            }
            content()
        }
    }
}
