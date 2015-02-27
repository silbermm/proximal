'use strict()';

require('../home');
require('../dashboard');
require('../library');
require("../children");

module.exports = function($stateProvider, $urlRouterProvider) {

  $stateProvider.state('home', {
    url: '/home',
    controller: 'HomeController as ctrl',
    templateUrl: 'home/home.html'
  })
  .state('dashboard', {
    url: '/dashboard',
    controller: 'DashboardCtrl as ctrl',
    templateUrl: 'dashboard/dashboard.html',
    data: {title: "Dashboard"}
  })
  .state('library', {
    url: '/library',
    controller: 'LibraryCtrl as ctrl',
    templateUrl: 'library/library.html',
    data: {title: "Library"}
  })
  .state('children', {
    url: '/children',
    controller: 'ChildrenCtrl as childCtrl',
    templateUrl: 'children/children.html',
    data: {
      title: "Children",
      hideChildren: false 
    }
  })
  .state('children.view',{
    url: "/{id}",
    controller: 'ViewChildCtrl',
    controllerAs: 'childctrl',
    templateUrl: 'view/view_child.html',
    data: {
      title: "Children",
      hideChildren: true,
      breadcrumbs: [
        {"path" : "children", "text" : "All Children" },
      ]
    }
  })
  .state('children.view.assessment',{
    url: "/assessment",
    controller: 'AssessmentCtrl',
    controllerAs: 'assessment', 
    templateUrl: 'assessments/assessment.html',
    templateAs: 'assessment',
    data: {
      title: "New Assessment",
      hideChildren: true,
      breadcrumbs: [
        {"path" : "children", "text" : "All Children" }
      ]
    }
  })
  .state('children.view.homework', {
    url: "/homework",
    controller: "HomeworkCtrl",
    controllerAs: 'homework',
    templateUrl: 'homework/homework.html',
    data: {
      title: "Homework",
      hideChildren: true,
      breadcrumbs: [
        {"path" : "children", "text" : "All Children" }
      ]
    }
  })

  ;

  $urlRouterProvider.otherwise('/home');
};
