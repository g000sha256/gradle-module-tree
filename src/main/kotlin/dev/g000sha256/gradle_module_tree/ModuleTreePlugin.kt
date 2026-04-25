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

import java.io.File
import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings

public class ModuleTreePlugin : Plugin<Settings> {

    override fun apply(target: Settings) {
        target.extensions.add(
            IncludesBuilder::class.java,
            "include",
            target.createIncludeBuilder(),
        )
    }

    private fun Settings.createIncludeBuilder(
        directories: List<String> = emptyList(),
    ): IncludesBuilder {
        return object : IncludesBuilder {

            override fun directory(name: String, builder: IncludesBuilder.() -> Unit) {
                val nestedDirectories = directories + name

                createDirectory(directories = nestedDirectories)

                val includesBuilder = createIncludeBuilder(directories = nestedDirectories)
                includesBuilder.builder()
            }

            override fun module(name: String) {
                val nestedDirectories = directories + name

                createDirectory(directories = nestedDirectories)

                val path = nestedDirectories.joinToString(separator = ":", prefix = ":")
                include(path)
            }
        }
    }

    private fun Settings.createDirectory(directories: List<String>) {
        val path = directories.joinToString(separator = "/")
        val file = File(rootDir, path)
        file.mkdirs()
    }
}
