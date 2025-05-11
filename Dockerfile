# ベースイメージは OpenJDK の軽量イメージ
FROM openjdk:23-slim AS java

# アプリケーション JAR ファイルを格納するディレクトリを指定
WORKDIR /app

# ビルドで生成された JAR ファイルをコンテナ内にコピー
COPY target/demo-1.jar app.jar

# JAR を実行
ENTRYPOINT ["java", "-jar", "app.jar"]
