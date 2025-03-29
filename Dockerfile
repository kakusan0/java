# ベースイメージは OpenJDK の軽量イメージ
FROM openjdk:23-slim AS java

# アプリケーション JAR ファイルを格納するディレクトリを指定
WORKDIR /app

# ビルドで生成された JAR ファイルをコンテナ内にコピー
COPY target/demo-1.jar app.jar

# JAR を実行
ENTRYPOINT ["java", "-jar", "app.jar"]

FROM ubuntu:latest AS ssh

RUN apt-get update && apt-get install -y openssh-server
RUN mkdir /var/run/sshd
RUN echo 'root:root' | chpasswd
RUN sed -i 's/#PasswordAuthentication yes/PasswordAuthentication yes/g' /etc/ssh/sshd_config
RUN sed -i 's/#PermitRootLogin prohibit-password/PermitRootLogin yes/' /etc/ssh/sshd_config

EXPOSE 22
CMD ["/usr/sbin/sshd", "-D"]
