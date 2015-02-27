'use strict()';

require('../../shared/common_service');

module.exports = function ($scope, $log, $modalInstance,Child,common) {
  
  var _this = this;

  _this.availableLevels = common.educationLevels;

  _this.clear = function(){
    _this.child.birthDate = null;
  };
     
  _this.open = function($event){
    $event.preventDefault();
    $event.stopPropagation();
    _this.opened = true;
  };

  _this.dateOptions = {
    formatYear: 'yyyy',
    startingDay: 1
  };

  _this.initDate = new Date("Jan 1, 2008"); 
  _this.format = 'mediumDate';

  _this.ok = function(){
    var d = $scope.child.birthDate.getTime()/1000;
    var child = new Child({'firstName': $scope.child.firstName,'lastName': $scope.child.lastName,'birthDate': d, "educationLevel": $scope.child.gradeLevel});
    $modalInstance.close(child);
  };

  _this.cancel = function(){
    $modalInstance.dismiss("cancel");
  };
};


