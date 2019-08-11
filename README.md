# Feature Switch DSL

Gradle Plugin providing an alternative DSL from BuildConfig
for declaring feature switch without duplicate.

Also a tiny project allowing to see how to write a code generating Gradle Plugin
and plug it with Android Gradle plugin.

## TODO

- auto-resolve packageId from manifest for generated code
- support multiple conditions with product variant
- remove "Condition is always" from ConstantConditionIf lint
