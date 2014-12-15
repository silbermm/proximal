angular.module("proximal").factory "Child", [
  "$log"
  "$resource"
  ($log, $resource) ->
    return $resource("api/v1/children/:id")
]
