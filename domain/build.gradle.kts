plugins {
    id("org.jetbrains.kotlin.jvm")
}

dependencies {
    implementation(group = "javax.inject", name = "javax.inject", version = "1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    testImplementation("junit:junit:4.13.2")
}