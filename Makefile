setup:
	chmod +x gradlew
	./gradlew wrapper --gradle-version 9.5.0

build:
	./gradlew build

test:
	./gradlew test

start:
	./gradlew bootRun

dev:
	./gradlew bootRun --args='--spring.profiles.active=dev'

prod:
	./gradlew bootRun --args='--spring.profiles.active=prod'

clean:
	./gradlew clean