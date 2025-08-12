# JobSearchApp (Android) - Starter Project

This is a starter Android project (Kotlin) that demonstrates a local app which:
- Runs a scheduled job (WorkManager) to call OpenAI with a job-search prompt at 8:00 AM daily.
- Stores raw results in Room DB.
- Posts a local notification when new results arrive.
- Provides a Settings screen to store your OpenAI API key, roles, and locations.

## How it works
- The `JobFetchWorker` builds a prompt from settings and uses `OpenAIClient` to POST to the Chat Completions endpoint.
- The response is currently stored as raw JSON text in the Room DB. You should improve parsing to extract structured job fields.
- The worker triggers a notification when the fetch completes.

## Build instructions
1. Download and open this project in Android Studio (Arctic Fox or later).
2. Ensure you have JDK 11+.
3. Sync Gradle and allow it to download dependencies.
4. Run the app on a device (recommended) or emulator.
5. In Settings, enter your OpenAI API key and save.
6. The app schedules periodic work; you can also tap "Fetch Now" to enqueue work manually.

## Notes & Next steps
- This project is intentionally minimal. Important improvements before production:
  - Parse Chat API JSON properly (extract assistant message `content`).
  - Convert returned text into structured Job entities (title, company, apply link, description).
  - Add paging, detailed UI for job list, click-to-open links.
  - Add background execution battery optimizations and WorkManager constraints.
  - Securely store API key (encrypt with Android Keystore).
  - Add network error handling & retry/backoff strategies.
  - If you need Telegram monitoring, add Telegram Bot integration separately.

## License
MIT
