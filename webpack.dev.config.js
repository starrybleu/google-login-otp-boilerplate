const webpack = require('webpack');

module.exports = {
    devtool: 'inline-source-map',
    devServer: {
        disableHostCheck: true,
        historyApiFallback: true,
        compress: true,
        host: '0.0.0.0',
        port: 3000,
        publicPath: '/',
        proxy: {
            '**': {
                target: 'http://localhost:8080',
                secure: false,
                prependPath: false,
                proxyTimeout: 1000 * 60 * 10
            }
        }
    },
    plugins: [
        new webpack.NamedModulesPlugin()
    ]
}