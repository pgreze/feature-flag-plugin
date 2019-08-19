package com.github.pgreze.fflag

import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import java.io.Serializable

open class FeatureFlagExtension {

    /** Optional when used with android-application plugin */
    var packageId: String? = null

    lateinit var flags: NamedDomainObjectContainer<FeatureFlag>

    /**
     * Allowing to use nice Groovy syntax:
     *
     * featureFlags {
     *     flags {
     *         hello { conditions = "world" }
     *     }
     * }
     */
    fun flags(action: Action<in NamedDomainObjectContainer<FeatureFlag>>) {
        action.execute(flags)
    }

    /**
     * Adds a new field into [flags].
     *
     * Similar to [com.android.build.gradle.internal.dsl.BuildType.buildConfigField]
     */
    @JvmOverloads
    fun flag(name: String, conditions: String, description: String? = null) {
        flags.create(name) {
            it.conditions = conditions
            it.description = description
        }
    }
}

// Requires a single argument constructor with name in order to be used with NamedDomainObjectContainer
data class FeatureFlag @JvmOverloads constructor(
    /** Field name */
    var name: String,
    /** Set of condition enabling this feature flag */
    var conditions: String? = null,
    /** Optional description used in field Javadoc */
    var description: String? = null
) : Serializable // Needs to be Serializable or cannot be part of a Task input
