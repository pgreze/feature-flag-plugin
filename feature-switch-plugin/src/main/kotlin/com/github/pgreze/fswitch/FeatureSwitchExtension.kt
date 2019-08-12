package com.github.pgreze.fswitch

open class FeatureSwitchExtension {
    /** Optional when used with android-application plugin */
    var packageId: String? = null
    // NamedDomainObjectContainer?
    var switchs: Map<String, String> = emptyMap()
}
