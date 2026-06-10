# 谷谷酱

谷谷酱是一个 Android Kotlin App，用于导入微店订单表，识别消费者 ID，并将其自动填充到本地模板中生成贺卡或小票图片。

## MVP 范围

- 本地模板读取：`PNG + JSON`
- 手动输入消费者 ID 单张生成
- 导入 `CSV / XLS / XLSX` 订单文件
- 推荐字段映射并生成订单列表
- 批量生成 PNG 图片
- 本地保存生成记录
- 预留微店 API / Webhook 模块，不在第一版接真实接口

## 当前工程结构

- `app/src/main/java/com/guguchan/app/ui`：Compose 页面与 ViewModel
- `app/src/main/java/com/guguchan/app/data`：模型、Room 实体、DAO、仓储
- `app/src/main/java/com/guguchan/app/domain/usecase`：导入、映射、生成、批量生成用例
- `app/src/main/java/com/guguchan/app/render`：模板文字绘制与图片导出
- `app/src/main/java/com/guguchan/app/integration`：微店后续扩展预留
- `app/src/main/assets/templates`：本地模板资源

## 本地运行前提

当前机器尚未安装或配置以下工具，因此我没有在此环境完成编译验证：

- Java 17+
- Android SDK
- Gradle 或可用的 Gradle Wrapper

补齐环境后可按以下顺序执行：

1. 安装 Android Studio 或 Java 17 + Android SDK。
2. 在项目根目录补齐 `gradlew`/`gradlew.bat` 与 `gradle/wrapper`。
3. 同步 Gradle 并运行 `assembleDebug`。

## GitHub 上传

当前机器未安装 `gh`，因此未能直接创建远端仓库并推送。已经完成本地项目与 Git 初始化，待安装 GitHub CLI 或配置远端后执行：

```powershell
git remote add origin https://github.com/<your-account>/guguchan.git
git add .
git commit -m "Initial commit"
git push -u origin main
```
