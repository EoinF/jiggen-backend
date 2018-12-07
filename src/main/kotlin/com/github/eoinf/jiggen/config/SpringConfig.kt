package com.github.eoinf.jiggen.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jmx.export.MBeanExporter

@Configuration
open class SpringConfig {
    /*
     * Required bean to avoid duplicate dataSource MXbeans from being registered
     *
     */
    @Bean
    open fun exporter(): MBeanExporter {
        val exporter = MBeanExporter()
        exporter.setAutodetect(true)
        exporter.setExcludedBeans("dataSource")
        return exporter
    }
}