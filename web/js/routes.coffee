angular.module("proximal").config ($stateProvider, $urlRouterProvider) ->

  $stateProvider.state 'home', {
    url: '/home',
    controller: "HomeCtrl as ctrl",
    templateUrl: "../home/home.html",
    data: {"pageTitle" : "HOME"}
  }

  $stateProvider.state 'login', {
    url: '/login',
    controller: 'LoginCtrl as ctrl',
    templateUrl: '../login/login.html',
    data: {"pageTitle" : "Login"}
  }

  $urlRouterProvider.otherwise "/home"
