package no.nav.infotrygd.kontantstotte.model.converters

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import java.time.LocalDate

@Converter
class NavCharDateConverter : AttributeConverter<LocalDate?, String?> {
    private val converter = _root_ide_package_.no.nav.infotrygd.kontantstotte.model.converters.NavLocalDateConverter()

    override fun convertToDatabaseColumn(attribute: LocalDate?): String? = converter.convertToDatabaseColumn(attribute)?.toString()

    override fun convertToEntityAttribute(dbData: String?): LocalDate? = dbData?.let { converter.convertToEntityAttribute(it.toInt()) }
}
