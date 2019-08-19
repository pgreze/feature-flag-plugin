package com.github.pgreze.fflag

import com.google.common.truth.Truth.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object CamelCaseTokenizerTest : Spek({

    describe("CamelCaseTokenizer") {

        it("single value = listOf(value)") {
            assertThat("value".camelCaseTokens())
                .isEqualTo(listOf("value"))
        }

        it("singleValue = listOf(single, value)") {
            assertThat("singleValue".camelCaseTokens())
                .isEqualTo(listOf("single", "value"))
        }
    }
})
