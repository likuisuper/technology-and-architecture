# 该镜像需要依赖的基础镜像
FROM java:8
# 指定维护者的名字
MAINTAINER cxylk
# 将当前主机下的jar包复制到容器的/目录下，并重命名为redis-docker-file.jar
COPY redis-0.0.1-SNAPSHOT.jar /redis-docker-file.jar
# 运行过程中创建一个redis-docker-file.jar的文件
RUN bash -c 'touch /redis-docker-file.jar'
# 声明服务在8080端口
EXPOSE 8080
# 指定docker容器启动时运行jar包
ENTRYPOINT ["java","-jar","/redis-docker-file.jar"]