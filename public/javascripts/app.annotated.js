(function() {
  angular.module('proximal', ["templates-main", "ui.router", "ui.router.state", "ngAnimate"]).run(['$rootScope', '$state', '$log', function($rootScope, $state, $log) {
    return $rootScope.$on('$stateChangeStart', function(event, toState, toParams, fromState, fromParams) {
      return $log.debug("state changed!");
    });
  }]).controller('AppCtrl', ['$scope', '$state', '$log', function($scope, $state, $log) {
    return $scope.state = $state;
  }]);

}).call(this);

(function() {
  angular.module("proximal").config(['$stateProvider', '$urlRouterProvider', function($stateProvider, $urlRouterProvider) {
    $stateProvider.state('home', {
      url: '/home',
      controller: "HomeCtrl as ctrl",
      templateUrl: "../home/home.html"
    });
    $stateProvider.state('login', {
      url: '/login',
      controller: 'LoginCtrl as ctrl',
      templateUrl: '../login/login.html'
    });
    $stateProvider.state('dashboard', {
      url: '/dashboard',
      controller: 'DashboardCtrl as ctrl',
      templateUrl: '../dashboard/dashboard.html'
    });
    return $urlRouterProvider.otherwise("/home");
  }]);

}).call(this);

(function() {
  angular.module("proximal").controller("DashboardCtrl", ['$log', function($log) {
    return this.page = "Dashboard Page";
  }]);

}).call(this);

(function() {
  angular.module("proximal").controller("HomeCtrl", ['$log', function($log) {
    return this.page = "Home Page";
  }]);

}).call(this);

(function() {
  angular.module("proximal").controller("LoginCtrl", ['$log', function($log) {
    return this.page = "Login Page";
  }]);

}).call(this);

(function() {


}).call(this);

(function() {


}).call(this);

(function() {
  angular.module("proximal").directive("proxFooter", ['$log', function($log) {
    return {
      restrict: "E",
      scope: {},
      replace: true,
      templateUrl: "../shared/footer/footer.html",
      link: function(scope, element, attr) {
        return scope.copyright = new Date().getFullYear();
      }
    };
  }]);

}).call(this);

(function() {
  angular.module("proximal").controller("HeaderCtrl", ['$log', function($log) {}]);

}).call(this);

(function() {
  angular.module("proximal").directive("proxHeader", ['$log', function($log) {
    return {
      restrict: "E",
      scope: {
        copyright: '='
      },
      replace: true,
      templateUrl: "../shared/header/header.html",
      controller: "HeaderCtrl",
      link: function(scope, element, attr) {
        return $log.debug(element);
      }
    };
  }]);

}).call(this);
