# Logstash의 공식 이미지를 기반으로 합니다.
FROM docker.elastic.co/logstash/logstash:8.17.2

# MySQL JDBC 드라이버를 컨테이너로 복사
COPY mysql-connector-j-9.2.0.jar /usr/share/logstash/mysql-connector-java.jar

