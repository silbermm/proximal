"use strict()";
(function(){
  
  function NewAssessmentController($log, Assessments, $modalInstance, items) {
    var _this = this;
    _this.items = items;
    $log.debug(_this.items);

    _this.done = false;

    _this.rateQuestion = 0;
    _this.max = 5;
    _this.isReadOnly = false;

    _this.hoveringOver = function(value) {
      _this.overStar = value;
      _this.percent = 100 * (value / $scope.max);
    };

    _this.scored = function(){
      return _this.rateQuestion > 0;
    };

    _this.next = function(){
      
    };
   
    _this.ok = function(){
      $modalInstance.close();
    };

    _this.cancel = function(){
      $modalInstance.dismiss('cancel');
    };

  }

  angular.module('proximal').controller("NewAssessmentCtrl", [ '$log', 'Assesments', '$modalInstance', 'items', NewAssessmentController]);
})();
