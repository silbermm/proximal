(function(){

	function HomeworkCtrl($log, $modal, toaster, standardsService, Homework) {
		var _this = this;

		standardsService.getAllStandards().success(function(data){
			_this.availableStandards = data;
		}).error(function(data){
			$log.error(data);
		});

		_this.begin = function() {
			var modalInstance = $modal.open({
				templateUrl: "../assets/javascripts/children/view/homework/add/add_homework.html", 
				controller: 'AddHomeworkCtrl',
				controllerAs: 'addHomework',
				resolve: {
					items: function () {
						return _this.standardSelected;
					}
				}
			});

			modalInstance.result.then(function (result) {
				$log.info(result);
				var homework = new Homework(result);
				homework.$save(function(saved){
					$log.debug("YES, Saved homework!");
					toaster.pop("success", null, "Successfully added your childs homework!");
				}, function(error){
					$log.error("BOO! Can't save homework :(");
					toaster.pop("error", null, "There was an error when trying to add the homework. Please try again.");
				});

			}, function () {
				$log.info('Modal dismissed at: ' + new Date());
			});
		};
	}
	angular.module('proximal').controller("HomeworkCtrl", ["$log", '$modal', 'toaster', 'standardsService', "Homework", HomeworkCtrl]);
})();