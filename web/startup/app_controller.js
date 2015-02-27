'use strict()';
var _ = require("lodash");
module.exports = function($scope, $state, $log, $cookieStore) {
  var _this = this;
  var mobileView = 992;
  _this.getWidth = function(){
    return window.innerWidth;
  };

  /*$scope.$watch(_this.getWidth, function(newValue, oldValue){
    if(newValue >= mobileView){
      if(_.isDefined($cookieStore.get("toggle"))){
        if($cookieStore.get("toggle") === false){
          _this.toggle = false;
        }else {
          _this.toggle = true;
        }
      }else {
        _this.toggle = true;
      }
    } else {
      _this.toggle = false;
    }
  });
  */

  _this.toggleSidebar = function(){
    _this.toggle = !_this.toggle;
    $cookieStore.put('toggle', _this.toggle);
  };

  window.onresize = function(){
    $scope.$apply();
  };
};
