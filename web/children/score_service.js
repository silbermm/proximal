'use strict()';
module.exports = function ($log, $resource) {
  return $resource("api/v1/scores/:studentId", null, {update: {method:'PUT'}});
};
