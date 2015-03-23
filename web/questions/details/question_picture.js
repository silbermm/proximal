(function(){

var img = require('../../images/emptyImage.png');

module.exports = function($log,$q){
  return {
    restrict: "A",
    scope: {
      pic: "=pic"
    },
    link: function(scope,elem,attr) {
      if(scope.pic === undefined) {
        elem.css({"background-image": "url(" + img + ")"});
      } else {
        elem.css({"background-image": "url(data:image/png;base64," + scope.question.picture});
      }     
      elem.css({"background-size": "contain"});
      elem.css({"background-repeat": "no-repeat"});
      
      if(!_.isUndefined(attr.picturePadding)) { 
        elem.css({"padding-bottom" : attr.picturePadding});
      }

    } 
  };
};
})();
