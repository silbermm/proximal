"use strict()";
(function(){

	module.exports = AddActivity;

  AddActivity.$inject = [
    "$log",
    "$modalInstance",
    "standardsService",
    "Child",
    "prox.common",
    "$stateParams",
    "items"
  ];
    
  function AddActivity($log, $modalInstance, Standards, Child, Common, $stateParams, items){
		var vm = this;

		vm.child = Child.get({"id": $stateParams.id});
		vm.standard = items;

		vm.status = Common.homeworkStatuses;

		vm.entity = {
			childId : Number($stateParams.id),
			statementId : null,
			activity : {

			},
			homework : {
				studentId: Number($stateParams.id)
			},
      acts : [
      
      ]
		};

		vm.steps = [
			{step: 1, title: "Choose Statement", completed: false, selected: true},
			{step: 2, title: "General Information", completed: false, selected: false},
      {step: 3, title: "Actions", completed: false, selected: false}
		];

		vm.setForm = function(f){
			vm.forms = f;
		};

		//Get statements for this standard and grade level of the child
		Standards.getStatements(vm.standard.id).success(function(d){
			vm.statements = d.statements;
			//vm.statements = _.filter(d.statements, function(st){
			//return (_.contains(st.levels, vm.child.educationLevel.id) ) || (st.levels.length === 0);
			//});
		}).error(function(d){
			$log.error(d);
		});

		vm.nextStep = function(){
			// which step are we on?
			var currentStep = _.find(vm.steps, function(s){
				return s.selected === true;
			});
			vm.steps[currentStep.step -1].completed = true;
			vm.steps[currentStep.step -1].selected = false;
			vm.steps[currentStep.step].selected = true;
			return true;

		};

		vm.isNextDisabled = function(){
			return (vm.steps[0].selected && vm.statement === undefined) || (vm.steps[1].selected && !vm.isEntityValid());
		};

		vm.goTo = function(step){
			_.each(vm.steps, function(s){
				if(s.step === step.step){
					s.selected = true;
				} else {
					s.selected = false;
					s.completed = false;
				}
			});
		};

    vm.addAction = function(){
      if(vm.actionToAdd !== undefined){
        vm.entity.acts.push({"actType": "homework", "action": vm.actionToAdd}); 
      }
      vm.actionToAdd = null;
      vm.showAdd = false;
    };

		vm.ok = function () {
			vm.entity.homework.dateGiven = new Date(vm.dateGiven).getTime();
			if(vm.dateDue !== undefined) 
				vm.entity.homework.dateDue = new Date(vm.dateDue).getTime();
			vm.entity.activity.date = new Date().getTime();
			$modalInstance.close(vm.entity);
		};

		vm.cancel = function () {
			$modalInstance.dismiss('cancel');
		};

		vm.isEntityValid = function(){
			//TODO: Validate all required fields
			return (vm.entity.activity.title !== undefined && vm.entity.activity.subject !== undefined);
		};
	}

})();
