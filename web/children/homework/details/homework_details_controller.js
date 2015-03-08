'use strict()';
(function(){ 
  module.exports = function($log, Standards, Child, Score, Common, $stateParams){
    var vm = this;  
    vm.max = 5;

    vm.progress = Common.homeworkStatuses;

    vm.hoveringOver = function(value) {
      vm.overStar = value;
      vm.percent = 100 * (value / vm.max);
    };

    vm.updateProgress = function(a) {

    };

    vm.updateScore = function(s){
      console.log(s);
      if(s.score.id === undefined) {
        var score = { 
          studentId : Number($stateParams.id),
          actId : s.id,
          timestamp:  (new Date()).getTime(),
          score: s.score.score
        };
        Score.save({}, score, function(saved){
          //TODO: Update the act's progress to Finished and save    
        });
      } else {
        Score.update({}, s.score); 
      }
    };
  };
})();
