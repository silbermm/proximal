AssesmentService = ($resource) ->
	$resource("api/v1/assessments")
   
angular.module("proximal").factory "Assesments",['$resource', AssesmentService]
