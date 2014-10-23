angular.module("proximal").directive "proxChildren",[
  '$log',
  'personService',
  ($log, personService) ->
    return {
      restrict: "E"
      replace: true
      templateUrl: "assets/javascripts/children/children_directive_template.html",
      scope: {
        children: '=' 
      }
      link: (scope, el, attrs) ->
         
        return
    }
]
