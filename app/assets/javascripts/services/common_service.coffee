angular.module('proximal').factory "prox.common",[
  "$log"
  ($log) ->
    @educationLevels = ["k","1","2","3","4","5","6","7","8","9","10","11","12"]
    return {
      educationLevels: @educationLevels
    }
]
