"use strict()";
(function(){
  
  module.exports = function NewAssessmentController($log, Assessments, $modalInstance, items) {
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
          assessmentId: _this.items.assessment.id,
          studentId: _this.items.childId,
          questionId: _this.items.question.id,
          score: _this.rateQuestion,
          standardId: _this.items.standardId,
          timestamp: (new Date()).getTime()
        };
        Assessments.score({assessmentId: _this.items.assessment.id},questionScore, function(newQues){
          _this.items.question = newQues.question;
          _this.rateQuestion = 0;
          _this.items.picture = newQues.picture; 
        }); 
         
        
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
  };
})();
