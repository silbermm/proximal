angular.module('proximal').factory "prox.common",[
  "$log"
  "$http"
  ($log,$http) ->
    @educationLevels = [
      { value: "k", description: "Kindergarden"}
      { value: "1", description: "1st Grade" }
      { value: "2", description: "2nd Grade" }
      { value: "3", description: "3rd Grade" }
      { value: "4", description: "4th Grade" }
      { value: "5", description: "5th Grade" }
      { value: "6", description: "6th Grade" }
      { value: "7", description: "7th Grade" }
      { value: "8", description: "8th Grade" }
      { value: "9", description: "9th Grade" }
      { value: "10", description: "10th Grade" }
      { value: "11", description: "11th Grade" }
      { value: "12", description: "12th Grade" }
    ]
    
    #commonly used html elements
    @div_col_12 = $("<div>", {class: "col-md-12"})

    return {
      educationLevels: @educationLevels
      div_col_12: @div_col_12
    }
]
