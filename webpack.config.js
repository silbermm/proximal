var path = require('path');
var webpack = require("webpack");
var ExtractTextPlugin = require("extract-text-webpack-plugin");

module.exports = {
 entry: './web/app.js',
 output: {
   path: './public',
   publicPath: 'assets/',
   filename: 'bundle.js'
 },
module: {
  loaders: [
    { test: /\.js$/, exclude: /node_modules/, loader: 'babel-loader'}, 
    { test: /\.js$/, exclude: /node_modules/, loader: "jshint-loader"},
    { test: /\.(jpe?g|png|gif|svg)$/i, loaders: ['image?bypassOnDebug&optimizationLevel=7&interlaced=false']}, 
    { test: /\.less$/, loader: "style-loader!css-loader!less-loader"},
    { test: /\.html$/, loader: "ng-cache?prefix=[dir]" },
    { test: /\.css$/, loader: "style-loader!css-loader" }
  ]
},
 plugins: [ 
   new ExtractTextPlugin("bundle.css")
 ]
};
