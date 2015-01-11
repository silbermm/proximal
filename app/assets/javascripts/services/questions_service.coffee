angular.module("proximal").factory "Question", [
  "$log"
  "$resource"
  "$http"
  ($log,$resource,$http) ->
    return $resource('/api/v1/questions/:id',null, { update: {method: 'PUT'} })
]
