'use strict()';

var _ = require("lodash");

module.exports = function($log,Child, $modal){
    
  var _this = this;
    
  _this.page = "Children Page";
  _this.children = [];
  _this.children = Child.query(); 
   
  _this.removeChild = function(id) { 
    var toRemove = _.findIndex(_this.children, function(chr){ 
      return chr.id ===  id;
    });
    var child = new Child(toRemove);
    child.remove();
    _this.children.splice(toRemove,1);
  }; 
    
  _this.createChild = function(){
      
    var modalInstance = $modal.open({
      templateUrl: 'add/add_child.html',
      controller: 'AddChildCtrl',
      controllerAs: "addChild"
    });

    var addChild = function(child){
      child.$save();
      _this.children.push(child);
    };

    modalInstance.result.then(function(c){
      addChild(c);
    });
  };
};
