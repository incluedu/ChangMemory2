import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

tasks.withType<KotlinJvmCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
    }
}

dependencies {
    fun rootProp(name: String): String = rootProject.property(name) as String

    api("com.badlogicgames.gdx:gdx:${rootProp("gdxVersion")}")
    api("org.jetbrains.kotlin:kotlin-stdlib:${rootProp("kotlinVersion")}")

    api("io.github.quillraven.libktx:ktx-app:${rootProp("ktxVersion")}")
    api("io.github.quillraven.libktx:ktx-assets:${rootProp("ktxVersion")}")
    api("io.github.quillraven.libktx:ktx-collections:${rootProp("ktxVersion")}")
    api("io.github.quillraven.libktx:ktx-graphics:${rootProp("ktxVersion")}")
    api("io.github.quillraven.libktx:ktx-log:${rootProp("ktxVersion")}")
    api("io.github.quillraven.libktx:ktx-scene2d:${rootProp("ktxVersion")}")
    api("io.github.quillraven.libktx:ktx-actors:${rootProp("ktxVersion")}")

    if (rootProp("enableGraalNative") == "true") {
        implementation("io.github.berstanio:gdx-svmhelper-annotations:${rootProp("graalHelperVersion")}")
    }
}

tasks.jar {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
