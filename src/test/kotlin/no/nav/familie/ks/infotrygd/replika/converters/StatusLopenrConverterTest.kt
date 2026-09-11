package no.nav.familie.ks.infotrygd.replika.converters

import no.nav.familie.ks.infotrygd.replika.model.converters.StatusLopenrConverter
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class StatusLopenrConverterTest {
    val converter: StatusLopenrConverter =
        StatusLopenrConverter()

    @Test
    fun convertToDatabaseColumn() {
        val result = converter.convertToDatabaseColumn(2)
        assertThat(result).isEqualTo("02")

        assertThat(converter.convertToDatabaseColumn(null)).isNull()
    }

    @Test
    fun convertToEntityAttribute() {
        val result = converter.convertToEntityAttribute("02")
        assertThat(result).isEqualTo(2)

        assertThat(converter.convertToEntityAttribute(null)).isNull()
    }
}
