# 基础镜像使用JDK 17
FROM openjdk:17

# 指定维护者信息
LABEL maintainer="974942287@qq.com"

# 将JAR文件添加到容器中
ADD target/PhotoBackupProject.jar app.jar

# 暴露端口
EXPOSE 8118

# 运行JAR文件
ENTRYPOINT ["java","-jar","/app.jar"]