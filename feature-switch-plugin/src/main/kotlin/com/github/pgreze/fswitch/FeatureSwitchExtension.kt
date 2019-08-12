package com.github.pgreze.fswitch

import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import java.io.Serializable

open class FeatureSwitchExtension {

    /** Optional when used with android-application plugin */
    var packageId: String? = null

    lateinit var switchs: NamedDomainObjectContainer<FeatureSwitch>

    /**
     * Allowing to use nice Groovy syntax:
     *
     * featureSwitchs {
     *     switchs {
     *         hello { conditions = "world" }
     *     }
     * }
     */
    fun switchs(action: Action<in NamedDomainObjectContainer<FeatureSwitch>>) {
        action.execute(switchs)
    }

    /**
     * Adds a new field into [switchs].
     *
     * Similar to [com.android.build.gradle.internal.dsl.BuildType.buildConfigField]
     */
    @JvmOverloads
    fun fswitch(name: String, conditions: String, description: String? = null) {
        switchs.create(name) {
            it.conditions = conditions
            it.description = description
        }
    }
}

// Requires a single argument constructor with name in order to be used with NamedDomainObjectContainer
data class FeatureSwitch @JvmOverloads constructor(
    /** Field name */
    var name: String,
    /** Set of condition enabling this feature switch */
    var conditions: String? = null,
    /** Optional description used in field Javadoc */
    var description: String? = null
) : Serializable // Needs to be Serializable or cannot be part of a Task input
