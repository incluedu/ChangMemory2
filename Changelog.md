# Changelog - ChangMemory II

All notable changes and version milestones of this project will be documented in this file. This project is based on the historical Java predecessor *ChangMemory* (2015).

---

## [2.0.0-alpha.1] - In Development (Unreleased)
### Added
* `[+]` Complete porting of the entire game source code from Java to **Kotlin**.
* `[+]` Modernized the build system to the current **Gradle** multi-module structure.
* `[+]` Upgraded the desktop launcher to **LWJGL3** for perfect modern PC compatibility.
* `[+]` Future-proofed `AndroidManifest.xml` with modern `dataExtractionRules` for Android 12 up to Android 16+.
* `[+]` Migrated all asset and level files to the central `assets/` directory for robust cross-platform loading.
* `[+]` Migrated core build scripts from Groovy (`build.gradle`) to the modern, type-safe **Kotlin DSL (`build.gradle.kts`)**.
* `[+]` Integrated **LibKTX** modules (`ktx-actors`, `ktx-log`, and `ktx-scene2d`) to eliminate legacy Java boilerplate code.
* `[+]` Implemented an inline lambda factory `invoke` operator inside `CommandListener` to completely eliminate Java boilerplate (`object : CommandListener()`) in favor of flat Kotlin syntax.
* `[+]` Added comprehensive, professional English KDoc documentation to `CommandListener`, `WindowPause`, `WindowGameOver`, `ScoreList`, and their respective screen wrapper methods.
* `[+]` Refactored interactive game buttons (`BtnBack`, Google Play services UI) into minimalist, crash-safe KTX **`onClick`** lambda listeners.
* `[+]` Cleaned up and modularized complex layout containers (`WindowGameOver`, `WindowPause`) using specialized Kotlin scope functions (`.apply`) and localized string constants.
* `[+]` Fully overhauled the global asset management (`Assets.kt` & `AssetCard.kt`), implementing type-safe generic resource loading (`manager.load<T>`), automated array disposers (`cardAssetList.dispose`), and central companion object configurations for all game sounds.
* `[+]` Converted `AudioManager` and `InfoList` into native Kotlin **`object`** singletons, completely eradicating legacy Java `.instance` boilerplate and private constructor wrappers.
* `[+]` Modernized background music loading by swapping the old Java utility framework randomizers with highly efficient, zero-allocation native Kotlin **`Random.nextInt()`** loops.
* `[+]` Implemented formal, rich KDoc documentation with strict Markdown backtick syntax configurations to protect IDE symbol expansion compilers from crashing on reserved keywords.
* `[+]` Centralized the global engine versioning by introducing a unified `APP_VERSION` compile-time constant inside core `Constants.kt`, creating a single source of truth for all platform modules.
* `[+]` Created a dedicated `DesktopActionResolver.kt` file within the `lwjgl3` module to cleanly separate launcher configurations from desktop-specific no-op cloud overrides.

### Changed
* `[c]` Rebranded the project from *ChangMemory* to **ChangMemory II**.
* `[c]` Drastically increased thread safety and stability across all screens using Kotlin's null-safety features (`lateinit var`, `?.let`, smart casts).
* `[c]` Radically downsized `CardScreen` by decoupling UI event handling and routing click actions directly into `WindowPause` and `WindowGameOver`.
* `[c]` Transformed `Assets` and `ScoreList` into native, robust Kotlin **`object`** singletons, removing the legacy `.instance` boilerplate.
* `[c]` Refactored the leaderboard rendering (`scorePane`) into a declarative, responsive UI layout via **KTX Scene2D DSL**.
* `[c]` Migrated legacy `Gdx.app.debug` statements inside asset classes and core handlers to zero-allocation **`Iktx-log`** inline lambdas.
* `[c]` Decoupled `SoundListObject` from its heavy parent singleton scope by stripping the redundant **`inner`** modifier, transforming it into a lightweight, standalone Kotlin **`data class`** to drastically reduce garbage collection overhead on mobile architectures.
* `[c]` Unified input processor polling hooks across the frame initialization stream by utilizing flat, expression-based Kotlin `if-else` return pipelines.
* `[c]` Re-licensed the entire *ChangMemory II* codebase from Apache 2.0 to the simpler, more community-friendly **MIT License**.
* `[c]` Fully overhauled and modernized the `README.md` documentation, adding explicit setup guides and direct cross-references to the project license and changelog files.
* `[c]` Refactored the core `GameController` and `AchievementManager` layers to fully utilize modern Kotlin **`object`** singletons, removing old instance instantiations.
* `[c]` Replaced high-allocation chronological approximations inside the main time loop with performant, zero-allocation native **`TimeUtils`** nano-subtractions.
* `[c]` Upgraded the card array evaluation pipeline inside `processVisibleCards()` to utilize type-safe, readable KTX **`.first()`** and **`.last()`** collection operators.
* `[c]` Consolidated all scattered balancing configuration variables (base timers, scores, penalties) into structured, central **`companion object`** constants.
* `[c]` Fully migrated the `android` module's native launcher class from Java to a 100% pure **Kotlin implementation (`AndroidLauncher.kt`)**, successfully eliminating legacy Java setter boilerplate (`setActionResolver`) in favor of type-safe Kotlin property syntax.
* `[c]` Upgraded diagnostics inside the Android runtime environment to use zero-allocation, high-performance **`Iktx-log`** inline lambdas to significantly reduce mobile garbage collection overhead.
* `[c]` Refactored the core **`MenuScreen.kt`** lifecycle structure to utilize zero-allocation **`ktx-log`** diagnostics, removing the legacy static `TAG` string string-builders.
* `[c]` Consolidated input processing pipelines inside the menu update ticks by replacing bitwise evaluations with clean logical short-circuit pathways.

### Removed
* `[-]` Permanently removed all legacy AdMob mobile advertising layout containers, banner configuration instances, and network permission hooks from the Android codebase to ensure an ad-free user experience.
* `[-]` Deleted the obsolete `license.apache2` template file to maintain a single, clean licensing architecture across the repository.

### Fixed
* `[f]` Fixed a critical runtime crash on the score screen where an unresolved `LabelStyle` identifier name (`font16`) caused skin deployment crashes; fixed by binding directly to the default chalkboard typography font context.
* `[f]` Fixed deep Z-index rendering bugs where the interactive pause button was obscured by background image swaps during difficulty increments.
* `[f]` Fixed a hidden state freeze where exiting a paused game left flags active, causing subsequent matches to lock instantly.
* `[f]` Resolved critical input crashes by synchronizing `stage.clear()` execution order to fire strictly before fresh asset deployment.
* `[f]` Fixed a game statistics bug where the final count of solved cards incorrectly displayed as `0` on the Game Over screen due to a premature variable reset during level advancements. This was resolved by tracking a new persistent `totalCardSetSolvedCount` in the `GameController`.
* `[f]` Consolidated state reset routines by removing redundant `prepareManualRestart` hooks in favor of atomic, unified variable clearing.
* `[f]` Fixed a dangerous hidden logic bug in `AudioManager.update()` where a bitwise **`and`** operator was erroneously used instead of a proper short-circuit logical **`&&`** evaluation during frame queue checks.
* `[f]` Fixed a critical Java-Base module restriction failure (`Symbol is declared in module java.base...`) caused by accidental IDE auto-imports of internal JDK system drivers (`BMPSet.BACK`). All inputs now reference explicit LibGDX key registers (`com.badlogic.gdx.Input.Keys`).
* `[f]` Fixed a core user experience flow oversight in `CardScreen.resume()` where minimizing or interrupting the application background layer forced gameplay to continue instantly even if the player had manually locked focus inside the modal pause window overlay.
* `[f]` Fixed a significant analytical statistics bug where `cardSetTries` incorrectly registered a double penalty iteration (+2) instead of a proper singular pass (+1) upon failed card match attempts.
* `[f]` Fixed a subtle sound displacement issue inside the lucky strike trigger chain by routing audio execution safely back into the deferred **`AudioManager.add()`** delay register.
* `[f]` Corrected a minor textual layout spelling mistake inside the notification announcement stream from `"TO MANY TRY"` to grammatically sound **`"TOO MANY TRIES"`**.
* `[f]` Resolved a critical multi-platform compiler error chain by routing all core screen and actor service configurations through the modern synchronized instance reference bridge (`ChangMemory.instance.actionResolver`).
* `[f]` Fixed a critical structural runtime freeze by shifting the memory cleanup routine (`stage.dispose()`) from the temporary `hide()` method into the official, permanent `dispose()` engine lifecycle hook.

---

## History - ChangMemory (Java Era)

### Version 1.0.5
* `[a]` Add new leaderboards for highest level, most cards flipped, most cards solved and most lucky strike in a single game.

### Version 1.0.4
* `[c]` Hidden achievements should now work correctly.
* `[c]` Now show score in card-screen HUD display.
* `[c]` Time add in card-screen HUD display only shows when 1 or more seconds left.
* `[c]` Cards in card-screen flipping now faster.
* `[f]` Hopefully fix a bug that sometimes flips a single card back after being resolved.

### Version 1.0.3
* `[c]` Change Android Target SDK to Android 5.1.1.
* `[f]` GameTime in `ScoreScreen` was not saved correctly.
* `[a]` Add a couple of new achievements! `:-)`

### Version 1.0.2
* `[c]` Show version number in credits screen.
* `[f]` Back button now works in credits screen.

### Version 1.0.1
* `[f]` Fix points achievement not working when aborting the game.
* `[c]` Change the handling of the back button in `GameOverWindow` and `GamePauseWindow` to go back to MenuScreen when pressing the button in one of these states.
* `[c]` Button leaderboards and button achievements only visible when signed in to Google Plus.

### Version 1.0.0.034 *(25-Apr-2015)*
* `[c]` First release.

---

## Alpha Phases (Archive)

### Version 0.3.1.0033 alpha *(25-Apr-2015 / Build 33)*
* `[+]` Add counter for cards flipped, cards solved and lucky strike to `GameOverWindow`.
* `[c]` Skip achievements local saving; achievements will now only be available online and load from Google.
* `[+]` Add new achievements for score.

### Version 0.3.0.0028 alpha *(17-Apr-2015 / Build 28)*
* `[f]` Fix a bug with level 20 Achievement.
* `[c]` Back button in game screen now goes to pause window (before it exited to `MenuScreen`).
* `[c]` An aborted game will now also be added to the leaderboards.

### Version 0.2.4 alpha *(11-Apr-2015 / Build 24)*
* `[-]` Remove some outdated images.
* `[*]` New level builder for more consistent levels.
* `[*]` A lot of code cleanup in the main game class.

### Version 0.2.3 alpha *(10-Apr-2015 / Build 23)*
* `[*]` Fix a problem where Achievements would not be received when the Achievement comes offline.

### Version 0.2.2 alpha *(10-Apr-2015 / Build 22)*
* `[+]` Add *Google Analytics V4* service.
* `[+]` Add **SCORE** button to `GameOverWindow`.
* `[c]` Change methods for *Android Game Services* to access with *Google Play Services, Version 7.0 (March 2015)*.
