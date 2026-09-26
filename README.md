# ChangMemory2

A modern cross-platform memory card game built with [libGDX](https://libgdx.com/) and generated via [gdx-liftoff](https://github.com/libgdx/gdx-liftoff).

## 📜 Story & Overview
This game was originally written in Java for my son back in 2015 using LibGDX. To future-proof the codebase and leverage modern development standards, it has been fully updated to the latest LibGDX ecosystem and completely rewritten in **Kotlin** utilizing **LibKTX**.

The project is completely **Open Source**. If you are interested in game development, Kotlin, or LibGDX, you are more than welcome to help me improve the game, add features, or optimize asset pipelines!

💬 **Want to see what's new?** Check out our latest updates in the [Changelog](CHANGELOG.md).

## 🎮 Platforms

- `core`: Main module with the application logic shared by all platforms.
- `lwjgl3`: Primary desktop platform using LWJGL3 (replaces the legacy 'desktop' module).
- `android`: Android mobile platform layer (Requires Android SDK).

## 🛠️ Gradle Build Tool

This project uses [Gradle](https://gradle.org/) to manage dependencies and build targets.
The Gradle wrapper is included, so you can execute tasks directly using `gradlew.bat` (Windows) or `./gradlew` (macOS / Linux) commands.

Useful Gradle tasks and flags:

- `--continue`: Errors will not stop execution; forces Gradle to run remaining independent tasks.
- `--daemon`: Toggles the Gradle daemon to significantly accelerate subsequent build loops.
- `--offline`: Forces the build compiler to use cached dependency archives without network calls.
- `--refresh-dependencies`: Forces validation and re-download of all remote dependencies.
- `android:lint`: Performs Android project structural validations.
- `build`: Assembles sources, compiles binaries, and archives packages for every platform module.
- `clean`: Removes all local `build` target folders to secure clean compilation passes.
- `lwjgl3:jar`: Builds the application's standalone runnable JAR file (found at `lwjgl3/build/libs`).
- `lwjgl3:run`: Automatically compiles and launches the desktop application frame.
- `test`: Executes localized unit tests (if any are present).

> **Note:** Tasks that are not bound to a specific subproject can be executed with a `name:` prefix. For example, `core:clean` safely clears build artifacts exclusively for the shared core engine module.

## ⚖️ License
This project is open-source software licensed under the **MIT License**. See the [LICENSE](LICENSE) file for the full copyright and permission notice.
