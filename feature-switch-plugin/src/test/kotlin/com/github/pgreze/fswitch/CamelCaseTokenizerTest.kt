package com.github.pgreze.fswitch

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class CamelCaseTokenizerTest {

    @Test
    fun `single value = listOf(value)`() {
        assertThat("value".camelCaseTokens())
            .isEqualTo(listOf("value"))
    }

    @Test
    fun `singleValue = listOf(single, value)`() {
        assertThat("singleValue".camelCaseTokens())
            .isEqualTo(listOf("single", "value"))
    }
}
