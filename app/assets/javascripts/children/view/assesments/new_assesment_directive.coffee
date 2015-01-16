NewAssesmentLink = (scope,el,attr)->


NewAssesment = ($log)->
  return {
    restrict: "EA"
    scope: {}
    link: NewAssesmentLink(scope,el,attr)
  }

angular.module("proximal").directive("newAssesment", [$log, NewAssesment])
