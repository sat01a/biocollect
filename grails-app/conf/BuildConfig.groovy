import grails.util.Environment

grails.servlet.version = "3.0" // Change depending on target container compliance (2.5 or 3.0)
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.work.dir = "target/work"
grails.project.target.level = 1.7
grails.project.source.level = 1.7
grails.server.port.http = 8087

grails.project.fork = [
    // configure settings for compilation JVM, note that if you alter the Groovy version forked compilation is required
    //  compile: [maxMemory: 256, minMemory: 64, debug: false, maxPerm: 256, daemon:true],

    // configure settings for the test-app JVM, uses the daemon by default
    test: false, // [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, daemon:true],
    // configure settings for the run-app JVM
    run: false, // [maxMemory: 768, minMemory: 512, debug: true, maxPerm: 256],
    // configure settings for the run-war JVM
    war: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, forkReserve:false],
    // configure settings for the Console UI JVM
    console: false// [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256]
]


// settings for the development environment
if (Environment.current == Environment.DEVELOPMENT) {
    // Enable hot swap reloading for Grails 2.3+
    grails.reload.enabled = true

    grails.plugin.location.'biocollect-sightings' = './plugins/biocollect-sightings'
    println('Using inline biocollect-sightings plugin...')
}

grails.project.dependency.resolver = "maven"
grails.project.dependency.resolution = {

    // inherit Grails' default dependencies
    inherits("global") {
        excludes 'xercesImpl'
    }
    log "error" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    checksums true // Whether to verify checksums on resolve
    legacyResolve true // needs to be true for inline plugin whether to do a secondary resolve on plugin installation, not advised and here for backwards compatibility

    repositories {
        mavenLocal()
        mavenRepo "http://nexus.ala.org.au/content/groups/public/"
    }

    def tomcatVersion = '7.0.55'
    def metadataExtractorVersion = "2.6.2"
    def imgscalrVersion = "4.2"
    def httpmimeVersion = "4.2.1"
    def jtsVersion = "1.8"
    def geoToolsVersion = "11.1"

    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes e.g.

        compile "com.drewnoakes:metadata-extractor:${metadataExtractorVersion}"
        compile "org.imgscalr:imgscalr-lib:${imgscalrVersion}"
        compile "org.apache.httpcomponents:httpmime:${httpmimeVersion}"
        compile "com.vividsolutions:jts:${jtsVersion}"
        compile "org.geotools.xsd:gt-xsd-kml:${geoToolsVersion}"
        compile "joda-time:joda-time:2.3"
        compile "org.codehaus.groovy.modules.http-builder:http-builder:0.7.1"
        compile "org.apache.httpcomponents:httpcore:4.4.1"
        compile "org.apache.httpcomponents:httpclient:4.4.1"


        compile group: 'au.org.ala',
                name: 'ala-cas-client',
                version:'2.1-SNAPSHOT',
                transitive:false
    }

    plugins {
        // plugins for the build system only
        build ":tomcat:7.0.55.2"
        build (":release:3.1.1", ":rest-client-builder:2.1.1") {
            export = false
        }

        runtime ":jquery:1.11.1"
        //compile ':asset-pipeline:1.9.9'
        // required by the cached-resources plugin
        runtime ":cache-headers:1.1.6"
        if (Environment.current == Environment.PRODUCTION) {
            runtime ":cached-resources:1.0"
        }
        runtime (":rest:0.8") {
            excludes "httpclient", "httpcore"
        }
        runtime ":ala-bootstrap2:2.2"
        runtime ":csv:0.3.1"
        runtime ":lesscss-resources:1.3.3"

        compile ":ala-auth:1.3.1"
        compile ":markdown:1.1.1"
        compile ":resources:1.2.14"
        compile ':cache:1.1.8'
        compile ':cache-headers:1.1.7'
        compile ":cache-ehcache:1.0.5"
        compile ":google-visualization:0.6.2"
        compile ":mail:1.0.7"
        compile ":excel-export:0.2.0"
        compile ":excel-import:1.0.1"

        compile (":images-client-plugin:0.2.3") {
            exclude "ala-web-theme"
        }
        compile ':cookie:1.4'

        if (Environment.current != Environment.DEVELOPMENT) {
            compile ":biocollect-sightings:1.0-SNAPSHOT"
        }

    }
}
