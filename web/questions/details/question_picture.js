(function(){
require('./questionPicture.html');
var img = require('../../images/emptyImage.png');

module.exports = function($log,$q){
  return {
    restrict: "A",
    scope: {
      pic: "=pic",
      width: "@width"
    },
    templateUrl: "details/questionPicture.html",
    link: function(scope,elem,attr) {
       
      scope.showPicture = showPicture;
      scope.style = { width: scope.width };

      function showPicture(){
        return scope.pic !== undefined;
      }
    } 
  };
};
})();
