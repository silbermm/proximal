'use strict()';
(function(){
  module.exports = activities; 
  activities.$inject = ['$resource'];
  function activities($resource){
    return {
      'data': $resource("api/v1/activities/:activityId"),
      'all' : $resource("api/v1/activities/all"),
      'sets' : $resource("api/v1/activitysets/:activitySetId")
    };
  }
})();
