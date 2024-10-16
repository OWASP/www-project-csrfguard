const TerserPlugin = require('terser-webpack-plugin');

module.exports = {
    entry: './csrfguard.js',
    output: {
        filename: 'csrfguard.min.js',
        path: __dirname
    },
    module: {
        rules: [
            {
                test: /\.js$/,
                exclude: /node_modules/,
                use: {
                    loader: 'babel-loader'
                }
            }
        ]
    },
    optimization: {
        minimize: true,
        minimizer: [
            new TerserPlugin({
                terserOptions: {
                    compress: {
                        drop_console: true // Remove console.* statements
                    }
                }
            })
        ]
    },
    mode: 'production' // Enables built-in optimizations like minification
};
