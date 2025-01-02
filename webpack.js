const {resolve} = require("node:path");
module.exports = {
    entry: resolve('src/index.js'), // 必要に応じて変更
    output: {
        filename: 'main.js',
        path: resolve(__dirname, 'main/resources/static/js/'),
    },
    mode: 'development', // 明示的に指定
    module: {
        rules: [
            {
                test: /\.js$/,
                exclude: /node_modules/,
                use: {
                    loader: 'babel-loader',
                },
            },
        ],
    },
    resolve: {
        extensions: ['.js'], // デフォルトで解決する拡張子
    },
};