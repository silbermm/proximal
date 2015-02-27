NewAssesmentLink = (scope,el,attr)->


NewAssesment = ($log)->
  return {
    restrict: "EA"
    scope: {}
    templateUrl: "../assets/javascripts/children/view/assesments/new_assesment.html"
    link: NewAssesmentLink(scope,el,attr)
  }

angular.module("proximal").directive("newAssesment", ['$log', NewAssesment])
