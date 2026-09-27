# To-Do-Liste - ChangMemory II Modernisierung

Diese Liste dokumentiert die verbleibenden Schritte, um *ChangMemory II* auf einen absolut sauberen, performanten und fehlerfreien Kotlin/LibKTX-Stand zu bringen.

---

## 🟥 Priorität 1: Benutzeroberfläche & Screens modernisieren (Nächste Schritte)
Ziel: Alten Java-Layout-Boilerplate-Code entfernen, anonyme Listener durch flache Kotlin-Lambdas ersetzen und auf KTX Scene2D DSL umstellen.

- [ ] **`MenuScreen.kt` (Hauptmenü - Refactoring Teil 2)**
    - [ ] **Architektur-Upgrade:** Die Klasse splitten! Das Layout (die Layer-Funktionen) in eine eigene Klasse `MenuLayout.kt` (erbt von `Table`) auslagern, um die Screen-Lifecycle-Logik strikt vom UI-Design zu trennen.
- [ ] **`SettingsScreen.kt` (Einstellungen)**
    - [ ] UI-Slider, Checkboxen und Layout-Tabellen auf KTX-DSL-Syntax umstellen.
    - [ ] Die Werteänderungen direkt und sauber an `GamePreferences` koppeln.
    - [ ] Das Logging auf `ktx-log` umrüsten.
- [ ] **`ScoreScreen.kt` (Highscore-Tafel)**
    - [ ] Die Ausrichtung und das Zusammenspiel mit dem neuen `ScoreList`-Singleton-Objekt prüfen.
    - [ ] Die Scroll-Mechanik der Rangliste mit sauberen LibKTX-Bindings optimieren.
- [ ] **`CreditsScreen.kt`** & **`LoadingScreen.kt`**
    - [ ] Text-Anzeigen modernisieren und die Lade-Sequenzen für visuelle Assets aufräumen.
- [ ] **Scoreboard-Typografie verfeinern (Optisches Upgrade)**
  - [ ] Eine passende Kreide-Schriftart im **Monospace-Format** (.ttf) einbinden, damit die Zahlenkolonnen (Scores/Zeiten) auf der Tafel exakt vertikal untereinander fluchten.


---

## 🟨 Priorität 2: Spielobjekte & Manager aufräumen
Ziel: Speicherfressende Strukturen optimieren und die Update-Schleifen allokationsfrei halten.

- [ ] **`Card.kt` (Das Karten-Objekt / Actor)**
    - [ ] Den Karten-Actor auf unnötige Objekt-Erzeugungen innerhalb der permanenten `update`-Schleife prüfen.
    - [ ] Die Zustandsänderungen (Flippen, Solved) in saubere Kotlin-Properties umwandeln.
- [ ] **`CardList.kt`**
    - [ ] Die Logik für das Zufallsmuster der Karten optimieren, um Ruckler bei höheren Schwierigkeitsgraden zu verhindern.
- [ ] **`GamePreferences.kt` (Einstellungen speichern)**
    - [ ] Die Einstellungs-Klasse in ein echtes Kotlin-**`object`**-Singleton umwandeln, um das alte `.Companion.instance`-Muster endgültig loszuwerden.
- [ ] **Build-System finalisieren (Groovy-zu-KTS Migration)**
    - [ ] Die verbleibenden Build-Skripte von `lwjgl3/build.gradle` und `android/build.gradle` auf das moderne Kotlin DSL-Format (`.gradle.kts`) umstellen.


---

## 🟩 Priorität 3: Android-Dienste & Google Play (Später / Nach hinten verschoben)
Ziel: Die veralteten Google-Schnittstellen von 2015 durch moderne, stabile Implementierungen ersetzen.

- [ ] **Google Play Games Services (GPGS) v2 einbinden**
    - [ ] Die aktuelle Google Play Games v2 SDK-Abhängigkeit in der `android/build.gradle.kts` eintragen.
    - [ ] Den modernen `PlayGames.getLeaderboardsClient(this)` in der `AndroidLauncher.kt` aktivieren.
    - [ ] Den modernen `PlayGames.getAchievementsClient(this)` in der `AndroidLauncher.kt` aktivieren.

---

## 🏆 Erledigte Meilensteine (Wall of Fame)
- [x] Das gesamte Build-System erfolgreich auf **Kotlin DSL (`.gradle.kts`)** umgestellt.
- [x] Veralteten LWJGL2-Desktop-Launcher durch eine moderne **LWJGL3-Engine** ersetzt.
- [x] `Assets`, `ScoreList`, `AudioManager`, `InfoList` und `AchievementManager` in native Kotlin-**`object` Singletons** verwandelt.
- [x] **`MenuScreen.kt` (Teil 1)**: Vollständig von 54 Warnungen befreit, auf KTX-Logging umgestellt, den zerstörerischen `hide()`-Lifecycle-Bug gefixt und die Button-Clicks stark vereinfacht.
- [x] Den schweren Logik-Fehler behoben, bei dem falsche Karten-Paare `cardSetTries` doppelt (+2 statt +1) bestraft haben.
- [x] Die Musikallokationen durch Wechsel von bitweisem `and` auf logisches `&&` in der Audio-Schleife korrigiert.
- [x] `ActionResolver`-Interface komplett plattformunabhängig entkoppelt und ein typensicheres `GameAchievement`-Enum eingeführt.
- [x] Den `AndroidLauncher` sowie den `Lwjgl3Launcher` zu 100 % auf **Kotlin und KTX-Log** migriert.
- [x] Die AdMob-Werbung restlos aus dem gesamten Android-Subsystem entfernt.
- [x] Die gesamte Versionsnummer als `APP_VERSION` in den zentralen `Constants` verankert.
