package tools.json

import com.fasterxml.jackson.databind.ObjectMapper
import org.jetbrains.annotations.NotNull
import java.io.File

class Discord {
    @NotNull
    lateinit var token: String

    companion object {
        fun fromFile(file: File): Discord {
            val objectMapper = ObjectMapper()
            return objectMapper.readValue(file, Discord::class.java)
        }
    }
}
