The directory with Allure plugins. To add the plugin simply unpack it to this folder.

VM options:
-Dapp.env.default=PROD -Dapp.browsers="chrome,firefox" -Dapp.headless=false -Dapp.parallel.enabled=false -Dselenium.grid.enabled=true

Jenkins Command:
mvn clean install -DskipTests
mvn clean compile test-compile exec:java -Dapp.env.default=PROD -Dapp.browsers=chrome,firefox -Dapp.headless=true