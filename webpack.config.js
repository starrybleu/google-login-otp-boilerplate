const path = require('path');
const webpack = require('webpack');
const webpackMerge = require('webpack-merge');
const target = process.env.npm_lifecycle_event; // npm run dev || npm run prod 로 build 해야 함

const common = {
    cache: false,
    entry: path.join(__dirname, '/src/main/resources/static/js/index.js'),
    output: {
        path: path.join(__dirname, '/src/main/resources/static/js/'),
        filename: 'bundle.js',
        publicPath: 'http://localhost:3000/static/js/'
    },
    module: {
        loaders: [
            {
                test: /\.js$/,
                loader: 'babel-loader',
                exclude: /node_modules/
            },
            {
                test: /\.css$/,
                loader: 'style-loader!css-loader'
            },
            {
                test: /\.(png|woff|woff2|eot|ttf|svg)$/,
                loader: 'url-loader?limit=1000000'
            }
        ]
    },
    plugins: [
        new webpack.NamedModulesPlugin(),
        new webpack.ProvidePlugin({
            $: 'jquery',
            jQuery: 'jquery'
        })
    ]
};

const prodConfig = {
    plugins: [
        new webpack.optimize.UglifyJsPlugin({
            compress: {warnings: false}
        })
    ]
}

let config = {};
console.log('target: ', target);
if (target === 'prod') {
    console.log('### !!! production build... !!!');
    config = webpackMerge(common, prodConfig);
} else {
    console.log('### development build...');
    config = webpackMerge(common, require('./webpack.dev.config.js'));
}

module.exports = config;