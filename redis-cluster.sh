#!/bin/bash
# 현재 IP 주소 가져오기
if command -v hostname >/dev/null 2>&1; then
  if [[ "$OSTYPE" == "darwin"* ]]; then
    REDIS_IP=$(ifconfig | grep 'inet ' | grep -v '127.0.0.1' | awk 'NR==1{print $2}')  # macOS
  else
    REDIS_IP=$(hostname -I | awk '{print $1}')  # Linux
  fi
  export REDISCLI_AUTH=$(echo $REDISCLI_AUTH)
else
  echo "호스트 이름 명령어를 찾을 수 없습니다."
  exit 1
fi

# 환경변수 설정
export REDIS_IP
export REDISCLI_AUTH
# Redis 설정 파일을 저장할 디렉토리 생성
mkdir -p redis_conf

# 포트 번호 배열 설정
master_ports=(7001 7002 7003)
slave_ports=(7004 7005 7006)
master_types=("master" "master" "master")
slave_types=("slave" "slave" "slave")

# 각 Redis 노드에 대한 설정 파일 생성
for i in "${!master_ports[@]}"; do
  cat <<EOL > redis_conf/redis-${master_types[$i]}-$((i + 1)).conf
port ${master_ports[$i]}
cluster-enabled yes
cluster-config-file nodes.conf
cluster-node-timeout 3000
appendonly yes
requirepass  ${REDISCLI_AUTH}
masterauth  ${REDISCLI_AUTH}
protected-mode no
bind 0.0.0.0
cluster-announce-ip ${REDIS_IP}
cluster-announce-port ${master_ports[$i]}
cluster-announce-bus-port $((17001 + ${master_ports[$i]} - 7001))
EOL
done

for i in "${!slave_ports[@]}"; do
  cat <<EOL > redis_conf/redis-${slave_types[$i]}-$((i + 1)).conf
port ${slave_ports[$i]}
cluster-enabled yes
cluster-config-file nodes.conf
cluster-node-timeout 3000
appendonly yes
requirepass  ${REDISCLI_AUTH}
masterauth  ${REDISCLI_AUTH}
protected-mode no
bind 0.0.0.0
cluster-announce-ip ${REDIS_IP}
cluster-announce-port ${slave_ports[$i]}
cluster-announce-bus-port $((17001 + ${slave_ports[$i]} - 7001))
EOL
done


# Docker Compose로 Redis 노드들을 백그라운드에서 실행
docker compose -f docker-compose-local.yml up -d redis-master-1 redis-master-2 redis-master-3 redis-slave-1 redis-slave-2 redis-slave-3 redis
echo "REDIS IP : ${REDIS_IP}"

# 모든 노드들이 완전히 실행될 때까지 대기
echo "Waiting for Redis nodes to be ready..."
# 클러스터 구성 명령어 실행
docker exec -it redis-master-1 redis-cli --cluster create \
  redis-master-1:7001 \
  redis-master-2:7002 \
  redis-master-3:7003 \
  redis-slave-1:7004 \
  redis-slave-2:7005 \
  redis-slave-3:7006 \
  --cluster-replicas 1 -a ${REDISCLI_AUTH} --cluster-yes

# 성공 메시지 출력
echo "Redis cluster has been created successfully."