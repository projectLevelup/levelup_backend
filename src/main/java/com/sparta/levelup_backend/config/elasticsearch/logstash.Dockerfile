FROM docker.elastic.co/logstash/logstash:8.17.2

# MySQL JDBC 드라이버 및 동의어 파일 복사
COPY src/main/java/com/sparta/levelup_backend/config/elasticsearch/mysql-connector-java.jar /usr/share/logstash/mysql-connector-java.jar
COPY src/main/java/com/sparta/levelup_backend/config/elasticsearch/synonyms.txt /usr/share/logstash/synonyms.txt
COPY src/main/java/com/sparta/levelup_backend/config/elasticsearch/RollOver.json /usr/share/logstash/RollOver.json
COPY src/main/java/com/sparta/levelup_backend/config/elasticsearch/ILM.json /usr/share/logstash/ILM.json

# Logstash의 기본 엔트리포인트 사용
ENTRYPOINT ["/usr/local/bin/docker-entrypoint"]
