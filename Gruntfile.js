module.exports = function(grunt) {
  require('load-grunt-tasks')(grunt); 
  grunt.initConfig({
    pkg : grunt.file.readJSON('package.json'),
    bower: {
      install: {
         options: {
          targetDir: './web/bower_components/',
          layout: 'byType',
          install: true,
          verbose: false,
          cleanTargetDir: false,
          cleanBowerDir: false,
          bowerOptions: {}
        }
      }
    },
    html2js: {
      options: {
        rename : function (moduleName) {
          return moduleName.replace('web/js/', '');
        },
        htmlmin: {
          collapseBooleanAttributes: true,
          collapseWhitespace: true,
          removeAttributeQuotes: true,
          removeComments: true,
          removeEmptyAttributes: true,
          removeRedundantAttributes: true,
          removeScriptTypeAttributes: true,
          removeStyleLinkTypeAttributes: true
        }
      },
      main: {
        src: ['web/js/**/*.html'],
        dest: 'public/javascripts/templates.js'
      }
    },
    coffee: {
      compile: {
        files:{
          'public/javascripts/app.js': [
            'web/js/*.coffee', 'web/js/**/*.coffee'
          ],
          'web/spec/test.js': [
            'web/spec/**/*.coffee'
          ]
        }
      }
    }
  });
  grunt.registerTask('default', [ 'html2js','coffee' ]);
}
