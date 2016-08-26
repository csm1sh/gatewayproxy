#Install jamod jar into local repository
mvn install:install-file  -Dfile=./libs/jamod-1.2-SNAPSHOT.jar -DgroupId=net.wimpi.modbus -DartifactId=jamod -Dversion=1.2-SNAPSHOT -Dpackaging=jar

#Start web app
java -jar gatewayproxy-0.0.1-SNAPSHOT.jar --spring.config.location=./gatewayproxy.properties

--启动中间服务---
主函数入口：com.hagongda.Application
启动后访问默认端口8080. http://localhost:8080/