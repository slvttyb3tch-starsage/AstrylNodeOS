#!/bin/bash
# astryl_server_deploy.sh
# purpose: deploy the astryl coven off-grid home base

echo "[*] updating system and installing docker..."
apt update && apt install -y docker.io docker-compose
systemctl enable docker
systemctl start docker

echo "[*] deploying astryl drive (nextcloud)..."
docker run -d \
  --name astryl_drive \
  -p 8080:80 \
  -v /opt/astryl/nextcloud:/var/www/html \
  nextcloud:latest

echo "[*] deploying astryl network (sing-box)..."
mkdir -p /opt/astryl/singbox
docker run -d \
  --name astryl_network \
  --network host \
  -v /opt/astryl/singbox:/etc/sing-box \
  ghcr.io/sagernet/sing-box:latest \
  run -c /etc/sing-box/astryl_singbox.json

echo "[+] astryl coven server deployed."