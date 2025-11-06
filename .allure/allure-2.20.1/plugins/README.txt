The directory with Allure plugins. To add the plugin simply unpack it to this folder.

VM options:
-Dapp.env.default=PROD -Dapp.browsers="chrome,firefox" -Dapp.headless=false -Dapp.parallel.enabled=false -Dselenium.grid.enabled=true

Jenkins Command:
mvn test -Dapp.env.default=PROD -Dapp.browsers=chrome,firefox -Dapp.headless=true


SELENIUM-GRID CONFIGURATION:
java -jar selenium-server-4.38.0.jar hub --host 192.168.168.131 --port 4444
java -jar selenium-server-4.38.0.jar node --hub http://192.168.168.131:4444
