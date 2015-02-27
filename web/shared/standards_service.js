'use strict()';


(function(){
  require('angular');
  angular.module("proximal2").factory("standardsService", [ "$log","$resource", "$http", StandardsService]);

  function StandardsService($log, $resource, $http) {
    var Standards = $resource("/api/v2/standards/:id");
    return {
      standards: function(){
        return Standards;
      },
      addStandard: function(c) {
        return $http.post("/api/v1/standards", c);
      }, 
      updateStandard: function(id, c) {
        return $http.put("/api/v1/standards/" + id, c);
      },
      getAllStandards: function(){
        return $http.get("/api/v1/standards");
      },
      removeStandard: function(id){
        return $http.delete("/api/v1/standards/" + id);
      },
      getStandard: function(id){
        return $http.get("/api/v1/standards/" + id);
      },
      addStatement: function(standardId, statement){
        return $http.post("api/v1/standards/" + standardId + "/statements", statement);
      },
      getStatements: function(standardId){
        return $http.get("api/v1/standards/" + standardId + "/statements");
      }
    };
  }
})();
