setup:
	chmod +x gradlew
	./gradlew wrapper --gradle-version 9.5.0
	mkdir -p code
	cp gradlew code/
	cp gradlew.bat code/ 2>/dev/null || true
	chmod +x code/gradlew
	cd code && ./gradlew wrapper --gradle-version 9.5.0

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