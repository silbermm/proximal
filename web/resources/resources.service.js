"use strict()";
(function(){

  module.exports = ResourcesFactory;

  ResourcesFactory.$inject = [ '$resource' ];

  function ResourcesFactory($resource) {
    return $resource("api/v1/resources/:resourceId");
  }

})();
