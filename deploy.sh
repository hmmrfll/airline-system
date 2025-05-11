#!/bin/bash
# Сохраните этот скрипт как deploy.sh

# Скомпилировать проект без создания WAR
mvn compile

# Копировать классы
cp -r target/classes/ /opt/homebrew/opt/tomcat@9/libexec/webapps/airline-system-1.0-SNAPSHOT/WEB-INF/

# Копировать JSP файлы и другие ресурсы
cp -r src/main/webapp/ /opt/homebrew/opt/tomcat@9/libexec/webapps/airline-system-1.0-SNAPSHOT/

echo "Deployment completed"