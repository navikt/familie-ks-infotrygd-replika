package no.nav.familie.ks.infotrygd.replika.converters

import no.nav.commons.foedselsnummer.Foedselsnummer
import no.nav.familie.ks.infotrygd.replika.model.converters.ReversedFoedselNrConverter
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class ReversedFoedselsnummerConverterTest {
    private val dbValue = "54010150572"
    private val entity = Foedselsnummer("01015450572") // TestData.foedselsNr(foedselsdato = LocalDate.of(1854, 1, 1))

    private val converter = ReversedFoedselNrConverter()

    @Test
    fun convertToDatabaseColumn() {
        val result = converter.convertToDatabaseColumn(entity)
        assertThat(result).isEqualTo(dbValue)
        assertThat(converter.convertToDatabaseColumn(null)).isEqualTo("00000000000")
    }

    @Test
    fun convertToEntityAttribute() {
        val result = converter.convertToEntityAttribute(dbValue)
        assertThat(result).isEqualTo(entity)
        assertThat(converter.convertToEntityAttribute(null)).isNull()
        assertThat(converter.convertToEntityAttribute("00000000000")).isNull()
    }
}
