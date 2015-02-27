'use strict()';
(function(){
  module.exports = function($resource) {
	  return $resource("api/v1/assessments/:assessmentId", null, { 'score': {method:'PUT'}});
  };
})();
