angular.module("proximal").factory "standardsService", [
  "$log"
  "$resource"
  "$http"
  ($log, $resource, $http) ->
    Standards = $resource("/api/v2/standards/:id") 
    return {
      standards: ->
        return Standards

      addStandard: (c) ->
        return $http.post("/api/v1/standards", c)
      
      updateStandard: (id, c) ->
        return $http.put("/api/v1/standards/" + id, c)

      getAllStandards: ->
        return $http.get("/api/v1/standards")

      removeStandard: (id)->
        return $http.delete("/api/v1/standards/" + id)

      getStandard: (id)->
        return $http.get("/api/v1/standards/" + id)

      addStatement: (standardId, statement)->
        return $http.post("api/v1/standards/" + standardId + "/statements", statement)

      getStatements: (standardId) ->
        return $http.get("api/v1/standards/" + standardId + "/statements")
    }
]
