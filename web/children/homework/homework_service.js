'use strict()';
(function(){
  module.exports = function($resource){
    return $resource("api/v1/activities/homework/:id");
  };
})();
