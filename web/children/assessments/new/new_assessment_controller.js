"use strict()";
(function(){
  
  function NewAssessmentController($log, Assessments, $modalInstance, items) {
    var _this = this;
    _this.items = items;

    _this.done = false;

    _this.rateQuestion = 0;
    _this.max = 5;
    _this.isReadOnly = false;

    _this.hoveringOver = function(value) {
      _this.overStar = value;
      _this.percent = 100 * (value / _this.max);
    };

    _this.scored = function(){
      return _this.rateQuestion > 0;
    };

    _this.next = function(){
      if(_this.rateQuestion > 0) { 
        _this.error = undefined;
        var questionScore = {
          studentId: _this.items.childId,
          questionId: _this.items.question.question.id,
          score: _this.rateQuestion,
          timestamp: (new Date()).getMilliseconds()
        };
        _this.items.question.question = Assessments.score({assessmentId: _this.items.question.assessment.id},questionScore ); 
         
        
      } else {
        _this.error = "Please rate the students answer first";
      }
    };
   
    _this.ok = function(){
      $modalInstance.close();
    };

    _this.cancel = function(){
      $modalInstance.dismiss('cancel');
    };

  }
})();
