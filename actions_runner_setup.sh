# フォルダ作成
mkdir actions-runner && cd actions-runner

# 最新ランナーのダウンロード
curl -L -o actions-runner-win-x64-2.323.0.zip \
  https://github.com/actions/runner/releases/download/v2.323.0/actions-runner-win-x64-2.323.0.zip
TOKEN=AFQRYZLKQKDCC37PNUPPGELIEBQI2
# (オプション) SHA256ハッシュ検証
echo "e8ca92e3b1b907cdcc0c94640f4c5b23f377743993a4a5c859cb74f3e6eb33ef  actions-runner-win-x64-2.323.0.zip" | sha256sum -c -

# ZIP解凍（Windows Git Bash なら標準でunzip有！）
unzip actions-runner-win-x64-2.323.0.zip

# ランナーのセットアップ（PowerShellの ./config.cmd 相当。cmdスクリプトをコール！）
cmd //c config.cmd --url https://github.com/kakusan0/java --token "${TOKEN}"

# ランナーの起動
cmd //c run.cmd