AssesmentService = ($resource) ->
	$resource("api/v1/assessments/:assessmentId", null, { 'score': {method:'PUT'}})
angular.module("proximal").factory "Assesments",['$resource', AssesmentService]
