'use strict()';
(function(){
  module.exports = QuestionsByResource;

  QuestionsByResource.$inject = ['$resource', '$http'];
    
  function QuestionsByResource($resource, $http){
    return $resource('/api/v1/questions/byResource/:resourceId');
  }

})();
