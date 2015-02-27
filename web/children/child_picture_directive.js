'use strict()';
var boyImg = require('../images/boy.png');
module.exports = function ($log) {
  return {
    restrict: "A",
    scope: {
      child: '=child'
    },
    link: function(scope,el,attr) {
      
      var LoadPhoto = function(ch) { 
        el.css({"background-image": "url(" + boyImg + ")"});
        el.css({"background-size": "contain"});
        el.css({"background-repeat": "no-repeat"});
        
        if(!_.isUndefined(attr.picturePadding)) {
          el.css({"padding-bottom": attr.picturePadding});
        }
      };
            
      if(scope.child === undefined) {
        scope.$watch("child", function(newVal){
          LoadPhoto(newVal);  
        });
      } else {
        LoadPhoto(scope.child);
      }
    }
  };
};
