'use strict';

angular.module('aquilaApp')
    .controller('EmployerDetailController', function ($scope, $stateParams, Employer) {
        $scope.employer = {};
        $scope.load = function (id) {
            Employer.get({id: id}, function(result) {
              $scope.employer = result;
            });
        };
        $scope.load($stateParams.id);
    });
