'use strict()';

var app = require('angular').module('proximal2');
app.factory("prox.common",[ "$log", "$http", CommonService]);

function CommonService($log,$http) {   
  this.educationLevels = [
      { value: "k", description: "Kindergarden"},
      { value: "1", description: "1st Grade" },
      { value: "2", description: "2nd Grade" },
      { value: "3", description: "3rd Grade" },
      { value: "4", description: "4th Grade" },
      { value: "5", description: "5th Grade" },
      { value: "6", description: "6th Grade" },
      { value: "7", description: "7th Grade" },
      { value: "9", description: "9th Grade" },
      { value: "10", description: "10th Grade" },
      { value: "11", description: "11th Grade" },
      { value: "12", description: "12th Grade" }
    ];

    this.homeworkStatuses = [
      {text: "Not Started"},
      {text: "In-Progress"},
      {text: "Finished"}
    ];
    
    return {
      educationLevels: this.educationLevels,
      homeworkStatuses: this.homeworkStatuses
    };
}
