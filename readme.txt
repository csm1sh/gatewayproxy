mvn install:install-file  -Dfile=./jamod-1.2-SNAPSHOT.jar -DgroupId=net.wimpi.modbus -DartifactId=jamod -Dversion=1.2-SNAPSHOT -Dpackaging=jar

--启动中间服务---
主函数入口：com.hagongda.Application
启动后访问默认端口8080. http://localhost:8080/