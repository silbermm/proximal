angular.module('proximal').filter('epoch', function() {
  return function(input) {
    return new Date(input).getTime();
  };
});