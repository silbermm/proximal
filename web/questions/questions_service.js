'use strict()';
(function(){
  module.exports = function($log,$resource,$http){
    return $resource('/api/v1/questions/:id',null, { update: {method: 'PUT'} });
  };
})();
