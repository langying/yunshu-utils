mvn clean package -Dmaven.test.skip=true
mvn install:install-file -DgroupId=com.yunshu.util -DartifactId=yunshu-util -Dversion=1.0.0 -Dpackaging=jar -Dfile=target/yunshu-util-1.0.0.jar

