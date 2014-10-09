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
          'web/specs/test.js': [
            'web/specs/**/*.coffee',
            'web/spec/**/*.coffee'
          ]
        }
      }
    },
    concat: {
      basic_and_extras: {
        files: {
          "public/javascripts/common.js" : [
            'web/bower_components/lodash/dist/lodash.js',
            'web/bower_components/jquery/dist/jquery.js',
            'web/bower_components/dist/js/bootstrap.js',
            'web/bower_components/angular/angular.js',
            'web/bower_components/angular-resource/angular-resource.js',
            'web/bower_components/angular-animate/angular-animate.js',
            'web/bower_components/angular-cookies/angular-cookies.js',
            'web/bower_components/angular-ui-router/release/angular-ui-router.js',
            'web/bower_components/angular-ui-bootstrap-bower/ui-bootstrap.js',
            'web/bower_components/angular-ui-bootstrap-bower/ui-bootstrap-tpls.js'
          ],
          "public/javascripts/spec.js" : [
            'web/bower_components/angular-mocks/angular-mocks.js',
            'web/bower_components/jasmine/lib/jasmine-core/jasmine.js',
            'web/specs/test.js'
          ]
        }
      }
    },
    less : {
      development : {
        options : {
          paths : [
            "web/less/",
            "web/bower_components/bootstrap/less/",
            "web/bower_components/font-awesome/less/"
          ]
        },
        files : {
          "public/stylesheets/main.css" : "web/less/main.less"
        }
      }
    },
    cssmin: {
      combine: {
        files: {
          'public/stylesheets/main.min.css': [
            'public/stylesheets/main.css',
          ]
        }
      }
    },
    ngAnnotate: {
      options: {
        singleQuotes: true,
      },
      app: {
        files: {
          'public/javascripts/app.annotated.js' : ['public/javascripts/app.js']
        }
      }
    },
    uglify : {
      options : {
        banner : '/*! <%= pkg.name %> <%= grunt.template.today("yyyy-mm-dd") %> */\n',
        compress : {
          global_defs : {
            "DEBUG" : false
          },
          dead_code : true
        }
      },
      build : {
        files : {
          "public/javascripts/common.min.js" : [
            "public/javascripts/common.js"
           ],
           "public/javascripts/templates.min.js" : [
            "public/javascripts/templates.js"
           ],
           "public/javascripts/app.min.js" : [
            "public/javascripts/app.annotated.js"
           ]
        }
      }
    },
    copy : {
      main: {
        files: [
          {expand: true, flatten: true, src: ['web/bower_components/font-awesome/fonts/**'], dest: 'public/fonts/'},
        ]
      }
    },
    imagemin: {
      dynamic: {
        files: [{
          expand: true,
          cwd: 'web/images/',
          src: ['**/*.{png,jpg,gif,ico}'],
          dest: 'public/images/'
        }]
      }
    },
    testem: {
      basic: {
        src : [
          'public/javascripts/common.js', 'public/javascripts/app.js', 'public/javascripts/templates.js', 'web/specs/test.js'
        ],
        options: {
          launch_in_dev: 'firefox',
          serve_files: [
            'public/javascripts/common.js',
            'public/javascripts/app.js',
            'public/javascripts/templates.js',
            'public/javascripts/spec.js'
          ]
        }
      }
    },
    watch : {
      scripts: {
        files: ['web/js/**/*.coffee', 'web/less/**/*.less', 'web/js/**/*.html', 'specs/**/*.coffee', 'Gruntfile.js'],
        tasks: ['default'],
        options: {
          spawn: false,
        },
      },
    }
  });
  grunt.registerTask('default', [ 'html2js','coffee','ngAnnotate','concat','less','copy','imagemin','cssmin','uglify']);
  grunt.registerTask('watchd',  [ 'html2js','coffee','ngAnnotate','concat','less','copy','imagemin','cssmin','uglify', 'watch'])
  grunt.registerTask('testem', ['html2js','coffee', 'concat','testem:ci:basic']);
}
