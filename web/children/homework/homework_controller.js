(function(){
  
  require('../../shared/standards_service');

	module.exports = function($log, $modal, toaster, $stateParams, standardsService, Homework) {
		var _this = this;

		standardsService.getAllStandards().success(function(data){
			_this.availableStandards = data;
		}).error(function(data){
			$log.error(data);
		});

    _this.allHomework = Homework.query({'id': $stateParams.id});

		_this.begin = function() {
			var modalInstance = $modal.open({
				templateUrl: "add/add_homework.html", 
				controller: 'AddHomeworkCtrl',
				controllerAs: 'addHomework',
        backdrop: false,
				resolve: {
					items: function () {
						return _this.standardSelected;
					}
				}
			});

			modalInstance.result.then(function (result) {
				var homework = new Homework(result);
				homework.$save(function(saved){
					toaster.pop("success", null, "Successfully added your childs homework!");
          _this.allHomework.push(saved);
				}, function(error){
					toaster.pop("error", null, "There was an error when trying to add the homework. Please try again.");
				});

			}, function () {
				$log.info('Modal dismissed at: ' + new Date());
			});
		};
	};
})();
