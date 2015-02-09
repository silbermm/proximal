"use strict()";
(function(){

	function AddHomeworkCtrl($log, $modalInstance, Standards, Child, Common, $stateParams, items){
		var _this = this;

		_this.child = Child.get({"id": $stateParams.id});
		_this.standard = items;

		_this.status = Common.homeworkStatuses;

		_this.entity = {
			childId : Number($stateParams.id),
			statementId : null,
			activity : {

			},
			homework : {
				studentId: Number($stateParams.id)
			}
		};

		_this.steps = [
			{step: 1, title: "Choose Statement", completed: false, selected: true},
			{step: 2, title: "Add Information", completed: false, selected: false}
		];

		_this.setForm = function(f){
			_this.forms = f;
		};

		//Get statements for this standard and grade level of the child
		Standards.getStatements(_this.standard.id).success(function(d){
			_this.statements = d.statements;
			//_this.statements = _.filter(d.statements, function(st){
			//return (_.contains(st.levels, _this.child.educationLevel.id) ) || (st.levels.length === 0);
			//});
		}).error(function(d){
			$log.error(d);
		});

		_this.nextStep = function(){
			// which step are we on?
			var currentStep = _.find(_this.steps, function(s){
				return s.selected === true;
			});
			_this.steps[currentStep.step -1].completed = true;
			_this.steps[currentStep.step -1].selected = false;
			_this.steps[currentStep.step].selected = true;
			return true;

		};

		_this.isNextDisabled = function(){
			return (_this.steps[0].selected && _this.statement === undefined) || (_this.steps[1].selected && !_this.isEntityValid());
		};

		_this.goTo = function(step){
			_.each(_this.steps, function(s){
				if(s.step === step.step){
					s.selected = true;
				} else {
					s.selected = false;
					s.completed = false;
				}
			});
		};

		_this.ok = function () {
			_this.entity.homework.dateGiven = new Date(_this.dateGiven).getTime();
			if(_this.dateDue !== undefined) 
				_this.entity.homework.dateDue = new Date(_this.dateDue).getTime();
			_this.entity.activity.date = new Date().getTime();
			$modalInstance.close(_this.entity);
		};

		_this.cancel = function () {
			$modalInstance.dismiss('cancel');
		};

		_this.isEntityValid = function(){
			//TODO: Validate all required fields
			return (_this.entity.activity.title !== undefined && _this.entity.activity.subject !== undefined);
		};
	}

	angular.module("proximal").controller("AddHomeworkCtrl", ["$log", "$modalInstance", "standardsService", "Child", "prox.common", "$stateParams", "items", AddHomeworkCtrl]);
})();