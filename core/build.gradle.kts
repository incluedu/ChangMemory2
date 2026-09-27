dependencies {
    fun rootProp(name: String): String = rootProject.property(name) as String

    // Core libGDX & Kotlin
    api("com.badlogicgames.gdx:gdx:${rootProp("gdxVersion")}")
    api("org.jetbrains.kotlin:kotlin-stdlib:${rootProp("kotlinVersion")}")
    api("com.badlogicgames.gdx:gdx-freetype:${rootProp("gdxVersion")}")

    // LibKTX Module
    api("io.github.quillraven.libktx:ktx-app:${rootProp("ktxVersion")}")
    api("io.github.quillraven.libktx:ktx-assets:${rootProp("ktxVersion")}")
    api("io.github.quillraven.libktx:ktx-collections:${rootProp("ktxVersion")}")
    api("io.github.quillraven.libktx:ktx-graphics:${rootProp("ktxVersion")}")
    api("io.github.quillraven.libktx:ktx-log:${rootProp("ktxVersion")}")
    api("io.github.quillraven.libktx:ktx-scene2d:${rootProp("ktxVersion")}")
    api("io.github.quillraven.libktx:ktx-actors:${rootProp("ktxVersion")}")
    api("io.github.quillraven.libktx:ktx-freetype:${rootProp("ktxVersion")}")

    // GraalVM
    if (rootProp("enableGraalNative") == "true") {
        implementation("io.github.berstanio:gdx-svmhelper-annotations:${rootProp("graalHelperVersion")}")
    }
}
