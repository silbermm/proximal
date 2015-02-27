var path = require('path');
var webpack = require("webpack");
var ExtractTextPlugin = require("extract-text-webpack-plugin");

module.exports = {
 entry: './web/app.js',
 output: {
   path: './public',
   publicPath: 'www.test.com/',
   filename: 'bundle.js'
 },
module: {
   loaders: [
   { test: /\.js$/, exclude: /node_modules/, loader: 'babel-loader'}, 
   { test: /\.js$/, exclude: /node_modules/, loader: "jshint-loader" },
   { test: /\.scss$/, loader: ExtractTextPlugin.extract('style-loader', 'css-loader!sass-loader')}, 
   { test: /\.(jpe?g|png|gif|svg)$/i, loaders: ['image?bypassOnDebug&optimizationLevel=7&interlaced=false']}, 
     { 
       test: /\.html$/, 
       loader: "ng-cache?prefix=[dir]" 
     }
    ]
  },

 plugins: [ 
   new ExtractTextPlugin("bundle.css")
 ]
};