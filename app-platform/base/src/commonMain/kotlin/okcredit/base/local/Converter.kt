package okcredit.base.local

interface Converter<T> {
    /**
     * Deserialize to an instance of `T`. The input is retrieved from [Preferences.getString].
     */
    fun deserialize(serialized: String): T

    /**
     * Serialize the `value` to a String. The result will be used with [Preferences.putString].
     */
    fun serialize(value: T): String
}
