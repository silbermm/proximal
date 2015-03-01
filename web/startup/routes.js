'use strict()';

require('../home');
require('../dashboard');
require('../library');
require("../children");
require("../admin");

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
  .state('admin', {
    url: '/admin',
    controller: 'AdminCtrl as ctrl',
    templateUrl: 'admin/admin.html',
    data: { title: 'Administrative Tasks', hideAdmin: false },
  })
  .state('admin.questions',{
    url: '/questions',
    controller: 'QuestionsCtrl as ctrl',
    templateUrl: 'questions/questions.html',
    data: {
      title: "Questions",
      hideAdmin: true,
      breadcrumbs: [
        {"path": "admin", "text": "Admin"}
      ]
    }
  })
  .state('admin.questions.edit',{
    url: '/{questionId}',
    controller: 'EditQuestionsCtrl as question',
    templateUrl: 'edit/edit_question.html',
    data: {
      title: "Edit",
      hideAdmin: true,
      hideQuestions: true,
      breadcrumbs: [
        {"path": "admin", "text": "Admin"},
        {"path": "admin.questions", "text": "Questions"}
      ]
    }
  })

  ;

  $urlRouterProvider.otherwise('/home');
};
