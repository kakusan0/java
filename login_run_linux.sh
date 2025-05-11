#!/usr/bin/env bash
set -e

REPO="kakusan0/java"
REPO_URL="https://github.com/kakusan0/java"
VERSION="2.323.0"
ARCHIVE="actions-runner-linux-arm64-${VERSION}.tar.gz"
RELEASE_URL="https://github.com/actions/runner/releases/download/v${VERSION}/$ARCHIVE"

# runner登録用トークン自動取得（ghコマンド利用）
TOKEN=$(gh api --method POST repos/$REPO/actions/runners/registration-token --jq .token)

# Runner DL＆検証
mkdir -p actions-runner && cd actions-runner
curl -LO $RELEASE_URL
curl -LO "https://github.com/actions/runner/releases/download/v${VERSION}/sha256sum.txt"

HASH=$(grep "${ARCHIVE}" sha256sum.txt | cut -d ' ' -f1)
echo "${HASH}  ${ARCHIVE}" | sha256sum

tar xzf ${ARCHIVE}

# ランナー構成
./config.sh --url "${REPO_URL}" --token "${TOKEN}" --unattended --name $(hostname)-gh --labels gh

# 起動
./run.sh