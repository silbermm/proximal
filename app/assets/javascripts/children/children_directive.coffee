angular.module("proximal").directive "proxChildren",[
  '$log',
  'personService',
  ($log, personService) ->
    return {
      restrict: "E"
      replace: true
      controller: "ChildrenCtrl"
      templateUrl: "assets/javascripts/children/templates/children_directive_template.html",
      scope: {
        children: '='
      }
      link: (scope, el, attrs) ->
         
        return
    }
]
