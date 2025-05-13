// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    // Use alias for Android Application plugin declaration (applied in :app module)
    alias(libs.plugins.android.application) apply false
    // Use alias for Google Services plugin declaration (applied in :app module)
    alias(libs.plugins.google.gms.google.services) apply false
    // NOTE: We removed the declaration for jetbrains.kotlin.android here
}
