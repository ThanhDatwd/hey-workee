plugins {
	java
	id("org.springframework.boot") version "3.2.5"
	id("io.spring.dependency-management") version "1.1.4"
	id("com.diffplug.spotless") version "6.25.0"
}

group = "com.katech.service"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-jooq")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.postgresql:postgresql:42.7.7")
	// https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt
	implementation("io.jsonwebtoken:jjwt:0.12.5")
	implementation("org.springframework.boot:spring-boot-starter-data-redis:3.2.5")
	implementation("org.springframework.boot:spring-boot-starter-amqp")
//	implementation("com.mysql:mysql-connector-j:9.2.0")
	implementation("org.springframework.session:spring-session-core:3.4.3")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")

	// https://mvnrepository.com/artifact/commons-codec/commons-codec
	implementation("commons-codec:commons-codec:1.17.0")
	implementation("io.projectreactor.netty:reactor-netty:1.1.21")
	// https://mvnrepository.com/artifact/org.springframework/spring-webflux
	implementation("org.springframework:spring-webflux:6.1.10")
	implementation("org.freemarker:freemarker:2.3.33")
	implementation("org.springframework.boot:spring-boot-starter-validation")

	implementation("org.springframework.boot:spring-boot-starter-websocket")


	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	implementation("org.mapstruct:mapstruct:1.6.3")
	annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")

}

apply(plugin = "com.diffplug.spotless")

tasks.withType<Test> {
	useJUnitPlatform()
}

spotless {
	java {
		// Use the default importOrder configuration
		importOrder()
		// optional: you can specify import groups directly
		// note: you can use an empty string for all the imports you didn't specify explicitly, '|' to join group without blank line, and '\\#` prefix for static imports
		removeUnusedImports()
		// Cleanthat will refactor your code, but it may break your style: apply it before your formatter
		cleanthat()          // has its own section below

		// Choose one of these formatters.
		googleJavaFormat("1.17.0").aosp().reflowLongStrings().skipJavadocFormatting()
		formatAnnotations()  // fixes formatting of type annotations, see below

	}
}
