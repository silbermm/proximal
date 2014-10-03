angular.module("proximal").config ($stateProvider, $urlRouterProvider) ->

  $stateProvider.state 'home', {
    url: '/home',
    controller: "HomeCtrl as ctrl",
    templateUrl: "../home/home.html"
  }

  $stateProvider.state 'login', {
    url: '/login',
    controller: 'LoginCtrl as ctrl',
    templateUrl: '../login/login.html'
  }

  $stateProvider.state 'dashboard', {
    url: '/dashboard',
    controller: 'DashboardCtrl as ctrl',
    templateUrl: '../dashboard/dashboard.html' 
  }


  $urlRouterProvider.otherwise "/home"
