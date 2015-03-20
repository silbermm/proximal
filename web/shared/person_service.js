'use strict()';

require('angular');

angular.module("proximal2").factory("personService", [ "$log", "$http", "$resource", PersonService]);

function PersonService($log, $http, $resource){
  return { 
    profile: $resource("/api/v1/profile"),
    addChild: function(c) {
      return $http.post("/api/v1/children", c);
    },   
    getChildren: function(){
      return $http.get("/api/v1/children");
    },
    removeChild: function(id){
      return $http.delete("/api/v1/children/" + id);
    },
    getChild: function(id){
      return $http.get("/api/v1/children/" + id);
    }
  };
}
