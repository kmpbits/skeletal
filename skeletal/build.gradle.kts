import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinMultiplatform
import org.jetbrains.compose.ExperimentalComposeLibrary

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.mavenPublish)
}

group = property("GROUP") as String
version = property("VERSION_NAME") as String

kotlin {
    jvmToolchain(17)

    androidTarget {
        publishLibraryVariants("release")
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    jvm("desktop")

    sourceSets {
        commonMain.dependencies {
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.runtime)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
            @OptIn(ExperimentalComposeLibrary::class)
            implementation(compose.uiTest)
        }
        val desktopTest by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }
    }
}

android {
    namespace = "io.github.kmpbits.skeletal"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

mavenPublishing {
    configure(
        KotlinMultiplatform(
            javadocJar = JavadocJar.Empty(),
            sourcesJar = true,
        )
    )

    publishToMavenCentral()
    if (project.hasProperty("signingInMemoryKey")) signAllPublications()

    pom {
        name.set("Skeletal")
        description.set(
            "Automatic loading skeletons for Compose Multiplatform — wrap your existing " +
                "composables, no parallel skeleton UI to build or maintain."
        )
        url.set("https://github.com/kmpbits/skeletal")
        inceptionYear.set("2026")

        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }
        developers {
            developer {
                id.set("kmpbits")
                name.set("KMP Bits")
                url.set("https://github.com/kmpbits/")
            }
        }
        scm {
            url.set("https://github.com/kmpbits/skeletal")
            connection.set("scm:git:git://github.com/kmpbits/skeletal.git")
            developerConnection.set("scm:git:ssh://git@github.com/kmpbits/skeletal.git")
        }
    }
}
