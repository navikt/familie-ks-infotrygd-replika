package no.nav.infotrygd.kontantstotte.model.converters

import jakarta.persistence.Converter

@Converter
class NavLocalDateConverter : no.nav.infotrygd.kontantstotte.model.converters.AbstractNavLocalDateConverter("yyyyMMdd")
