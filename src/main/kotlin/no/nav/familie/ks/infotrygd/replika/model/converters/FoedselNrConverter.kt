package no.nav.familie.ks.infotrygd.replika.model.converters

import jakarta.persistence.AttributeConverter
import no.nav.commons.foedselsnummer.Foedselsnummer

class FoedselNrConverter : AttributeConverter<Foedselsnummer?, String?> {
    override fun convertToDatabaseColumn(attribute: Foedselsnummer?): String? = attribute?.asString

    override fun convertToEntityAttribute(dbData: String?): Foedselsnummer? = dbData?.let { Foedselsnummer(it) }
}
