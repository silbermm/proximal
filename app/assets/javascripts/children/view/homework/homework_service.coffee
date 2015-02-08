HomeworkService = ($resource)->
	$resource("api/v1/activities/homework/{id}")

angular.module("proximal").factory("Homework",['$resource', HomeworkService])