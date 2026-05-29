<div align="center">

# 🤖 On-Device Agent

**An AI-powered Android assistant that manages your calendar, drafts emails, and answers questions — all running on your device.**

[![Android](https://img.shields.io/badge/Platform-Android-3DDC84?logo=android&logoColor=white)](https://developer.android.com/)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.0-7F52FF?logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack_Compose-Material3-4285F4?logo=jetpack-compose&logoColor=white)](https://developer.android.com/jetpack/compose)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

</div>

---

## ✨ Features

- 🧠 **Multi-LLM Support** — Connect to OpenAI (GPT-4o), Anthropic (Claude), or Google Gemini as your AI backend
- 📅 **Calendar Management** — View, create, and manage calendar events through natural language
- 📧 **Email Drafting** — AI-assisted email composition with a built-in simulated inbox
- 💬 **Conversational Chat** — Natural chat interface with persistent message history
- 🔧 **Tool-Augmented AI** — The agent can call tools (calendar, email) to take real actions on your behalf
- 🗄️ **Local Database** — Chat history and email drafts stored locally with Room
- 🎨 **Material Design 3** — Modern, clean UI built entirely with Jetpack Compose

---

## 🏗️ Architecture

```
com.example/
├── data/
│   ├── api/              # Retrofit API clients for OpenAI, Anthropic, Gemini
│   ├── database/         # Room database, DAOs for chat & email persistence
│   ├── model/            # Data classes (ChatMessage, EmailItem)
│   └── repository/       # Repository layer for data access
├── services/
│   ├── CalendarManager   # System calendar read/write operations
│   └── LLMAgentService   # Core agent logic, tool calling, prompt orchestration
└── ui/
    ├── AgentViewModel    # Main ViewModel managing app state
    ├── screens/          # Compose UI screens (chat, inbox, calendar, settings)
    └── theme/            # Material 3 theming (colors, typography)
```

---

## 🛠️ Tech Stack

| Category | Technology |
|----------|-----------|
| **Language** | Kotlin 2.0 |
| **UI Framework** | Jetpack Compose + Material Design 3 |
| **Networking** | Retrofit + OkHttp + Moshi |
| **Database** | Room (SQLite) |
| **Architecture** | MVVM with Repository Pattern |
| **Build System** | Gradle (Kotlin DSL) with Version Catalog |
| **Min SDK** | Android 7.0 (API 24) |
| **Target SDK** | Android 16 (API 36) |

---

## 🚀 Getting Started

### Prerequisites

- [Android Studio](https://developer.android.com/studio) (latest stable)
- Android SDK 36
- A physical device or emulator running Android 7.0+

### Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/testencomnom-collab/on-device-agent.git
   cd on-device-agent
   ```

2. **Open in Android Studio**
   Select **File → Open** and choose the project directory.

3. **Configure API Keys**
   Create a `.env` file in the project root (see [`.env.example`](.env.example)):
   ```env
   GEMINI_API_KEY=your_gemini_api_key_here
   ```

4. **Run the app**
   Build and run on an emulator or physical device via Android Studio.

### Supported LLM Providers

You can configure your preferred AI provider in the app's settings screen:

| Provider | Model | Configuration |
|----------|-------|---------------|
| **Google Gemini** | Gemini 2.5 Flash | API key via `.env` file |
| **OpenAI** | GPT-4o | API key via in-app settings |
| **Anthropic** | Claude 3.5 Sonnet | API key via in-app settings |

---

## 🔒 Security

- API keys are stored locally on-device and **never** transmitted to third parties
- The `.env` file containing your Gemini API key is excluded from version control via `.gitignore`
- Release signing keystores are **not** included in the repository

---

## 📄 License

This project is licensed under the MIT License — see the [LICENSE](LICENSE) file for details.

---

<div align="center">

**Built with ❤️ using Kotlin & Jetpack Compose**

</div>
