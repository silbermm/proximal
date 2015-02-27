'use strict()';

require('angular');

angular.module("proximal2").factory("personService", [ "$log", "$http", PersonService]);

function PersonService($log, $http){
  return {
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
