package no.nav.infotrygd.kontantstotte.model.converters

import jakarta.persistence.AttributeConverter
import no.nav.commons.foedselsnummer.Foedselsnummer

class ReversedLongFoedselNrConverter : AttributeConverter<Foedselsnummer?, Long?> {
    private val converter =
        _root_ide_package_.no.nav.infotrygd.kontantstotte.model.converters.ReversedFoedselNrConverter()

    override fun convertToDatabaseColumn(attribute: Foedselsnummer?): Long? = converter.convertToDatabaseColumn(attribute)?.toLong() ?: 0

    override fun convertToEntityAttribute(dbData: Long?): Foedselsnummer? =
        converter.convertToEntityAttribute(dbData?.toString()?.padStart(11, '0'))
}
