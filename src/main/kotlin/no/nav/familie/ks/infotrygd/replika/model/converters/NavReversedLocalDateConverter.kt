package no.nav.familie.ks.infotrygd.replika.model.converters

import jakarta.persistence.Converter

@Converter
class NavReversedLocalDateConverter : AbstractNavLocalDateConverter("ddMMyyyy")
