<div align="center">

# btulz.transforms

**Color-Coding 业务系统工具链**

Java 代码生成、数据库结构创建、SQL 执行与 Excel 解析工具集 —— Color-Coding / ibas 生态系统的核心开发工具。

A Java toolkit for code generation, database structure creation, SQL execution, data initialization, and Excel parsing — the core development tool of the Color-Coding / ibas ecosystem.

[![License](https://img.shields.io/badge/license-Apache--2.0-blue.svg)](LICENSE)
[![Java](https://img.shields.io/badge/Java-1.8+-orange.svg)](https://www.oracle.com/java/)
[![Maven](https://img.shields.io/badge/Maven-3.x-red.svg)](https://maven.apache.org/)
[![Version](https://img.shields.io/badge/version-0.2.0-green.svg)](btulz.transforms.core/pom.xml)
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg)](#-贡献--contributing)

</div>

---

## 📖 目录 | Table of Contents

- [✨ 特性 | Features](#-特性--features)
- [📦 模块结构 | Modules](#-模块结构--modules)
- [🚀 快速开始 | Quick Start](#-快速开始--quick-start)
- [💻 CLI 用法 | CLI Usage](#-cli-用法--cli-usage)
- [🗄️ 数据库支持 | Database Support](#️-数据库支持--database-support)
- [🏗️ 架构 | Architecture](#️-架构--architecture)
- [📚 相关项目 | Related Projects](#-相关项目--related-projects)
- [🤝 贡献 | Contributing](#-贡献--contributing)
- [📄 许可证 | License](#-许可证--license)
- [🙏 鸣谢 | Thanks](#-鸣谢--thanks)

---

## ✨ 特性 | Features

- **🔧 代码生成** — 基于模板引擎，从领域模型自动生成 BOBAS 框架代码（Java、TypeScript、配置文件等）
- **🗄️ 数据库结构创建** — 自动生成并执行多数据库 DDL，支持创建/更新/删除数据表结构
- **📜 SQL 执行** — 命令行 SQL 执行工具，支持脚本编排与条件分支
- **📊 Excel 解析** — 解析 Excel 中的领域模型并输出 XML 模型文件
- **🔄 数据初始化** — 从 JAR 包分析业务对象，自动处理数据结构和初始化数据
- **🎨 GUI 界面** — Swing GUI 命令执行器（Shell 模块），XML 定义命令表单
- **🌐 多数据库支持** — MSSQL、MySQL、PostgreSQL、SAP HANA、SQLite、Sybase、达梦 DM8
- **🧩 模板系统** — 区域标记（Region Markers）驱动的模板解析，支持变量替换与迭代

---

## 📦 模块结构 | Modules

| 模块 | 类型 | 说明 |
|------|------|------|
| `btulz.transforms.core` | JAR | **核心引擎** — 模板解析、模型表示、代码/DB/SQL/Excel 转换器，SQL 执行编排 |
| `btulz.transforms.bobas` | JAR | **BOBAS 集成层** — 数据库初始化、JAR 包数据转换、路由配置，依赖 core + bobas |
| `btulz.transforms.shell` | JAR | **GUI 层** — Swing 界面与 XML 命令执行环境，独立运行 |

### 模块依赖关系

```
core   (无内部依赖；外部: log4j, dom4j, JDBC, Apache POI)
bobas  (依赖 core + bobas.businessobjectscommon)
shell  (无内部依赖；独立 UI)
```

---

## 🚀 快速开始 | Quick Start

### 环境要求 | Prerequisites

- **JDK** 1.8+
- **Maven** 3.x

### 构建 | Build

```bash
# 克隆仓库
git clone https://github.com/color-coding/btulz.transforms.git
cd btulz.transforms

# 构建全部模块（按 compile_order.txt 顺序，输出到 release/）
./compile_packages.sh            # Linux / macOS
compile_packages.bat             # Windows

# 构建单个模块
cd btulz.transforms.core && mvn clean package install -Dmaven.test.skip=true
```

### 运行测试 | Run Tests

```bash
cd btulz.transforms.core && mvn test
cd btulz.transforms.core && mvn test -Dtest=TestCommands
```

---

## 💻 CLI 用法 | CLI Usage

### Core 模块

```bash
java -jar btulz.transforms.core-0.2.0.jar  code   -help    # 从模板生成代码
java -jar btulz.transforms.core-0.2.0.jar  ds     -help    # 创建数据库结构
java -jar btulz.transforms.core-0.2.0.jar  sql    -help    # 执行 SQL 命令
java -jar btulz.transforms.core-0.2.0.jar  dsJar  -help    # 分析 JAR 包中的数据结构和 SQL
java -jar btulz.transforms.core-0.2.0.jar  excel  -help    # 解析 Excel 领域模型
java -jar btulz.transforms.core-0.2.0.jar  ls              # 列出可用命令
```

### BOBAS 模块

```bash
java -jar btulz.transforms.bobas-0.2.0.jar  init     -help  # 数据库初始化
java -jar btulz.transforms.bobas-0.2.0.jar  ds       -help  # 数据库结构（bobas 变体）
java -jar btulz.transforms.bobas-0.2.0.jar  routing  -help  # 路由配置
```

### Shell GUI

```bash
java -jar btulz.transforms.shell-0.1.1.jar          # 启动 Swing GUI
btulz.shell.sh / btulz.shell.bat                    # 快速启动脚本
```

> 💡 `code` 命令使用默认模板时需加 `-Release` 参数。

---

## 🗄️ 数据库支持 | Database Support

| 数据库 | 状态 |
|--------|:----:|
| Microsoft SQL Server | ✅ |
| MySQL | ✅ |
| PostgreSQL | ✅ |
| SAP HANA | ✅ |
| SQLite | ✅ |
| Sybase | ✅ |
| 达梦 DM8 | ✅ |

> ⚠️ Core 模块 POM 仅内置 MSSQL、MySQL、PostgreSQL、HANA 的 JDBC 驱动，其余需自行提供。

---

## 🏗️ 架构 | Architecture

### 命令系统

命令基于注解驱动的注册机制，`@Prompt` 注解标记类的 CLI 触发字符串，`CommandsManager` 通过反射构建命令映射。

**继承体系**:
```
Command<C>                    — 抽象基类（run, createArguments, 参数检查）
  └─ Command4Release<C>       — 增加 JAR 资源提取（-Release 标志）
       └─ Command4Code        — 代码生成
       └─ Command4Ds          — 数据结构创建
       └─ Command4Sql         — SQL 执行
       └─ Command4DsJar       — JAR 分析
       └─ Command4Excel       — Excel 解析
```

### 模板系统

模板文件使用区域标记进行迭代解析：

```
$BEGIN_MODEL$ / $END_MODEL$                      — 按模型迭代
$BEGIN_MODEL_PROPERTY$ / $END_MODEL_PROPERTY$     — 按属性迭代
$BEGIN_BO$ / $END_BO$                            — 按业务对象迭代
$BEGIN_BOITEM$ / $END_BOITEM$                    — 按 BO 子项迭代
```

### 数据结构编排 (DS)

DS XML 文件采用 `Action > Step > Script` 结构，支持 `RunOnValue` 条件执行和 `THROW BREAK EXCEPTION` 跳过机制。

---

## 📚 相关项目 | Related Projects

| 项目 | 说明 |
|------|------|
| [ibas-framework](https://github.com/color-coding/ibas-framework) | BOBAS 业务对象框架，本工具生成的主要目标代码框架 |
| [ibas-typescript](https://github.com/color-coding/ibas-typescript) | 前端架构，本工具可生成前端 TypeScript 代码 |
| [ibas-businessone](https://github.com/color-coding/ibas-businessone) | SAP Business One 适配层，含 B1 专用转换工具 |

---

## 🤝 贡献 | Contributing

欢迎提交 Issue 和 Pull Request！

1. Fork 本仓库
2. 创建特性分支（`git checkout -b feature/amazing-feature`）
3. 提交更改（`git commit -m 'Add amazing feature'`）
4. 推送到分支（`git push origin feature/amazing-feature`）
5. 发起 Pull Request

---

## 📄 许可证 | License

本项目基于 [Apache License 2.0](LICENSE) 开源。
---

## 🙏 鸣谢 | Thanks

<table>
  <tr>
    <td align="center">
      <a href="https://github.com/NiurenZhu">牛加人等于朱</a><br>
      <sub>NiurenZhu</sub>
    </td>
    <td align="center">
      <a href="https://github.com/three-stones">老彭</a><br>
      <sub>three-stones</sub>
    </td>
  </tr>
</table>

<div align="center">

**[Color-Coding Studio](http://colorcoding.org/)** · 咔啦工作室

</div>
