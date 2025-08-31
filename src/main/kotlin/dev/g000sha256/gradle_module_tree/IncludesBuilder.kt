/*
 * Copyright 2025 Georgii Ippolitov (g000sha256)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.g000sha256.gradle_module_tree

/**
 * A DSL builder for defining hierarchical module structures in Gradle multi-module projects.
 *
 * This interface provides methods to define modules and directories in a tree-like structure,
 * automatically creating the physical directories and including modules in the Gradle build.
 *
 * Example usage in `settings.gradle.kts`:
 * ```kotlin
 * include {
 *     module("app")
 *     directory("core") {
 *         module("ui")
 *         module("util")
 *     }
 *     directory("feature") {
 *         module("profile")
 *         module("settings")
 *     }
 * }
 * ```
 */
public interface IncludesBuilder {

    /**
     * Creates a directory and applies the given builder block to define nested modules and directories.
     *
     * This method creates a physical directory with the given name and provides
     * a nested scope for defining sub-modules and sub-directories within it.
     *
     * @param name the name of the directory to create
     * @param builder a lambda that defines the contents of this directory
     */
    public fun directory(name: String, builder: IncludesBuilder.() -> Unit)

    /**
     * Creates a Gradle module with the given name.
     *
     * This method creates a physical directory for the module and includes it in the
     * Gradle build using the appropriate project path syntax (e.g., `:core:ui`).
     *
     * @param name the name of the module to create and include
     */
    public fun module(name: String)

}
