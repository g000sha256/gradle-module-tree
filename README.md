# Gradle Module Tree plugin

[![Maven Central](https://img.shields.io/maven-central/v/dev.g000sha256/gradle-module-tree?label=Maven%20Central&labelColor=171C35&color=E38E33)](https://central.sonatype.com/artifact/dev.g000sha256/gradle-module-tree)

A Gradle settings plugin that provides a hierarchical DSL for organizing and auto-creating multi-module project structures.

## Features

- 🌳 Hierarchical DSL for module organization
- 📁 Automatic directory creation
- 🔗 Automatic module inclusion in Gradle

## Setup

Add the plugin to your `settings.gradle.kts` file:

```kotlin
plugins {
    id(id = "dev.g000sha256.gradle-module-tree") version "2.0.0"
}
```

## Usage

Define your directories and modules in the `settings.gradle.kts` file. They will be created and included after syncing
the project:

```kotlin
include {
    module(name = "app")
    directory(name = "core") {
        module(name = "architecture")
        module(name = "di")
        module(name = "resources")
        module(name = "ui")
    }
    directory(name = "features") {
        directory(name = "main") {
            module(name = "data")
            module(name = "domain")
            module(name = "presentation")
        }
        directory(name = "profile") {
            module(name = "data")
            module(name = "domain")
            module(name = "presentation")
        }
    }
    directory(name = "utils") {
        module(name = "coroutines")
    }
}
```

This creates the following project structure:

```text
project
├── app
├── core
│   ├── architecture
│   ├── di
│   ├── resources
│   └── ui
├── features
│   ├── main
│   │   ├── data
│   │   ├── domain
│   │   └── presentation
│   └── profile
│       ├── data
│       ├── domain
│       └── presentation
└── utils
    └── coroutines
```

Then reference modules in dependencies:

```kotlin
dependencies {
    implementation(dependencyNotation = project(path = ":core:architecture"))
    implementation(dependencyNotation = project(path = ":core:resources"))
    implementation(dependencyNotation = project(path = ":utils:coroutines"))
}
```

### Type-safe project accessors

To use Gradle's incubating type-safe project accessors, enable them in your `settings.gradle.kts`:

```kotlin
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
```

Then reference modules using the `projects.*` accessor:

```kotlin
dependencies {
    implementation(dependencyNotation = projects.core.architecture)
    implementation(dependencyNotation = projects.core.resources)
    implementation(dependencyNotation = projects.utils.coroutines)
}
```
