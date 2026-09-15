package no.nav.familie.ks.infotrygd.replika

import no.nav.familie.ks.infotrygd.replika.exodus.ExodusProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(ExodusProperties::class)
class InfotrygdApplication

fun main(args: Array<String>) {
    runApplication<InfotrygdApplication>(*args)
}
